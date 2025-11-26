package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;
import model.vo.Category;
import model.vo.Occurrence;
import model.vo.Type;
import specification.elements.DataTypeElement;

public class DataTypeNode {

  private final DataTypeEntity entity;
  private final List<DataTypeNode> children;

  private DataTypeNode(DataTypeEntity entity, List<DataTypeNode> children) {
    this.entity = entity;
    this.children = children;
  }

  public static DataTypeNode of(
      UUID id, String name, String description, Category category, Type type,
      Occurrence occurrence) {
    return new DataTypeNode(
        new DataTypeEntity(id, name, description, category, type, occurrence),
        new ArrayList<>());
  }

  public void addChild(DataTypeNode node) {
    boolean isLeafNode = node.getChildren().isEmpty();
    boolean isNotStatementNameDataType = !node.getEntity().getName()
        .contains(DataTypeElement.STATEMENT);
    if (isLeafNode && isNotStatementNameDataType) {
      int targetIndex = IntStream.range(0, children.size())
          .filter(i -> !children.get(i).getChildren().isEmpty())
          .findFirst()
          .orElse(children.size());
      children.add(targetIndex, node);
      return;
    }

    children.add(node);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataTypeNode that = (DataTypeNode) o;
    return Objects.equals(entity.getId(), that.entity.getId());
  }

  @Override
  public int hashCode() {
    return entity.getId().hashCode();
  }

  @Override
  public String toString() {
    return entity.getName();
  }

  public List<DataTypeNode> getChildren() {
    return children;
  }

  public DataTypeEntity getEntity() {
    return entity;
  }
}
