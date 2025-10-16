package builder;

import builder.detail.TypeBuilderDetailPanel;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import model.DataTypeNode;
import model.vo.Category;
import ui.FrameInfo;

public class TypeBuilderScrollPane extends JScrollPane {

  public TypeBuilderScrollPane(TypeBuilder builder) {
    super();

    JPanel detailPanelContainer = buildDetailPanelContainer(builder);

    this.setViewportView(detailPanelContainer);
    this.setPreferredSize(new Dimension(FrameInfo.CONTENT_WIDTH, FrameInfo.CONTENT_HEIGHT));
    this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    this.getVerticalScrollBar().setUnitIncrement(20);
    this.setVisible(true);
  }

  private JPanel buildDetailPanelContainer(TypeBuilder builder) {
    JPanel container = new JPanel();

    container.setBackground(FrameInfo.PRIMARY);
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

    buildDetailPanel(builder, builder.getTree().getRoot()).forEach(container::add);

    return container;
  }

  private List<TypeBuilderDetailPanel> buildDetailPanel(TypeBuilder builder, DataTypeNode node) {
    List<TypeBuilderDetailPanel> detailPanels = new ArrayList<>();

    if (node.entity().getCategory() != Category.COMPLEX_TYPE) {
      detailPanels.add(new TypeBuilderDetailPanel(builder, node));
    }

    node.children().stream()
        .map(child -> buildDetailPanel(builder, child))
        .forEach(detailPanels::addAll);

    return detailPanels;
  }
}
