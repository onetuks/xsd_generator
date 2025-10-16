package definition;

import core.DataTypePipelineService;
import ui.FrameInfo;
import global.Navigator;

import javax.swing.*;

public class DataTypeDefinition extends JPanel {

    private final Navigator navigator;
    private final DataTypePipelineService dataTypePipelineService;

    private final DataTypeDefinitionInfoPanel dtInfoPanel;
    private final DataTypeDefinitionFieldTabbedPane dtFieldTabbedPane;

    public DataTypeDefinition(Navigator navigator, DataTypePipelineService service) {
        this.navigator = navigator;
        this.dataTypePipelineService = service;
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

    public DataTypePipelineService getDataTypePipelineService() {
        return dataTypePipelineService;
    }

    public DataTypeDefinitionInfoPanel getDtInfoPanel() {
        return dtInfoPanel;
    }

    public DataTypeDefinitionFieldTabbedPane getDtFieldTabbedPane() {
        return dtFieldTabbedPane;
    }
}
