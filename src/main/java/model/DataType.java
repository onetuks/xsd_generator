package model;

import model.vo.Category;

public class DataType {

    private String dtName;
    private String namespace;
    private String targetPath;
    private DataTypeNode root;

    public String getDtName() {
        return dtName;
    }

    public void setDtName(String dtName) {
        this.dtName = dtName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public DataTypeNode getRoot() {
        if (root == null) {
            return DataTypeNode.of(dtName, null, Category.COMPLEX_TYPE, null, null);
        }
        return root;
    }

    public void setRoot(DataTypeNode root) {
        this.root = root;
    }
}
