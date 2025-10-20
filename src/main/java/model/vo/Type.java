package model.vo;

public enum Type {
    STRING("xsd:string"),
    NUMBER("xsd:number"),
    DATE("xsd:date");

    private final String xsdType;

    Type(String xsdType) {
        this.xsdType = xsdType;
    }

    public String getXsdType() {
        return xsdType;
    }
}
