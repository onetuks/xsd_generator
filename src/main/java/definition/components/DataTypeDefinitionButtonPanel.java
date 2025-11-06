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
      dataTypeDefinitionPanel.getDtInfoPanel().clear();
      dataTypeDefinitionPanel.getDtFieldTabbedPane().clearAllTabs();
    });
    add(resetBtn);
  }

  private void initNextButton() {
    JButton nextBtn = new JButton("Next");
    nextBtn.addActionListener(e -> {
      try {
        dataTypeDefinitionPanel
            .getDataTypePipelineService()
            .updateDataTypeElements(
                dataTypeDefinitionPanel.getDtInfoPanel().getMTName(),
                dataTypeDefinitionPanel.getDtInfoPanel().getDTName(),
                dataTypeDefinitionPanel.getDtInfoPanel().getNamespace(),
                dataTypeDefinitionPanel.getDtInfoPanel().getTargetDir(),
                extractDataTypeFields());
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
