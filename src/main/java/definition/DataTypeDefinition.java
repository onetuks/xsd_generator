package definition;

import ui.FrameInfo;
import util.Navigator;

import javax.swing.*;

public class DataTypeDefinition extends JPanel {

    private final Navigator navigator;

    private final DataTypeDefinitionInfoPanel dtInfoPanel;
    private final DataTypeDefinitionFieldTabbedPane dtFieldTabbedPane;

    public DataTypeDefinition(Navigator navigator) {
        this.navigator = navigator;
        this.dtInfoPanel = new DataTypeDefinitionInfoPanel();
        this.dtFieldTabbedPane = new DataTypeDefinitionFieldTabbedPane();

        initComponent();
    }

    private void initComponent() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(FrameInfo.EMPTY_BORDER);
        add(Box.createVerticalStrut(8));
        add(dtInfoPanel);
        add(Box.createVerticalStrut(8));
        add(new DataTypeDefinitionStructurePanel(dtFieldTabbedPane));
        add(Box.createVerticalStrut(8));
        add(dtFieldTabbedPane);
        add(Box.createVerticalStrut(12));
        add(new DataTypeDefinitionButtonPanel(this));
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public DataTypeDefinitionInfoPanel getDtInfoPanel() {
        return dtInfoPanel;
    }

    public DataTypeDefinitionFieldTabbedPane getDtFieldTabbedPane() {
        return dtFieldTabbedPane;
    }
}
