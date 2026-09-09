package definition.components;

import java.awt.BorderLayout;
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
  private static final int ROW_HEIGHT = 30;

  private final JTextField infoTextField;
  private final JFileChooser fileChooser = new JFileChooser();

  public DataTypeDefinitionInfoComponent(InfoFieldType fieldType) {
    super();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.infoTextField = new JTextField();
    this.infoTextField.setPreferredSize(new Dimension(200, 25));

    if (fieldType == InfoFieldType.MT_NAME) {
      add(createMTDeclarationPanel());
    }

    add(createInputPanel(fieldType));
  }

  /**
   * 라벨은 왼쪽에 고정 너비로, 입력창은 남는 너비를 모두 채우도록 BorderLayout으로 구성한다.
   * 고정 px 너비를 쓰면 창 크기와 무관하게 필드 폭이 어긋나 보이던 문제가 있었다.
   */
  private JPanel createInputPanel(InfoFieldType fieldType) {
    JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
    inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, ROW_HEIGHT));

    JLabel jLabel = new JLabel(fieldType.getLabel());
    jLabel.setPreferredSize(LABEL_DIMENSION);

    inputPanel.add(jLabel, BorderLayout.WEST);
    inputPanel.add(infoTextField, BorderLayout.CENTER);

    setDirSelection(fieldType, inputPanel);

    return inputPanel;
  }

  private JPanel createMTDeclarationPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

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
    panel.add(dirSelectionBtn, BorderLayout.EAST);
  }

  public JTextField getInfoTextField() {
    return infoTextField;
  }
}
