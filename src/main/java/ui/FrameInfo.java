package ui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public interface FrameInfo {

  Color PRIMARY = new Color(39, 63, 79);
  Color SECONDARY = new Color(254, 119, 67);
  Color TERTIARY = new Color(239, 238, 234);
  Color BASE_COLOR = new Color(70, 75, 79);

  Border LINE_BORDER = BorderFactory.createLineBorder(FrameInfo.SECONDARY);
  Border EMPTY_BORDER = BorderFactory.createEmptyBorder(2, 2, 2,2);
  Border COMPOUND_BORDER = BorderFactory.createCompoundBorder(LINE_BORDER, EMPTY_BORDER);
}
