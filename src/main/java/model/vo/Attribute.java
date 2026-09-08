package model.vo;

import java.util.Arrays;
import java.util.Objects;

public enum Attribute {
  ACTION("action", "이 필드가 수행할 동작(SELECT/INSERT/UPDATE 등)을 나타내는 SAP PO Jdbc 어댑터 속성"),
  HAS_QUOT("hasQuot", "값을 따옴표로 감쌀지 여부를 나타내는 SAP PO Jdbc 어댑터 속성"),
  IS_INPUT("isInput", "이 필드가 입력 파라미터로 사용되는지를 나타내는 SAP PO Jdbc 어댑터 속성"),
  IS_OUTPUT("isOutput", "이 필드가 출력 파라미터로 사용되는지를 나타내는 SAP PO Jdbc 어댑터 속성");

  private final String name;
  private final String description;

  Attribute(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public static boolean isAttribute(String name) {
    return Arrays.stream(Attribute.values())
        .anyMatch(attribute -> Objects.equals(attribute.getName(), name));
  }

  public static boolean hasAttributeExceptAction(String attributeName) {
    return Arrays.stream(Attribute.values())
        .filter(attribute -> !Objects.equals(attribute.getName(), ACTION.getName()))
        .anyMatch(attribute -> Objects.equals(attribute.getName(), attributeName));
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}
