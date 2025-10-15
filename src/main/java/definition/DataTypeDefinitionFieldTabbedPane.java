package definition;

import javax.swing.*;

public class DataTypeDefinitionFieldTabbedPane extends JTabbedPane {

    public DataTypeDefinitionFieldTabbedPane() {
        super();
        clearAllTabs();
    }

    public void clearAllTabs() {
        removeAll();
        addNewTab();
    }

    public void addNewTab() {
        String newTabTitle = "#" + (getTabCount() + 1);
        addTab(newTabTitle, null, new DataTypeDefinitionFieldPanel(), null);
    }
}
