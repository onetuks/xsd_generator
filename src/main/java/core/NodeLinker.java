package core;

import dto.XDataType;
import dto.XDataTypes;
import model.XNode;
import model.XTree;

public class NodeLinker {

  public XTree link(XDataTypes dataTypes) {
    XTree tree =
        new XTree(dataTypes.getDataTypeName(), dataTypes.getNamespace(), dataTypes.getFilePath());

    dataTypes.getDataTypes()
        .forEach(dataType -> {
          XDataType parentDataType = dataTypes.findParent(dataType);

          XNode parentNode = tree.findNode(parentDataType.getName());
          XNode newNode =
              XNode.of(
                  dataType.getName(), dataType.getDescription(),
                  dataType.getCategory(), dataType.getType(), dataType.getOccurrence());

          parentNode.addChild(newNode);
        });

    return tree;
  }
}
