package definition.components;

import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class DataTypeDefinitionFieldTabbedPane extends JTabbedPane {

  public DataTypeDefinitionFieldTabbedPane() {
    super();
    clearAllTabs();
  }

  public void clearAllTabs() {
    removeAll();
    addNewTab();
  }

  public void addNewTab() {
    addTab(null, new DataTypeDefinitionFieldPanel());
    int addedIndex = getTabCount() - 1;
    setTabComponentAt(addedIndex, createTabHeader());
    renumberTabs();
  }

  /**
   * 마지막 탭을 제거한다. 남은 탭이 하나뿐이면 아무 동작도 하지 않는다.
   */
  public void removeLastTab() {
    removeTabWithGuard(getTabCount() - 1);
  }

  private void removeTabWithGuard(int index) {
    if (getTabCount() <= 1) {
      JOptionPane.showMessageDialog(
          this, "마지막 탭은 닫을 수 없습니다.", "Structure Manipulation",
          JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    removeTabAt(index);
    renumberTabs();
  }

  /**
   * 중간 탭만 개별적으로 닫을 수 있도록 탭별 닫기 버튼을 붙인다. 마지막 남은 탭은 지울 수 없다.
   */
  private JPanel createTabHeader() {
    JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
    header.setOpaque(false);

    JLabel titleLabel = new JLabel();
    JButton closeButton = new JButton("x");
    closeButton.setMargin(new java.awt.Insets(0, 4, 0, 4));
    closeButton.setBorder(BorderFactory.createEmptyBorder());
    closeButton.setContentAreaFilled(false);
    closeButton.setToolTipText("이 탭 닫기");
    closeButton.addActionListener(e -> {
      int index = indexOfTabComponent(header);
      if (index >= 0) {
        removeTabWithGuard(index);
      }
    });

    header.add(titleLabel);
    header.add(closeButton);
    header.putClientProperty("titleLabel", titleLabel);
    return header;
  }

  private void renumberTabs() {
    for (int i = 0; i < getTabCount(); i++) {
      String title = "#" + (i + 1);
      setTitleAt(i, title);
      JPanel header = (JPanel) getTabComponentAt(i);
      if (header != null) {
        ((JLabel) header.getClientProperty("titleLabel")).setText(title);
      }
    }
  }
}
