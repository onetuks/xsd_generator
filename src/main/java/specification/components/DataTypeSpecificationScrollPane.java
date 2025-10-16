package specification.components;

import specification.DataTypeSpecificationPanel;
import specification.elements.DataTypeElementSpecificationPanel;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class DataTypeSpecificationScrollPane extends JScrollPane {

    private final DataTypeSpecificationPanel specification;

    public DataTypeSpecificationScrollPane(DataTypeSpecificationPanel specification) {
        this.specification = specification;

        JPanel detailPanelContainer = createDetailPanelContainer();

        this.setViewportView(detailPanelContainer);
        this.setPreferredSize(new Dimension(1000, 600));
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.getVerticalScrollBar().setUnitIncrement(20);
    }

    public void refresh() {
        setViewportView(createDetailPanelContainer());
        revalidate();
        repaint();
    }

    private JPanel createDetailPanelContainer() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        specification.getService()
                .getDataTypeElements()
                .forEach(element -> container.add(new DataTypeElementSpecificationPanel(specification, element)));
        return container;
    }
}
