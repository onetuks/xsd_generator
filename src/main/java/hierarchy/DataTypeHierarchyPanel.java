package hierarchy;

import core.DataTypePipelineService;
import hierarchy.components.DataTypeHierarchyControlPanel;
import hierarchy.components.DataTypeHierarchyScrollPane;
import model.DataTypeNode;
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
            controlPanel.setFocusedNode(null);
            return;
        }

        controlPanel.setFocusedNode(
                (DataTypeNode) ((DefaultMutableTreeNode) scrollPane.getHierarchyTree()
                        .getLastSelectedPathComponent())
                        .getUserObject());
    }

    public void addChildTo(DataTypeNode parentNode) {
        if (!controlPanel.getEditModeCheckBox().isSelected()) {
            return;
        }

        DataTypeNode childNode = controlPanel.getFocusedNode();
        service.addChildTo(parentNode, childNode);
    }

    public void addSiblingTo(DataTypeNode olderNode) {
        if (!controlPanel.getEditModeCheckBox().isSelected()) {
            return;
        }

        DataTypeNode youngerNode = controlPanel.getFocusedNode();
        service.addSiblingTo(olderNode, youngerNode);
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
