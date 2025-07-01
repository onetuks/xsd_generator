package arborist;

public enum ManipulationType {
  BE_A_CHILD("Add as Child"),
  BE_A_SIBLING("Add as Sibling");

  private final String text;

  ManipulationType(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }
}
