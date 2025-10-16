package definition;

import java.util.Objects;
import model.vo.Attribute;
import model.vo.Category;
import model.vo.Occurrence;
import model.vo.Type;

public class DataTypeElement {

  public static final String STATEMENT = "StatementName";
  public static final String DB_TABLE_NAME = "dbTableName";
  public static final String ACTION = "action";
  public static final String ACCESS = "access";
  public static final String KEY = "key";
  public static final String TABLE = "table";
  public static final String ROW = "row";

  private final int level;
  private final String name;
  private final String description;
  private Category category;
  private final Type type;
  private final Occurrence occurrence;

  public DataTypeElement(String dataTypeName) {
    this.level = getProperLevel(dataTypeName);
    this.name = dataTypeName;
    this.description = "";
    this.category = Category.COMPLEX_TYPE;
    this.type = null;
    this.occurrence = null;
  }

  public DataTypeElement(String name, String description) {
    this.level = getProperLevel(name);
    this.name = name;
    this.description = description;
    this.category = getProperCategory(name);
    this.type = Type.STRING;
    this.occurrence = getProperOccurrence(name);
  }

  private int getProperLevel(String name) {
    if (Objects.equals(name, Attribute.ACTION.getName())) {
      return Level.THIRD;
    } else if (Attribute.isAttribute(name)) {
      return Level.FIFTH;
    } else if (name.startsWith(STATEMENT)) {
      return Level.FIRST;
    }

      return switch (name) {
          case ROW -> Level.FIRST;
          case DB_TABLE_NAME -> Level.SECOND;
          case ACCESS, KEY, TABLE -> Level.THIRD;
          default -> Level.FOURTH;
      };
  }

  private Category getProperCategory(String name) {
    if (Attribute.isAttribute(name)) {
      return Category.ATTRIBUTE;
    }
    return Category.ELEMENT;
  }

  private Occurrence getProperOccurrence(String name) {
    if (Attribute.isAttribute(name)) {
      return Occurrence.ofOptional();
    } else if (Objects.equals(name, ROW) || Objects.equals(name, STATEMENT)) {
      return Occurrence.ofZeroUnbounded();
    } else if (
        Objects.equals(name, DB_TABLE_NAME)
            || Objects.equals(name, ACCESS)
            || Objects.equals(name, KEY)
            || Objects.equals(name, TABLE)
    ) {
      return Occurrence.ofOnlyOne();
    }
    return Occurrence.ofZeroOne();
  }

  public int getLevel() {
    return level;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Category getCategory() {
    return category;
  }

  public Type getType() {
    return type;
  }

  public Occurrence getOccurrence() {
    return occurrence;
  }

  public void setCategory(Category changedCategory) {
    this.category = changedCategory;
  }
}
