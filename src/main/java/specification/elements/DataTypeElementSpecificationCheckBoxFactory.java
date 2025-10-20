package specification.elements;

import model.vo.Attribute;
import model.vo.Category;
import specification.DataTypeSpecificationPanel;

import javax.swing.*;
import java.util.Arrays;
import java.util.Objects;

public record DataTypeElementSpecificationCheckBoxFactory(DataTypeSpecificationPanel specificationPanel) {

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

        attributeCheckBox.setSelected(element.getAttributes().contains(attribute));

        attributeCheckBox.addActionListener(e -> {
            setCheckBoxEnabled(attributeCheckBox, element.getCategory());

            DataTypeElement targetElement = specificationPanel.getService().getDataTypeElements().stream()
                    .filter(dataTypeElement -> dataTypeElement == element)
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Element not found"));

            if (attributeCheckBox.isSelected()) {
                targetElement.getAttributes().add(attribute);
                return;
            }

            targetElement.getAttributes().remove(attribute);
        });

        return attributeCheckBox;
    }

    private void setCheckBoxEnabled(JCheckBox attributeCheckBox, Category category) {
        attributeCheckBox.setEnabled(category != Category.ATTRIBUTE);
    }
}
