package definition.components;

import definition.DataTypeDefinitionPanel;
import definition.services.DataTypeFieldParser;
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
