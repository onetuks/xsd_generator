package definition.components;

import definition.DataTypeDefinitionPanel;
import definition.services.DataTypeFieldParser;
import java.awt.Component;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import specification.elements.DataTypeElement;
import util.Navigator;

public class DataTypeDefinitionButtonPanel extends JPanel {

  private final DataTypeDefinitionPanel dataTypeDefinitionPanel;

  public DataTypeDefinitionButtonPanel(DataTypeDefinitionPanel dataTypeDefinitionPanel) {
    this.dataTypeDefinitionPanel = dataTypeDefinitionPanel;

    initResetButton();
    initNextButton();
  }

  private void initResetButton() {
    JButton resetBtn = new JButton("Reset");
    resetBtn.addActionListener(e -> {
      int result = JOptionPane.showConfirmDialog(
          this, "입력한 내용을 모두 초기화합니다. 계속하시겠습니까?", "Definition Reset",
          JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

      if (result != JOptionPane.OK_OPTION) {
        return;
      }

      dataTypeDefinitionPanel.getDtInfoPanel().clear();
      dataTypeDefinitionPanel.getDtFieldTabbedPane().clearAllTabs();
    });
    add(resetBtn);
  }

  private void initNextButton() {
    JButton nextBtn = new JButton("Next");
    nextBtn.addActionListener(e -> {
      try {
        List<DataTypeElement> fields = extractDataTypeFields();
        if (fields.isEmpty()) {
          throw new IllegalArgumentException("필드를 하나 이상 입력해주세요.");
        }
        if (!confirmLineAlignment()) {
          return;
        }

        dataTypeDefinitionPanel
            .getDataTypePipelineService()
            .updateDataTypeElements(
                dataTypeDefinitionPanel.getDtInfoPanel().getMTName(),
                dataTypeDefinitionPanel.getDtInfoPanel().getDTName(),
                dataTypeDefinitionPanel.getDtInfoPanel().getNamespace(),
                dataTypeDefinitionPanel.getDtInfoPanel().getTargetDir(),
                fields);
        dataTypeDefinitionPanel.getNavigator().showScreen(Navigator.SPECIFICATION);
      } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    });
    add(nextBtn);
  }

  /**
   * Name/Description은 줄 번호로 매칭되는 두 개의 독립된 텍스트영역이라, 한쪽에서만 줄을
   * 추가/삭제하면 그 이후 모든 필드의 설명이 밀려서 엉뚱하게 매칭된다. 줄 수가 다른 탭이 있으면
   * 사용자가 인지하고 계속할지 확인받는다.
   */
  private boolean confirmLineAlignment() {
    Component[] tabs = dataTypeDefinitionPanel.getDtFieldTabbedPane().getComponents();

    for (int i = 0; i < tabs.length; i++) {
      DataTypeDefinitionFieldPanel panel = (DataTypeDefinitionFieldPanel) tabs[i];
      int nameLines = panel.getNameTextArea().getText().split("\n", -1).length;
      int descriptionLines = panel.getDescriptionTextArea().getText().split("\n", -1).length;

      if (nameLines != descriptionLines) {
        int result = JOptionPane.showConfirmDialog(
            this,
            String.format(
                "#%d 탭에서 Name(%d줄)과 Description(%d줄)의 줄 수가 다릅니다.%n"
                    + "설명이 밀려서 엉뚱한 필드에 매칭되었을 수 있습니다. 그래도 계속하시겠습니까?",
                i + 1, nameLines, descriptionLines),
            "줄 수 불일치",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
          return false;
        }
      }
    }

    return true;
  }

  private List<DataTypeElement> extractDataTypeFields() {
    DataTypeFieldParser dataTypeFieldParser = new DataTypeFieldParser();
    return Arrays.stream(dataTypeDefinitionPanel.getDtFieldTabbedPane().getComponents())
        .map(component -> (DataTypeDefinitionFieldPanel) component)
        .map(component ->
            dataTypeFieldParser.parseData(
                component.getNameTextArea().getText(),
                component.getDescriptionTextArea().getText()))
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }
}
