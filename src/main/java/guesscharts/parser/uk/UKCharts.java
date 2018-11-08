package guesscharts.parser.uk;

import guesscharts.parser.Charts;
import guesscharts.parser.ChartsParser;
import guesscharts.spotify.Spotify;

public class UKCharts implements Charts {
	@Override
	public int highestPosition() {
		return 100;
	}

	@Override
	public int firstYear() {
		return 2005; // Earlier years have no previews
	}

	@Override
	public ChartsParser parser() {
		return new UKChartsParser();
	}

	@Override
	public boolean isConfigured() {
		return Spotify.hasPropertiesFile();
	}

	@Override
	public String toString() {
		return "U.K.";
	}
}
