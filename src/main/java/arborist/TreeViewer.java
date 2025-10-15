package arborist;

import builder.TypeBuilder;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.XTree;
import ui.FrameInfo;

public class TreeViewer extends JFrame {

  protected static final int TREE_VIEWER_WIDTH = 400;
  protected static final int TREE_VIEWER_HEIGHT = 450;

  private final TypeBuilder builder;
  private final XTree tree;

  private JCheckBox editCheckBox;
  private JLabel focusedDataTypeNameLabel;
  private String focusedDataTypeName;
  private String targetDataTypeName;

  public TreeViewer(TypeBuilder builder) {
    super();
    setup();

    this.builder = builder;
    this.tree = XTree.of(builder.getTree());

    add(buildTreeViewContainer());
  }

  TreeViewer(TypeBuilder builder, XTree tree) {
    super();
    setup();

    this.builder = builder;
    this.tree = tree;

    add(buildTreeViewContainer());
  }

  private JPanel buildTreeViewContainer() {
    JPanel container = new JPanel();
    container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
    container.setBackground(FrameInfo.PRIMARY);
    container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    TreeViewerScrollPane scrollPane = new TreeViewerScrollPane(this);
    TreeViewerControlPanel controlPanel = new TreeViewerControlPanel(this);

    container.add(scrollPane);
    container.add(Box.createHorizontalStrut(20));
    container.add(controlPanel);

    return container;
  }

  private void setup() {
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(FrameInfo.WIDOW_WIDTH - TREE_VIEWER_WIDTH, FrameInfo.WIDOW_HEIGHT);
    setLocation(FrameInfo.LOCATION, FrameInfo.LOCATION);
    setVisible(true);
  }

  public TypeBuilder getBuilder() {
    return builder;
  }

  public XTree getTree() {
    return tree;
  }

  public JCheckBox getEditCheckBox() {
    return editCheckBox;
  }

  public void setEditCheckBox(JCheckBox editCheckBox) {
    this.editCheckBox = editCheckBox;
  }

  public JLabel getFocusedDataTypeNameLabel() {
    return focusedDataTypeNameLabel;
  }

  public void setFocusedDataTypeNameLabel(JLabel focusedDataTypeNameLabel) {
    this.focusedDataTypeNameLabel = focusedDataTypeNameLabel;
  }

  public String getFocusedDataTypeName() {
    return focusedDataTypeName;
  }

  public void setFocusedDataTypeName(String focusedDataTypeName) {
    this.focusedDataTypeName = focusedDataTypeName;
  }

  public String getTargetDataTypeName() {
    return targetDataTypeName;
  }

  public void setTargetDataTypeName(String targetDataTypeName) {
    this.targetDataTypeName = targetDataTypeName;
  }
}
