package definition.components;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class DataTypeDefinitionStructurePanel extends JPanel {

  public DataTypeDefinitionStructurePanel(DataTypeDefinitionFieldTabbedPane dtFieldTabbedPane) {
    super();

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    add(new DataTypeDefinitionStructureManipulationPanel(dtFieldTabbedPane));
    add(Box.createHorizontalStrut(20));
    add(new DataTypeDefinitionJdbcStructurePanel(dtFieldTabbedPane));
  }
}
