import core.DataTypePipelineService;
import definition.DataTypeDefinition;
import specification.DataTypeSpecification;
import ui.FrameInfo;
import global.Navigator;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements Navigator {

    public static final String APP_NAME = "XSD Generator";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel();

    private final DataTypePipelineService service = new DataTypePipelineService();

    public MainFrame() throws HeadlessException {
        super(APP_NAME);

        registerContentPanels();

        setContentPane(contentPanel);
        pack(); // preferredSize 기반으로 크기 결정
        setLocation(FrameInfo.LOCATION, FrameInfo.LOCATION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void showScreen(String name) {
        cardLayout.show(contentPanel, name);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

    private void registerContentPanels() {
        contentPanel.add(new DataTypeDefinition(this, service), Navigator.DEFINITION);
        contentPanel.add(new DataTypeSpecification(this, service), Navigator.SPECIFICATION);
    }
}
