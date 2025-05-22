package guesscharts.parser.german;

import guesscharts.parser.Charts;
import guesscharts.parser.ChartsParser;

public class GermanCharts implements Charts {
    @Override
    public int firstYear() {
        return 1978;
    }

    @Override
    public ChartsParser parser() {
        return new GermanChartsParser();
    }

    @Override
    public String toString() {
        return "german";
    }
}
