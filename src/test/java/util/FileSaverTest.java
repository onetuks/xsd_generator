package util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileSaverTest {

  private final FileSaver fileSaver = new FileSaver();

  @Test
  void 저장하기_전에는_파일이_존재하지_않는다(@TempDir Path tempDir) {
    assertFalse(fileSaver.exists(tempDir.toString(), "SampleDT"));
  }

  @Test
  void 저장한_뒤에는_파일이_존재한다(@TempDir Path tempDir) {
    fileSaver.saveFile(tempDir.toString(), "SampleDT", "<xsd/>");

    assertTrue(fileSaver.exists(tempDir.toString(), "SampleDT"));
  }

  @Test
  void UTF8로_한글이_깨지지_않게_저장된다(@TempDir Path tempDir) throws IOException {
    fileSaver.saveFile(tempDir.toString(), "SampleDT", "한글 설명");

    String content = new String(
        Files.readAllBytes(tempDir.resolve("SampleDT.xsd")), StandardCharsets.UTF_8);
    assertTrue(content.contains("한글 설명"));
  }
}
