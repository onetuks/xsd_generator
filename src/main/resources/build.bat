set "PATH=%PATH%;C:\Program Files (x86)\WiX Toolset v3.14\bin"

"C:\Users\onetu\.jdks\ms-21.0.10\bin\jpackage.exe" ^
  --name xsd_generator ^
  --app-version 2.0 ^
  --input build\libs ^
  --main-jar xsd_generator-2.0.jar ^
  --main-class Main ^
  --type exe ^
  --win-shortcut ^
  --win-menu ^
  --win-dir-chooser ^
  --dest dist ^
  --verbose