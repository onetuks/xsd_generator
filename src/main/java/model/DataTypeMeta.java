package model;

import java.util.Objects;

public class DataTypeMeta {

    private final String dtName;
    private final String namespace;
    private final String filePath;

    public DataTypeMeta(String dtName, String namespace, String filePath) {
        this.dtName = dtName;
        this.namespace = namespace;
        this.filePath = filePath;

        validate(dtName, namespace, filePath);
    }

    private void validate(String dtName, String namespace, String filePath) {
        if (Objects.isNull(dtName) || dtName.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid data type name: " + dtName);
        } else if (Objects.isNull(namespace) || namespace.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid namespace: " + namespace);
        } else if (Objects.isNull(filePath) || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid file path: " + filePath);
        }
    }

    public String getDtName() {
        return dtName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getNamespace() {
        return namespace;
    }
}
