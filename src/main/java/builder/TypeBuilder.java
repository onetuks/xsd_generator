package builder;

import core.NodeLinker;
import definition.DataTypeDefinition;
import dto.XDataTypes;

import java.awt.Color;
import java.util.Objects;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.XNode;
import model.XTree;
import model.vo.Attribute;
import model.vo.Category;
import model.vo.Occurrence;
import ui.FrameInfo;

public class TypeBuilder extends JFrame {

    public static final int CONTENT_WIDTH = 25;
    public static final int CONTENT_HEIGHT = 25;
    public static final int TEXT_FIELD_WIDTH = 200;
    public static final int SMALL_TEXT_FIELD_WIDTH = 80;

    private final XTree tree;
    private final DataTypeDefinition dataTypeDefinition;

    public TypeBuilder(DataTypeDefinition dataTypeDefinition, XDataTypes xDataTypes) {
        super();
        this.dataTypeDefinition = dataTypeDefinition;
        setup();

        NodeLinker linker = new NodeLinker();
        this.tree = linker.link(xDataTypes);

        add(buildContainer());
    }

    public TypeBuilder(DataTypeDefinition dataTypeDefinition, XTree tree) {
        super();
        this.dataTypeDefinition = dataTypeDefinition;
        setup();

        this.tree = tree;

        add(buildContainer());
    }

    private JPanel buildContainer() {
        JPanel container = new JPanel();
        container.setBackground(FrameInfo.PRIMARY);

        TypeBuilderHeaderPanel headerPanel = new TypeBuilderHeaderPanel();
        TypeBuilderScrollPane scrollPane = new TypeBuilderScrollPane(this);
//        TypeBuilderButton buttonPanel = new TypeBuilderButton(dataTypeDefinition, this);

        container.add(headerPanel);
        container.add(scrollPane);
//        container.add(buttonPanel);

        return container;
    }

    public void removeDetailPanel(XNode node) {
        tree.removeNode(node);
        refreshFrame();
    }

    public void updateCategory(XNode node, Category updatedCategory) {
        node.getEntity().setCategory(updatedCategory);

        if (updatedCategory == Category.ATTRIBUTE) {
            node.getEntity().setDataType(model.vo.Type.STRING);
            node.getEntity().setOccurrence(Occurrence.ofOptional());
        } else {
            node.getEntity().setOccurrence(Occurrence.ofZeroOne());
        }

        refreshFrame();
    }

    public void updateType(XNode node, model.vo.Type updatedType) {
        node.getEntity().setType(updatedType);
        refreshFrame();
    }

    public void addAttribute(XNode node, Attribute attribute) {
        node.addChild(
                XNode.of(
                        attribute.getName(), null,
                        Category.ATTRIBUTE, model.vo.Type.STRING, Occurrence.ofOptional()));
    }

    public void removeAttribute(XNode node, Attribute attribute) {
        XNode targetNode = node.getChildren().stream()
                .filter(child -> child.getEntity().getCategory() == Category.ATTRIBUTE)
                .filter(child -> Objects.equals(child.getEntity().getName(), attribute.getName()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Attribute not found"));
        node.getChildren().remove(targetNode);
    }

    private void refreshFrame() {
        setVisible(false);

        TypeBuilder refreshedBuilder = new TypeBuilder(dataTypeDefinition, tree);
        Color refreshedBuilderBackground = refreshedBuilder.getBackground();

        new Thread(() -> {
            for (int alpha = 255; alpha >= 0; alpha--) {
                setBackground(new Color(0, 0, 0, alpha));
                refreshedBuilder.setBackground(
                        new Color(
                                refreshedBuilderBackground.getRed(),
                                refreshedBuilderBackground.getGreen(),
                                refreshedBuilderBackground.getBlue(),
                                255 - alpha));
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void setup() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(FrameInfo.WIDOW_WIDTH, FrameInfo.WIDOW_HEIGHT);
        setLocation(FrameInfo.LOCATION, FrameInfo.LOCATION);
        setVisible(true);
    }

    public XTree getTree() {
        return tree;
    }

    public DataTypeDefinition getDataTypeDefinition() {
        return dataTypeDefinition;
    }
}
