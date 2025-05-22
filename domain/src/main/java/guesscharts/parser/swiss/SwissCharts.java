package guesscharts.parser.swiss;

import guesscharts.parser.Charts;
import guesscharts.parser.ChartsParser;

public class SwissCharts implements Charts {
    @Override
    public int firstYear() {
        return 1968;
    }

    @Override
    public ChartsParser parser() {
        return new SwissChartsParser();
    }

    @Override
    public String toString() {
        return "swiss";
    }
}
