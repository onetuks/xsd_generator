package model;

import java.util.Objects;

public class DataTypeMeta {

  private static final String FORBIDDEN_FILENAME_CHARS_PATTERN = "[\\\\/:*?\"<>|]";

  private final String mtName;
  private final String dtName;
  private final String namespace;
  private final String filePath;

  public DataTypeMeta(String mtName, String dtName, String namespace, String filePath) {
    this.mtName = mtName;
    this.dtName = dtName;
    this.namespace = namespace;
    this.filePath = filePath;

    validate(mtName, dtName, namespace, filePath);
  }

  private void validate(String mtName, String dtName, String namespace, String filePath) {
    if (Objects.isNull(dtName) || dtName.trim().isEmpty()) {
      throw new IllegalArgumentException("DT Name을 입력해주세요.");
    } else if (isInvalidFilename(dtName)) {
      throw new IllegalArgumentException(
          "DT Name에 파일명으로 사용할 수 없는 문자(\\ / : * ? \" < > |)나 상대 경로(..)는 사용할 수 없습니다.");
    } else if (!Objects.isNull(mtName) && !mtName.trim().isEmpty() && isInvalidFilename(mtName)) {
      throw new IllegalArgumentException(
          "MT Name에 파일명으로 사용할 수 없는 문자(\\ / : * ? \" < > |)나 상대 경로(..)는 사용할 수 없습니다.");
    } else if (Objects.isNull(namespace) || namespace.trim().isEmpty()) {
      throw new IllegalArgumentException("Namespace를 입력해주세요.");
    } else if (Objects.isNull(filePath) || filePath.trim().isEmpty()) {
      throw new IllegalArgumentException("Target Dir을 선택해주세요.");
    }
  }

  private boolean isInvalidFilename(String name) {
    return name.contains("..") || name.matches(".*" + FORBIDDEN_FILENAME_CHARS_PATTERN + ".*");
  }

  public String getMtName() {
    return mtName;
  }

  public String getDtName() {
    return dtName;
  }

  public String getFilePath() {
    return filePath;
  }

  public String getNamespace() {
    return namespace;
  }
}
