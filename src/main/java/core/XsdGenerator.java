package core;

import model.DataTypeMeta;
import model.DataTypeNode;
import model.vo.Attribute;
import model.vo.Category;

public class XsdGenerator {

  private static final String INDENT_UNIT = "  ";

  private static final String XML_META_TAG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
  private static final String SCHEMA_TAG = "<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"%s\" targetNamespace=\"%s\">";
  private static final String SCHEMA_END = "</xsd:schema>";
  private static final String ROOT_COMPLEX_TYPE_TAG = "<xsd:complexType name=\"%s\">";
  private static final String COMPLEX_TYPE_TAG = "<xsd:complexType>";
  private static final String COMPLEX_TYPE_END = "</xsd:complexType>";
  private static final String SEQUENCE_TAG = "<xsd:sequence>";
  private static final String SEQUENCE_END = "</xsd:sequence>";
  private static final String DEFAULT_ELEMENT_TAG = "<xsd:element name=\"%s\" type=\"%s\"/>";
  private static final String WRAPPER_ELEMENT_TAG = "<xsd:element name=\"%s\" minOccurs=\"%s\" maxOccurs=\"%s\">";
  private static final String CONTENT_ELEMENT_TAG = "<xsd:element name=\"%s\" type=\"%s\" minOccurs=\"%s\" maxOccurs=\"%s\">";
  private static final String ELEMENT_END = "</xsd:element>";
  private static final String SIMPLE_CONTENT_TAG = "<xsd:simpleContent>";
  private static final String SIMPLE_CONTENT_END = "</xsd:simpleContent>";
  private static final String EXTENSION_TAG = "<xsd:extension base=\"%s\">";
  private static final String EXTENSION_END = "</xsd:extension>";
  private static final String ATTRIBUTE_TAG = "<xsd:attribute name=\"%s\" type=\"%s\"/>";
  private static final String DESCRIPTION_TAG = "<xsd:annotation><xsd:documentation>%s</xsd:documentation></xsd:annotation>";

  public String generateDT(DataTypeMeta meta, DataTypeNode root) {
    String namespace = escapeXml(meta.getNamespace());
    return XML_META_TAG
        + indent(0) + String.format(SCHEMA_TAG, namespace, namespace)
        + generateXsdString(root, 1)
        + indent(0) + SCHEMA_END;
  }

  public String generateMT(DataTypeMeta meta, DataTypeNode root) {
    String namespace = escapeXml(meta.getNamespace());
    return XML_META_TAG
        + indent(0) + String.format(SCHEMA_TAG, namespace, namespace)
        + (meta.getMtName().isEmpty() ? ""
        : indent(1) + appendMessageTypeTag(meta.getMtName(), meta.getDtName()))
        + generateXsdString(root, 1)
        + indent(0) + SCHEMA_END;
  }

  private String appendMessageTypeTag(String mtName, String dtName) {
    return String.format(DEFAULT_ELEMENT_TAG, escapeXml(mtName), escapeXml(dtName));
  }

  /**
   * XSD 태그 속성/텍스트에 삽입되는 사용자 입력값을 이스케이프하여 XML 인젝션 및 스키마 손상을 방지한다.
   */
  private String escapeXml(String value) {
    if (value == null) {
      return "";
    }
    return value
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&apos;");
  }

  /**
   * Git diff/사람이 읽기 쉽도록 태그 앞에 줄바꿈과 depth만큼의 들여쓰기를 붙인다.
   */
  private String indent(int depth) {
    StringBuilder sb = new StringBuilder("\n");
    for (int i = 0; i < depth; i++) {
      sb.append(INDENT_UNIT);
    }
    return sb.toString();
  }

  private String generateXsdString(DataTypeNode node, int depth) {
    if (node.getEntity().getCategory() == Category.COMPLEX_TYPE) {
      return appendComplexTypeTag(node, depth);
    } else if (node.getEntity().getCategory() == Category.ATTRIBUTE) {
      return appendAttributeTag(node, depth);
    }
    return appendElementTag(node, depth);
  }

  private String appendComplexTypeTag(DataTypeNode node, int depth) {
    return indent(depth) + String.format(ROOT_COMPLEX_TYPE_TAG, escapeXml(node.getEntity().getName())) +
        indent(depth + 1) + SEQUENCE_TAG +
        node.getChildren().stream()
            .map(child -> generateXsdString(child, depth + 2))
            .reduce("", (acc, str) -> acc + str) +
        indent(depth + 1) + SEQUENCE_END +
        indent(depth) + COMPLEX_TYPE_END;
  }

  private String appendAttributeTag(DataTypeNode node, int depth) {
    return indent(depth) + String.format(
        ATTRIBUTE_TAG,
        escapeXml(node.getEntity().getName()),
        node.getEntity().getType().getXsdType());
  }

  private String appendElementTag(DataTypeNode node, int depth) {
    // attr -> x, elem -> x (element)
    if (node.getChildren().isEmpty()) {
      return generateContentElementTag(node, depth);
    }

    // attr -> o, elem -> x (extension)
    // action 하나만 attribute로 갖는 노드는 complexType(wrapper)으로 유지하고,
    // hasQuot/isInput/isOutput처럼 action 외의 attribute를 하나라도 가지면
    // simpleContent extension으로 취급한다.
    boolean hasLeafAttribute = node.getChildren().stream()
        .filter(child -> child.getEntity().getCategory() == Category.ATTRIBUTE)
        .anyMatch(child -> Attribute.hasAttributeExceptAction(child.getEntity().getName()));
    if (hasLeafAttribute) {
      return generateExtensionContentElementTag(node, depth);
    }

    // attr -> o/x, elem -> o (complexType)
    return generateWrapperElementTag(node, depth);
  }

  private String generateContentElementTag(DataTypeNode node, int depth) {
    return indent(depth) + String.format(
        CONTENT_ELEMENT_TAG,
        escapeXml(node.getEntity().getName()),
        node.getEntity().getType().getXsdType(),
        node.getEntity().getOccurrence().getLowerBound(),
        node.getEntity().getOccurrence().getUpperBound()) +
        (
            node.getEntity().getDescription().isEmpty()
                ? ""
                : indent(depth + 1) + String.format(DESCRIPTION_TAG,
                    escapeXml(node.getEntity().getDescription()))
        ) +
        indent(depth) + ELEMENT_END;
  }

  private String generateWrapperElementTag(DataTypeNode node, int depth) {
    return indent(depth) + String.format(
        WRAPPER_ELEMENT_TAG,
        escapeXml(node.getEntity().getName()),
        node.getEntity().getOccurrence().getLowerBound(),
        node.getEntity().getOccurrence().getUpperBound()) +
        indent(depth + 1) + COMPLEX_TYPE_TAG +
        indent(depth + 2) + SEQUENCE_TAG +
        node.getChildren().stream()
            .filter(child -> child.getEntity().getCategory() == Category.ELEMENT)
            .map(child -> generateXsdString(child, depth + 3))
            .reduce("", (acc, str) -> acc + str) +
        indent(depth + 2) + SEQUENCE_END +
        node.getChildren().stream()
            .filter(child -> child.getEntity().getCategory() == Category.ATTRIBUTE)
            .map(child -> generateXsdString(child, depth + 2))
            .reduce("", (acc, str) -> acc + str) +
        indent(depth + 1) + COMPLEX_TYPE_END +
        indent(depth) + ELEMENT_END;
  }

  private String generateExtensionContentElementTag(DataTypeNode node, int depth) {
    return indent(depth) + String.format(
        WRAPPER_ELEMENT_TAG,
        escapeXml(node.getEntity().getName()),
        node.getEntity().getOccurrence().getLowerBound(),
        node.getEntity().getOccurrence().getUpperBound()) +
        (
            node.getEntity().getDescription().isEmpty()
                ? ""
                : indent(depth + 1) + String.format(DESCRIPTION_TAG,
                    escapeXml(node.getEntity().getDescription()))) +
        indent(depth + 1) + COMPLEX_TYPE_TAG +
        indent(depth + 2) + SIMPLE_CONTENT_TAG +
        indent(depth + 3) + String.format(EXTENSION_TAG, node.getEntity().getType().getXsdType()) +
        node.getChildren().stream()
            .filter(child -> child.getEntity().getCategory() == Category.ATTRIBUTE)
            .map(child -> generateXsdString(child, depth + 4))
            .reduce("", (acc, str) -> acc + str) +
        indent(depth + 3) + EXTENSION_END +
        indent(depth + 2) + SIMPLE_CONTENT_END +
        indent(depth + 1) + COMPLEX_TYPE_END +
        indent(depth) + ELEMENT_END;
  }
}
