package specification.elements;

import java.awt.Dimension;
import javax.swing.JPanel;
import specification.DataTypeSpecificationPanel;
import ui.FrameInfo;

public class DataTypeElementSpecificationPanel extends JPanel {

  private final DataTypeElementSpecificationButtonFactory buttons;
  private final DataTypeElementSpecificationTextFieldFactory textFields;
  private final DataTypeElementSpecificationComboBoxFactory comboBoxes;
  private final DataTypeElementSpecificationCheckBoxFactory checkBoxes;

  public DataTypeElementSpecificationPanel(DataTypeSpecificationPanel specification,
      DataTypeElement element) {
    this.buttons = new DataTypeElementSpecificationButtonFactory(specification);
    this.textFields = new DataTypeElementSpecificationTextFieldFactory(specification);
    this.comboBoxes = new DataTypeElementSpecificationComboBoxFactory(specification);
    this.checkBoxes = new DataTypeElementSpecificationCheckBoxFactory(specification);

    setBorder(FrameInfo.COMPOUND_BORDER);
    setMaximumSize(new Dimension(Integer.MAX_VALUE, 25 * 2));

    buildDetailPanel(element);
  }

  private void buildDetailPanel(DataTypeElement element) {
    add(buttons.createRemoveButton(element));
    add(textFields.createTextField(element,
        DataTypeElementSpecificationTextFieldFactory.DataTypeElementTextFieldType.NAME));
    add(comboBoxes.createCategoryComboBox(element));
    add(comboBoxes.createTypeComboBox(element));
    add(comboBoxes.createOccurrenceComboBox(element));
    add(textFields.createTextField(element,
        DataTypeElementSpecificationTextFieldFactory.DataTypeElementTextFieldType.DESCRIPTION));
    add(checkBoxes.createAttributeCheckBoxPanel(element));
  }
}
