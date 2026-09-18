using System;
using System.Diagnostics;
using System.IO;
using System.IO.Compression;
using System.Reflection;
using System.Text;
using System.Windows.Forms;

/// <summary>
/// 단일 exe 런처. 내장된 payload.zip(jpackage app-image)을 사용자 로컬 캐시에 한 번만 풀고
/// 실제 애플리케이션 exe 를 실행한다.
/// </summary>
internal static class Launcher
{
  private const string AppName = "xsd_generator";
  private const string AppVersion = "@APP_VERSION@";
  private const string ResourceName = "payload.zip";

  [STAThread]
  private static int Main(string[] args)
  {
    try
    {
      string home = Path.Combine(
          Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), AppName);
      string baseDir = Path.Combine(home, AppVersion);
      string targetExe = Path.Combine(baseDir, AppName + ".exe");
      string marker = Path.Combine(baseDir, ".ready");

      if (!File.Exists(marker) || !File.Exists(targetExe))
      {
        Unpack(home, baseDir, marker);
      }

      ProcessStartInfo psi = new ProcessStartInfo(targetExe);
      psi.UseShellExecute = false;
      psi.WorkingDirectory = Environment.CurrentDirectory;
      psi.Arguments = JoinArguments(args);
      Process.Start(psi);
      return 0;
    }
    catch (Exception e)
    {
      string log = Path.Combine(Path.GetTempPath(), AppName + "-launcher.log");
      try
      {
        File.WriteAllText(log, DateTime.Now.ToString("u") + Environment.NewLine + e.ToString());
      }
      catch (IOException)
      {
        log = "(로그 기록 실패)";
      }
      MessageBox.Show(
          e.Message + Environment.NewLine + Environment.NewLine + "자세한 내용: " + log,
          AppName + " 실행 실패", MessageBoxButtons.OK, MessageBoxIcon.Error);
      return 1;
    }
  }

  private static void Unpack(string home, string baseDir, string marker)
  {
    // 준비 완료 표시부터 지운다. 압축 해제가 중간에 실패하면 다음 실행 때 다시 시도한다.
    if (File.Exists(marker))
    {
      File.Delete(marker);
    }
    Directory.CreateDirectory(baseDir);

    using (Splash splash = Splash.Open())
    using (Stream payload = Assembly.GetExecutingAssembly().GetManifestResourceStream(ResourceName))
    {
      if (payload == null)
      {
        throw new InvalidOperationException("내장된 " + ResourceName + " 을(를) 찾을 수 없습니다.");
      }

      using (ZipArchive zip = new ZipArchive(payload, ZipArchiveMode.Read))
      {
        int total = zip.Entries.Count;
        int done = 0;
        foreach (ZipArchiveEntry entry in zip.Entries)
        {
          // 망가진 캐시가 남아 있어도 덮어쓰면서 복구한다.
          ExtractEntry(entry, Path.Combine(baseDir, entry.FullName));
          splash.Report(++done, total);
        }
      }
    }

    File.WriteAllText(marker, AppVersion);
    PurgeOtherVersions(home, baseDir);
  }

  private static void ExtractEntry(ZipArchiveEntry entry, string path)
  {
    if (entry.Name.Length == 0)
    {
      Directory.CreateDirectory(path);
      return;
    }

    Directory.CreateDirectory(Path.GetDirectoryName(path));
    for (int attempt = 1; ; attempt++)
    {
      try
      {
        if (File.Exists(path))
        {
          File.SetAttributes(path, FileAttributes.Normal);
        }
        entry.ExtractToFile(path, true);
        return;
      }
      catch (IOException)
      {
        if (attempt >= 3) { throw; }
      }
      catch (UnauthorizedAccessException)
      {
        if (attempt >= 3) { throw; }
      }
      // 백신 검사 등으로 파일이 잠시 잠긴 경우가 있어 잠깐 기다렸다 다시 시도한다.
      System.Threading.Thread.Sleep(300 * attempt);
    }
  }

  private static void PurgeOtherVersions(string home, string baseDir)
  {
    try
    {
      foreach (string dir in Directory.GetDirectories(home))
      {
        if (!string.Equals(dir, baseDir, StringComparison.OrdinalIgnoreCase))
        {
          SafeDelete(dir);
        }
      }
    }
    catch (IOException)
    {
      // 캐시 정리는 실패해도 실행에 영향이 없다.
    }
  }

  private static void SafeDelete(string dir)
  {
    try
    {
      if (Directory.Exists(dir))
      {
        Directory.Delete(dir, true);
      }
    }
    catch (Exception)
    {
      // 사용 중인 이전 버전은 다음 실행 때 정리된다.
    }
  }

  private static string JoinArguments(string[] args)
  {
    StringBuilder sb = new StringBuilder();
    foreach (string arg in args)
    {
      if (sb.Length > 0)
      {
        sb.Append(' ');
      }
      sb.Append('"').Append(arg.Replace("\"", "\\\"")).Append('"');
    }
    return sb.ToString();
  }

  /// <summary>최초 실행 시 압축 해제 진행 상황을 보여주는 간이 창.</summary>
  private sealed class Splash : IDisposable
  {
    private readonly Form form;
    private readonly ProgressBar bar;

    private Splash()
    {
      bar = new ProgressBar();
      bar.Location = new System.Drawing.Point(16, 44);
      bar.Size = new System.Drawing.Size(328, 18);
      bar.Maximum = 100;

      Label label = new Label();
      label.Text = "최초 실행 준비 중입니다. 잠시만 기다려 주세요.";
      label.Location = new System.Drawing.Point(16, 16);
      label.Size = new System.Drawing.Size(328, 20);

      form = new Form();
      form.Text = AppName;
      form.FormBorderStyle = FormBorderStyle.FixedDialog;
      form.ControlBox = false;
      form.StartPosition = FormStartPosition.CenterScreen;
      form.ClientSize = new System.Drawing.Size(360, 82);
      form.TopMost = true;
      form.Controls.Add(label);
      form.Controls.Add(bar);
    }

    internal static Splash Open()
    {
      Application.EnableVisualStyles();
      Splash splash = new Splash();
      splash.form.Show();
      Application.DoEvents();
      return splash;
    }

    internal void Report(int done, int total)
    {
      if (done % 40 != 0 && done != total)
      {
        return;
      }
      bar.Value = Math.Min(100, (int)(done * 100L / Math.Max(1, total)));
      Application.DoEvents();
    }

    public void Dispose()
    {
      form.Close();
      form.Dispose();
    }
  }
}
