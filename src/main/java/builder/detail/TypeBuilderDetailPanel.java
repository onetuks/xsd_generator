package builder.detail;

import static builder.TypeBuilder.CONTENT_HEIGHT;

import builder.TypeBuilder;
import java.awt.Dimension;
import javax.swing.JPanel;
import model.XNode;
import ui.FrameInfo;

public class TypeBuilderDetailPanel extends JPanel {

  private final TypeBuilderDetailButtons buttons;
  private final TypeBuilderDetailTextFields textFields;
  private final TypeBuilderDetailComboBoxes comboBoxes;
  private final TypeBuilderDetailCheckBoxes checkBoxes;

  public TypeBuilderDetailPanel(TypeBuilder builder, XNode node) {
    super();

    this.buttons = new TypeBuilderDetailButtons(builder);
    this.textFields = new TypeBuilderDetailTextFields();
    this.comboBoxes = new TypeBuilderDetailComboBoxes(builder);
    this.checkBoxes = new TypeBuilderDetailCheckBoxes(builder);

    setBackground(FrameInfo.PRIMARY);
    setBorder(FrameInfo.COMPOUND_BORDER);
    setMaximumSize(new Dimension(Integer.MAX_VALUE, CONTENT_HEIGHT * 2));
    setVisible(true);

    buildDetailPanel(node);
  }

  private void buildDetailPanel(XNode node) {
    add(buttons.buildRemoveButton(node));
    add(textFields.buildNameTextField(node));
    add(comboBoxes.buildCategoryComboBox(node));
    add(comboBoxes.buildTypeComboBox(node));
    add(comboBoxes.buildOccurrenceComboBox(node));
    add(textFields.buildDescriptionTextField(node));
    add(checkBoxes.buildAttributeCheckBoxPanel(node));
  }
}
