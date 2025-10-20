package ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public interface FrameInfo {

    Border EMPTY_BORDER = BorderFactory.createEmptyBorder(2, 2, 2, 2);
    Border COMPOUND_BORDER =
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(254, 119, 67)),
                    EMPTY_BORDER);
}
