package initializer;

import core.DataParser;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.Objects;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import ui.FrameInfo;

public class DataInputComponent extends JPanel {

  private static final String NAME = "Name";
  private static final String DESCRIPTION = "Description";

  private final JTextArea nameTextArea;
  private final JTextArea descriptionTextArea;

  protected DataInputComponent() {
    super();

    setLayout(new GridLayout(1, 2));
    setBackground(FrameInfo.PRIMARY);
    setForeground(FrameInfo.SECONDARY);

    this.nameTextArea = new JTextArea();
    this.descriptionTextArea = new JTextArea();

    nameTextArea.setBackground(FrameInfo.BASE_COLOR);
    nameTextArea.setForeground(FrameInfo.TERTIARY);
    descriptionTextArea.setBackground(FrameInfo.BASE_COLOR);
    descriptionTextArea.setForeground(FrameInfo.TERTIARY);

    add(buildDataInputPanel(NAME));
    add(buildDataInputPanel(DESCRIPTION));
  }

  private JPanel buildDataInputPanel(String dataInputLabel) {
    JPanel panel = new JPanel();

    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(FrameInfo.PRIMARY);
    panel.setForeground(FrameInfo.SECONDARY);
    panel.setBorder(new EmptyBorder(5, 5, 5, 5));

    panel.add(buildDataInputLabel(dataInputLabel));
    panel.add(buildDataInputScrollPane(dataInputLabel));

    return panel;
  }

  private JPanel buildDataInputLabel(String labelText) {
    JPanel panel = new JPanel();
    panel.setBackground(FrameInfo.PRIMARY);
    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

    JLabel label = new JLabel(labelText);
    label.setForeground(FrameInfo.TERTIARY);
    label.setBorder(new EmptyBorder(0, 0, 5, 0));
    panel.add(label);

    return panel;
  }

  private JScrollPane buildDataInputScrollPane(String dataInputLabel) {
    JScrollPane scrollPane = new JScrollPane();

    scrollPane.setBackground(FrameInfo.BASE_COLOR);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    if (Objects.equals(dataInputLabel, NAME)) {
      scrollPane.setViewportView(nameTextArea);
    } else if (Objects.equals(dataInputLabel, DESCRIPTION)) {
      scrollPane.setViewportView(descriptionTextArea);
    }

    return scrollPane;
  }

  protected void appendSchemaToTextArea(List<String> schemas) {
    schemas.forEach(schema -> {
      nameTextArea.append(schema + DataParser.NEW_LINE);
      descriptionTextArea.append(DataParser.NA + DataParser.NEW_LINE);
    });
  }

  public JTextArea getNameTextArea() {
    return nameTextArea;
  }

  public JTextArea getDescriptionTextArea() {
    return descriptionTextArea;
  }
}
