package specification.elements;

import java.awt.Dimension;
import java.util.Objects;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import specification.DataTypeSpecificationPanel;

public class DataTypeElementSpecificationTextFieldFactory {

  private final DataTypeSpecificationPanel specification;

  public DataTypeElementSpecificationTextFieldFactory(DataTypeSpecificationPanel specification) {
    this.specification = specification;
  }

  JTextField createTextField(DataTypeElement element, DataTypeElementTextFieldType type) {
    JTextField textField;
    if (type == DataTypeElementTextFieldType.NAME) {
      textField = new JTextField(element.getName());
    } else if (type == DataTypeElementTextFieldType.DESCRIPTION) {
      textField = new JTextField(element.getDescription());
    } else {
      textField = null;
    }

    Objects.requireNonNull(textField).setPreferredSize(new Dimension(200, 20));
    textField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        changeDataTypeTextField(element, textField.getText(), type);
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        changeDataTypeTextField(element, textField.getText(), type);
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        changeDataTypeTextField(element, textField.getText(), type);
      }
    });

    return textField;
  }

  private void changeDataTypeTextField(DataTypeElement element, String name,
      DataTypeElementTextFieldType type) {
    DataTypeElement targetElement = specification.getService().getDataTypeElements().stream()
        .filter(dataTypeElement -> dataTypeElement == element)
        .findFirst()
        .orElseThrow(() -> new RuntimeException("DataType Element not found"));

    if (type == DataTypeElementTextFieldType.NAME) {
      targetElement.setName(name);
    } else if (type == DataTypeElementTextFieldType.DESCRIPTION) {
      targetElement.setDescription(name);
    }
  }

  enum DataTypeElementTextFieldType {
    NAME, DESCRIPTION
  }
}
