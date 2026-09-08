package definition.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import util.IconLoader;

public class DataTypeDefinitionInfoComponent extends JPanel {

  public enum InfoFieldType {
    DT_NAME("DT Name"),
    MT_NAME("MT Name"),
    NAMESPACE("Namespace"),
    TARGET_DIR("Target Dir");

    private final String label;

    InfoFieldType(String label) {
      this.label = label;
    }

    public String getLabel() {
      return label;
    }
  }

  private static final Dimension LABEL_DIMENSION = new Dimension(80, 30);
  private static final Dimension TEXT_FIELD_DIMENSION = new Dimension(950, 25);

  private final JTextField infoTextField;
  private final JFileChooser fileChooser = new JFileChooser();

  public DataTypeDefinitionInfoComponent(InfoFieldType fieldType) {
    super();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(Component.LEFT_ALIGNMENT);

    this.infoTextField = new JTextField();
    this.infoTextField.setPreferredSize(TEXT_FIELD_DIMENSION);

    if (fieldType == InfoFieldType.MT_NAME) {
      add(createMTDeclarationPanel());
    }

    add(createInputPanel(fieldType));
  }

  private JPanel createInputPanel(InfoFieldType fieldType) {
    JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel jLabel = new JLabel(fieldType.getLabel());
    jLabel.setPreferredSize(LABEL_DIMENSION);

    inputPanel.add(jLabel);
    inputPanel.add(infoTextField);

    setDirSelection(fieldType, inputPanel);

    return inputPanel;
  }

  private JPanel createMTDeclarationPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JCheckBox mtDeclarationCheckBox = new JCheckBox("MT 파일도 함께 생성");
    mtDeclarationCheckBox.setToolTipText(
        "체크하면 DT 파일과 함께 MT(Message Type) 파일도 생성합니다. 체크하지 않으면 DT 파일만 생성됩니다.");
    mtDeclarationCheckBox.setSelected(false);
    infoTextField.setEnabled(false);
    mtDeclarationCheckBox.addActionListener(e -> {
      if (mtDeclarationCheckBox.isSelected()) {
        infoTextField.setEnabled(true);
        return;
      }

      infoTextField.setEnabled(false);
      infoTextField.setText(null);
    });

    panel.add(mtDeclarationCheckBox);
    return panel;
  }

  private void setDirSelection(InfoFieldType fieldType, JPanel panel) {
    if (fieldType != InfoFieldType.TARGET_DIR) {
      return;
    }

    this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    JButton dirSelectionBtn = new JButton(new IconLoader().loadIcon(IconLoader.FOLDER_ICON_PATH));
    dirSelectionBtn.setPreferredSize(new Dimension(25, 25));
    dirSelectionBtn.setBorderPainted(false);
    dirSelectionBtn.setContentAreaFilled(false);
    dirSelectionBtn.addActionListener(e -> {
      int statusCode = fileChooser.showDialog(this, null);
      if (statusCode == JFileChooser.APPROVE_OPTION) {
        infoTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
      }
    });

    infoTextField.setText(System.getProperty("user.home"));
    infoTextField.setEditable(false);
    panel.add(dirSelectionBtn);
  }

  public JTextField getInfoTextField() {
    return infoTextField;
  }
}
