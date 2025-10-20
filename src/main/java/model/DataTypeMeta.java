package model;

import java.util.Objects;

public record DataTypeMeta(
        String dtName,
        String namespace,
        String filePath
) {

    public DataTypeMeta {
        if (Objects.isNull(dtName) || dtName.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid data type name: " + dtName);
        } else if (Objects.isNull(namespace) || namespace.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid namespace: " + namespace);
        } else if (Objects.isNull(filePath) || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid file path: " + filePath);
        }
    }
}
