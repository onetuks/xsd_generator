package specification;

import builder.TypeBuilderButton;
import builder.TypeBuilderHeaderPanel;
import builder.TypeBuilderScrollPane;
import core.DataTypePipelineService;
import global.Navigator;
import model.XTree;

import javax.swing.*;

public class DataTypeSpecification extends JPanel {

    private final Navigator navigator;
    private final DataTypePipelineService service;

    public DataTypeSpecification(Navigator navigator, DataTypePipelineService service) {
        this.navigator = navigator;
        this.service = service;

        initComponent();
    }

    private void initComponent() {
        TypeBuilderHeaderPanel headerPanel = new TypeBuilderHeaderPanel();
        TypeBuilderScrollPane scrollPane = new TypeBuilderScrollPane();
        TypeBuilderButton
    }
}
