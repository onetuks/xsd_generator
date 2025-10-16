package core;

import definition.DataTypeElement;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataTypeFieldParser {

  public static final String NA = "N/A";
  public static final String NEW_LINE = "\n";
  private static final String BLANK = "";

  public List<DataTypeElement> parseData(String nameText, String descriptionText) {
    String[] names = nameText.trim().split("\n");
    String[] descriptions = descriptionText.split("\n");

    return IntStream.range(0, names.length)
        .filter(i -> {
          String name = names[i].trim().replaceAll(NA, BLANK);
          names[i] = name;
          return !name.isEmpty();
        })
        .mapToObj(i -> {
          String name = names[i];
          String description = descriptions.length <= i
              ? ""
              : descriptions[i].trim().replaceAll(NA, BLANK);
          return new DataTypeElement(name, description);
        })
        .collect(Collectors.toList());
  }
}
