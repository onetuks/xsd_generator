package specification.components;

import java.awt.Dimension;
import java.util.Locale;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import specification.DataTypeSpecificationPanel;
import specification.elements.DataTypeElementSpecificationPanel;

public class DataTypeSpecificationScrollPane extends JScrollPane {

  private final DataTypeSpecificationPanel specification;

  private String filterQuery = "";

  public DataTypeSpecificationScrollPane(DataTypeSpecificationPanel specification) {
    this.specification = specification;

    JPanel detailPanelContainer = createDetailPanelContainer();

    this.setViewportView(detailPanelContainer);
    this.setPreferredSize(new Dimension(1000, 560));
    this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    this.getVerticalScrollBar().setUnitIncrement(20);
  }

  public void refresh() {
    setViewportView(createDetailPanelContainer());
    revalidate();
    repaint();
  }

  /**
   * 이름에 query가 포함된 필드만 보이도록 필터링한다(대소문자 무시). 빈 문자열이면 전체 표시.
   */
  public void setFilterQuery(String query) {
    this.filterQuery = query == null ? "" : query;
    refresh();
  }

  private JPanel createDetailPanelContainer() {
    JPanel container = new JPanel();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    String normalizedQuery = filterQuery.trim().toLowerCase(Locale.ROOT);
    specification.getService()
        .getDataTypeElements()
        .stream()
        .filter(element -> normalizedQuery.isEmpty()
            || element.getName().toLowerCase(Locale.ROOT).contains(normalizedQuery))
        .forEach(element -> container.add(
            new DataTypeElementSpecificationPanel(specification, element)));
    return container;
  }
}
