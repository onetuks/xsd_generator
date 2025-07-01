package initializer;

import builder.TypeBuilder;
import core.DataParser;
import dto.XDataType;
import dto.XDataTypes;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import ui.FrameInfo;

public class Initializer extends JFrame {

  private final JdbcSchemaInvocator jdbcSchemaInvocator;
  private final DataParser dataParser;
  private final JFileChooser fileChooser;

  private JPanel container;
  private JButton resetButton;
  private JButton generationButton;
  private JTabbedPane tabbedPane;
  private JPanel schemaIndexPanel;
  private JPanel jdbcSchemaManipulationPanel;
  private JPanel dataTypeInfoPanel;
  private JTextField dataTypeNameTextField;
  private JButton addSchemaButton;
  private JButton removeSchemaButton;
  private JButton procedureButton;
  private JButton sqlDmlButton;
  private JButton selectButton;
  private JButton dmlButton;
  private JTextField namespaceTextField;
  private JTextField filePathTextField;
  private JButton directoryChooseButton;

  public Initializer() {
    super();
    setup();

    this.jdbcSchemaInvocator = new JdbcSchemaInvocator();
    this.dataParser = new DataParser();
    this.fileChooser = new JFileChooser();
    this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
  }

  private void setup() {
    this.container.setSize(FrameInfo.WIDOW_WIDTH, FrameInfo.WIDOW_HEIGHT);
    this.setContentPane(container);
    this.setTitle(FrameInfo.APP_NAME);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(FrameInfo.WIDOW_WIDTH, FrameInfo.WIDOW_HEIGHT);
    this.setLocation(FrameInfo.LOCATION, FrameInfo.LOCATION);

    this.setPanels();
    this.setTabbedPane();
    this.setButtonListener();

    this.setVisible(true);
  }

  private void setPanels() {
    schemaIndexPanel.setBorder(FrameInfo.COMPOUND_BORDER);
    jdbcSchemaManipulationPanel.setBorder(FrameInfo.COMPOUND_BORDER);
    dataTypeInfoPanel.setBorder(FrameInfo.COMPOUND_BORDER);
  }

  private void setTabbedPane() {
    tabbedPane.removeAll();
    tabbedPane.setForeground(FrameInfo.TERTIARY);
    tabbedPane.setUI(new BasicTabbedPaneUI() {

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
    addSchema();
  }

  private void setButtonListener() {
    // Basic Info
    directoryChooseButton.addActionListener(e -> chooseDirectory());
    resetButton.addActionListener(e -> resetDataTypeInfo());
    generationButton.addActionListener(e -> generateXNode());

    // Schema Index
    addSchemaButton.addActionListener(e -> addSchema());
    removeSchemaButton.addActionListener(e -> removeSchema());

    // JDBC Schema Manipulation
    selectButton.addActionListener(e -> {
      DataInputComponent component =
          (DataInputComponent) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
      component.appendSchemaToTextArea(jdbcSchemaInvocator.generateSELECTSchema());
    });

    dmlButton.addActionListener(e -> {
      DataInputComponent component =
          (DataInputComponent) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
      component.appendSchemaToTextArea(
          jdbcSchemaInvocator.generateDMLSchema(tabbedPane.getSelectedIndex() + 1));
    });

    sqlDmlButton.addActionListener(e -> {
      DataInputComponent component =
          (DataInputComponent) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
      component.appendSchemaToTextArea(
          jdbcSchemaInvocator.generateSQLDMLSchema(tabbedPane.getSelectedIndex() + 1));
    });

    procedureButton.addActionListener(e -> {
      DataInputComponent component =
          (DataInputComponent) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
      component.appendSchemaToTextArea(
          jdbcSchemaInvocator.generatePROCEDURESchema(tabbedPane.getSelectedIndex() + 1));
    });
  }

  private void chooseDirectory() {
    int statusCode = fileChooser.showDialog(this, null);
    if (statusCode == JFileChooser.APPROVE_OPTION) {
      filePathTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
    }
  }

  private void addSchema() {
    String newTabTitle = "#" + (tabbedPane.getTabCount() + 1);
    tabbedPane.addTab(newTabTitle, null, new DataInputComponent(), null);
  }

  private void removeSchema() {
    tabbedPane.remove(tabbedPane.getSelectedIndex());
  }

  private void resetDataTypeInfo() {
    dataTypeNameTextField.setText(null);
    namespaceTextField.setText(null);
    tabbedPane.removeAll();
    addSchema();
  }

  private void generateXNode() {
    try {
      setVisible(false);

      new TypeBuilder(
          this,
          new XDataTypes(
              dataTypeNameTextField.getText(), namespaceTextField.getText(),
              filePathTextField.getText(),
              gatherXDataTypes()));
    } catch (IllegalArgumentException ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private List<XDataType> gatherXDataTypes() {
    return Arrays.stream(tabbedPane.getComponents())
        .map(component -> (DataInputComponent) component)
        .map(component ->
            dataParser.parseData(
                component.getNameTextArea().getText(),
                component.getDescriptionTextArea().getText()))
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }
}
