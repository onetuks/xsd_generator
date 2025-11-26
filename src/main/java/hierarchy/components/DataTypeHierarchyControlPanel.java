package hierarchy.components;

import hierarchy.DataTypeHierarchyPanel;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.DataTypeNode;
import ui.FrameInfo;
import util.Navigator;

public class DataTypeHierarchyControlPanel extends JPanel {

  private static final int BUTTON_WIDTH = 90;
  private static final int BUTTON_HEIGHT = 25;
  private static final String HINT_LCK = "L-Ck: focus on";
  private static final String HINT_RCK = "R-Ck: move to";

  private final DataTypeHierarchyPanel hierarchy;

  private final JCheckBox editModeCheckBox = new JCheckBox("Edit Mode");

  private List<DataTypeNode> focusedNodes = new ArrayList<>();

  public DataTypeHierarchyControlPanel(DataTypeHierarchyPanel hierarchy) {
    super();

    this.hierarchy = hierarchy;

    setLayout(new GridLayout(0, 1, 0, 20));

    add(createHierarchyControlEditPanel());
    add(createHierarchyControlButtonPanel());
  }

  private JPanel createHierarchyControlEditPanel() {
    JPanel editPanel = new JPanel();
    editPanel.setLayout(new GridLayout(0, 1, 0, 10));

    editPanel.add(editModeCheckBox);
    JPanel hintPanel = new JPanel();
    hintPanel.setLayout(new GridLayout(0, 1, 0, 1));
    hintPanel.setBorder(FrameInfo.COMPOUND_BORDER);
    hintPanel.add(new JLabel(HINT_LCK));
    hintPanel.add(new JLabel(HINT_RCK));
    editPanel.add(hintPanel);

    return editPanel;
  }

  private JPanel createHierarchyControlButtonPanel() {
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(0, 1, 0, 5));
    buttonPanel.add(createHierarchyPrevButton());
    buttonPanel.add(createHierarchyResetButton());
    buttonPanel.add(createHierarchyCompleteButton());
    return buttonPanel;
  }

  private JButton createHierarchyPrevButton() {
    JButton cancelButton = createControlButton("Prev");
    cancelButton.addActionListener(
        e -> hierarchy.getNavigator().showScreen(Navigator.SPECIFICATION));
    return cancelButton;
  }

  private JButton createHierarchyResetButton() {
    JButton resetButton = createControlButton("Reset");
    resetButton.addActionListener(e -> {
      int result = JOptionPane.showConfirmDialog(
          null, "구조를 초기화합니다", "Hierarchy Manipulation",
          JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null);

      if (result == JOptionPane.OK_OPTION) {
        hierarchy.resetHierarchy();
      }
    });
    return resetButton;
  }

  private JButton createHierarchyCompleteButton() {
    JButton completeButton = createControlButton("Complete");
    completeButton.addActionListener(e -> hierarchy.completeHierarchy());
    return completeButton;
  }

  private JButton createControlButton(String buttonName) {
    JButton button = new JButton(buttonName);
    button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
    return button;
  }

  public JCheckBox getEditModeCheckBox() {
    return editModeCheckBox;
  }

  public List<DataTypeNode> getFocusedNodes() {
    return focusedNodes;
  }

  public void setFocusedNodes(List<DataTypeNode> focusedNodes) {
    this.focusedNodes = focusedNodes;
  }
}
