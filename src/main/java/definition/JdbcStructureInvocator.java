package definition;

import static definition.DataTypeElement.ACCESS;
import static definition.DataTypeElement.ACTION;
import static definition.DataTypeElement.DB_TABLE_NAME;
import static definition.DataTypeElement.KEY;
import static definition.DataTypeElement.ROW;
import static definition.DataTypeElement.STATEMENT;
import static definition.DataTypeElement.TABLE;

import java.util.ArrayList;
import java.util.List;

public class JdbcStructureInvocator {

  protected List<String> generateSELECTStructure() {
    List<String> schema = new ArrayList<>();

    schema.add(ROW);

    return schema;
  }

  protected List<String> generateDMLStructure(int schemaIndex) {
    List<String> schema = generateDefaultSchema(schemaIndex);

    schema.add(TABLE);
    schema.add(ACCESS);

    return schema;
  }

  protected List<String> generateSQLStructure(int schemaIndex) {
    List<String> schema = generateDefaultSchema(schemaIndex);

    schema.add(ACCESS);
    schema.add(KEY);

    return schema;
  }

  protected List<String> generatePROCEDUREStructure(int schemaIndex) {
    List<String> schema = generateDefaultSchema(schemaIndex);

    schema.add(TABLE);

    return schema;
  }

  private List<String> generateDefaultSchema(int schemaIndex) {
    List<String> schema = new ArrayList<>();

    schema.add(STATEMENT + schemaIndex);
    schema.add(DB_TABLE_NAME);
    schema.add(ACTION);

    return schema;
  }
}
