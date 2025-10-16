package specification.elements;

import static builder.TypeBuilder.CONTENT_HEIGHT;
import static builder.TypeBuilder.CONTENT_WIDTH;
import static ui.FrameInfo.ICON_SIZE;

import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import specification.DataTypeSpecificationPanel;

public record DataTypeElementSpecificationButtonFactory(DataTypeSpecificationPanel specification) {

    JButton createRemoveButton(DataTypeElement element) {
        JButton removeButton = new JButton(
                new ImageIcon(
                        new ImageIcon("resources/delete.png")
                                .getImage()
                                .getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH)));

        removeButton.setPreferredSize(new Dimension(CONTENT_WIDTH, CONTENT_HEIGHT));
        removeButton.setBorderPainted(false);

        removeButton.addActionListener(e -> specification.removeElement(element));

        return removeButton;
    }
}
