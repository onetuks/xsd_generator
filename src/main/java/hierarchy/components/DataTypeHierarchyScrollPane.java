package hierarchy.components;

import hierarchy.DataTypeHierarchyPanel;
import hierarchy.enums.HierarchyManipulationType;
import model.DataTypeNode;
import ui.FrameInfo;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;

public class DataTypeHierarchyScrollPane extends JScrollPane {

    public static final int TREE_VIEWER_WIDTH = 700;
    public static final int TREE_VIEWER_HEIGHT = 600;

    private final DataTypeHierarchyPanel hierarchy;

    private final JTree hierarchyTree = new JTree();

    public DataTypeHierarchyScrollPane(DataTypeHierarchyPanel hierarchy) {
        this.hierarchy = hierarchy;

        setPreferredSize(new Dimension(TREE_VIEWER_WIDTH, TREE_VIEWER_HEIGHT));
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setBorder(FrameInfo.COMPOUND_BORDER);

        bindTreeViewPort();

        hierarchyTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    hierarchy.setFocusedDataType();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    hierarchyTree.setSelectionPath(hierarchyTree.getPathForLocation(e.getX(), e.getY()));
                    String targetDataTypeName =
                            ((DefaultMutableTreeNode) hierarchyTree.getLastSelectedPathComponent())
                                    .getUserObject()
                                    .toString();

                    HierarchyManipulationType type = getManipulationType();
                    if (type == HierarchyManipulationType.BE_A_CHILD) {
                        hierarchy.addChildTo(targetDataTypeName);
                    } else if (type == HierarchyManipulationType.BE_A_SIBLING) {
                        hierarchy.addSiblingTo(targetDataTypeName);
                    }

                    bindTreeViewPort(); //todo: 무한 재귀 호출
                }
            }
        });
    }

    public void bindTreeViewPort() {
        setViewportView(createTree());
    }

    private JTree createTree() {
        hierarchyTree.setModel(
                new DefaultTreeModel(
                        createNode(hierarchy.getService().getRootNode())));

        if (hierarchyTree.getRowCount() > 0) {
            expandAll(hierarchyTree, new TreePath(hierarchyTree.getModel().getRoot()));
        }

        return hierarchyTree;
    }

    private HierarchyManipulationType getManipulationType() {
        String[] options = Arrays.stream(HierarchyManipulationType.values())
                .map(HierarchyManipulationType::getText)
                .toArray(String[]::new);

        int result = JOptionPane.showOptionDialog(
                null, "Select Manipulation Type", "Hierarchy Manipulation",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (result < 0 || result >= HierarchyManipulationType.values().length) {
            throw new IllegalArgumentException("방식을 선택해주세요");
        }

        return HierarchyManipulationType.values()[result];
    }

    private MutableTreeNode createNode(DataTypeNode node) {
        if (node == null) {
            return null;
        }

        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node.entity().getName());

        node.children().stream()
                .map(this::createNode)
                .filter(Objects::nonNull)
                .forEach(treeNode::add);

        return treeNode;
    }

    private void expandAll(JTree tree, TreePath path) {
        TreeNode node = (TreeNode) path.getLastPathComponent();
        if (node.getChildCount() > 0) {
            for (Enumeration<?> e = node.children(); e.hasMoreElements(); ) {
                TreeNode child = (TreeNode) e.nextElement();
                TreePath childPath = path.pathByAddingChild(child);
                expandAll(tree, childPath);
            }
        }
        tree.expandPath(path);
    }

    public JTree getHierarchyTree() {
        return hierarchyTree;
    }
}
