package arborist;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import ui.FrameInfo;

public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
      boolean expanded, boolean leaf, int row, boolean hasFocus) {
    Component component =
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

    setBackground(FrameInfo.PRIMARY);
    setForeground(FrameInfo.TERTIARY);

    return component;
  }

  @Override
  public Color getBackground() {
    return FrameInfo.PRIMARY;
  }


  @Override

  public Color getBackgroundSelectionColor() {

    return FrameInfo.SECONDARY;

  }


  @Override

  public Color getBackgroundNonSelectionColor() {

    return FrameInfo.PRIMARY;

  }

}
