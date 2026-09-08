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

  private boolean hasManualEdits = false;

  public DataTypeHierarchyPanel(Navigator navigator, DataTypePipelineService service) {
    this.navigator = navigator;
    this.service = service;

    this.scrollPane = new DataTypeHierarchyScrollPane(this);
    this.controlPanel = new DataTypeHierarchyControlPanel(this);

    buildHierarchy();

    add(scrollPane);
    add(Box.createHorizontalStrut(20));
    add(controlPanel);

    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent e) {
        enterHierarchy();
        revalidate();
        repaint();
      }
    });
  }

  /**
   * 화면 진입 시 호출된다. 이미 만들어진 구조가 있고 사용자가 수동으로 조정한 이력이 있다면,
   * 자동 재배치로 그 작업이 조용히 사라지지 않도록 확인을 먼저 받는다.
   */
  private void enterHierarchy() {
    if (service.getDataTypeElements().isEmpty()) {
      return;
    }

    if (service.getRootNode() != null && hasManualEdits) {
      int result = JOptionPane.showConfirmDialog(
          this,
          "Specification 단계로 돌아갔다 오면 직접 조정한 계층 구조가 초기화됩니다. 계속하시겠습니까?",
          "Hierarchy Manipulation",
          JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

      if (result != JOptionPane.OK_OPTION) {
        scrollPane.bindTreeViewPort();
        return;
      }
    }

    buildHierarchy();
  }

  /**
   * 확인 절차 없이 항상 필드 목록 기준으로 트리를 새로 만든다. Reset 버튼(자체 확인 다이얼로그 보유)이나
   * 최초 진입 시에만 호출해야 한다.
   */
  private void buildHierarchy() {
    if (service.getDataTypeElements().isEmpty()) {
      return;
    }

    service.updateDataTypeNode(service.getDataTypeElements());
    scrollPane.bindTreeViewPort();
    hasManualEdits = false;
  }

  public void resetHierarchy() {
    buildHierarchy();
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
    if (childrenNodes == null || childrenNodes.isEmpty()) {
      return;
    }
    childrenNodes.forEach(childNode -> service.addChildTo(parentNode, childNode));
    hasManualEdits = true;
  }

  public void addSiblingTo(DataTypeNode olderNode) {
    if (!controlPanel.getEditModeCheckBox().isSelected()) {
      return;
    }

    List<DataTypeNode> youngerNodes = controlPanel.getFocusedNodes();
    if (youngerNodes == null || youngerNodes.isEmpty()) {
      return;
    }
    youngerNodes.forEach(youngerNode -> service.addSiblingTo(olderNode, youngerNode));
    hasManualEdits = true;
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
