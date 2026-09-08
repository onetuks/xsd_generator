package core;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;
import java.util.stream.IntStream;
import model.DataTypeNode;
import model.DataTypeState;
import model.vo.Category;
import model.vo.Occurrence;
import model.vo.Type;
import specification.elements.DataTypeElement;
import util.FileSaver;

public class DataTypePipelineService {

  private final XsdGenerator xsdGenerator = new XsdGenerator();
  private final FileSaver fileSaver = new FileSaver();

  private final DataTypeState state = new DataTypeState();

  public void generateXSDFile() {
    fileSaver.saveFile(state.getMeta().getFilePath(), state.getMeta().getDtName(), previewDT());

    String mtXsdString = previewMT();
    if (mtXsdString != null) {
      fileSaver.saveFile(state.getMeta().getFilePath(), state.getMeta().getMtName(), mtXsdString);
    }
  }

  /**
   * 파일로 저장하기 전에 DT XSD 내용을 미리 확인하기 위해 사용한다.
   */
  public String previewDT() {
    return xsdGenerator.generateDT(state.getMeta(), state.getRootNode());
  }

  /**
   * 파일로 저장하기 전에 MT XSD 내용을 미리 확인하기 위해 사용한다. MT Name이 없으면 null을 반환한다.
   */
  public String previewMT() {
    if (state.getMeta().getMtName().isEmpty()) {
      return null;
    }
    return xsdGenerator.generateMT(state.getMeta(), state.getRootNode());
  }

  /**
   * generateXSDFile()이 만들 DT/MT 파일 중 이미 존재하는 파일이 있는지 확인한다.
   * 저장 전에 덮어쓰기 여부를 사용자에게 확인받기 위해 사용한다.
   */
  public boolean willOverwriteExistingFile() {
    boolean dtExists = fileSaver.exists(state.getMeta().getFilePath(), state.getMeta().getDtName());
    boolean mtExists = !state.getMeta().getMtName().isEmpty()
        && fileSaver.exists(state.getMeta().getFilePath(), state.getMeta().getMtName());
    return dtExists || mtExists;
  }

  public void updateDataTypeElements(
      String mtName, String dtName, String namespace, String targetDir,
      List<DataTypeElement> dataTypeElements) {
    state.setMeta(mtName, dtName, namespace, targetDir);
    state.setElements(dataTypeElements);
  }

  /**
   * DataTypeElement -> DataTypeNode - DT명을 최상위 루트 노드로 추가
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
              currentElement.getCategory(), currentElement.getType(),
              currentElement.getOccurrence());

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

  /**
   * XSD 파일이 저장되는 디렉터리 경로. 저장 완료 후 탐색기로 바로 열어주는 등의 용도로 사용한다.
   */
  public String getTargetDir() {
    return state.getMeta().getFilePath();
  }

  public DataTypeNode getRootNode() {
    return state.getRootNode();
  }
}
