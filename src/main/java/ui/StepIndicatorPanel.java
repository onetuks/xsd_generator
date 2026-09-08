package ui;

import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import util.Navigator;

/**
 * 현재 Definition/Specification/Hierarchy 중 어느 단계에 있는지 보여주는 브레드크럼.
 * 화면 전환마다 {@link #setActiveStep(String)}을 호출해 강조 표시를 갱신한다.
 */
public class StepIndicatorPanel extends JPanel {

  private final Map<String, JLabel> stepLabels = new LinkedHashMap<>();

  public StepIndicatorPanel() {
    super();

    setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

    addStep("①  Definition", Navigator.DEFINITION);
    addSeparator();
    addStep("②  Specification", Navigator.SPECIFICATION);
    addSeparator();
    addStep("③  Hierarchy", Navigator.HIERARCHY);

    setActiveStep(Navigator.DEFINITION);
  }

  private void addStep(String text, String screenName) {
    JLabel label = new JLabel(text);
    stepLabels.put(screenName, label);
    add(label);
  }

  private void addSeparator() {
    add(new JLabel("  →  "));
  }

  public void setActiveStep(String screenName) {
    stepLabels.forEach((name, label) -> {
      boolean isActive = name.equals(screenName);
      label.setFont(label.getFont().deriveFont(isActive ? Font.BOLD : Font.PLAIN));
      label.setForeground(isActive ? FrameInfo.PRIMARY_COLOR : java.awt.Color.GRAY);
    });
  }
}
