//@@author boundtotheearth

package seedu.momentum.logic.statistic;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * Encapsulates a piece of statistics data.
 */
public class StatisticEntry {
    private final String label;
    private final double value;

    /**
     * Constructs a {@code StatisticEntry}.
     *
     * @param label The label for the statistic.
     * @param value The statistic value.
     */
    public StatisticEntry(String label, double value) {
        requireNonNull(label);
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("(%s, %f)", label, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatisticEntry that = (StatisticEntry) o;
        return label.equals(that.label)
                && value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, value);
    }
}
