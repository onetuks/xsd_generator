package core;

import model.DataTypeNode;
import model.DataTypeState;
import model.vo.Category;
import model.vo.Occurrence;
import model.vo.Type;
import specification.elements.DataTypeElement;

import java.util.*;
import java.util.stream.IntStream;
import util.FileSaver;

public class DataTypePipelineService {

    private final XsdGenerator xsdGenerator = new XsdGenerator();
    private final FileSaver fileSaver = new FileSaver();

    private final DataTypeState state = new DataTypeState();

    public void generateXSDFile() {
        String xsdString = xsdGenerator.generate(state.getMeta(), state.getRootNode());
        fileSaver.saveFile(state.getMeta().getFilePath() + state.getMeta().getDtName(), xsdString);
    }

    public void updateDataTypeElements(
            String mtName, String dtName, String namespace, String targetDir,
            List<DataTypeElement> dataTypeElements) {
        state.setMeta(mtName, dtName, namespace, targetDir);
        state.setElements(dataTypeElements);
    }

    /**
     * DataTypeElement -> DataTypeNode
     * - DT명을 최상위 루트 노드로 추가
     */
    public void updateDataTypeNode(List<DataTypeElement> elements) {
        DataTypeElement rootElement = new DataTypeElement(state.getMeta().getDtName());
        state.setRootNode(
                DataTypeNode.of(
                        rootElement.getId(), rootElement.getName(), rootElement.getDescription(),
                        rootElement.getCategory(), rootElement.getType(), rootElement.getOccurrence()));

        IntStream.range(0, elements.size())
                .forEach(idx -> {
                    DataTypeElement currentElement = elements.get(idx);
                    DataTypeNode currentNode = DataTypeNode.of(
                            currentElement.getId(), currentElement.getName(), currentElement.getDescription(),
                            currentElement.getCategory(), currentElement.getType(), currentElement.getOccurrence());

                    DataTypeElement parentElement = findParentElement(idx, elements);
                    if (parentElement == null) {
                        parentElement = rootElement;
                    }
                    DataTypeNode parentNode = Objects.requireNonNull(findNode(parentElement));
                    parentNode.addChild(currentNode);

                    currentElement.getAttributes().stream()
                            .map(attribute ->
                                    DataTypeNode.of(
                                            UUID.randomUUID(),
                                            attribute.getName(),
                                            null,
                                            Category.ATTRIBUTE,
                                            Type.STRING,
                                            Occurrence.ofOptional()))
                            .forEach(currentNode::addChild);
                });
    }

    public void addChildTo(DataTypeNode parentNode, DataTypeNode childNode) {
        DataTypeNode originParentNode = findParentNode(childNode);

        Objects.requireNonNull(originParentNode).getChildren().remove(childNode);
        Objects.requireNonNull(parentNode).getChildren().add(childNode);
    }

    public void addSiblingTo(DataTypeNode olderNode, DataTypeNode youngerNode) {
        DataTypeNode olderParentNode = findParentNode(olderNode);
        DataTypeNode youngerParentNode = findParentNode(youngerNode);

        Objects.requireNonNull(olderParentNode).getChildren().add(youngerNode);
        Objects.requireNonNull(youngerParentNode).getChildren().remove(youngerNode);
    }

    private DataTypeNode findNode(DataTypeElement element) {
        Queue<DataTypeNode> queue = new ArrayDeque<>();
        queue.add(state.getRootNode());

        while (!queue.isEmpty()) {
            DataTypeNode node = queue.poll();

            if (Objects.equals(node.getEntity().getId(), element.getId())) {
                return node;
            }

            queue.addAll(node.getChildren());
        }

        return null;
    }

    private DataTypeNode findParentNode(DataTypeNode targetNode) {
        Queue<DataTypeNode> queue = new ArrayDeque<>();
        queue.add(state.getRootNode());

        while (!queue.isEmpty()) {
            DataTypeNode node = queue.poll();

            boolean isParent = node.getChildren().stream()
                    .anyMatch(child -> Objects.equals(child, targetNode));
            if (isParent) {
                return node;
            }

            queue.addAll(node.getChildren());
        }

        return null;
    }

    private DataTypeElement findParentElement(int idx, List<DataTypeElement> elements) {
        for (int i = idx - 1; i >= 0; i--) {
            if (elements.get(i).getLevel() < elements.get(idx).getLevel()) {
                return elements.get(i);
            }
        }
        return null;
    }

    public List<DataTypeElement> getDataTypeElements() {
        return state.getElements();
    }

    public DataTypeNode getRootNode() {
        return state.getRootNode();
    }
}
