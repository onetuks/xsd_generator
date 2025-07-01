import static org.junit.jupiter.api.Assertions.assertEquals;

import core.NodeLinker;
import dto.XDataTypes;
import model.XTree;
import org.junit.jupiter.api.Test;

public class NodeLinkerTest {

  @Test
  public void linkTest() {
    // given
    NodeLinker linker = new NodeLinker();
    XDataTypes dataTypes = TestFixture.createXDataTypes();
    XTree expected = TestFixture.createXTree();

    // when
    XTree result = linker.link(dataTypes);

    // then
    assertEquals(expected.getNamespace(), result.getNamespace());
    assertEquals(expected.getFilePath(), result.getFilePath());
    assertEquals(expected.getRoot().getEntity().getName(), result.getRoot().getEntity().getName());
    assertEquals(
        expected.getRoot()
            .getChildren().get(0).getEntity().getName(),
        result.getRoot()
            .getChildren().get(0).getEntity().getName());
    assertEquals(
        expected.getRoot()
            .getChildren().get(0)
            .getChildren().get(0).getEntity().getName(),
        result.getRoot()
            .getChildren().get(0)
            .getChildren().get(0).getEntity().getName());
    assertEquals(
        expected.getRoot()
            .getChildren().get(0)
            .getChildren().get(1).getEntity().getName(),
        result.getRoot()
            .getChildren().get(0)
            .getChildren().get(1).getEntity().getName());
    assertEquals(
        expected.getRoot()
            .getChildren().get(0)
            .getChildren().get(2).getEntity().getName(),
        result.getRoot()
            .getChildren().get(0)
            .getChildren().get(2).getEntity().getName());
    assertEquals(
        expected.getRoot()
            .getChildren().get(1)
            .getChildren().get(0).getEntity().getName(),
        result.getRoot()
            .getChildren().get(1)
            .getChildren().get(0).getEntity().getName());
    assertEquals(
        expected.getRoot()
            .getChildren().get(1)
            .getChildren().get(1).getEntity().getName(),
        result.getRoot()
            .getChildren().get(1)
            .getChildren().get(1).getEntity().getName());
    assertEquals(
        expected.getRoot()
            .getChildren().get(1)
            .getChildren().get(2).getEntity().getName(),
        result.getRoot()
            .getChildren().get(1)
            .getChildren().get(2).getEntity().getName());
  }
}
