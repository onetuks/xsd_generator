package specification.components;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DataTypeSpecificationHeaderPanel extends JPanel {

  private static final int COMBO_BOX_WIDTH = 80;
  private static final int BIG_COMBO_BOX_WIDTH = 90;
  private static final int TEXT_FIELD_WIDTH = 200;
  private static final int CHECK_BOX_WIDTH = 220;

  private static final String NAME = "Name";
  private static final String CATEGORY = "Category";
  private static final String TYPE = "Type";
  private static final String OCCURRENCE = "Occurrence";
  private static final String DESCRIPTION = "Description";
  private static final String ATTRIBUTES = "Attributes";

  public DataTypeSpecificationHeaderPanel() {
    super();
    setup();

    add(createHeaderLabel(NAME));
    add(createHeaderLabel(CATEGORY));
    add(createHeaderLabel(TYPE));
    add(createHeaderLabel(OCCURRENCE));
    add(createHeaderLabel(DESCRIPTION));
    add(createHeaderLabel(ATTRIBUTES));
  }

  protected void setup() {
    setLayout(new FlowLayout(FlowLayout.LEFT));
    setPreferredSize(new Dimension(1000, (int) (25 * 1.5)));
    setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
  }

  private JLabel createHeaderLabel(String labelText) {
    JLabel label = new JLabel(labelText);

    switch (labelText) {
      case CATEGORY:
        label.setPreferredSize(new Dimension(BIG_COMBO_BOX_WIDTH, 25));
        break;
      case TYPE:
        label.setPreferredSize(new Dimension(COMBO_BOX_WIDTH, 25));
        break;
      case OCCURRENCE:
        label.setPreferredSize(new Dimension(80, 25));
        break;
      case NAME:
      case DESCRIPTION:
        label.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, 25));
        break;
      case ATTRIBUTES:
        label.setPreferredSize(new Dimension(CHECK_BOX_WIDTH, 25));
        break;
    }

    label.setHorizontalAlignment(SwingConstants.CENTER);
    label.setVerticalAlignment(SwingConstants.CENTER);

    return label;
  }
}
