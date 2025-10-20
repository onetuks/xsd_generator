package specification.elements;

import model.vo.Category;
import model.vo.Occurrence;
import model.vo.Type;

import javax.swing.*;
import java.util.Arrays;
import java.util.Objects;

public record DataTypeElementSpecificationComboBoxFactory(specification.DataTypeSpecificationPanel specification) {

    JComboBox<Category> createCategoryComboBox(DataTypeElement element) {
        JComboBox<Category> categoryComboBox = new JComboBox<>(
                Arrays.stream(Category.values())
                        .filter(category -> category != Category.COMPLEX_TYPE)
                        .toArray(Category[]::new));

        categoryComboBox.setSelectedItem(element.getCategory());

        categoryComboBox.addActionListener(e -> {
            Category updatedCategory =
                    Category.valueOf(Objects.requireNonNull(categoryComboBox.getSelectedItem()).toString());

            DataTypeElement targetElement =
                    specification.getService().getDataTypeElements().stream()
                            .filter(dataTypeElement -> element == dataTypeElement)
                            .findAny()
                            .orElseThrow(() -> new RuntimeException("DataTypeElement not found"));

            targetElement.setCategory(updatedCategory);

            if (updatedCategory == Category.ATTRIBUTE) {
                targetElement.setType(Type.STRING);
                targetElement.setOccurrence(Occurrence.ofOptional());
            } else {
                targetElement.setOccurrence(Occurrence.ofZeroOne());
            }
        });

        return categoryComboBox;
    }

    JComboBox<Type> createTypeComboBox(DataTypeElement element) {
        JComboBox<Type> typeComboBox = new JComboBox<>(Type.values());

        typeComboBox.setSelectedItem(element.getType());

        typeComboBox.addActionListener(e -> {
            Type updatedType = Type.valueOf(Objects.requireNonNull(typeComboBox.getSelectedItem()).toString());
            element.setType(updatedType);
        });

        return typeComboBox;
    }

    JComboBox<String> createOccurrenceComboBox(DataTypeElement element) {
        JComboBox<String> occurrenceComboBox = new JComboBox<>(Occurrence.getOccurrenceCombo());

        occurrenceComboBox.setSelectedItem(element.getOccurrence().toString());

        occurrenceComboBox.addActionListener(e ->
                element.setOccurrence(Occurrence.of((String) occurrenceComboBox.getSelectedItem())));

        return occurrenceComboBox;
    }
}
