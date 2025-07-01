package arborist;

import static arborist.TreeViewer.TREE_VIEWER_HEIGHT;
import static arborist.TreeViewer.TREE_VIEWER_WIDTH;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import model.XNode;
import ui.FrameInfo;

public class TreeViewerScrollPane extends JScrollPane {

  private final TreeViewer treeViewer;

  public TreeViewerScrollPane(TreeViewer treeViewer) {
    super();

    this.treeViewer = treeViewer;

    setPreferredSize(new Dimension(TREE_VIEWER_WIDTH, TREE_VIEWER_HEIGHT));
    setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    setBorder(FrameInfo.COMPOUND_BORDER);

    setViewportView(buildTree());
  }

  private JTree buildTree() {
    JTree jTree = new JTree(buildTreeNode(treeViewer.getTree().getRoot()));
    jTree.setBackground(FrameInfo.PRIMARY);
    jTree.setCellRenderer(new CustomTreeCellRenderer());

    jTree.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (!treeViewer.getEditCheckBox().isSelected()) {
          treeViewer.setFocusedDataTypeName(null);
          treeViewer.getFocusedDataTypeNameLabel().setText("");
          return;
        }

        if (SwingUtilities.isRightMouseButton(e)) {
          jTree.setSelectionPath(jTree.getPathForLocation(e.getX(), e.getY()));
          treeViewer.setTargetDataTypeName(
              ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent()).getUserObject().toString());

          ManipulationType type = getManipulationType();
          if (type == ManipulationType.BE_A_CHILD) {
            treeViewer.getTree()
                .addChild(
                    treeViewer.getTargetDataTypeName(),
                    treeViewer.getFocusedDataTypeName());
          } else if (type == ManipulationType.BE_A_SIBLING) {
            treeViewer.getTree()
                .addSibling(
                    treeViewer.getTargetDataTypeName(),
                    treeViewer.getFocusedDataTypeName());
          }

          treeViewer.setVisible(false);
          new TreeViewer(treeViewer.getBuilder(), treeViewer.getTree());

          return;
        }

        treeViewer.setFocusedDataTypeName(
            ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent()).getUserObject().toString());
        treeViewer.getFocusedDataTypeNameLabel()
            .setText(treeViewer.getFocusedDataTypeName());
      }

      private ManipulationType getManipulationType() {
        String[] options = Arrays.stream(ManipulationType.values())
            .map(ManipulationType::getText)
            .toArray(String[]::new);

        int result = JOptionPane.showOptionDialog(
            null, "Select Manipulation Type", "Tree Manipulation",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (result < 0 || result >= ManipulationType.values().length) {
          throw new IllegalArgumentException("방식을 선택해주세요");
        }

        return ManipulationType.values()[result];
      }

      @Override
      public void mousePressed(MouseEvent e) {
      }

      @Override
      public void mouseReleased(MouseEvent e) {
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }
    });

    return jTree;
  }

  private MutableTreeNode buildTreeNode(XNode node) {
    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node.getEntity().getName());

    node.getChildren().stream()
        .map(this::buildTreeNode)
        .forEach(treeNode::add);

    return treeNode;
  }
}
