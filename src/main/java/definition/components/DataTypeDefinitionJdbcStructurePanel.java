package definition.components;

import definition.services.JdbcStructureInvocator;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import ui.FrameInfo;

public class DataTypeDefinitionJdbcStructurePanel extends JPanel {

  private final JdbcStructureInvocator jdbcStructureInvocator = new JdbcStructureInvocator();

  public DataTypeDefinitionJdbcStructurePanel(JTabbedPane tabbedPane) {
    super();

    setBorder(FrameInfo.COMPOUND_BORDER);
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    add(titlePanel());
    add(buttonPanel(tabbedPane));
  }

  private JPanel titlePanel() {
    JPanel titlePanel = new JPanel();
    JLabel title = new JLabel("SQL Template Insert");
    title.setToolTipText(
        "실제 DB에 연결하지 않고, SAP PO Jdbc 어댑터에서 흔히 쓰는 필드 구성을 템플릿으로 삽입합니다.");
    titlePanel.add(title);
    return titlePanel;
  }

  private JPanel buttonPanel(JTabbedPane tabbedPane) {
    JPanel panel = new JPanel();

    JButton selectBtn = new JButton("Select");
    selectBtn.setToolTipText("SELECT 결과(row) 템플릿을 삽입합니다.");
    selectBtn.addActionListener(e -> {
      DataTypeDefinitionFieldPanel component =
          (DataTypeDefinitionFieldPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
      component.appendSchemaToTextArea(jdbcStructureInvocator.generateSELECTStructure());
    });

    JButton updateBtn = new JButton("DML");
    updateBtn.setToolTipText("StatementName/dbTableName/action/table/access 템플릿을 삽입합니다.");
    updateBtn.addActionListener(e -> {
      DataTypeDefinitionFieldPanel component =
          (DataTypeDefinitionFieldPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
      component.appendSchemaToTextArea(
          jdbcStructureInvocator.generateDMLStructure(tabbedPane.getSelectedIndex() + 1));
    });

    JButton sqlBtn = new JButton("SQL");
    sqlBtn.setToolTipText("StatementName/dbTableName/action/access/key 템플릿을 삽입합니다.");
    sqlBtn.addActionListener(e -> {
      DataTypeDefinitionFieldPanel component =
          (DataTypeDefinitionFieldPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
      component.appendSchemaToTextArea(
          jdbcStructureInvocator.generateSQLStructure(tabbedPane.getSelectedIndex() + 1));
    });

    JButton procedureBtn = new JButton("Procedure");
    procedureBtn.setToolTipText("StatementName/dbTableName/action/table 템플릿을 삽입합니다.");
    procedureBtn.addActionListener(e -> {
      DataTypeDefinitionFieldPanel component =
          (DataTypeDefinitionFieldPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
      component.appendSchemaToTextArea(
          jdbcStructureInvocator.generatePROCEDUREStructure(tabbedPane.getSelectedIndex() + 1));
    });

    panel.add(selectBtn);
    panel.add(updateBtn);
    panel.add(sqlBtn);
    panel.add(procedureBtn);

    return panel;
  }
}
