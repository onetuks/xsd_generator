package specification.elements;

import java.awt.Dimension;
import javax.swing.JButton;
import specification.DataTypeSpecificationPanel;
import util.IconLoader;

public class DataTypeElementSpecificationButtonFactory {

  private final DataTypeSpecificationPanel specification;

  public DataTypeElementSpecificationButtonFactory(DataTypeSpecificationPanel specification) {
    this.specification = specification;
  }

  JButton createRemoveButton(DataTypeElement element) {
    JButton removeButton = new JButton(new IconLoader().loadIcon(IconLoader.DELETE_ICON_PATH));

    removeButton.setPreferredSize(new Dimension(25, 25));
    removeButton.setBorderPainted(false);
    removeButton.setContentAreaFilled(false);

    removeButton.addActionListener(e -> specification.removeElement(element));

    return removeButton;
  }
}
