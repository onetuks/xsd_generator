package definition.components;

import static definition.components.DataTypeDefinitionInfoComponent.DT_NAME;
import static definition.components.DataTypeDefinitionInfoComponent.MT_NAME;
import static definition.components.DataTypeDefinitionInfoComponent.NAMESPACE;
import static definition.components.DataTypeDefinitionInfoComponent.TARGET_DIR;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import ui.FrameInfo;

public class DataTypeDefinitionInfoPanel extends JPanel {

  private final DataTypeDefinitionInfoComponent dtNameComponent;
  private final DataTypeDefinitionInfoComponent mtNameComponent;
  private final DataTypeDefinitionInfoComponent namespaceComponent;
  private final DataTypeDefinitionInfoComponent targetDirComponent;

  public DataTypeDefinitionInfoPanel() {
    super();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(FrameInfo.COMPOUND_BORDER);

    this.dtNameComponent = new DataTypeDefinitionInfoComponent(DT_NAME);
    this.mtNameComponent = new DataTypeDefinitionInfoComponent(MT_NAME);
    this.namespaceComponent = new DataTypeDefinitionInfoComponent(NAMESPACE);
    this.targetDirComponent = new DataTypeDefinitionInfoComponent(TARGET_DIR);

    add(dtNameComponent);
    add(mtNameComponent);
    add(namespaceComponent);
    add(targetDirComponent);
  }

  public void clear() {
    dtNameComponent.getInfoTextField().setText(null);
    mtNameComponent.getInfoTextField().setText(null);
    namespaceComponent.getInfoTextField().setText(null);
  }

  public String getDTName() {
    return dtNameComponent.getInfoTextField().getText();
  }

  public String getMTName() {
    return mtNameComponent.getInfoTextField().getText();
  }

  public String getNamespace() {
    return namespaceComponent.getInfoTextField().getText();
  }

  public String getTargetDir() {
    return targetDirComponent.getInfoTextField().getText();
  }
}
