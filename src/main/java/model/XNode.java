package model;

import dto.XDataType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import model.vo.Category;
import model.vo.Occurrence;
import model.vo.Type;

public class XNode {

  private final XEntity entity;
  private final List<XNode> children;

  public static XNode of(
      String name, String description, Category category, Type type, Occurrence occurrence) {
    return new XNode(new XEntity(name, description, category, type, occurrence));
  }

  public static XNode of(XNode originNode) {
    return new XNode(
        new XEntity(
            originNode.getEntity().getName(),
            originNode.getEntity().getDescription(),
            originNode.getEntity().getCategory(),
            originNode.getEntity().getType(),
            originNode.getEntity().getOccurrence()),
        originNode.getChildren().stream()
            .map(XNode::of)
            .collect(Collectors.toList())
    );
  }

  private XNode(XEntity entity) {
    this.entity = entity;
    this.children = new ArrayList<>();
  }

  private XNode(XEntity entity, List<XNode> children) {
    this.entity = entity;
    this.children = children;
  }

  public void addChild(XNode node) {
    boolean isLeafNode = node.getChildren().isEmpty();
    boolean isNotStatementNameDataType = !node.getEntity().getName().contains(XDataType.STATEMENT);
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

  public XEntity getEntity() {
    return entity;
  }

  public List<XNode> getChildren() {
    return children;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XNode xNode = (XNode) o;
    return Objects.equals(entity, xNode.entity);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(entity);
  }
}
