package builder.detail;

import builder.TypeBuilder;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.JComboBox;
import model.XNode;
import model.vo.Category;
import model.vo.Occurrence;
import model.vo.Type;
import ui.FrameInfo;

public class TypeBuilderDetailComboBoxes {

  private final TypeBuilder builder;

  public TypeBuilderDetailComboBoxes(TypeBuilder builder) {
    this.builder = builder;
  }

  protected JComboBox<Category> buildCategoryComboBox(XNode node) {
    JComboBox<Category> categoryComboBox = new JComboBox<>(
        Arrays.stream(Category.values())
            .filter(category -> category != Category.COMPLEX_TYPE)
            .toArray(Category[]::new));

    categoryComboBox.setBackground(FrameInfo.SECONDARY);
    categoryComboBox.setForeground(FrameInfo.TERTIARY);

    categoryComboBox.setSelectedItem(node.getEntity().getCategory());

    categoryComboBox.addActionListener(e -> {
      Category updatedCategory =
          Category.valueOf(Objects.requireNonNull(categoryComboBox.getSelectedItem()).toString());
      builder.updateCategory(node, updatedCategory);
    });

    return categoryComboBox;
  }

  protected JComboBox<Type> buildTypeComboBox(XNode node) {
    JComboBox<Type> typeComboBox = new JComboBox<>(Type.values());

    typeComboBox.setBackground(FrameInfo.SECONDARY);
    typeComboBox.setForeground(FrameInfo.TERTIARY);

    typeComboBox.setSelectedItem(node.getEntity().getType());

    typeComboBox.addActionListener(e -> {
      Type updatedType = Type.valueOf(Objects.requireNonNull(typeComboBox.getSelectedItem()).toString());
      builder.updateType(node, updatedType);
    });

    return typeComboBox;
  }

  protected JComboBox<String> buildOccurrenceComboBox(XNode node) {
    JComboBox<String> occurrenceComboBox = new JComboBox<>(Occurrence.getOccurrenceCombo());

    occurrenceComboBox.setBackground(FrameInfo.SECONDARY);
    occurrenceComboBox.setForeground(FrameInfo.TERTIARY);

    occurrenceComboBox.setSelectedItem(node.getEntity().getOccurrence().toString());

    occurrenceComboBox.addActionListener(e ->
        node.getEntity().setOccurrence(Occurrence.of((String) occurrenceComboBox.getSelectedItem())));

    return occurrenceComboBox;
  }
}
