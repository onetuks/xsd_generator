package specification.components;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import specification.DataTypeSpecificationPanel;
import util.Navigator;

public class DataTypeSpecificationButtonPanel extends JPanel {

  private final DataTypeSpecificationPanel specification;

  public DataTypeSpecificationButtonPanel(DataTypeSpecificationPanel specification) {
    super(new FlowLayout(FlowLayout.RIGHT));
    this.specification = specification;

    initComponent();
  }

  private void initComponent() {
    add(createPrevButton());
    add(createNextButton());
  }

  private JButton createPrevButton() {
    JButton prevBtn = new JButton("Prev");
    prevBtn.addActionListener(e -> specification.getNavigator().showScreen(Navigator.DEFINITION));
    return prevBtn;
  }

  private JButton createNextButton() {
    JButton nextBtn = new JButton("Next");
    nextBtn.addActionListener(e -> {
      specification.getService()
          .updateDataTypeNode(specification.getService().getDataTypeElements());
      specification.getNavigator().showScreen(Navigator.HIERARCHY);
    });
    return nextBtn;
  }
}
