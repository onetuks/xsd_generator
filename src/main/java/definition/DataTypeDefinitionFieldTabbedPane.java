package definition;

import ui.FrameInfo;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

public class DataTypeDefinitionFieldTabbedPane extends JTabbedPane {

    public DataTypeDefinitionFieldTabbedPane() {
        super();

        clear();
        setForeground(FrameInfo.TERTIARY);
        setUI(new BasicTabbedPaneUI() {

            @Override
            protected void paintTabBackground(
                    Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                    boolean isSelected) {
                g.setColor(isSelected ? FrameInfo.SECONDARY : FrameInfo.PRIMARY);
                g.fillRect(x, y, w, h);
            }

            @Override
            protected void paintTabBorder(
                    Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                    boolean isSelected) {
                g.setColor(FrameInfo.SECONDARY);
                g.drawRect(x, y, w, h);
            }

            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                Insets insets = tabPane.getInsets();
                int width = tabPane.getWidth() - insets.left - insets.right;
                int height = tabPane.getHeight() - insets.top - insets.bottom;
                int tabAreaHeight = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
                g.setColor(FrameInfo.SECONDARY);
                g.fillRect(insets.left, insets.top + tabAreaHeight, width, height - tabAreaHeight);
            }
        });
    }

    public void clear() {
        removeAll();
        addTab("#" + (getTabCount() + 1), null, new DataTypeDefinitionFieldPanel(), null);
    }
}
