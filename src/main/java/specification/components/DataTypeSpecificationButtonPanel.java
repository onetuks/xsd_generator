package specification.components;

import java.awt.FlowLayout;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import specification.DataTypeSpecificationPanel;
import specification.elements.DataTypeElement;
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
    nextBtn.addActionListener(e -> {
      if (!confirmNoDuplicateNames()) {
        return;
      }
      specification.getNavigator().showScreen(Navigator.HIERARCHY);
    });
    return nextBtn;
  }

  /**
   * 같은 이름의 필드가 여러 개 있으면 의도한 것인지 확인받는다. 동일 이름의
   * xsd:element/xsd:attribute가 중복 생성될 수 있기 때문이다.
   */
  private boolean confirmNoDuplicateNames() {
    List<DataTypeElement> elements = specification.getService().getDataTypeElements();

    Set<String> seen = new LinkedHashSet<>();
    Set<String> duplicates = new LinkedHashSet<>();
    for (DataTypeElement element : elements) {
      if (!seen.add(element.getName())) {
        duplicates.add(element.getName());
      }
    }

    if (duplicates.isEmpty()) {
      return true;
    }

    int result = JOptionPane.showConfirmDialog(
        this,
        "이름이 중복된 필드가 있습니다: "
            + duplicates.stream().collect(Collectors.joining(", "))
            + "\n같은 이름의 element/attribute가 중복 생성될 수 있습니다. 계속하시겠습니까?",
        "중복된 필드명",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

    return result == JOptionPane.OK_OPTION;
  }
}
