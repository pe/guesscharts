package guesscharts.java_fx;

import guesscharts.parser.Charts;
import guesscharts.parser.ChartsParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ChartsSettingsTest {
	private ChartsSettings settings;

	@BeforeEach
	void createNewSettings() {
		settings = new ChartsSettings();
	}

	@Nested
	class WhenNew {

		@Test
		void chartsIsNull() {
			assertThat(settings.charts.get(), nullValue());
		}

		@Test
		void minYearIs0() {
			assertThat(settings.minYear.get(), equalTo(0));
		}

		@Test
		void maxYearIs0() {
			assertThat(settings.maxYear.get(), equalTo(0));
		}

		@Test
		void yearFromIs0() {
			assertThat(settings.yearFrom.get(), equalTo(0));
		}

		@Test
		void yearToIs0() {
			assertThat(settings.yearTo.get(), equalTo(0));
		}

		@Test
		void minPositionIs0() {
			assertThat(settings.minPosition.get(), equalTo(0));
		}

		@Test
		void maxPositionIs0() {
			assertThat(settings.maxPosition.get(), equalTo(0));
		}

		@Test
		void positionFromIs0() {
			assertThat(settings.positionFrom.get(), equalTo(0));
		}

		@Test
		void positionToIs0() {
			assertThat(settings.positionTo.get(), equalTo(0));
		}
	}

	@Nested
	class AfterChartsWhereSet {
		final Charts anyCharts = new Charts() {
			@Override
			public ChartsParser parser() {
				throw new UnsupportedOperationException();
			}

			@Override
			public int lowestPosition() {
				return 1;
			}

			@Override
			public int highestPosition() {
				return 2;
			}

			@Override
			public int firstYear() {
				return 3;
			}

			@Override
			public int lastYear() {
				return 4;
			}
		};

		@BeforeEach
		void createNewSettings() {
			settings.charts.set(anyCharts);
		}

		@Test
		void chartsIsSet() {
			assertThat(settings.charts.get(), is(anyCharts));
		}

		@Test
		void minYearIsSet() {
			assertThat(settings.minYear.get(), equalTo(anyCharts.firstYear()));
		}

		@Test
		void maxYearIsSet() {
			assertThat(settings.maxYear.get(), equalTo(anyCharts.lastYear()));
		}

		@Test
		void yearFromIsSet() {
			assertThat(settings.yearFrom.get(), equalTo(anyCharts.firstYear()));
		}

		@Test
		void yearToIsSet() {
			assertThat(settings.yearTo.get(), equalTo(anyCharts.lastYear()));
		}

		@Test
		void minPositionIsSet() {
			assertThat(settings.minPosition.get(), equalTo(anyCharts.lowestPosition()));
		}

		@Test
		void maxPositionIsSet() {
			assertThat(settings.maxPosition.get(), equalTo(anyCharts.highestPosition()));
		}

		@Test
		void positionFromIsSet() {
			assertThat(settings.positionFrom.get(), equalTo(anyCharts.lowestPosition()));
		}

		@Test
		void positionToIsSet() {
			assertThat(settings.positionTo.get(), equalTo(anyCharts.highestPosition()));
		}
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 1, 2})
	void yearFromIsLessThanOrEqualToYearTo(int yearFrom) {
		settings.yearTo.set(1);

		settings.yearFrom.set(yearFrom);

		assertThat(settings.yearFrom.get(), lessThanOrEqualTo(settings.yearTo.get()));
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 1, 2})
	void yearToIsGreaterThanOrEqualToYearFrom(int yearTo) {
		settings.yearFrom.set(1);

		settings.yearTo.set(yearTo);

		assertThat(settings.yearTo.get(), greaterThanOrEqualTo(settings.yearFrom.get()));
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 1, 2})
	void positionFromIsLessThanOrEqualToYearTo(int positionFrom) {
		settings.positionTo.set(1);

		settings.positionFrom.set(positionFrom);

		assertThat(settings.positionFrom.get(), lessThanOrEqualTo(settings.positionTo.get()));
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 1, 2})
	void positionToIsGreaterThanOrEqualToYearFrom(int positionTo) {
		settings.positionFrom.set(1);

		settings.positionTo.set(positionTo);

		assertThat(settings.positionTo.get(), greaterThanOrEqualTo(settings.positionFrom.get()));
	}
}
