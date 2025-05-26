package guesscharts.java_fx;

import static org.assertj.core.api.Assertions.assertThat;
import guesscharts.parser.Charts;
import guesscharts.parser.ChartsParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ChartsSettingsTest {
    final Charts anyCharts = new MockCharts(3, 4);

    private ChartsSettings settings;

    @BeforeEach
    void createNewSettings() {
        settings = new ChartsSettings(anyCharts);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void yearFromIsLessThanOrEqualToYearTo(int yearFrom) {
        settings.yearTo.set(1);

        settings.yearFrom.set(yearFrom);

        assertThat(settings.yearFrom.get()).isLessThanOrEqualTo(settings.yearTo.get());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void yearToIsGreaterThanOrEqualToYearFrom(int yearTo) {
        settings.yearFrom.set(1);

        settings.yearTo.set(yearTo);

        assertThat(settings.yearTo.get()).isGreaterThanOrEqualTo(settings.yearFrom.get());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void positionFromIsLessThanOrEqualToYearTo(int positionFrom) {
        settings.positionTo.set(1);

        settings.positionFrom.set(positionFrom);

        assertThat(settings.positionFrom.get()).isLessThanOrEqualTo(settings.positionTo.get());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void positionToIsGreaterThanOrEqualToYearFrom(int positionTo) {
        settings.positionFrom.set(1);

        settings.positionTo.set(positionTo);

        assertThat(settings.positionTo.get()).isGreaterThanOrEqualTo(settings.positionFrom.get());
    }

    @Nested
    class WhenNew {
        @Test
        void chartsIsSet() {
            assertThat(settings.charts.get()).isEqualTo(anyCharts);
        }

        @Test
        void minYearIsSet() {
            assertThat(settings.minYear.get()).isEqualTo(anyCharts.firstYear());
        }

        @Test
        void maxYearIsSet() {
            assertThat(settings.maxYear.get()).isEqualTo(anyCharts.lastYear());
        }

        @Test
        void yearFromIsSet() {
            assertThat(settings.yearFrom.get()).isEqualTo(anyCharts.firstYear());
        }

        @Test
        void yearToIsSet() {
            assertThat(settings.yearTo.get()).isEqualTo(anyCharts.lastYear());
        }

        @Test
        void minPositionIsSet() {
            assertThat(settings.minPosition.get()).isEqualTo(anyCharts.lowestPosition());
        }

        @Test
        void maxPositionIsSet() {
            assertThat(settings.maxPosition.get()).isEqualTo(anyCharts.highestPosition());
        }

        @Test
        void positionFromIsSet() {
            assertThat(settings.positionFrom.get()).isEqualTo(anyCharts.lowestPosition());
        }

        @Test
        void positionToIsSet() {
            assertThat(settings.positionTo.get()).isEqualTo(anyCharts.highestPosition());
        }
    }

    @Nested
    class AfterChartsWhereSet {
        @Test
        void chartsIsSet() {
            Charts otherCharts = new MockCharts(3, 4);

            settings.charts.set(otherCharts);

            assertThat(settings.charts.get()).isEqualTo(otherCharts);
        }
    }

    private record MockCharts(int firstYear, int lastYear) implements Charts {
        @Override
        public ChartsParser parser() {
            throw new UnsupportedOperationException();
        }
    }
}
