package specification.elements;

import static builder.TypeBuilder.CONTENT_HEIGHT;

import java.awt.Dimension;
import javax.swing.JPanel;

import specification.DataTypeSpecificationPanel;
import ui.FrameInfo;

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
        setMaximumSize(new Dimension(Integer.MAX_VALUE, CONTENT_HEIGHT * 2));

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
