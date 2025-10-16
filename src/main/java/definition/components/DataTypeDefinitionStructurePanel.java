package definition.components;

import javax.swing.*;

public class DataTypeDefinitionStructurePanel extends JPanel {

    public DataTypeDefinitionStructurePanel(DataTypeDefinitionFieldTabbedPane dtFieldTabbedPane) {
        super();

        add(new DataTypeDefinitionStructureManipulationPanel(dtFieldTabbedPane));
        add(new DataTypeDefinitionJdbcStructurePanel(dtFieldTabbedPane));
    }
}
