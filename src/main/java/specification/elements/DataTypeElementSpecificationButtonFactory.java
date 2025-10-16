package specification.elements;

import specification.DataTypeSpecificationPanel;
import util.IconLoader;

import javax.swing.*;
import java.awt.*;

public record DataTypeElementSpecificationButtonFactory(DataTypeSpecificationPanel specification) {

    JButton createRemoveButton(DataTypeElement element) {
        JButton removeButton = new JButton(new IconLoader().loadIcon(IconLoader.DELETE_ICON_PATH));

        removeButton.setPreferredSize(new Dimension(25, 25));
        removeButton.setBorderPainted(false);
        removeButton.setContentAreaFilled(false);

        removeButton.addActionListener(e -> specification.removeElement(element));

        return removeButton;
    }
}
