package specification.elements;

import java.util.Arrays;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import model.vo.Attribute;
import model.vo.Category;
import specification.DataTypeSpecificationPanel;

public class DataTypeElementSpecificationCheckBoxFactory {

  // 다른 Xxx Factory와 생성자 시그니처를 맞추기 위해 인자는 받되, 이 팩토리는 element를
  // 직접 조작하므로 specification 참조가 필요 없다.
  public DataTypeElementSpecificationCheckBoxFactory(DataTypeSpecificationPanel specification) {
  }

  JPanel createAttributeCheckBoxPanel(DataTypeElement element) {
    JPanel attributeCheckBoxPanel = new JPanel();

    Arrays.stream(Attribute.values())
        .filter(attribute -> attribute != Attribute.ACTION)
        .map(attribute -> createAttributeCheckBox(attribute, element))
        .forEach(attributeCheckBoxPanel::add);

    return attributeCheckBoxPanel;
  }

  private JCheckBox createAttributeCheckBox(Attribute attribute, DataTypeElement element) {
    JCheckBox attributeCheckBox = new JCheckBox(attribute.getName());
    attributeCheckBox.setToolTipText(attribute.getDescription());

    setCheckBoxEnabled(attributeCheckBox, element.getCategory());

    attributeCheckBox.setSelected(element.getAttributes().contains(attribute));

    attributeCheckBox.addActionListener(e -> {
      setCheckBoxEnabled(attributeCheckBox, element.getCategory());

      if (attributeCheckBox.isSelected()) {
        element.getAttributes().add(attribute);
        return;
      }

      element.getAttributes().remove(attribute);
    });

    return attributeCheckBox;
  }

  private void setCheckBoxEnabled(JCheckBox attributeCheckBox, Category category) {
    attributeCheckBox.setEnabled(category != Category.ATTRIBUTE);
  }
}
