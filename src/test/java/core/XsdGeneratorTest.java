package core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import model.DataTypeMeta;
import model.DataTypeNode;
import model.vo.Category;
import model.vo.Occurrence;
import model.vo.Type;
import org.junit.jupiter.api.Test;

class XsdGeneratorTest {

  private final XsdGenerator xsdGenerator = new XsdGenerator();

  @Test
  void generateDT_는_필드를_포함한_XSD_문자열을_생성한다() {
    DataTypeMeta meta = new DataTypeMeta("", "SampleDT", "urn:sample", "D:/target");
    DataTypeNode root = DataTypeNode.of(
        UUID.randomUUID(), "SampleDT", "", Category.COMPLEX_TYPE, null, null);
    root.addChild(DataTypeNode.of(
        UUID.randomUUID(), "fieldName", "설명", Category.ELEMENT, Type.STRING,
        Occurrence.ofZeroOne()));

    String xsd = xsdGenerator.generateDT(meta, root);

    assertTrue(xsd.contains("<xsd:complexType name=\"SampleDT\">"));
    assertTrue(xsd.contains("name=\"fieldName\""));
    assertTrue(xsd.contains("<xsd:documentation>설명</xsd:documentation>"));
  }

  @Test
  void generateMT_는_MT명이_있으면_MessageType_엘리먼트를_추가한다() {
    DataTypeMeta meta = new DataTypeMeta("SampleMT", "SampleDT", "urn:sample", "D:/target");
    DataTypeNode root = DataTypeNode.of(
        UUID.randomUUID(), "SampleDT", "", Category.COMPLEX_TYPE, null, null);

    String mt = xsdGenerator.generateMT(meta, root);

    assertTrue(mt.contains("<xsd:element name=\"SampleMT\" type=\"SampleDT\"/>"));
  }

  @Test
  void generateMT_는_MT명이_없으면_MessageType_엘리먼트를_추가하지_않는다() {
    DataTypeMeta meta = new DataTypeMeta("", "SampleDT", "urn:sample", "D:/target");
    DataTypeNode root = DataTypeNode.of(
        UUID.randomUUID(), "SampleDT", "", Category.COMPLEX_TYPE, null, null);

    String mt = xsdGenerator.generateMT(meta, root);

    assertFalse(mt.contains("type=\"SampleDT\"/>"));
  }

  @Test
  void 필드명과_설명에_포함된_특수문자는_XML_이스케이프_처리된다() {
    DataTypeMeta meta = new DataTypeMeta("", "SampleDT", "urn:sample", "D:/target");
    DataTypeNode root = DataTypeNode.of(
        UUID.randomUUID(), "SampleDT", "", Category.COMPLEX_TYPE, null, null);
    root.addChild(DataTypeNode.of(
        UUID.randomUUID(), "field", "A & B <injected>\"quoted\"", Category.ELEMENT, Type.STRING,
        Occurrence.ofZeroOne()));

    String xsd = xsdGenerator.generateDT(meta, root);

    assertFalse(xsd.contains("<injected>"));
    assertTrue(xsd.contains("A &amp; B &lt;injected&gt;&quot;quoted&quot;"));
  }

  @Test
  void 네임스페이스에_포함된_특수문자도_이스케이프_처리된다() {
    DataTypeMeta meta = new DataTypeMeta("", "SampleDT", "urn:sample&\"ns\"", "D:/target");
    DataTypeNode root = DataTypeNode.of(
        UUID.randomUUID(), "SampleDT", "", Category.COMPLEX_TYPE, null, null);

    String xsd = xsdGenerator.generateDT(meta, root);

    assertTrue(xsd.contains("urn:sample&amp;&quot;ns&quot;"));
  }
}
