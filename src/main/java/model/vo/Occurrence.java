package model.vo;

import java.util.Objects;

public class Occurrence {

  public final static String ZERO = "0";
  public final static String ONE = "1";
  public final static String UNBOUNDED = "unbounded";
  public final static String UNBOUNDED_N = "N";
  public final static String OPTIONAL = "optional";

  private final String lowerBound;
  private final String upperBound;

  public Occurrence(String lowerBound, String upperBound) {
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
  }

  public static Occurrence ofZeroOne() {
    return new Occurrence(ZERO, ONE);
  }

  public static Occurrence ofZeroUnbounded() {
    return new Occurrence(ZERO, UNBOUNDED);
  }

  public static Occurrence ofOnlyOne() {
    return new Occurrence(ONE, ONE);
  }

  public static Occurrence ofOneUnbounded() {
    return new Occurrence(ONE, UNBOUNDED);
  }

  public static Occurrence ofOptional() {
    return new Occurrence(OPTIONAL, null);
  }

  /**
   * ATTRIBUTE는 항상 "optional" 하나만 선택 가능하고, 그 외(ELEMENT)는 실제 bound 쌍을 갖는
   * 값만 선택 가능해야 한다. 구분 없이 전체 옵션을 보여주면 ELEMENT에 "optional"이 선택되어
   * upperBound가 null인 상태로 XSD가 생성되는 오류(maxOccurs="null")로 이어진다.
   */
  public static String[] getOccurrenceCombo(Category category) {
    if (category == Category.ATTRIBUTE) {
      return new String[]{ofOptional().getLowerBound()};
    }

    return new String[]{
        ofZeroOne().toString(),
        ofZeroUnbounded().toString(),
        ofOnlyOne().toString(),
        ofOneUnbounded().toString()
    };
  }

  public static Occurrence of(String occurrenceText) {
    if (Objects.equals(occurrenceText, ofZeroOne().toString())) {
      return ofZeroOne();
    } else if (Objects.equals(occurrenceText, ofZeroUnbounded().toString())) {
      return ofZeroUnbounded();
    } else if (Objects.equals(occurrenceText, ofOnlyOne().toString())) {
      return ofOnlyOne();
    } else if (Objects.equals(occurrenceText, ofOneUnbounded().toString())) {
      return ofOneUnbounded();
    }
    return ofOptional();
  }

  @Override
  public String toString() {
    if (Objects.equals(lowerBound, OPTIONAL)) {
      return OPTIONAL;
    }

    return lowerBound + ".." + (Objects.equals(upperBound, UNBOUNDED) ? UNBOUNDED_N : upperBound);
  }

  public String getLowerBound() {
    return lowerBound;
  }

  public String getUpperBound() {
    return upperBound;
  }
}
