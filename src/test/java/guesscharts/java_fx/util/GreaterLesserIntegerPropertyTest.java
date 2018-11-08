package guesscharts.java_fx.util;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

class GreaterLesserIntegerPropertyTest {
	private IntegerProperty other;
	private GreaterLesserIntegerProperty testee;

	@BeforeEach
	void createNewProperties() {
		other = new SimpleIntegerProperty(1);
		testee = new GreaterLesserIntegerProperty(1);
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 1, 2})
	void isLessThanOrEqualTo(int value) {
		testee.ensureLessThanOrEqualTo(other);

		other.set(value);

		assertThat(testee.get(), lessThanOrEqualTo(other.get()));
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 1, 2})
	void isGreaterThanOrEqualTo(int value) {
		testee.ensureGreaterThanOrEqualTo(other);

		other.set(value);

		assertThat(testee.get(), greaterThanOrEqualTo(value));
	}
}
