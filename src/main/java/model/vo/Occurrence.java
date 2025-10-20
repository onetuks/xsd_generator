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

    private Occurrence(String lowerBound, String upperBound) {
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

    public static String[] getOccurrenceCombo() {
        return new String[]{
                ofZeroOne().toString(),
                ofZeroUnbounded().toString(),
                ofOnlyOne().toString(),
                ofOneUnbounded().toString(),
                ofOptional().getLowerBound()
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

    public String getLowerBound() {
        return lowerBound;
    }

    public String getUpperBound() {
        return upperBound;
    }

    @Override
    public String toString() {
        if (Objects.equals(lowerBound, OPTIONAL)) {
            return OPTIONAL;
        }

        return lowerBound + ".." + (Objects.equals(upperBound, UNBOUNDED) ? UNBOUNDED_N : upperBound);
    }
}
