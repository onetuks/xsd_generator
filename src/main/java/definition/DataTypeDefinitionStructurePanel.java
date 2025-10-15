package definition;

import javax.swing.*;
import java.awt.*;

public class DataTypeDefinitionStructurePanel extends JPanel {

    public DataTypeDefinitionStructurePanel(DataTypeDefinitionFieldTabbedPane dtFieldTabbedPane) {
        super();

        add(new DataTypeDefinitionStructureManipulationPanel(dtFieldTabbedPane));
        add(new DataTypeDefinitionJdbcStructurePanel(dtFieldTabbedPane));
    }
}
