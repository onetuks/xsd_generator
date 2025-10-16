package core;

import model.DataTypeState;
import model.DataTypeNode;
import specification.elements.DataTypeElement;

import java.util.*;
import java.util.stream.IntStream;

public class DataTypePipelineService {

    private final DataTypeState state = new DataTypeState();

    public void updateDataTypeState(
            String dtName, String namespace, String targetDir,
            List<DataTypeElement> dataTypeElements) {
        state.setMeta(dtName, namespace, targetDir);
        state.setElements(dataTypeElements);
    }

    public void updateDataTypeNode(List<DataTypeElement> elements) {
        IntStream.of(0, elements.size())
                .forEach(idx -> {
                    DataTypeElement currentElement = elements.get(idx);
                    DataTypeNode currentNode = DataTypeNode.of(
                            currentElement.getName(), currentElement.getDescription(),
                            currentElement.getCategory(), currentElement.getType(), currentElement.getOccurrence());

                    DataTypeElement parentElement = findParentElement(idx, elements);
                    DataTypeNode parentNode = Objects.requireNonNull(findNode(parentElement));

                    parentNode.addChild(currentNode);
                });
    }

    private DataTypeNode findNode(DataTypeElement element) {
        Queue<DataTypeNode> queue = new ArrayDeque<>();
        queue.add(state.getRootNode());

        while (!queue.isEmpty()) {
            DataTypeNode node = queue.poll();

            if (Objects.equals(node.entity().getName(), element.getName())) {
                return node;
            }

            List<DataTypeNode> reversedChildren = new ArrayList<>(node.children());
            Collections.reverse(reversedChildren);
            queue.addAll(reversedChildren);
        }

        return null;
    }

    private DataTypeElement findParentElement(int idx, List<DataTypeElement> elements) {
        for (int i = idx - 1; i >= 0; i--) {
            if (elements.get(i).getLevel() < elements.get(idx).getLevel()) {
                return elements.get(i);
            }
        }
        return elements.get(idx);
    }

    public List<DataTypeElement> getDataTypeElement() {
        return state.getElements();
    }

    public DataTypeNode getRootNode() {
        return state.getRootNode();
    }
}
