package specification.elements;

import java.util.Arrays;
import java.util.Objects;
import javax.swing.JComboBox;

import model.vo.Category;
import model.vo.Occurrence;
import model.vo.Type;
import ui.FrameInfo;

public record DataTypeElementSpecificationComboBoxFactory() {

    JComboBox<Category> createCategoryComboBox(DataTypeElement element) {
        JComboBox<Category> categoryComboBox = new JComboBox<>(
                Arrays.stream(Category.values())
                        .filter(category -> category != Category.COMPLEX_TYPE)
                        .toArray(Category[]::new));

        categoryComboBox.setSelectedItem(element.getCategory());

        categoryComboBox.addActionListener(e -> {
            Category updatedCategory =
                    Category.valueOf(Objects.requireNonNull(categoryComboBox.getSelectedItem()).toString());

            element.setCategory(updatedCategory);

            if (updatedCategory == Category.ATTRIBUTE) {
                element.setType(Type.STRING);
                element.setOccurrence(Occurrence.ofOptional());
            } else {
                element.setOccurrence(Occurrence.ofZeroOne());
            }
        });

        return categoryComboBox;
    }

    JComboBox<Type> createTypeComboBox(DataTypeElement element) {
        JComboBox<Type> typeComboBox = new JComboBox<>(Type.values());

        typeComboBox.setBackground(FrameInfo.SECONDARY);
        typeComboBox.setForeground(FrameInfo.TERTIARY);

        typeComboBox.setSelectedItem(element.getType());

        typeComboBox.addActionListener(e -> {
            Type updatedType = Type.valueOf(Objects.requireNonNull(typeComboBox.getSelectedItem()).toString());
            element.setType(updatedType);
        });

        return typeComboBox;
    }

    JComboBox<String> createOccurrenceComboBox(DataTypeElement element) {
        JComboBox<String> occurrenceComboBox = new JComboBox<>(Occurrence.getOccurrenceCombo());

        occurrenceComboBox.setBackground(FrameInfo.SECONDARY);
        occurrenceComboBox.setForeground(FrameInfo.TERTIARY);

        occurrenceComboBox.setSelectedItem(element.getOccurrence().toString());

        occurrenceComboBox.addActionListener(e ->
                element.setOccurrence(Occurrence.of((String) occurrenceComboBox.getSelectedItem())));

        return occurrenceComboBox;
    }
}
