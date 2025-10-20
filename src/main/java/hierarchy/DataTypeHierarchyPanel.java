package hierarchy;

import core.DataTypePipelineService;
import hierarchy.components.DataTypeHierarchyControlPanel;
import hierarchy.components.DataTypeHierarchyScrollPane;
import util.Navigator;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class DataTypeHierarchyPanel extends JPanel {

    private final Navigator navigator;
    private final DataTypePipelineService service;

    private final DataTypeHierarchyScrollPane scrollPane;
    private final DataTypeHierarchyControlPanel controlPanel;

    public DataTypeHierarchyPanel(Navigator navigator, DataTypePipelineService service) {
        this.navigator = navigator;
        this.service = service;

        this.scrollPane = new DataTypeHierarchyScrollPane(this);
        this.controlPanel = new DataTypeHierarchyControlPanel(this);

        resetHierarchy();

        add(scrollPane);
        add(Box.createHorizontalStrut(20));
        add(controlPanel);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                resetHierarchy();
                revalidate();
                repaint();
            }
        });
    }

    public void resetHierarchy() {
        if (service.getDataTypeElements().isEmpty()) {
            return;
        }

        service.updateDataTypeNode(service.getDataTypeElements());
        scrollPane.bindTreeViewPort();
    }

    public void setFocusedDataType() {
        if (!controlPanel.getEditModeCheckBox().isSelected()) {
            controlPanel.getFocusedDataTypeNameLabel().setText("");
            return;
        }

        controlPanel.getFocusedDataTypeNameLabel()
                .setText(
                        ((DefaultMutableTreeNode) scrollPane.getHierarchyTree()
                                .getLastSelectedPathComponent())
                                .getUserObject()
                                .toString());
    }

    public void addChildTo(String parentDataTypeName) {
        if (!controlPanel.getEditModeCheckBox().isSelected()) {
            return;
        }

        String childDataTypeName = controlPanel.getFocusedDataTypeNameLabel().getText();
        service.addChildTo(parentDataTypeName, childDataTypeName);
    }

    public void addSiblingTo(String olderSiblingDataTypeName) {
        if (!controlPanel.getEditModeCheckBox().isSelected()) {
            return;
        }

        String youngerSiblingDataTypeName = controlPanel.getFocusedDataTypeNameLabel().getText();
        service.addSiblingTo(olderSiblingDataTypeName, youngerSiblingDataTypeName);
    }

    public void completeHierarchy() {
        service.generateXSDFile();
        JOptionPane.showMessageDialog(this, "XSD File Generated!");
    }

    /* getter & setter */
    public Navigator getNavigator() {
        return navigator;
    }

    public DataTypePipelineService getService() {
        return service;
    }
}
