package model;

import java.util.Objects;
import model.vo.Category;
import model.vo.Occurrence;
import model.vo.Type;

public class XEntity {

  private final String name;
  private final String description;
  private Category category;
  private Type type;
  private Occurrence occurrence;

  public XEntity(
      String name, String description, Category category, Type type, Occurrence occurrence) {
    this.name = name;
    this.description = description == null ? "" : description;
    this.category = category;
    this.type = type;
    this.occurrence = occurrence;
  }

  public Category getCategory() {
    return category;
  }

  public String getDescription() {
    return description;
  }

  public String getName() {
    return name;
  }

  public Occurrence getOccurrence() {
    return occurrence;
  }

  public Type getType() {
    return type;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public void setOccurrence(Occurrence occurrence) {
    this.occurrence = occurrence;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public void setDataType(Type type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XEntity xEntity = (XEntity) o;
    return Objects.equals(name, xEntity.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }
}
