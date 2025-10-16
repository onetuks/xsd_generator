package definition.components;

import definition.services.DataTypeFieldParser;
import definition.DataTypeDefinitionPanel;
import specification.elements.DataTypeElement;
import util.Navigator;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
                        .updateDataTypeState(
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
