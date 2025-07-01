import dto.XDataType;
import dto.XDataTypes;
import java.util.ArrayList;
import java.util.List;
import model.XNode;
import model.XTree;
import model.vo.Category;
import model.vo.Occurrence;
import model.vo.Type;

public class TestFixture {

  private static final String DATA_TYPE_NAME = "DT_TEST";
  private static final String NAMESPACE = "https://test.com/test/";
  private static final String FILE_PATH = "D:/path/path";

  public static XTree createXTree() {
    XTree tree = new XTree(DATA_TYPE_NAME, NAMESPACE, FILE_PATH);

    XNode statementName1 = XNode.of("StatementName1", null, Category.ELEMENT, Type.STRING, Occurrence.ofZeroUnbounded());
    XNode dbTableName1 = XNode.of("dbTableName", null, Category.ELEMENT, null, Occurrence.ofOnlyOne());
    XNode action1 = XNode.of("action", null, Category.ATTRIBUTE, Type.STRING, Occurrence.ofOnlyOne());
    XNode access1 = XNode.of("access", null, Category.ELEMENT, Type.STRING, Occurrence.ofOnlyOne());
    XNode key = XNode.of("key", null, Category.ELEMENT, null, Occurrence.ofOnlyOne());
    XNode pjt_id1 = XNode.of("PJT_ID", null, Category.ELEMENT, Type.STRING, Occurrence.ofOnlyOne());

    XNode statementName2 = XNode.of("StatementName2", null, Category.ELEMENT, null, Occurrence.ofZeroUnbounded());
    XNode dbTableName2 = XNode.of("dbTableName", null, Category.ELEMENT, null, Occurrence.ofOnlyOne());
    XNode action2 = XNode.of("action", null, Category.ATTRIBUTE, Type.STRING, Occurrence.ofOnlyOne());
    XNode table2 = XNode.of("table", null, Category.ELEMENT, Type.STRING, Occurrence.ofOnlyOne());
    XNode access2 = XNode.of("access", null, Category.ELEMENT, null, Occurrence.ofOnlyOne());
    XNode pjt_id2 = XNode.of("PJT_ID", null, Category.ELEMENT, Type.STRING, Occurrence.ofOnlyOne());
    XNode pjt_code2 = XNode.of("PJT_CODE", null, Category.ELEMENT, Type.STRING, Occurrence.ofZeroOne());

    key.addChild(pjt_id1);
    dbTableName1.addChild(action1);
    dbTableName1.addChild(access1);
    dbTableName1.addChild(key);
    statementName1.addChild(dbTableName1);
    tree.getRoot().addChild(statementName1);

    access2.addChild(pjt_id2);
    access2.addChild(pjt_code2);
    dbTableName2.addChild(action2);
    dbTableName2.addChild(table2);
    dbTableName2.addChild(access2);
    statementName2.addChild(dbTableName2);
    tree.getRoot().addChild(statementName2);

    return tree;
  }

  public static XDataTypes createXDataTypes() {
    List<XDataType> dataTypes = new ArrayList<>();

    dataTypes.add(new XDataType("StatementName1", null));
    dataTypes.add(new XDataType("dbTableName", null));
    dataTypes.add(new XDataType("action", null));
    dataTypes.add(new XDataType("access", null));
    dataTypes.add(new XDataType("key", null));
    dataTypes.add(new XDataType("PJT_ID", "pjt 아이디"));
    dataTypes.add(new XDataType("StatementName2", null));
    dataTypes.add(new XDataType("dbTableName", null));
    dataTypes.add(new XDataType("action", null));
    dataTypes.add(new XDataType("table", null));
    dataTypes.add(new XDataType("access", null));
    dataTypes.add(new XDataType("PJT_ID", "pjt 아이디"));
    dataTypes.add(new XDataType("PJT_CODE", "코드코드"));

    return new XDataTypes(DATA_TYPE_NAME, NAMESPACE, FILE_PATH, dataTypes);
  }

  public static String createXsdString() {
    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"https://test.com/test\" targetNamespace=\"https://test.com/test\">" +
        "<xsd:complexType name=\"DT_TEST\">" +
        "<xsd:sequence>" +
        "<xsd:element name=\"StatementName1\" minOccurs=\"0\" maxOccurs=\"unbounded\">" +
        "<xsd:complexType>" +
        "<xsd:sequence>" +
        "<xsd:element name=\"dbTableName\" minOccurs=\"1\" maxOccurs=\"1\">" +
        "<xsd:complexType>" +
        "<xsd:sequence>" +
        "<xsd:element name=\"access\" type=\"xsd:string\" minOccurs=\"1\" maxOccurs=\"1\"></xsd:element>" +
        "<xsd:element name=\"key\" minOccurs=\"1\" maxOccurs=\"1\">" +
        "<xsd:complexType>" +
        "<xsd:sequence>" +
        "<xsd:element name=\"PJT_ID\" type=\"xsd:string\" minOccurs=\"1\" maxOccurs=\"1\"></xsd:element>" +
        "</xsd:sequence>" +
        "</xsd:complexType>" +
        "</xsd:element>" +
        "</xsd:sequence>" +
        "<xsd:attribute name=\"action\" type=\"xsd:string\"/>" +
        "</xsd:complexType>" +
        "</xsd:element>" +
        "</xsd:sequence>" +
        "</xsd:complexType>" +
        "</xsd:element>" +
        "<xsd:element name=\"StatementName2\" minOccurs=\"0\" maxOccurs=\"unbounded\">" +
        "<xsd:complexType>" +
        "<xsd:sequence>" +
        "<xsd:element name=\"dbTableName\" minOccurs=\"1\" maxOccurs=\"1\">" +
        "<xsd:complexType>" +
        "<xsd:sequence>" +
        "<xsd:element name=\"table\" type=\"xsd:string\" minOccurs=\"1\" maxOccurs=\"1\"></xsd:element>" +
        "<xsd:element name=\"access\" minOccurs=\"1\" maxOccurs=\"1\">" +
        "<xsd:complexType>" +
        "<xsd:sequence>" +
        "<xsd:element name=\"PJT_ID\" type=\"xsd:string\" minOccurs=\"1\" maxOccurs=\"1\"></xsd:element>" +
        "<xsd:element name=\"PJT_CODE\" type=\"xsd:string\" minOccurs=\"0\" maxOccurs=\"1\"></xsd:element>" +
        "</xsd:sequence>" +
        "</xsd:complexType>" +
        "</xsd:element>" +
        "</xsd:sequence>" +
        "<xsd:attribute name=\"action\" type=\"xsd:string\"/>" +
        "</xsd:complexType>" +
        "</xsd:element>" +
        "</xsd:sequence>" +
        "</xsd:complexType>" +
        "</xsd:element>" +
        "</xsd:sequence>" +
        "</xsd:complexType>" +
        "</xsd:schema>";
  }
}
