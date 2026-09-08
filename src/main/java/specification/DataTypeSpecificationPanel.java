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

  /**
   * 필드 한 행의 상태(예: Category)가 바뀌어 해당 행의 다른 입력 컴포넌트(Occurrence 옵션 등)까지
   * 다시 그려야 할 때 호출한다.
   */
  public void refreshElements() {
    scrollPane.refresh();
  }

  public Navigator getNavigator() {
    return navigator;
  }

  public DataTypePipelineService getService() {
    return service;
  }
}
