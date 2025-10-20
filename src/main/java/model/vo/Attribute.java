package model.vo;

import java.util.Arrays;
import java.util.Objects;

public enum Attribute {
    ACTION("action"),
    HAS_QUOT("hasQuot"),
    IS_INPUT("isInput"),
    IS_OUTPUT("isOutput");

    private final String name;

    Attribute(String name) {
        this.name = name;
    }

    public static boolean isAttribute(String name) {
        return Arrays.stream(Attribute.values())
                .anyMatch(attribute -> Objects.equals(attribute.getName(), name));
    }

    public static boolean hasAttributeExceptAction(String attributeName) {
        return Arrays.stream(Attribute.values())
                .filter(attribute -> !Objects.equals(attribute.getName(), ACTION.getName()))
                .anyMatch(attribute -> Objects.equals(attribute.getName(), attributeName));
    }

    public String getName() {
        return name;
    }
}
