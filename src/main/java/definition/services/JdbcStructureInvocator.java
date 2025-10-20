package definition.services;

import java.util.ArrayList;
import java.util.List;

import static specification.elements.DataTypeElement.*;

public class JdbcStructureInvocator {

    public List<String> generateSELECTStructure() {
        List<String> schema = new ArrayList<>();

        schema.add(ROW);

        return schema;
    }

    public List<String> generateDMLStructure(int schemaIndex) {
        List<String> schema = generateDefaultSchema(schemaIndex);

        schema.add(TABLE);
        schema.add(ACCESS);

        return schema;
    }

    public List<String> generateSQLStructure(int schemaIndex) {
        List<String> schema = generateDefaultSchema(schemaIndex);

        schema.add(ACCESS);
        schema.add(KEY);

        return schema;
    }

    public List<String> generatePROCEDUREStructure(int schemaIndex) {
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
