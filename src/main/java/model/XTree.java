package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import model.vo.Category;

public class XTree {

  private final String namespace;
  private final String filePath;
  private final XNode root;

  public XTree(String dataTypeName, String namespace, String filePath) {
    validateDataTypeName(dataTypeName);
    validateNamespace(namespace);
    this.namespace = namespace;
    this.filePath = filePath;
    this.root = XNode.of(dataTypeName, null, Category.COMPLEX_TYPE, null, null);
  }

  private XTree(String namespace, String filePath, XNode root) {
    this.namespace = namespace;
    this.filePath = filePath;
    this.root = root;
  }

  public static XTree of(XTree tree) {
    return new XTree(tree.getNamespace(), tree.getFilePath(), XNode.of(tree.getRoot()));
  }

  public void removeNode(XNode node) {
    List<XNode> children = node.getChildren();
    List<XNode> siblings = findParent(node.getEntity().getName()).getChildren();
    siblings.remove(node);
    siblings.addAll(children);
  }

  public XNode findNode(String name) {
    Queue<XNode> queue = new ArrayDeque<>();
    queue.add(root);

    while (!queue.isEmpty()) {
      XNode node = queue.poll();

      if (Objects.equals(node.getEntity().getName(), name)) {
        return node;
      }

      List<XNode> reversedChildren = new ArrayList<>(node.getChildren());
      Collections.reverse(reversedChildren);
      queue.addAll(reversedChildren);
    }

    return null;
  }

  public XNode findParent(String name) {
    Queue<XNode> queue = new ArrayDeque<>();
    queue.add(root);

    while (!queue.isEmpty()) {
      XNode node = queue.poll();

      boolean hasTarget = node.getChildren().stream()
          .anyMatch(child -> Objects.equals(name, child.getEntity().getName()));
      if (hasTarget) {
        return node;
      }

      queue.addAll(node.getChildren());
    }

    return root;
  }

  public void addChild(String parentName, String childName) {
    XNode node = findNode(childName);
    XNode parent = findNode(parentName);
    this.removeNode(node);
    parent.addChild(node);
  }

  public void addSibling(String siblingName, String childName) {
    XNode node = findNode(childName);
    XNode parent = findParent(siblingName);
    this.removeNode(node);
    parent.addChild(node);
  }

  private void validateDataTypeName(String dataTypeName) {
    if (Objects.isNull(dataTypeName) || dataTypeName.trim().isEmpty()) {
      throw new IllegalArgumentException("Invalid data type name: " + dataTypeName);
    }
  }

  private void validateNamespace(String namespace) {
    if (Objects.isNull(namespace) || namespace.trim().isEmpty()) {
      throw new IllegalArgumentException("Invalid namespace: " + namespace);
    }
  }

  public String getNamespace() {
    return namespace;
  }

  public String getFilePath() {
    return filePath;
  }

  public XNode getRoot() {
    return root;
  }

  public String getFileFullPath() {
    return filePath + root.getEntity().getName();
  }
}
