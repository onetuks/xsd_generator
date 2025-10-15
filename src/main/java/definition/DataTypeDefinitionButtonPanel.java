package definition;

import core.DataTypeFieldParser;
import dto.XDataType;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataTypeDefinitionButtonPanel extends JPanel {

    private final DataTypeFieldParser dataTypeFieldParser = new DataTypeFieldParser();

    private final DataTypeDefinitionInfoPanel dtInfoPanel;
    private final DataTypeDefinitionFieldTabbedPane dtFieldTabbedPane;

    public DataTypeDefinitionButtonPanel(
            DataTypeDefinitionInfoPanel dtInfoPanel,
            DataTypeDefinitionFieldTabbedPane dtFieldTabbedPane
    ) {
        this.dtInfoPanel = dtInfoPanel;
        this.dtFieldTabbedPane = dtFieldTabbedPane;

        initResetButton();
        initNextButton();
    }

    private void initResetButton() {
        JButton resetBtn = new JButton("Reset");
        resetBtn.addActionListener(e -> {
            dtInfoPanel.clear();
            dtFieldTabbedPane.clear();
        });
        add(resetBtn);
    }

    private void initNextButton() {
        JButton nextBtn = new JButton("Next");
        nextBtn.addActionListener(e -> {
            try {
//                setVisible(false);
//                new TypeBuilder(
//                        this,
//                        new XDataTypes(
//                                dtInfoPanel.getDtNameComponent().getInfoTextField().getText(),
//                                dtInfoPanel.getNamespaceComponent().getInfoTextField().getText(),
//                                dtInfoPanel.getTargetDirComponent().getInfoTextField().getText(),
//                                extractDataTypeFields()));
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(nextBtn);
    }

    private List<XDataType> extractDataTypeFields() {
        return Arrays.stream(dtFieldTabbedPane.getComponents())
                .map(component -> (DataTypeDefinitionFieldPanel) component)
                .map(component ->
                        dataTypeFieldParser.parseData(
                                component.getNameTextArea().getText(),
                                component.getDescriptionTextArea().getText()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
