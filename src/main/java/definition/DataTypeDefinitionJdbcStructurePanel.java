package definition;

import ui.FrameInfo;

import javax.swing.*;
import java.awt.event.ActionListener;

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
        panel.add(new JButton("SELECT") {
            @Override
            public void addActionListener(ActionListener l) {
                DataTypeDefinitionFieldPanel component =
                        (DataTypeDefinitionFieldPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
                component.appendSchemaToTextArea(jdbcStructureInvocator.generateSELECTStructure());
            }
        });
        panel.add(new JButton("DML") {
            @Override
            public void addActionListener(ActionListener l) {
                DataTypeDefinitionFieldPanel component =
                        (DataTypeDefinitionFieldPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
                component.appendSchemaToTextArea(
                        jdbcStructureInvocator.generateDMLStructure(tabbedPane.getSelectedIndex() + 1));
            }
        });
        panel.add(new JButton("SQL") {
            @Override
            public void addActionListener(ActionListener l) {
                DataTypeDefinitionFieldPanel component =
                        (DataTypeDefinitionFieldPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
                component.appendSchemaToTextArea(
                        jdbcStructureInvocator.generateSQLStructure(tabbedPane.getSelectedIndex() + 1));
            }
        });
        panel.add(new JButton("PROCEDURE") {
            @Override
            public void addActionListener(ActionListener l) {
                DataTypeDefinitionFieldPanel component =
                        (DataTypeDefinitionFieldPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
                component.appendSchemaToTextArea(
                        jdbcStructureInvocator.generatePROCEDUREStructure(tabbedPane.getSelectedIndex() + 1));
            }
        });
        return panel;
    }
}
