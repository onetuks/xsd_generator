package specification.elements;

import specification.DataTypeSpecificationPanel;
import ui.FrameInfo;

import javax.swing.*;
import java.awt.*;

public class DataTypeElementSpecificationPanel extends JPanel {

    private final DataTypeElementSpecificationButtonFactory buttons;
    private final DataTypeElementSpecificationTextFieldFactory textFields;
    private final DataTypeElementSpecificationComboBoxFactory comboBoxes;
    private final DataTypeElementSpecificationCheckBoxFactory checkBoxes;

    public DataTypeElementSpecificationPanel(DataTypeSpecificationPanel specification, DataTypeElement element) {
        this.buttons = new DataTypeElementSpecificationButtonFactory(specification);
        this.textFields = new DataTypeElementSpecificationTextFieldFactory();
        this.comboBoxes = new DataTypeElementSpecificationComboBoxFactory();
        this.checkBoxes = new DataTypeElementSpecificationCheckBoxFactory();

        setBorder(FrameInfo.COMPOUND_BORDER);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 25 * 2));

        buildDetailPanel(element);
    }

    private void buildDetailPanel(DataTypeElement element) {
        add(buttons.createRemoveButton(element));
        add(textFields.createNameTextField(element));
        add(comboBoxes.createCategoryComboBox(element));
        add(comboBoxes.createTypeComboBox(element));
        add(comboBoxes.createOccurrenceComboBox(element));
        add(textFields.createDescriptionTextField(element));
        add(checkBoxes.createAttributeCheckBoxPanel(element));
    }
}
