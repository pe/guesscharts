import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public abstract class ChartsParser {
	public abstract InputStream getMp3();
	
	public abstract InputStream getMp3(String songURL) throws IOException;
	
	public abstract URI getSongDetailsLink();

	public abstract String getSong();
	
	public abstract String getSongURL();
	
	public abstract void getNewSong(int yearStart, int yearEnd, int positionStart, int positionEnd);

	public abstract Integer[] getYears();

	public abstract Integer[] getPositions();
}