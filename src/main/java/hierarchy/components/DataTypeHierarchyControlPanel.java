package hierarchy.components;

import hierarchy.DataTypeHierarchyPanel;
import java.util.ArrayList;
import model.DataTypeNode;
import ui.FrameInfo;
import util.Navigator;
import java.util.List;

import javax.swing.*;
import java.awt.*;

public class DataTypeHierarchyControlPanel extends JPanel {

    private static final int BUTTON_WIDTH = 90;
    private static final int BUTTON_HEIGHT = 25;
    private static final String HINT_LCK = "L-Ck: focus on";
    private static final String HINT_RCK = "R-Ck: move to";
    private static final String HINT_FOCUS = "Focused DT";

    private final DataTypeHierarchyPanel hierarchy;

    private final JCheckBox editModeCheckBox = new JCheckBox("Edit Mode");
    private final JPanel focusedPanel = new JPanel();
    private final List<JLabel> focusedDataTypeNameLabels = new ArrayList<>();

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
        editPanel.add(createFocusedDataTypePanel());

        return editPanel;
    }

    private JPanel createFocusedDataTypePanel() {
        focusedPanel.setLayout(new GridLayout(0, 1, 0, 3));
        focusedPanel.setBorder(FrameInfo.COMPOUND_BORDER);

        focusedPanel.add(new JLabel(HINT_FOCUS));
        focusedDataTypeNameLabels.add(createFocusedLabel(""));
        focusedPanel.add(focusedDataTypeNameLabels.get(0));

        return focusedPanel;
    }

    private JLabel createFocusedLabel(String labelText) {
      JLabel focusedLabel = new JLabel(labelText);
      focusedLabel.setForeground(FrameInfo.PRIMARY_COLOR);
      return focusedLabel;
    }

    private JPanel createHierarchyControlButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 0, 5));
        buttonPanel.add(createHierarchyPrevButton());
        buttonPanel.add(createHierarchyResetButton());
        buttonPanel.add(createHierarchyCompleteButton());
        return buttonPanel;
    }

    private JButton createHierarchyResetButton() {
        JButton resetButton = createControlButton("Reset");
        resetButton.addActionListener(e -> hierarchy.resetHierarchy());
        return resetButton;
    }

    private JButton createHierarchyCompleteButton() {
        JButton completeButton = createControlButton("Complete");
        completeButton.addActionListener(e -> hierarchy.completeHierarchy());
        return completeButton;
    }

    private JButton createHierarchyPrevButton() {
        JButton cancelButton = createControlButton("Prev");
        cancelButton.addActionListener(e -> hierarchy.getNavigator().showScreen(Navigator.SPECIFICATION));
        return cancelButton;
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

        if (focusedNodes == null || focusedNodes.isEmpty()) {
          focusedDataTypeNameLabels.clear();
          focusedDataTypeNameLabels.add(createFocusedLabel(""));
          focusedPanel.add(focusedDataTypeNameLabels.get(0));
          return;
        }

        focusedNodes.stream()
          .map(focusedNode -> createFocusedLabel(focusedNode.getEntity().getName()))
          .forEach(focusedPanel::add);
    }
}
