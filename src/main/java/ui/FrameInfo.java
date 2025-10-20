package ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public interface FrameInfo {

    Color PRIMARY_COLOR = new Color(254, 119, 67);

    Border EMPTY_BORDER = BorderFactory.createEmptyBorder(2, 2, 2, 2);
    Border COMPOUND_BORDER =
            BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(PRIMARY_COLOR), EMPTY_BORDER);
}
