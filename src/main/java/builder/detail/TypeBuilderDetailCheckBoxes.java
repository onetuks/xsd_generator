package builder.detail;

public class TypeBuilderDetailCheckBoxes {



  private final TypeBuilder builder;



  public TypeBuilderDetailCheckBoxes(TypeBuilder builder) {

    this.builder = builder;

  }

﻿

  protected JPanel buildAttributeCheckBoxPanel(XNode node) {

    JPanel attributeCheckBoxPanel = new JPanel();



    attributeCheckBoxPanel.setBackground(FrameInfo.PRIMARY);



    Arrays.stream(Attribute.values())

        .filter(attribute -> attribute != Attribute.ACTION)

        .map(attribute -> buildAttributeCheckBox(attribute, node))

        .forEach(attributeCheckBoxPanel::add);



    return attributeCheckBoxPanel;

  }



  private JCheckBox buildAttributeCheckBox(Attribute attribute, XNode node) {

    JCheckBox attributeCheckBox = new JCheckBox(attribute.getName());



    attributeCheckBox.setBackground(FrameInfo.SECONDARY);

    attributeCheckBox.setForeground(FrameInfo.TERTIARY);



    setCheckBoxEnabled(attributeCheckBox, node.getEntity().getCategory());



    attributeCheckBox.addActionListener(e -> {

      setCheckBoxEnabled(attributeCheckBox, node.getEntity().getCategory());



      if (attributeCheckBox.isSelected()) {

        builder.addAttribute(node, attribute);

        return;

      }



      builder.removeAttribute(node, attribute);

    });



    return attributeCheckBox;

  }



  private void setCheckBoxEnabled(JCheckBox attributeCheckBox, Category category) {

    attributeCheckBox.setEnabled(category != Category.ATTRIBUTE);

  }

}