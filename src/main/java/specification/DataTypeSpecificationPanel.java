package specification;

import core.DataTypePipelineService;
import specification.components.DataTypeSpecificationButtonPanel;
import specification.components.DataTypeSpecificationHeaderPanel;
import specification.components.DataTypeSpecificationScrollPane;
import specification.elements.DataTypeElement;
import util.Navigator;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class DataTypeSpecificationPanel extends JPanel {

    private final Navigator navigator;
    private final DataTypePipelineService service;

    private List<DataTypeElement> dataTypeElements;

    public DataTypeSpecificationPanel(Navigator navigator, DataTypePipelineService service) {
        this.navigator = navigator;
        this.service = service;

        this.dataTypeElements = new ArrayList<>();

        initComponent();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                dataTypeElements = service.getDataTypeElement();
            }
        });
    }

    public void removeElement(DataTypeElement element) {
        dataTypeElements.remove(element);
        //todo: 만약 이것만으로는 안된다면 ScrollPane 조작해야함
    }

    private void initComponent() {
        add(new DataTypeSpecificationHeaderPanel());
        add(new DataTypeSpecificationScrollPane(this));
        add(new DataTypeSpecificationButtonPanel(this));
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public DataTypePipelineService getService() {
        return service;
    }

    public List<DataTypeElement> getDataTypeElements() {
        return dataTypeElements;
    }
}
