package model.vo;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class OccurrenceTest {

  @Test
  void ATTRIBUTE_카테고리는_optional_옵션만_제공한다() {
    String[] options = Occurrence.getOccurrenceCombo(Category.ATTRIBUTE);

    assertArrayEquals(new String[]{"optional"}, options);
  }

  @Test
  void ELEMENT_카테고리는_optional_옵션을_제공하지_않는다() {
    String[] options = Occurrence.getOccurrenceCombo(Category.ELEMENT);

    assertFalse(Arrays.asList(options).contains("optional"));
  }
}
