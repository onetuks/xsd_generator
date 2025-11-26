package specification;

import core.DataTypePipelineService;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;
import specification.components.DataTypeSpecificationButtonPanel;
import specification.components.DataTypeSpecificationHeaderPanel;
import specification.components.DataTypeSpecificationScrollPane;
import specification.elements.DataTypeElement;
import util.Navigator;

public class DataTypeSpecificationPanel extends JPanel {

  private final Navigator navigator;
  private final DataTypePipelineService service;

  private final DataTypeSpecificationScrollPane scrollPane;

  public DataTypeSpecificationPanel(Navigator navigator, DataTypePipelineService service) {
    this.navigator = navigator;
    this.service = service;

    this.scrollPane = new DataTypeSpecificationScrollPane(this);

    add(new DataTypeSpecificationHeaderPanel());
    add(scrollPane);
    add(new DataTypeSpecificationButtonPanel(this));

    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent e) {
        scrollPane.refresh();
        revalidate();
        repaint();
      }
    });
  }

  public void removeElement(DataTypeElement element) {
    service.getDataTypeElements().remove(element);
    scrollPane.refresh();
  }

  public Navigator getNavigator() {
    return navigator;
  }

  public DataTypePipelineService getService() {
    return service;
  }
}
