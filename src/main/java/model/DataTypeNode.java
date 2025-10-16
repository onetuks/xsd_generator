package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import model.vo.Category;
import model.vo.Occurrence;
import model.vo.Type;
import specification.elements.DataTypeElement;

public record DataTypeNode(DataTypeEntity entity, List<DataTypeNode> children) {

    public static DataTypeNode of(
            String name, String description, Category category, Type type, Occurrence occurrence) {
        return new DataTypeNode(new DataTypeEntity(name, description, category, type, occurrence));
    }

    public static DataTypeNode of(DataTypeNode originNode) {
        return new DataTypeNode(
                new DataTypeEntity(
                        originNode.entity().getName(),
                        originNode.entity().getDescription(),
                        originNode.entity().getCategory(),
                        originNode.entity().getType(),
                        originNode.entity().getOccurrence()),
                originNode.children().stream()
                        .map(DataTypeNode::of)
                        .collect(Collectors.toList())
        );
    }

    private DataTypeNode(DataTypeEntity entity) {
        this(entity, new ArrayList<>());
    }

    public void addChild(DataTypeNode node) {
        boolean isLeafNode = node.children().isEmpty();
        boolean isNotStatementNameDataType = !node.entity().getName().contains(DataTypeElement.STATEMENT);
        if (isLeafNode && isNotStatementNameDataType) {
            int targetIndex = IntStream.range(0, children.size())
                    .filter(i -> !children.get(i).children().isEmpty())
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
        DataTypeNode node = (DataTypeNode) o;
        return Objects.equals(entity, node.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entity);
    }
}
