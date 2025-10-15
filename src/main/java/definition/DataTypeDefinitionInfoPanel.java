package definition;

import ui.FrameInfo;

import javax.swing.*;
import java.awt.*;

public class DataTypeDefinitionInfoPanel extends JPanel {

    private final DataTypeDefinitionInfoComponent dtNameComponent;
    private final DataTypeDefinitionInfoComponent namespaceComponent;
    private final DataTypeDefinitionInfoComponent targetDirComponent;

    public DataTypeDefinitionInfoPanel() {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(FrameInfo.COMPOUND_BORDER);

        this.dtNameComponent = new DataTypeDefinitionInfoComponent("DT Name");
        this.namespaceComponent = new DataTypeDefinitionInfoComponent("Namespace");
        this.targetDirComponent = new DataTypeDefinitionInfoComponent("Target Dir");

        add(dtNameComponent);
        add(namespaceComponent);
        add(targetDirComponent);
    }

    public void clear() {
        dtNameComponent.getInfoTextField().setText(null);
        namespaceComponent.getInfoTextField().setText(null);
    }

    public String getDTName() {
        return dtNameComponent.getInfoTextField().getText();
    }

    public String getNamespace() {
        return namespaceComponent.getInfoTextField().getText();
    }

    public String getTargetDir() {
        return targetDirComponent.getInfoTextField().getText();
    }
}
