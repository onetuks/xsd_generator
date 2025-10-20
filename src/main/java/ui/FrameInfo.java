package ui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public interface FrameInfo {

  Border EMPTY_BORDER = BorderFactory.createEmptyBorder(2, 2, 2,2);
  Border COMPOUND_BORDER =
          BorderFactory.createCompoundBorder(
                  BorderFactory.createLineBorder(new Color(254, 119, 67)),
                  EMPTY_BORDER);
}
