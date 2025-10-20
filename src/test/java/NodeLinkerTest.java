import dto.XDataTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(expected.getRoot().entity().getName(), result.getRoot().entity().getName());
        assertEquals(
                expected.getRoot()
                        .children().get(0).entity().getName(),
                result.getRoot()
                        .children().get(0).entity().getName());
        assertEquals(
                expected.getRoot()
                        .children().get(0)
                        .children().get(0).entity().getName(),
                result.getRoot()
                        .children().get(0)
                        .children().get(0).entity().getName());
        assertEquals(
                expected.getRoot()
                        .children().get(0)
                        .children().get(1).entity().getName(),
                result.getRoot()
                        .children().get(0)
                        .children().get(1).entity().getName());
        assertEquals(
                expected.getRoot()
                        .children().get(0)
                        .children().get(2).entity().getName(),
                result.getRoot()
                        .children().get(0)
                        .children().get(2).entity().getName());
        assertEquals(
                expected.getRoot()
                        .children().get(1)
                        .children().get(0).entity().getName(),
                result.getRoot()
                        .children().get(1)
                        .children().get(0).entity().getName());
        assertEquals(
                expected.getRoot()
                        .children().get(1)
                        .children().get(1).entity().getName(),
                result.getRoot()
                        .children().get(1)
                        .children().get(1).entity().getName());
        assertEquals(
                expected.getRoot()
                        .children().get(1)
                        .children().get(2).entity().getName(),
                result.getRoot()
                        .children().get(1)
                        .children().get(2).entity().getName());
    }
}
