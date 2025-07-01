package builder.detail;

import static builder.TypeBuilder.CONTENT_HEIGHT;
import static builder.TypeBuilder.TEXT_FIELD_WIDTH;

import java.awt.Dimension;
import javax.swing.JTextField;
import model.XNode;
import ui.FrameInfo;

public class TypeBuilderDetailTextFields {

  protected JTextField buildNameTextField(XNode node) {
    JTextField nameTextField = new JTextField(node.getEntity().getName());

    setDefaultTextFieldConfiguration(nameTextField);

    return nameTextField;
  }

  protected JTextField buildDescriptionTextField(XNode node) {
    JTextField descriptionTextField = new JTextField(node.getEntity().getDescription());

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
