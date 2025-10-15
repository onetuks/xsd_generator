package definition;

import util.IconLoader;

import javax.swing.*;
import java.awt.*;

import static builder.TypeBuilder.CONTENT_HEIGHT;
import static builder.TypeBuilder.CONTENT_WIDTH;

public class DataTypeDefinitionInfoComponent extends JPanel {

    private static final Dimension LABEL_DIMENSION = new Dimension(80, 30);
    private static final Dimension TEXT_FIELD_DIMENSION = new Dimension(400, 25);

    private final JTextField infoTextField;
    private final JFileChooser fileChooser;

    public DataTypeDefinitionInfoComponent(String label) {
        super();

        setLayout(new FlowLayout(FlowLayout.LEFT));

        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(LABEL_DIMENSION);

        this.infoTextField = new JTextField();
        this.infoTextField.setPreferredSize(TEXT_FIELD_DIMENSION);

        JButton dirSelectionBtn = new JButton(new IconLoader().loadIcon(IconLoader.FOLDER_ICON_PATH));
        dirSelectionBtn.setPreferredSize(new Dimension(CONTENT_WIDTH, CONTENT_HEIGHT));
        dirSelectionBtn.setBorderPainted(false);
        dirSelectionBtn.setContentAreaFilled(false);
        dirSelectionBtn.addActionListener(e -> {
            int statusCode = fileChooser.showDialog(this, null);
            if (statusCode == JFileChooser.APPROVE_OPTION) {
                infoTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        add(jLabel, BorderLayout.WEST);
        add(infoTextField, BorderLayout.CENTER);

        if (label.contains("Dir")) {
            infoTextField.setText("D:\\");
            add(dirSelectionBtn);
        }
    }

    public JTextField getInfoTextField() {
        return infoTextField;
    }
}
