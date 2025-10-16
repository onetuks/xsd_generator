package model;

import specification.elements.DataTypeElement;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class DataTypeState {

    private DataTypeMeta meta;

    private TreeMap<String, String> fields = new TreeMap<>();
    private List<DataTypeElement> elements = new ArrayList<>();
    private DataTypeNode rootNode = null;

    public DataTypeMeta getMeta() {
        return meta;
    }

    public void setMeta(String dtName, String namespace, String filePath) {
        this.meta = new DataTypeMeta(dtName, namespace, filePath);
    }

    public TreeMap<String, String> getFields() {
        return fields;
    }

    public List<DataTypeElement> getElements() {
        return elements;
    }

    public void setElements(List<DataTypeElement> dataTypeElements) {
        this.elements = dataTypeElements;
    }

    public DataTypeNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(DataTypeNode rootNode) {
        this.rootNode = rootNode;
    }
}
