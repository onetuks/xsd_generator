package definition.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import util.IconLoader;

public class DataTypeDefinitionInfoComponent extends JPanel {

  static final String DT_NAME = "DT Name";
  static final String MT_NAME = "MT Name";
  static final String NAMESPACE = "Namespace";
  static final String TARGET_DIR = "Target Dir";

  private static final Dimension LABEL_DIMENSION = new Dimension(80, 30);
  private static final Dimension TEXT_FIELD_DIMENSION = new Dimension(950, 25);

  private final JTextField infoTextField;
  private final JFileChooser fileChooser = new JFileChooser();

  public DataTypeDefinitionInfoComponent(String label) {
    super();

    setLayout(new FlowLayout(FlowLayout.LEFT));

    JLabel jLabel = new JLabel(label);
    jLabel.setPreferredSize(LABEL_DIMENSION);

    this.infoTextField = new JTextField();
    this.infoTextField.setPreferredSize(TEXT_FIELD_DIMENSION);

    add(jLabel, BorderLayout.WEST);
    add(infoTextField, BorderLayout.CENTER);

    setDirSelection(label);
    setMTDeclaration(label);
  }

  private void setMTDeclaration(String label) {
    if (!label.contains(MT_NAME)) {
      return;
    }

    JCheckBox mtDeclarationCheckBox = new JCheckBox();
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

    add(mtDeclarationCheckBox);
  }

  private void setDirSelection(String label) {
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

    if (label.contains(TARGET_DIR)) {
      infoTextField.setText("D:\\");
      add(dirSelectionBtn);
    }
  }

  public JTextField getInfoTextField() {
    return infoTextField;
  }
}
