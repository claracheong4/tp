package seedu.momentum.logic.statistic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.momentum.logic.statistic.StatisticTestUtil.TEST_MODEL;
import static seedu.momentum.logic.statistic.StatisticTestUtil.TEST_WEEKLY_TIME_PER_PROJECT;
import static seedu.momentum.testutil.Assert.assertThrows;

import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import seedu.momentum.commons.core.Clock;
import seedu.momentum.testutil.TypicalTimes;

class PeriodicTotalTimePerProjectStatisticTest {
    @Test
    public void constructor_nullPeriod_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new PeriodicTotalTimePerProjectStatistic(null, ChronoUnit.MINUTES));
    }

    @Test
    public void constructor_nullUnits_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new PeriodicTotalTimePerProjectStatistic(ChronoUnit.WEEKS, null));
    }

    @Test
    public void calculate_typicalProjects_correctData() {
        Clock.initFixed(TypicalTimes.DAY_ADD_DAY);
        PeriodicTotalTimePerProjectStatistic stat =
                new PeriodicTotalTimePerProjectStatistic(ChronoUnit.WEEKS, ChronoUnit.MINUTES);
        stat.calculate(TEST_MODEL);

        assertEquals(TEST_WEEKLY_TIME_PER_PROJECT, stat);
    }

    @Test
    public void calculate_notInPeriod_correctData() {
        Clock.initFixed(TypicalTimes.DAY_ADD_YEAR);
        PeriodicTotalTimePerProjectStatistic stat =
                new PeriodicTotalTimePerProjectStatistic(ChronoUnit.WEEKS, ChronoUnit.MINUTES);
        stat.calculate(TEST_MODEL);
        PeriodicTotalTimePerProjectStatistic expectedStat =
                new PeriodicTotalTimePerProjectStatistic(ChronoUnit.WEEKS, ChronoUnit.MINUTES,
                        FXCollections.observableArrayList(
                                new StatisticEntry("Alpha", 0),
                                new StatisticEntry("Beta", 0),
                                new StatisticEntry("Charlie", 0),
                                new StatisticEntry("Delta", 0)
                        ));

        assertEquals(expectedStat, stat);
    }

}
