package hierarchy.enums;

public enum HierarchyManipulationType {
    BE_A_CHILD("Add as Child"),
    BE_A_SIBLING("Add as Sibling");

    private final String text;

    HierarchyManipulationType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
