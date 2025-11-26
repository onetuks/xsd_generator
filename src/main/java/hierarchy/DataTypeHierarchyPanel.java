package hierarchy;

import core.DataTypePipelineService;
import hierarchy.components.DataTypeHierarchyControlPanel;
import hierarchy.components.DataTypeHierarchyScrollPane;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import model.DataTypeNode;
import util.Navigator;

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
      controlPanel.setFocusedNodes(null);
      return;
    }

    TreePath[] selectionPaths = scrollPane.getHierarchyTree().getSelectionPaths();
    if (selectionPaths == null) {
      controlPanel.setFocusedNodes(null);
      return;
    }

    List<DataTypeNode> focusedNodes = Arrays.stream(selectionPaths)
        .map(path -> (DefaultMutableTreeNode) path.getLastPathComponent())
        .map(dataTypeNode -> (DataTypeNode) dataTypeNode.getUserObject())
        .collect(Collectors.toList());
    controlPanel.setFocusedNodes(focusedNodes);
  }

  public void addChildTo(DataTypeNode parentNode) {
    if (!controlPanel.getEditModeCheckBox().isSelected()) {
      return;
    }

    List<DataTypeNode> childrenNodes = controlPanel.getFocusedNodes();
    childrenNodes.forEach(childNode -> service.addChildTo(parentNode, childNode));
  }

  public void addSiblingTo(DataTypeNode olderNode) {
    if (!controlPanel.getEditModeCheckBox().isSelected()) {
      return;
    }

    List<DataTypeNode> youngerNodes = controlPanel.getFocusedNodes();
    youngerNodes.forEach(youngerNode -> service.addSiblingTo(olderNode, youngerNode));
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

  public DataTypeHierarchyControlPanel getControlPanel() {
    return controlPanel;
  }
}
