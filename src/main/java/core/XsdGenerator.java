package core;

import model.DataTypeNode;
import model.XTree;
import model.vo.Attribute;
import model.vo.Category;

public class XsdGenerator {

  private static final String XML_META_TAG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
  private static final String SCHEMA_TAG = "<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"%s\" targetNamespace=\"%s\">";
  private static final String SCHEMA_END = "</xsd:schema>";
  private static final String ROOT_COMPLEX_TYPE_TAG = "<xsd:complexType name=\"%s\">";
  private static final String COMPLEX_TYPE_TAG = "<xsd:complexType>";
  private static final String COMPLEX_TYPE_END = "</xsd:complexType>";
  private static final String SEQUENCE_TAG = "<xsd:sequence>";
  private static final String SEQUENCE_END = "</xsd:sequence>";
  private static final String WRAPPER_ELEMENT_TAG = "<xsd:element name=\"%s\" minOccurs=\"%s\" maxOccurs=\"%s\">";
  private static final String CONTENT_ELEMENT_TAG = "<xsd:element name=\"%s\" type=\"%s\" minOccurs=\"%s\" maxOccurs=\"%s\">";
  private static final String ELEMENT_END = "</xsd:element>";
  private static final String SIMPLE_CONTENT_TAG = "<xsd:simpleContent>";
  private static final String SIMPLE_CONTENT_END = "</xsd:simpleContent>";
  private static final String EXTENSION_TAG = "<xsd:extension base=\"%s\">";
  private static final String EXTENSION_END = "</xsd:extension>";
  private static final String ATTRIBUTE_TAG = "<xsd:attribute name=\"%s\" type=\"%s\"/>";
  private static final String DESCRIPTION_TAG = "<xsd:annotation><xsd:documentation>%s</xsd:documentation></xsd:annotation>";

  public String generate(XTree tree) {
    return generateSchemaTag(tree, generateXsdString(tree.getRoot()));
  }

  private String generateXsdString(DataTypeNode node) {
    if (node.entity().getCategory() == Category.COMPLEX_TYPE) {
      return generateComplexTypeTag(node);
    } else if (node.entity().getCategory() == Category.ATTRIBUTE) {
      return generateAttributeTag(node);
    }
    return generateElementTag(node);
  }

  private String generateComplexTypeTag(DataTypeNode node) {
    return String.format(ROOT_COMPLEX_TYPE_TAG, node.entity().getName()) +
        SEQUENCE_TAG +
        node.children().stream()
            .map(this::generateXsdString)
            .reduce("", (acc, str) -> acc + str) +
        SEQUENCE_END +
        COMPLEX_TYPE_END;
  }

  private String generateAttributeTag(DataTypeNode node) {
    return String.format(
        ATTRIBUTE_TAG,
        node.entity().getName(),
        node.entity().getType().getXsdType());
  }

  private String generateElementTag(DataTypeNode node) {
    // attr -> x, elem -> x (element)
    if (node.children().isEmpty()) {
      return generateContentElementTag(node);
    }

    // attr -> o, elem -> x (extension)
    boolean hasLeafAttribute = node.children().stream()
        .filter(child -> child.entity().getCategory() == Category.ATTRIBUTE)
        .anyMatch(child -> Attribute.hasAttributeExceptAction(child.entity().getName()));
    if (hasLeafAttribute) {
      return generateExtensionContentElementTag(node);
    }

    // attr -> o/x, elem -> o (complexType)
    return generateWrapperElementTag(node);
  }

  private String generateContentElementTag(DataTypeNode node) {
    return String.format(
        CONTENT_ELEMENT_TAG,
        node.entity().getName(),
        node.entity().getType().getXsdType(),
        node.entity().getOccurrence().getLowerBound(),
        node.entity().getOccurrence().getUpperBound()) +
        (
            node.entity().getDescription().isEmpty()
                ? ""
                : String.format(DESCRIPTION_TAG, node.entity().getDescription())
        ) +
        ELEMENT_END;
  }

  private String generateWrapperElementTag(DataTypeNode node) {
    return String.format(
        WRAPPER_ELEMENT_TAG,
        node.entity().getName(),
        node.entity().getOccurrence().getLowerBound(),
        node.entity().getOccurrence().getUpperBound()) +
        COMPLEX_TYPE_TAG +
        SEQUENCE_TAG +
        node.children().stream()
            .filter(child -> child.entity().getCategory() == Category.ELEMENT)
            .map(this::generateXsdString)
            .reduce("", (acc, str) -> acc + str) +
        SEQUENCE_END +
        node.children().stream()
            .filter(child -> child.entity().getCategory() == Category.ATTRIBUTE)
            .map(this::generateXsdString)
            .reduce("", (acc, str) -> acc + str) +
        COMPLEX_TYPE_END +
        ELEMENT_END;
  }

  private String generateExtensionContentElementTag(DataTypeNode node) {
    return String.format(
        WRAPPER_ELEMENT_TAG,
        node.entity().getName(),
        node.entity().getOccurrence().getLowerBound(),
        node.entity().getOccurrence().getUpperBound()) +
        (
            node.entity().getDescription().isEmpty()
                ? ""
                : String.format(DESCRIPTION_TAG, node.entity().getDescription())) +
        COMPLEX_TYPE_TAG +
        SIMPLE_CONTENT_TAG +
        String.format(EXTENSION_TAG, node.entity().getType().getXsdType()) +
        node.children().stream()
            .filter(child -> child.entity().getCategory() == Category.ATTRIBUTE)
            .map(this::generateXsdString)
            .reduce("", (acc, str) -> acc + str) +
        EXTENSION_END +
        SIMPLE_CONTENT_END +
        COMPLEX_TYPE_END +
        ELEMENT_END;
  }

  private String generateSchemaTag(XTree tree, String xsdString) {
    return XML_META_TAG
        + String.format(SCHEMA_TAG, tree.getNamespace(), tree.getNamespace())
        + xsdString
        + SCHEMA_END;
  }
}
