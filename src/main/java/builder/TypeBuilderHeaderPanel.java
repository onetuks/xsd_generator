package builder;

import static builder.TypeBuilder.CONTENT_HEIGHT;
import static builder.TypeBuilder.SMALL_TEXT_FIELD_WIDTH;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import ui.FrameInfo;

public class TypeBuilderHeaderPanel extends JPanel {

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

  public TypeBuilderHeaderPanel() {
    super();
    setup();

    add(buildHeaderLabel(NAME));
    add(buildHeaderLabel(CATEGORY));
    add(buildHeaderLabel(TYPE));
    add(buildHeaderLabel(OCCURRENCE));
    add(buildHeaderLabel(DESCRIPTION));
    add(buildHeaderLabel(ATTRIBUTES));
  }

  protected void setup() {
    setLayout(new FlowLayout(FlowLayout.LEFT));
    setBackground(FrameInfo.SECONDARY);
    setPreferredSize(new Dimension(FrameInfo.CONTENT_WIDTH, (int) (CONTENT_HEIGHT * 1.5)));
    setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
  }

  private JLabel buildHeaderLabel(String labelText) {
    JLabel label = new JLabel(labelText);
    label.setForeground(FrameInfo.TERTIARY);

    switch (labelText) {
      case CATEGORY:
        label.setPreferredSize(new Dimension(BIG_COMBO_BOX_WIDTH, CONTENT_HEIGHT));
        break;
      case TYPE:
        label.setPreferredSize(new Dimension(COMBO_BOX_WIDTH, CONTENT_HEIGHT));
        break;
      case OCCURRENCE:
        label.setPreferredSize(new Dimension(SMALL_TEXT_FIELD_WIDTH, CONTENT_HEIGHT));
        break;
      case NAME:
      case DESCRIPTION:
        label.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, CONTENT_HEIGHT));
        break;
      case ATTRIBUTES:
        label.setPreferredSize(new Dimension(CHECK_BOX_WIDTH, CONTENT_HEIGHT));
        break;
    }

    label.setHorizontalAlignment(SwingConstants.CENTER);
    label.setVerticalAlignment(SwingConstants.CENTER);

    return label;
  }
}
