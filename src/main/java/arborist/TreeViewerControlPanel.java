package arborist;

import javax.swing.*;

public class TreeViewerControlPanel extends JPanel {

  private static final int BUTTON_WIDTH = 90;
  private static final int BUTTON_HEIGHT = 25;
  private static final String HINT_LCK = "L-Ck: focus on";
  private static final String HINT_RCK = "R-Ck: move to";
  private static final String HINT_FOCUS = "Focused DT";

//  private final TreeViewer treeViewer;
//
//  protected TreeViewerControlPanel(TreeViewer treeViewer) {
//    super();
//
//    this.treeViewer = treeViewer;
//
//    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//    setPreferredSize(new Dimension(BUTTON_WIDTH, TREE_VIEWER_HEIGHT));
//    setMaximumSize(new Dimension(BUTTON_WIDTH, TREE_VIEWER_HEIGHT));
//    setBackground(FrameInfo.PRIMARY);
//    setAlignmentX(Component.CENTER_ALIGNMENT);
//    setAlignmentY(Component.CENTER_ALIGNMENT);
//
//    add(buildTreeViewControlEditPanel());
//    add(buildTreeViewControlButtonPanel());
//  }
//
//  private JPanel buildTreeViewControlEditPanel() {
//    JPanel editPanel = new JPanel();
//
//    editPanel.setBackground(FrameInfo.PRIMARY);
//
//    editPanel.add(buildTreeViewEditCheckbox());
//
//    editPanel.add(buildHintPanel(HINT_LCK));
//    editPanel.add(buildHintPanel(HINT_RCK));
//
//    editPanel.add(buildFocusedPanel());
//
//    return editPanel;
//  }
//
//  private JPanel buildFocusedPanel() {
//    JPanel focusedPanel = new JPanel();
//
//    focusedPanel.setLayout(new BoxLayout(focusedPanel, BoxLayout.Y_AXIS));
//    focusedPanel.setBackground(FrameInfo.PRIMARY);
//    focusedPanel.setBorder(BorderFactory.createLineBorder(FrameInfo.SECONDARY));
//
//    focusedPanel.add(buildHintPanel(HINT_FOCUS));
//    focusedPanel.add(
//        buildHintPanel(
//            Optional.ofNullable(treeViewer.getFocusedDataTypeName())
//                .orElse("")));
//
//    return focusedPanel;
//  }
//
//  private JPanel buildTreeViewControlButtonPanel() {
//    JPanel buttonPanel = new JPanel();
//
//    buttonPanel.setBackground(FrameInfo.PRIMARY);
//
//    buttonPanel.add(buildTreeViewResetButton());
//    buttonPanel.add(buildTreeViewCompleteButton());
//    buttonPanel.add(buildTreeViewCancelButton());
//
//    return buttonPanel;
//  }
//
//  private JPanel buildHintPanel(String text) {
//    JPanel panel = new JPanel();
//
//    panel.setBackground(FrameInfo.PRIMARY);
//    panel.setForeground(FrameInfo.TERTIARY);
//    panel.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
//    panel.setAlignmentY(Component.CENTER_ALIGNMENT);
//
//    JLabel focusedLabel = new JLabel(text);
//    focusedLabel.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
//    focusedLabel.setForeground(FrameInfo.TERTIARY);
//
//    treeViewer.setFocusedDataTypeNameLabel(focusedLabel);
//
//    panel.add(treeViewer.getFocusedDataTypeNameLabel());
//
//    return panel;
//  }
//
//  private JCheckBox buildTreeViewEditCheckbox() {
//    JCheckBox editCheckBox = new JCheckBox("Edit Mode");
//
//    editCheckBox.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
//    editCheckBox.setBackground(FrameInfo.SECONDARY);
//    editCheckBox.setForeground(FrameInfo.TERTIARY);
//
//    treeViewer.setEditCheckBox(editCheckBox);
//
//    return editCheckBox;
//  }
//
//  private JButton buildTreeViewResetButton() {
//    JButton resetButton = buildDefaultButton("Reset");
//
//    resetButton.addActionListener(e -> {
//      treeViewer.setVisible(false);
//      new TreeViewer(treeViewer.getBuilder());
//    });
//
//    return resetButton;
//  }
//
//  private JButton buildTreeViewCompleteButton() {
//    JButton completeButton = buildDefaultButton("Complete");
//
//    completeButton.addActionListener(e -> {
//      treeViewer.setVisible(false);
//      new TypeBuilder(treeViewer.getBuilder().getDataTypeDefinition(), treeViewer.getTree());
//    });
//
//    return completeButton;
//  }
//
//  private JButton buildTreeViewCancelButton() {
//    JButton cancelButton = buildDefaultButton("Cancel");
//
//    cancelButton.addActionListener(e -> {
//      treeViewer.setVisible(false);
//      new TypeBuilder(treeViewer.getBuilder().getDataTypeDefinition(), treeViewer.getBuilder().getTree());
//    });
//
//    return cancelButton;
//  }
//
//  private JButton buildDefaultButton(String buttonName) {
//    JButton button = new JButton(buttonName);
//    button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
//    button.setBackground(FrameInfo.SECONDARY);
//    button.setForeground(FrameInfo.TERTIARY);
//    return button;
//  }
}
