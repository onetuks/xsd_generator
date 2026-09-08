package specification.elements;

import java.awt.Dimension;
import java.util.Objects;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import specification.DataTypeSpecificationPanel;

public class DataTypeElementSpecificationTextFieldFactory {

  // 다른 Xxx Factory와 생성자 시그니처를 맞추기 위해 인자는 받되, 이 팩토리는 element를
  // 직접 조작하므로 specification 참조가 필요 없다.
  public DataTypeElementSpecificationTextFieldFactory(DataTypeSpecificationPanel specification) {
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
    if (type == DataTypeElementTextFieldType.NAME) {
      element.setName(name);
    } else if (type == DataTypeElementTextFieldType.DESCRIPTION) {
      element.setDescription(name);
    }
  }

  enum DataTypeElementTextFieldType {
    NAME, DESCRIPTION
  }
}
