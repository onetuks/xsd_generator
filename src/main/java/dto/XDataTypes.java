package dto;

import java.util.List;

public class XDataTypes {

  private final String dataTypeName;
  private final String namespace;
  private final String filePath;
  private final List<XDataType> dataTypes;

  public XDataTypes(
      String dataTypeName, String namespace, String filePath, List<XDataType> dataTypes) {
    this.dataTypeName = dataTypeName;
    this.namespace = namespace;
    this.filePath = filePath;
    this.dataTypes = dataTypes;
  }

  public XDataType findParent(XDataType target) {
    XDataType parent = new XDataType(dataTypeName);

    int startIndex = dataTypes.indexOf(target);
    for (int i = startIndex - 1; i >= 0; i--) {
      if (dataTypes.get(i).getLevel() < target.getLevel()) {
        return dataTypes.get(i);
      }
    }

    return parent;
  }

  public String getDataTypeName() {
    return dataTypeName;
  }

  public String getNamespace() {
    return namespace;
  }

  public String getFilePath() {
    return filePath;
  }

  public List<XDataType> getDataTypes() {
    return dataTypes;
  }
}
