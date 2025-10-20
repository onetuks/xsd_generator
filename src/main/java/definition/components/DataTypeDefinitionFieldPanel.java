package definition.components;

import definition.services.DataTypeFieldParser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class DataTypeDefinitionFieldPanel extends JPanel {

    private static final String NAME = "Name";
    private static final String DESCRIPTION = "Description";

    private final JTextArea nameTextArea;
    private final JTextArea descriptionTextArea;

    protected DataTypeDefinitionFieldPanel() {
        super();

        setLayout(new GridLayout(1, 2));

        this.nameTextArea = new JTextArea();
        this.descriptionTextArea = new JTextArea();

        add(buildDataInputPanel(NAME));
        add(buildDataInputPanel(DESCRIPTION));
    }

    private JPanel buildDataInputPanel(String dataInputLabel) {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setPreferredSize(new Dimension(200, 300));

        panel.add(buildDataInputLabel(dataInputLabel));
        panel.add(buildDataInputScrollPane(dataInputLabel));

        return panel;
    }

    private JPanel buildDataInputLabel(String labelText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        JLabel label = new JLabel(labelText);
        label.setBorder(new EmptyBorder(0, 0, 5, 0));
        panel.add(label);

        return panel;
    }

    private JScrollPane buildDataInputScrollPane(String dataInputLabel) {
        JScrollPane scrollPane = new JScrollPane();

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        if (Objects.equals(dataInputLabel, NAME)) {
            scrollPane.setViewportView(nameTextArea);
        } else if (Objects.equals(dataInputLabel, DESCRIPTION)) {
            scrollPane.setViewportView(descriptionTextArea);
        }

        return scrollPane;
    }

    protected void appendSchemaToTextArea(List<String> schemas) {
        schemas.forEach(schema -> {
            nameTextArea.append(schema + DataTypeFieldParser.NEW_LINE);
            descriptionTextArea.append(DataTypeFieldParser.NA + DataTypeFieldParser.NEW_LINE);
        });
    }

    public JTextArea getNameTextArea() {
        return nameTextArea;
    }

    public JTextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }
}
