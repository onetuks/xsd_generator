package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import model.DataTypeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import specification.elements.DataTypeElement;

class DataTypePipelineServiceTest {

  private DataTypePipelineService service;

  @BeforeEach
  void setUp() {
    service = new DataTypePipelineService();
    service.updateDataTypeElements("", "SampleDT", "urn:sample", "D:/target",
        Collections.emptyList());
  }

  private DataTypeNode findChildByName(DataTypeNode parent, String name) {
    return parent.getChildren().stream()
        .filter(child -> child.getEntity().getName().equals(name))
        .findFirst()
        .orElseThrow(() -> new AssertionError(name + " 노드를 찾을 수 없음: " + parent.getChildren()));
  }

  @Test
  void 레벨_규칙에_따라_필드가_계층적으로_배치된다() {
    DataTypeElement statement = new DataTypeElement("StatementName1", "");
    DataTypeElement table = new DataTypeElement("dbTableName", "");
    DataTypeElement access = new DataTypeElement("access", "");
    List<DataTypeElement> elements = Arrays.asList(statement, table, access);

    service.updateDataTypeNode(elements);

    DataTypeNode root = service.getRootNode();
    DataTypeNode statementNode = findChildByName(root, "StatementName1");
    DataTypeNode tableNode = findChildByName(statementNode, "dbTableName");
    DataTypeNode accessNode = findChildByName(tableNode, "access");

    assertTrue(accessNode.getChildren().isEmpty());
  }

  @Test
  void addChildTo는_노드를_기존_부모에서_제거하고_새_부모의_자식으로_옮긴다() {
    DataTypeElement statement = new DataTypeElement("StatementName1", "");
    DataTypeElement table = new DataTypeElement("dbTableName", "");
    DataTypeElement access = new DataTypeElement("access", "");
    service.updateDataTypeNode(Arrays.asList(statement, table, access));

    DataTypeNode root = service.getRootNode();
    DataTypeNode statementNode = findChildByName(root, "StatementName1");
    DataTypeNode tableNode = findChildByName(statementNode, "dbTableName");
    DataTypeNode accessNode = findChildByName(tableNode, "access");

    service.addChildTo(root, accessNode);

    assertTrue(root.getChildren().contains(accessNode));
    assertFalse(tableNode.getChildren().contains(accessNode));
  }

  @Test
  void addSiblingTo는_대상_노드를_older의_부모_아래로_옮긴다() {
    DataTypeElement statement = new DataTypeElement("StatementName1", "");
    DataTypeElement table = new DataTypeElement("dbTableName", "");
    DataTypeElement access = new DataTypeElement("access", "");
    service.updateDataTypeNode(Arrays.asList(statement, table, access));

    DataTypeNode root = service.getRootNode();
    DataTypeNode statementNode = findChildByName(root, "StatementName1");
    DataTypeNode tableNode = findChildByName(statementNode, "dbTableName");
    DataTypeNode accessNode = findChildByName(tableNode, "access");

    service.addSiblingTo(statementNode, accessNode);

    assertTrue(root.getChildren().contains(accessNode));
    assertFalse(tableNode.getChildren().contains(accessNode));
  }

  @Test
  void previewMT는_MT명이_없으면_null을_반환한다() {
    service.updateDataTypeNode(Collections.emptyList());

    assertNull(service.previewMT());
  }

  @Test
  void previewDT는_저장하지_않고_XSD_문자열만_반환한다() {
    service.updateDataTypeNode(Collections.emptyList());

    String preview = service.previewDT();

    assertTrue(preview.contains("<xsd:complexType name=\"SampleDT\">"));
  }

  @Test
  void updateDataTypeElements는_필드_목록과_메타정보를_저장한다() {
    DataTypeElement field = new DataTypeElement("field", "설명");
    service.updateDataTypeElements("SampleMT", "SampleDT", "urn:sample", "D:/target",
        Collections.singletonList(field));

    assertEquals(1, service.getDataTypeElements().size());
    assertEquals("field", service.getDataTypeElements().get(0).getName());
  }
}
