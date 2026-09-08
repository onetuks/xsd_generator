package util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSaver {

  private static final String FILE_EXTENSION = ".xsd";

  public boolean exists(String dirPath, String filename) {
    return Files.exists(resolveFilePath(dirPath, filename));
  }

  public void saveFile(String dirPath, String filename, String xsdString) {
    try {
      Path filePath = resolveFilePath(dirPath, filename);
      // XSD 선언부(<?xml ... encoding="UTF-8"?>)와 실제 파일 인코딩을 일치시켜야
      // 한글 등 비ASCII 설명이 파일에서 깨지지 않는다.
      Files.write(filePath, xsdString.getBytes(StandardCharsets.UTF_8));
      System.out.println("Saved to " + filePath);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private Path resolveFilePath(String dirPath, String filename) {
    return Paths.get(dirPath, filename + FILE_EXTENSION);
  }
}
