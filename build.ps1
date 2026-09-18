<#
  xsd_generator 단일 exe 빌드 스크립트.

  build/libs/*.jar  ->  jlink 최소 런타임  ->  jpackage app-image  ->  payload.zip
  -> C# 런처(dist/xsd_generator-<version>.exe) 안에 통째로 내장.

  결과물은 dist 폴더의 exe 파일 하나뿐이며, 대상 PC 에 Java 가 없어도 동작한다.
#>
[CmdletBinding()]
param(
  # jpackage/jlink/jdeps 를 제공하는 JDK. 지정하면 다른 후보를 찾지 않고 이 JDK 만 쓴다.
  [string]$JdkHome
)

$ErrorActionPreference = 'Stop'
$root = $PSScriptRoot
$appName = 'xsd_generator'

function Get-JdkInfo {
  param([string]$JdkPath)

  if (-not $JdkPath) { return $null }
  if (-not (Test-Path (Join-Path $JdkPath 'bin\jpackage.exe'))) { return $null }

  $version = $null
  $release = Join-Path $JdkPath 'release'
  if (Test-Path $release) {
    $line = Get-Content $release | Where-Object { $_ -like 'JAVA_VERSION=*' } | Select-Object -First 1
    if ($line -and $line -match 'JAVA_VERSION="?([0-9]+(\.[0-9]+)*)') { $version = $Matches[1] }
  }
  if (-not $version) {
    $out = & (Join-Path $JdkPath 'bin\java.exe') -version 2>&1 | Out-String
    if ($out -match '"([0-9]+(\.[0-9]+)*)') { $version = $Matches[1] }
  }
  if (-not $version) { return $null }

  $major = [int]($version -split '\.')[0]
  return [pscustomobject]@{ Home = (Resolve-Path $JdkPath).Path; Version = $version; Major = $major }
}

function Resolve-Jdk {
  param([string]$Explicit)

  if ($Explicit) {
    $info = Get-JdkInfo $Explicit
    if (-not $info) { throw "-JdkHome 경로에서 bin\jpackage.exe 를 찾지 못했습니다: $Explicit" }
    return $info
  }

  # jpackage 를 가진 후보를 모두 모은 뒤 가장 높은 버전을 고른다.
  # (JAVA_HOME 이 구버전을 가리키는 경우가 흔해서 우선순위보다 버전을 우선한다.)
  $paths = @($env:JAVA_HOME)
  $onPath = Get-Command jpackage.exe -ErrorAction SilentlyContinue
  if ($onPath) { $paths += (Split-Path (Split-Path $onPath.Source -Parent) -Parent) }
  $paths += (Get-ChildItem "$env:USERPROFILE\.jdks" -Directory -ErrorAction SilentlyContinue |
    ForEach-Object { $_.FullName })

  $found = @($paths | ForEach-Object { Get-JdkInfo $_ } | Where-Object { $_ })
  if ($found.Count -eq 0) {
    throw 'jpackage.exe 를 가진 JDK(16 이상)를 찾지 못했습니다. -JdkHome 으로 직접 지정하세요.'
  }
  return ($found | Sort-Object Major, @{ Expression = { $_.Version } } -Descending | Select-Object -First 1)
}

function Invoke-Tool {
  param([string]$Exe, [string[]]$ToolArgs, [string]$FailMessage)

  $output = & $Exe @ToolArgs 2>&1
  if ($LASTEXITCODE -ne 0) {
    $output | ForEach-Object { Write-Host "      $_" }
    throw $FailMessage
  }
  return $output
}

$jdk = Resolve-Jdk -Explicit $JdkHome
$jdeps = Join-Path $jdk.Home 'bin\jdeps.exe'
$jlink = Join-Path $jdk.Home 'bin\jlink.exe'
$jpackage = Join-Path $jdk.Home 'bin\jpackage.exe'
Write-Host "[1/6] JDK $($jdk.Version): $($jdk.Home)"

$gradleText = Get-Content (Join-Path $root 'build.gradle') -Raw
if ($gradleText -notmatch "(?m)^\s*version\s*=\s*'([^']+)'") {
  throw 'build.gradle 에서 version 을 읽지 못했습니다.'
}
$version = $Matches[1]
Write-Host "      버전: $version"

$buildDir = Join-Path $root 'build'
$distDir = Join-Path $root 'dist'
$runtimeDir = Join-Path $buildDir 'runtime'
$imageParent = Join-Path $buildDir 'appimage'
$imageDir = Join-Path $imageParent $appName
$payload = Join-Path $buildDir 'payload.zip'
$jar = Join-Path $buildDir "libs\$appName-$version.jar"
$outExe = Join-Path $distDir "$appName-$version.exe"

Write-Host '[2/6] jar 빌드'
$env:JAVA_HOME = $jdk.Home
& (Join-Path $root 'gradlew.bat') clean jar --console=plain
if ($LASTEXITCODE -ne 0) { throw 'gradle jar 빌드 실패' }
if (-not (Test-Path $jar)) { throw "jar 를 찾지 못했습니다: $jar" }

Write-Host '[3/6] jlink 최소 런타임 생성'
$modules = (Invoke-Tool $jdeps @(
  '--print-module-deps', '--ignore-missing-deps', '--multi-release', "$($jdk.Major)", $jar
) 'jdeps 모듈 분석 실패' | Out-String).Trim()
if (-not $modules) { throw 'jdeps 가 모듈 목록을 반환하지 않았습니다.' }
Write-Host "      모듈: $modules"

# zip-9 압축은 JDK 21 부터 지원한다. 그 이전 버전은 숫자 레벨을 쓴다.
$compress = if ($jdk.Major -ge 21) { '--compress=zip-9' } else { '--compress=2' }
if (Test-Path $runtimeDir) { Remove-Item $runtimeDir -Recurse -Force }
Invoke-Tool $jlink @(
  '--add-modules', $modules, '--strip-debug', '--no-header-files', '--no-man-pages',
  $compress, '--output', $runtimeDir
) 'jlink 실패' | Out-Null

Write-Host '[4/6] jpackage app-image 생성'
if (Test-Path $imageParent) { Remove-Item $imageParent -Recurse -Force }
Invoke-Tool $jpackage @(
  '--type', 'app-image', '--name', $appName, '--app-version', $version,
  '--input', (Join-Path $buildDir 'libs'), '--main-jar', "$appName-$version.jar",
  '--main-class', 'Main', '--runtime-image', $runtimeDir, '--dest', $imageParent
) 'jpackage 실패' | Out-Null

Write-Host '[5/6] payload.zip 압축'
if (Test-Path $payload) { Remove-Item $payload -Force }
$sevenZip = @(
  "$env:ProgramFiles\7-Zip\7z.exe",
  "${env:ProgramFiles(x86)}\7-Zip\7z.exe"
) | Where-Object { Test-Path $_ } | Select-Object -First 1
if ($sevenZip) {
  Invoke-Tool $sevenZip @('a', '-tzip', '-mx=9', '-bso0', '-bsp0', $payload, "$imageDir\*") '7-Zip 압축 실패' | Out-Null
} else {
  Compress-Archive -Path (Join-Path $imageDir '*') -DestinationPath $payload -CompressionLevel Optimal
}

Write-Host '[6/6] 런처 exe 컴파일'
$fx = Join-Path $env:WINDIR 'Microsoft.NET\Framework64\v4.0.30319'
$csc = Join-Path $fx 'csc.exe'
if (-not (Test-Path $csc)) { throw "csc.exe 를 찾지 못했습니다: $csc" }

$launcherSrc = Get-Content (Join-Path $root 'launcher\Launcher.cs') -Raw
$generated = Join-Path $buildDir 'Launcher.generated.cs'
Set-Content -Path $generated -Value ($launcherSrc -replace '@APP_VERSION@', $version) -Encoding utf8

New-Item -ItemType Directory -Force -Path $distDir | Out-Null
if (Test-Path $outExe) { Remove-Item $outExe -Force }
Invoke-Tool $csc @(
  '/nologo', '/target:winexe', '/platform:anycpu', '/optimize+',
  "/reference:$fx\System.dll", "/reference:$fx\System.Drawing.dll",
  "/reference:$fx\System.Windows.Forms.dll",
  "/reference:$fx\System.IO.Compression.dll", "/reference:$fx\System.IO.Compression.FileSystem.dll",
  "/resource:$payload,payload.zip", "/out:$outExe", $generated
) '런처 컴파일 실패' | Out-Null

$sizeMb = [math]::Round((Get-Item $outExe).Length / 1MB, 1)
Write-Host ''
Write-Host "완료: $outExe ($sizeMb MB)"
Write-Host '이 exe 파일 하나만 복사해서 실행하면 됩니다.'
