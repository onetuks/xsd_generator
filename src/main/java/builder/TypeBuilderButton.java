package builder;

import arborist.TreeViewer;
import core.FileSaver;
import core.XsdGenerator;
import initializer.Initializer;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import ui.FrameInfo;

public class TypeBuilderButton extends JPanel {

  private final XsdGenerator xsdGenerator;
  private final FileSaver fileSaver;
  private final Initializer initializer;
  private final TypeBuilder builder;

  public TypeBuilderButton(Initializer initializer, TypeBuilder builder) {
    super(new FlowLayout(FlowLayout.RIGHT));
    this.xsdGenerator = new XsdGenerator();
    this.fileSaver = new FileSaver();
    this.initializer = initializer;
    this.builder = builder;

    setBackground(FrameInfo.PRIMARY);
    setGenerateButton();
    setTreeViewButton();
    setCancelButton();
  }

  private void setGenerateButton() {
    JButton generateButton = new JButton("Generate");

    generateButton.setBackground(FrameInfo.SECONDARY);
    generateButton.setForeground(FrameInfo.TERTIARY);

    generateButton.addActionListener(e -> {
      fileSaver.saveFile(
          builder.getTree().getFileFullPath(),
          xsdGenerator.generate(builder.getTree()));

      JOptionPane.showMessageDialog(this, "XSD File Generated!");
    });

    add(generateButton);
  }

  private void setTreeViewButton() {
    JButton treeViewButton = new JButton("Show Structure");

    treeViewButton.setBackground(FrameInfo.SECONDARY);
    treeViewButton.setForeground(FrameInfo.TERTIARY);

    treeViewButton.addActionListener(e -> {
      builder.setVisible(false);
      new TreeViewer(builder);
    });

    add(treeViewButton);
  }

  private void setCancelButton() {
    JButton cancelButton = new JButton("Cancel");

    cancelButton.setBackground(FrameInfo.SECONDARY);
    cancelButton.setForeground(FrameInfo.TERTIARY);

    cancelButton.addActionListener(e -> {
      builder.setVisible(false);
      initializer.setVisible(true);
    });

    add(cancelButton);
  }
}
