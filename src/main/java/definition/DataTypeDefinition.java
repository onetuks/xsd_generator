package definition;

import ui.FrameInfo;

import javax.swing.*;

public class DataTypeDefinition extends JFrame {

    private final DataTypeDefinitionInfoPanel dtInfoPanel;
    private final DataTypeDefinitionFieldTabbedPane dtFieldTabbedPane;

    private final JPanel container = new JPanel();

    public DataTypeDefinition() {
        super(FrameInfo.APP_NAME);

        this.dtInfoPanel = new DataTypeDefinitionInfoPanel();
        this.dtFieldTabbedPane = new DataTypeDefinitionFieldTabbedPane();

        initComponent();
    }

    private void initComponent() {
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        setContentPane(container);

        container.setBorder(FrameInfo.EMPTY_BORDER);
        container.add(Box.createVerticalStrut(8));
        container.add(dtInfoPanel);
        container.add(Box.createVerticalStrut(8));
        container.add(new DataTypeDefinitionStructurePanel(dtFieldTabbedPane));
        container.add(Box.createVerticalStrut(8));
        container.add(dtFieldTabbedPane);
        container.add(Box.createVerticalStrut(12));
        container.add(new DataTypeDefinitionButtonPanel(dtInfoPanel, dtFieldTabbedPane));

        // 프레임 기본 설정: pack() -> 위치 -> visible
        pack(); // preferredSize 기반으로 크기 결정
        setLocation(FrameInfo.LOCATION, FrameInfo.LOCATION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
