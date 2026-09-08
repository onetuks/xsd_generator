package hierarchy;

import core.DataTypePipelineService;
import hierarchy.components.DataTypeHierarchyControlPanel;
import hierarchy.components.DataTypeHierarchyScrollPane;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
    if (!confirmSaveWithPreview()) {
      return;
    }

    service.generateXSDFile();

    String[] options = {"확인", "저장 폴더 열기"};
    int choice = JOptionPane.showOptionDialog(
        this, "XSD File Generated!", "완료",
        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

    if (choice == 1) {
      openTargetDir();
    }
  }

  private void openTargetDir() {
    try {
      if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
        Desktop.getDesktop().open(new File(service.getTargetDir()));
      }
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(
          this, "저장 폴더를 여는 중 오류가 발생했습니다: " + ex.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * 저장 전에 생성될 XSD 내용을 DT -> MT 순서로 하나씩 따로 보여주고 저장 여부를 확인받는다.
   * 둘 중 하나라도 취소하면 아무 것도 저장하지 않는다.
   */
  private boolean confirmSaveWithPreview() {
    if (!confirmPreview("DT", service.previewDT(), service.willOverwriteDtFile())) {
      return false;
    }

    String mtPreview = service.previewMT();
    if (mtPreview != null) {
      return confirmPreview("MT", mtPreview, service.willOverwriteMtFile());
    }

    return true;
  }

  /**
   * XSD 내용을 미리 보여주고 저장 여부를 확인받는다. 기존 파일을 덮어쓰게 되는 경우
   * 안내 문구도 함께 보여준다.
   */
  private boolean confirmPreview(String label, String xsdText, boolean willOverwrite) {
    JTextArea previewArea = new JTextArea(xsdText, 20, 80);
    previewArea.setEditable(false);
    previewArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    previewArea.setCaretPosition(0);

    String message = willOverwrite
        ? String.format("동일한 이름의 %s 파일이 이미 존재합니다. 아래 내용으로 덮어쓰시겠습니까?", label)
        : String.format("아래 내용으로 %s 파일을 저장하시겠습니까?", label);

    JPanel messagePanel = new JPanel(new BorderLayout(0, 8));
    messagePanel.add(new JLabel(message), BorderLayout.NORTH);
    messagePanel.add(new JScrollPane(previewArea), BorderLayout.CENTER);

    int result = JOptionPane.showConfirmDialog(
        this, messagePanel, label + " 미리보기",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    return result == JOptionPane.OK_OPTION;
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
