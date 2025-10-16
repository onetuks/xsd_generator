package builder.detail;

import static builder.TypeBuilder.CONTENT_HEIGHT;
import static builder.TypeBuilder.TEXT_FIELD_WIDTH;

import java.awt.Dimension;
import javax.swing.JTextField;
import model.DataTypeNode;
import ui.FrameInfo;

public class TypeBuilderDetailTextFields {

  protected JTextField buildNameTextField(DataTypeNode node) {
    JTextField nameTextField = new JTextField(node.entity().getName());

    setDefaultTextFieldConfiguration(nameTextField);

    return nameTextField;
  }

  protected JTextField buildDescriptionTextField(DataTypeNode node) {
    JTextField descriptionTextField = new JTextField(node.entity().getDescription());

    setDefaultTextFieldConfiguration(descriptionTextField);

    return descriptionTextField;
  }

  private void setDefaultTextFieldConfiguration(JTextField textField) {
    textField.setBackground(FrameInfo.PRIMARY);
    textField.setForeground(FrameInfo.TERTIARY);
    textField.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, CONTENT_HEIGHT));
    textField.setEditable(false);
  }
}
