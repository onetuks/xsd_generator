package definition.components;

import definition.services.JdbcStructureInvocator;
import ui.FrameInfo;

import javax.swing.*;

public class DataTypeDefinitionJdbcStructurePanel extends JPanel {

    private final JdbcStructureInvocator jdbcStructureInvocator = new JdbcStructureInvocator();

    public DataTypeDefinitionJdbcStructurePanel(JTabbedPane tabbedPane) {
        super();

        setBorder(FrameInfo.COMPOUND_BORDER);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(titlePanel());
        add(buttonPanel(tabbedPane));
    }

    private JPanel titlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("Jdbc Manipulation"));
        return titlePanel;
    }

    private JPanel buttonPanel(JTabbedPane tabbedPane) {
        JPanel panel = new JPanel();

        JButton selectBtn = new JButton("Select");
        selectBtn.addActionListener(e -> {
            DataTypeDefinitionFieldPanel component =
                    (DataTypeDefinitionFieldPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
            component.appendSchemaToTextArea(jdbcStructureInvocator.generateSELECTStructure());
        });

        JButton updateBtn = new JButton("Update");
        updateBtn.addActionListener(e -> {
            DataTypeDefinitionFieldPanel component =
                    (DataTypeDefinitionFieldPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
            component.appendSchemaToTextArea(
                    jdbcStructureInvocator.generateDMLStructure(tabbedPane.getSelectedIndex() + 1));
        });

        JButton sqlBtn = new JButton("SQL");
        sqlBtn.addActionListener(e -> {
            DataTypeDefinitionFieldPanel component =
                    (DataTypeDefinitionFieldPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
            component.appendSchemaToTextArea(
                    jdbcStructureInvocator.generateSQLStructure(tabbedPane.getSelectedIndex() + 1));
        });

        JButton procedureBtn = new JButton("Procedure");
        procedureBtn.addActionListener(e -> {
            DataTypeDefinitionFieldPanel component =
                    (DataTypeDefinitionFieldPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
            component.appendSchemaToTextArea(
                    jdbcStructureInvocator.generatePROCEDUREStructure(tabbedPane.getSelectedIndex() + 1));
        });

        panel.add(selectBtn);
        panel.add(updateBtn);
        panel.add(sqlBtn);
        panel.add(procedureBtn);

        return panel;
    }
}
