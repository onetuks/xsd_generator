package specification.elements;

import static builder.TypeBuilder.CONTENT_HEIGHT;
import static builder.TypeBuilder.TEXT_FIELD_WIDTH;

import java.awt.Dimension;
import javax.swing.JTextField;

public record DataTypeElementSpecificationTextFieldFactory() {

    JTextField createNameTextField(DataTypeElement element) {
        JTextField nameTextField = new JTextField(element.getName());
        nameTextField.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, CONTENT_HEIGHT));
        return nameTextField;
    }

    JTextField createDescriptionTextField(DataTypeElement element) {
        JTextField descriptionTextField = new JTextField(element.getDescription());
        descriptionTextField.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, CONTENT_HEIGHT));
        return descriptionTextField;
    }
}
