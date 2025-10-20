package model;

import model.vo.Category;
import model.vo.Occurrence;
import model.vo.Type;

import java.util.Objects;
import java.util.UUID;

public class DataTypeEntity {

    private final UUID id;
    private final String name;
    private final String description;
    private Category category;
    private Type type;
    private final Occurrence occurrence;

    public DataTypeEntity(
            String name, String description, Category category, Type type, Occurrence occurrence) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description == null ? "" : description;
        this.category = category;
        this.type = type;
        this.occurrence = occurrence;
    }

    public UUID getId() {
        return id;
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

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataTypeEntity xEntity = (DataTypeEntity) o;
        return Objects.equals(name, xEntity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
