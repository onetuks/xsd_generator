package model;

import java.util.ArrayList;
import java.util.List;
import specification.elements.DataTypeElement;

public class DataTypeState {

  private DataTypeMeta meta;

  private List<DataTypeElement> elements = new ArrayList<>();
  private DataTypeNode rootNode = null;

  public DataTypeMeta getMeta() {
    return meta;
  }

  public void setMeta(String mtName, String dtName, String namespace, String filePath) {
    this.meta = new DataTypeMeta(mtName, dtName, namespace, filePath);
  }

  public List<DataTypeElement> getElements() {
    return elements;
  }

  public void setElements(List<DataTypeElement> dataTypeElements) {
    this.elements = dataTypeElements;
  }

  public DataTypeNode getRootNode() {
    return rootNode;
  }

  public void setRootNode(DataTypeNode rootNode) {
    this.rootNode = rootNode;
  }
}
