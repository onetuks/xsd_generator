package definition.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import specification.elements.DataTypeElement;

class DataTypeFieldParserTest {

  private final DataTypeFieldParser parser = new DataTypeFieldParser();

  @Test
  void 이름과_설명을_줄_단위로_매칭하여_필드를_생성한다() {
    String names = "fieldA\nfieldB";
    String descriptions = "설명A\n설명B";

    List<DataTypeElement> elements = parser.parseData(names, descriptions);

    assertEquals(2, elements.size());
    assertEquals("fieldA", elements.get(0).getName());
    assertEquals("설명A", elements.get(0).getDescription());
    assertEquals("fieldB", elements.get(1).getName());
    assertEquals("설명B", elements.get(1).getDescription());
  }

  @Test
  void 빈_이름_라인은_결과에서_제외된다() {
    String names = "fieldA\n\nfieldB";
    String descriptions = "설명A\n설명중간\n설명B";

    List<DataTypeElement> elements = parser.parseData(names, descriptions);

    assertEquals(2, elements.size());
    assertEquals("fieldA", elements.get(0).getName());
    assertEquals("fieldB", elements.get(1).getName());
  }

  @Test
  void 설명_라인이_이름보다_적으면_남는_필드는_빈_설명으로_처리된다() {
    String names = "fieldA\nfieldB\nfieldC";
    String descriptions = "설명A";

    List<DataTypeElement> elements = parser.parseData(names, descriptions);

    assertEquals(3, elements.size());
    assertEquals("설명A", elements.get(0).getDescription());
    assertEquals("", elements.get(1).getDescription());
    assertEquals("", elements.get(2).getDescription());
  }

  @Test
  void NA_표기는_빈_문자열로_치환된다() {
    String names = "fieldA";
    String descriptions = "N/A";

    List<DataTypeElement> elements = parser.parseData(names, descriptions);

    assertEquals("", elements.get(0).getDescription());
  }
}
