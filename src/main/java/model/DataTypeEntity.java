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
    private final Category category;
    private final Type type;
    private final Occurrence occurrence;

    public DataTypeEntity(
            UUID id, String name, String description, Category category, Type type, Occurrence occurrence) {
        this.id = id;
        this.name = name;
        this.description = description == null ? "" : description;
        this.category = category;
        this.type = type;
        this.occurrence = occurrence;
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

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public UUID getId() {
        return id;
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
}
