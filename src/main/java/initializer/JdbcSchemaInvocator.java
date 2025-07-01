package initializer;

import static dto.XDataType.ACCESS;
import static dto.XDataType.ACTION;
import static dto.XDataType.DB_TABLE_NAME;
import static dto.XDataType.KEY;
import static dto.XDataType.ROW;
import static dto.XDataType.STATEMENT;
import static dto.XDataType.TABLE;

import java.util.ArrayList;
import java.util.List;

public class JdbcSchemaInvocator {

  protected List<String> generateSELECTSchema() {
    List<String> schema = new ArrayList<>();

    schema.add(ROW);

    return schema;
  }

  protected List<String> generateDMLSchema(int schemaIndex) {
    List<String> schema = generateDefaultSchema(schemaIndex);

    schema.add(TABLE);
    schema.add(ACCESS);

    return schema;
  }

  protected List<String> generateSQLDMLSchema(int schemaIndex) {
    List<String> schema = generateDefaultSchema(schemaIndex);

    schema.add(ACCESS);
    schema.add(KEY);

    return schema;
  }

  protected List<String> generatePROCEDURESchema(int schemaIndex) {
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
