package guesscharts.spotify;

import guesscharts.ParsingError;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.Properties;

/**
 * Gets the URL of a Spotify song preview.
 */
public class Spotify {
	private final String accessToken;
	private static final File propertiesFile = new File("spotify.properties");

	public Spotify() {
		Properties properties = loadProperties(propertiesFile);
		String clientId = properties.getProperty("clientId");
		String clientSecret = properties.getProperty("clientSecret");
		accessToken = getAccessToken(clientId, clientSecret);
	}

	/**
	 * @return <code>true</code> if the properties file with <code>clientId</code> and <code>clientSecret</code> can be read
	 */
	public static boolean hasPropertiesFile() {
		return propertiesFile.canRead();
	}

	private static Properties loadProperties(File fileName) {
		Properties properties = new Properties();
		try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(fileName))) {
			properties.load(stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return properties;
	}

	private static String getAccessToken(String clientId, String clientSecret) {
		Objects.requireNonNull(clientId);
		Objects.requireNonNull(clientSecret);

		String authorization = "Basic "
				+ Base64.getEncoder().encodeToString(String.format("%s:%s", clientId, clientSecret).getBytes());
		try {
			String response = Jsoup.connect("https://accounts.spotify.com/api/token").method(Method.POST)
					.ignoreContentType(true).header("Authorization", authorization)
					.data("grant_type", "client_credentials").execute().body();
			return new JSONObject(response).getString("access_token");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * The URL of the Spotify preview for a <code>spotifyId</code>.
	 *
	 * @param spotifyId the Spotify ID of a song
	 * @return the URL to the preview of the song
	 * @throws ParsingError if this id has no preview on Spotify
	 */
	public String getPreviewUrl(String spotifyId) throws IOException {
		Objects.requireNonNull(spotifyId);

		String url = String.format("https://api.spotify.com/v1/tracks/%s?market=GB", spotifyId);
		String authorization = String.format("Bearer %s", accessToken);
		String response = Jsoup.connect(url).method(Method.GET).ignoreContentType(true)
				.header("Authorization", authorization).data("grant_type", "client_credentials").execute().body();
		JSONObject track = new JSONObject(response);
		if (track.isNull("preview_url")) {
			throw new ParsingError(String.format("%s has no preview on Spotify", spotifyId));
		}
		return track.getString("preview_url");
	}
}
