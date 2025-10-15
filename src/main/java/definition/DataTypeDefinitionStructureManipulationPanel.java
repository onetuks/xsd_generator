package definition;

import ui.FrameInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DataTypeDefinitionStructureManipulationPanel extends JPanel {

    public DataTypeDefinitionStructureManipulationPanel(JTabbedPane tabbedPane) {
        super();

        setBorder(FrameInfo.COMPOUND_BORDER);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(titlePanel());
        add(buttonPanel(tabbedPane));
    }

    private JPanel titlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("Structure Manipulation"));
        return titlePanel;
    }

    private JPanel buttonPanel(JTabbedPane tabbedPane) {
        JPanel panel = new JPanel();
        panel.add(new JButton("Add") {
            @Override
            public void addActionListener(ActionListener l) {
                String newTabTitle = "#" + (tabbedPane.getTabCount() + 1);
                tabbedPane.addTab(newTabTitle, null, new DataTypeDefinitionFieldPanel(), null);
            }
        });
        panel.add(new JButton("Remove") {
            @Override
            public void addActionListener(ActionListener l) {
                tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
            }
        });
        return panel;
    }
}
