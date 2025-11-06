package specification.elements;

import model.vo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DataTypeElement {

    public static final String STATEMENT = "StatementName";
    public static final String DB_TABLE_NAME = "dbTableName";
    public static final String ACTION = "action";
    public static final String ACCESS = "access";
    public static final String KEY = "key";
    public static final String TABLE = "table";
    public static final String ROW = "row";

    private final UUID id;
    private final int level;
    private String name;
    private String description;
    private Category category;
    private Type type;
    private Occurrence occurrence;
    private final List<Attribute> attributes;

    public DataTypeElement(String dataTypeName) {
        this.id = UUID.randomUUID();
        this.level = Level.ROOT;
        this.name = dataTypeName;
        this.description = "";
        this.category = Category.COMPLEX_TYPE;
        this.type = null;
        this.occurrence = null;
        this.attributes = new ArrayList<>();
    }

    public DataTypeElement(String name, String description) {
        this.id = UUID.randomUUID();
        this.level = getProperLevel(name);
        this.name = name;
        this.description = description;
        this.category = getProperCategory(name);
        this.type = Type.STRING;
        this.occurrence = getProperOccurrence(name);
        this.attributes = new ArrayList<>();
    }

    private int getProperLevel(String name) {
        if (Objects.equals(name, Attribute.ACTION.getName())) {
            return Level.THIRD;
        } else if (Attribute.isAttribute(name)) {
            return Level.FIFTH;
        } else if (name.startsWith(STATEMENT)) {
            return Level.FIRST;
        }

        switch (name) {
            case ROW:
                return Level.FIRST;
            case DB_TABLE_NAME:
                return Level.SECOND;
            case ACCESS:
            case KEY:
            case TABLE:
                return Level.THIRD;
        }
        return Level.FOURTH;
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

    public UUID getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Occurrence getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    public void setCategory(Category changedCategory) {
        this.category = changedCategory;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DataTypeElement that = (DataTypeElement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
