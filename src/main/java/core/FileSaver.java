package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;

public class FileSaver {

  private static final String FILE_EXTENSION = ".xsd";

  public void saveFile(String filePath, String xsdString) {
    try {
      File file = new File(filePath + FILE_EXTENSION);
      if (!file.exists()) {
        boolean isCreated = file.createNewFile();
        if (!isCreated) {
          throw new IOException("Could not create file " + filePath);
        }
      }

      try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
        bw.write(xsdString);
        bw.flush();
        System.out.println("Saved to " + filePath);
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
