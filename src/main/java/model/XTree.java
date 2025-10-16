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
    private final DataTypeNode root;

    public XTree(String dataTypeName, String namespace, String filePath) {
        validateDataTypeName(dataTypeName);
        validateNamespace(namespace);
        this.namespace = namespace;
        this.filePath = filePath;
        this.root = DataTypeNode.of(dataTypeName, null, Category.COMPLEX_TYPE, null, null);
    }

    private XTree(String namespace, String filePath, DataTypeNode root) {
        this.namespace = namespace;
        this.filePath = filePath;
        this.root = root;
    }

    public static XTree of(XTree tree) {
        return new XTree(tree.getNamespace(), tree.getFilePath(), DataTypeNode.of(tree.getRoot()));
    }

    public void removeNode(DataTypeNode node) {
        List<DataTypeNode> children = node.children();
        List<DataTypeNode> siblings = findParent(node.entity().getName()).children();
        siblings.remove(node);
        siblings.addAll(children);
    }

    public DataTypeNode findNode(String name) {
        Queue<DataTypeNode> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            DataTypeNode node = queue.poll();

            if (Objects.equals(node.entity().getName(), name)) {
                return node;
            }

            List<DataTypeNode> reversedChildren = new ArrayList<>(node.children());
            Collections.reverse(reversedChildren);
            queue.addAll(reversedChildren);
        }

        return null;
    }

    public DataTypeNode findParent(String name) {
        Queue<DataTypeNode> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            DataTypeNode node = queue.poll();

            boolean hasTarget = node.children().stream()
                    .anyMatch(child -> Objects.equals(name, child.entity().getName()));
            if (hasTarget) {
                return node;
            }

            queue.addAll(node.children());
        }

        return root;
    }

    public void addChild(String parentName, String childName) {
        DataTypeNode node = findNode(childName);
        DataTypeNode parent = findNode(parentName);
        this.removeNode(node);
        parent.addChild(node);
    }

    public void addSibling(String siblingName, String childName) {
        DataTypeNode node = findNode(childName);
        DataTypeNode parent = findParent(siblingName);
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

    public DataTypeNode getRoot() {
        return root;
    }

    public String getFileFullPath() {
        return filePath + root.entity().getName();
    }
}
