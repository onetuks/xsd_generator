package definition;

import core.DataTypeFieldParser;
import global.Navigator;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataTypeDefinitionButtonPanel extends JPanel {

    private final DataTypeDefinition dataTypeDefinition;

    public DataTypeDefinitionButtonPanel(DataTypeDefinition dataTypeDefinition) {
        this.dataTypeDefinition = dataTypeDefinition;

        initResetButton();
        initNextButton();
    }

    private void initResetButton() {
        JButton resetBtn = new JButton("Reset");
        resetBtn.addActionListener(e -> {
            dataTypeDefinition.getDtInfoPanel().clear();
            dataTypeDefinition.getDtFieldTabbedPane().clearAllTabs();
        });
        add(resetBtn);
    }

    private void initNextButton() {
        JButton nextBtn = new JButton("Next");
        nextBtn.addActionListener(e -> {
            try {
                dataTypeDefinition
                        .getDataTypePipelineService()
                        .updateDataType(
                                dataTypeDefinition.getDtInfoPanel().getDTName(),
                                dataTypeDefinition.getDtInfoPanel().getNamespace(),
                                dataTypeDefinition.getDtInfoPanel().getTargetDir());
                dataTypeDefinition.getDataTypePipelineService().updateDataType(extractDataTypeFields());
                dataTypeDefinition.getNavigator().showScreen(Navigator.SPECIFICATION);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(nextBtn);
    }

    private List<DataTypeElement> extractDataTypeFields() {
        DataTypeFieldParser dataTypeFieldParser = new DataTypeFieldParser();
        return Arrays.stream(dataTypeDefinition.getDtFieldTabbedPane().getComponents())
                .map(component -> (DataTypeDefinitionFieldPanel) component)
                .map(component ->
                        dataTypeFieldParser.parseData(
                                component.getNameTextArea().getText(),
                                component.getDescriptionTextArea().getText()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
