package definition.components;

import ui.FrameInfo;

import javax.swing.*;
import java.awt.*;

public class DataTypeDefinitionStructureManipulationPanel extends JPanel {

    private final DataTypeDefinitionFieldTabbedPane fieldTabbedPane;

    public DataTypeDefinitionStructureManipulationPanel(DataTypeDefinitionFieldTabbedPane fieldTabbedPane) {
        super();

        this.fieldTabbedPane = fieldTabbedPane;

        setBorder(FrameInfo.COMPOUND_BORDER);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(titlePanel());
        add(buttonPanel());
    }

    private JPanel titlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("Structure Manipulation"));
        return titlePanel;
    }

    private JPanel buttonPanel() {
        JPanel panel = new JPanel();

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> fieldTabbedPane.addNewTab());

        JButton removeBtn = new JButton("Remove");
        removeBtn.addActionListener(e -> fieldTabbedPane.removeTabAt(fieldTabbedPane.getTabCount() - 1));

        panel.add(addBtn);
        panel.add(removeBtn);

        return panel;
    }
}
