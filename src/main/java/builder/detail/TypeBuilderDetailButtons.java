package builder.detail;

import static builder.TypeBuilder.CONTENT_HEIGHT;
import static builder.TypeBuilder.CONTENT_WIDTH;
import static ui.FrameInfo.ICON_SIZE;

import builder.TypeBuilder;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import model.DataTypeNode;
import ui.FrameInfo;

public class TypeBuilderDetailButtons {

  private final TypeBuilder builder;

  public TypeBuilderDetailButtons(TypeBuilder builder) {
    this.builder = builder;
  }

  protected JButton buildRemoveButton(DataTypeNode node) {
    JButton removeButton = new JButton(
        new ImageIcon(
            new ImageIcon("resources/delete.png")
                .getImage()
                .getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH)));

    removeButton.setBackground(FrameInfo.PRIMARY);
    removeButton.setPreferredSize(new Dimension(CONTENT_WIDTH, CONTENT_HEIGHT));
    removeButton.setBorder(null);

    removeButton.addActionListener(e -> builder.removeDetailPanel(node));

    return removeButton;
  }
}
