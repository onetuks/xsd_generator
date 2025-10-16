package specification.elements;

import model.vo.Attribute;
import model.vo.Category;

import javax.swing.*;
import java.util.Arrays;

public record DataTypeElementSpecificationCheckBoxFactory() {

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

        setCheckBoxEnabled(attributeCheckBox, element.getCategory());

        attributeCheckBox.addActionListener(e -> {
            setCheckBoxEnabled(attributeCheckBox, element.getCategory());

            if (attributeCheckBox.isSelected()) {
                element.getAttributes().add(attribute);
            }

            element.getAttributes().remove(attribute);
        });

        return attributeCheckBox;
    }

    private void setCheckBoxEnabled(JCheckBox attributeCheckBox, Category category) {
        attributeCheckBox.setEnabled(category != Category.ATTRIBUTE);
    }
}
