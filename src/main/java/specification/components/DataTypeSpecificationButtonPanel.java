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
    // 트리 빌드는 Hierarchy 화면 진입 시점에 담당한다.
    // 여기서 미리 만들면 기존에 수동으로 조정한 계층 구조가 있는지 판단할 수 없게 된다.
    nextBtn.addActionListener(e -> specification.getNavigator().showScreen(Navigator.HIERARCHY));
    return nextBtn;
  }
}
