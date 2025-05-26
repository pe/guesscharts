package guesscharts.java_fx;

import guesscharts.java_fx.util.GreaterLesserIntegerProperty;
import guesscharts.parser.Charts;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

class ChartsSettings {
    final ObjectProperty<Charts> charts = new SimpleObjectProperty<>();

    final IntegerProperty minYear = new SimpleIntegerProperty();
    final IntegerProperty maxYear = new SimpleIntegerProperty();
    final GreaterLesserIntegerProperty yearFrom = new GreaterLesserIntegerProperty();
    final GreaterLesserIntegerProperty yearTo = new GreaterLesserIntegerProperty();

    final IntegerProperty minPosition = new SimpleIntegerProperty();
    final IntegerProperty maxPosition = new SimpleIntegerProperty();
    final GreaterLesserIntegerProperty positionFrom = new GreaterLesserIntegerProperty();
    final GreaterLesserIntegerProperty positionTo = new GreaterLesserIntegerProperty();

    public ChartsSettings(Charts charts) {
        this.charts.addListener((e, oldCharts, newCharts) -> {
            minYear.set(newCharts.firstYear());
            maxYear.set(newCharts.lastYear());
            yearFrom.set(newCharts.firstYear());
            yearTo.set(newCharts.lastYear());

            minPosition.set(newCharts.lowestPosition());
            maxPosition.set(newCharts.highestPosition());
            positionFrom.set(newCharts.lowestPosition());
            positionTo.set(newCharts.highestPosition());
        });

        yearFrom.ensureGreaterThanOrEqualTo(minYear);
        yearFrom.ensureLessThanOrEqualTo(yearTo);
        yearTo.ensureLessThanOrEqualTo(maxYear);
        yearTo.ensureGreaterThanOrEqualTo(yearFrom);

        positionFrom.ensureGreaterThanOrEqualTo(minPosition);
        positionFrom.ensureLessThanOrEqualTo(positionTo);
        positionTo.ensureLessThanOrEqualTo(maxPosition);
        positionTo.ensureGreaterThanOrEqualTo(positionFrom);

        this.charts.set(charts);
    }
}
