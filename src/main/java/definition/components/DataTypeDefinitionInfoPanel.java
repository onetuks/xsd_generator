package definition.components;

import definition.components.DataTypeDefinitionInfoComponent.InfoFieldType;
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

    this.dtNameComponent = new DataTypeDefinitionInfoComponent(InfoFieldType.DT_NAME);
    this.mtNameComponent = new DataTypeDefinitionInfoComponent(InfoFieldType.MT_NAME);
    this.namespaceComponent = new DataTypeDefinitionInfoComponent(InfoFieldType.NAMESPACE);
    this.targetDirComponent = new DataTypeDefinitionInfoComponent(InfoFieldType.TARGET_DIR);

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
