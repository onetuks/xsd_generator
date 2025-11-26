import core.DataTypePipelineService;
import definition.DataTypeDefinitionPanel;
import hierarchy.DataTypeHierarchyPanel;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import specification.DataTypeSpecificationPanel;
import util.Navigator;

public class Main extends JFrame implements Navigator {

  public static final String APP_NAME = "XSD Generator";

  private final CardLayout cardLayout = new CardLayout();
  private final JPanel contentPanel = new JPanel(cardLayout);

  private final DataTypePipelineService service = new DataTypePipelineService();

  public Main() throws HeadlessException {
    super(APP_NAME);

    registerContentPanels();

    setContentPane(contentPanel);
    setLocation(100, 100);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setPreferredSize(new Dimension(1100, 800));
    pack(); // preferredSize 기반으로 크기 결정
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      Main frame = new Main();
      frame.setVisible(true);
    });
  }

  @Override
  public void showScreen(String name) {
    cardLayout.show(contentPanel, name);
    pack();
    contentPanel.revalidate();
    contentPanel.repaint();
  }

  private void registerContentPanels() {
    contentPanel.add(new DataTypeDefinitionPanel(this, service), Navigator.DEFINITION);
    contentPanel.add(new DataTypeSpecificationPanel(this, service), Navigator.SPECIFICATION);
    contentPanel.add(new DataTypeHierarchyPanel(this, service), Navigator.HIERARCHY);
  }
}
