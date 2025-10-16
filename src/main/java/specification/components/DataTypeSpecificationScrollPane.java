package specification.components;

import specification.DataTypeSpecificationPanel;
import specification.elements.DataTypeElementSpecificationPanel;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ui.FrameInfo;

public class DataTypeSpecificationScrollPane extends JScrollPane {

    private final DataTypeSpecificationPanel specification;

    public DataTypeSpecificationScrollPane(DataTypeSpecificationPanel specification) {
        this.specification = specification;

        JPanel detailPanelContainer = createDetailPanelContainer();

        this.setViewportView(detailPanelContainer);
        this.setPreferredSize(new Dimension(FrameInfo.CONTENT_WIDTH, FrameInfo.CONTENT_HEIGHT));
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.getVerticalScrollBar().setUnitIncrement(20);
        this.setVisible(true);
    }

    private JPanel createDetailPanelContainer() {
        JPanel container = new JPanel(new BoxLayout(this, BoxLayout.Y_AXIS));
        specification.getDataTypeElements()
                .forEach(element -> container.add(new DataTypeElementSpecificationPanel(specification, element)));
        return container;
    }
}
