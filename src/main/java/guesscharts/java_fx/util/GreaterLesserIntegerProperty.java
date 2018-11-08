package guesscharts.java_fx.util;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GreaterLesserIntegerProperty extends SimpleIntegerProperty {
	/**
	 * @see SimpleIntegerProperty#SimpleIntegerProperty()
	 */
	public GreaterLesserIntegerProperty() {
		super();
	}

	/**
	 * @see SimpleIntegerProperty#SimpleIntegerProperty(int)
	 */
	public GreaterLesserIntegerProperty(int initialValue) {
		super(initialValue);
	}

	/**
	 * Ensures this property is always less than or equal to the <code>other</code>.
	 * <p>
	 * Adds a {@link javafx.beans.value.ChangeListener ChangeListener} to the <code>other</code>
	 * property updating <code>this</code> property when it becomes greater than the
	 * other.
	 */
	public void ensureLessThanOrEqualTo(IntegerProperty other) {
		other.addListener((e, oldValue, newValue) -> {
			if (get() > newValue.intValue()) {
				set(newValue.intValue());
			}
		});
	}

	/**
	 * Ensures this property is always greater than or equal to the
	 * <code>other</code>.
	 * <p>
	 * Adds a {@link javafx.beans.value.ChangeListener ChangeListener} to the <code>other</code>
	 * property updating <code>this</code> property when it becomes smaller than the
	 * other.
	 */
	public void ensureGreaterThanOrEqualTo(IntegerProperty other) {
		other.addListener((e, oldValue, newValue) -> {
			if (get() < newValue.intValue()) {
				set(newValue.intValue());
			}
		});
	}
}
