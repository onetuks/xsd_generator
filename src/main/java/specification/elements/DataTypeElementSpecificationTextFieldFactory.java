package specification.elements;

import javax.swing.*;
import java.awt.*;

public record DataTypeElementSpecificationTextFieldFactory() {

    JTextField createNameTextField(DataTypeElement element) {
        JTextField nameTextField = new JTextField(element.getName());
        nameTextField.setPreferredSize(new Dimension(200, 20));
        return nameTextField;
    }

    JTextField createDescriptionTextField(DataTypeElement element) {
        JTextField descriptionTextField = new JTextField(element.getDescription());
        descriptionTextField.setPreferredSize(new Dimension(200, 20));
        return descriptionTextField;
    }
}
