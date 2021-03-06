/*
 * Diese Klasse stellt Methoden zum Laden der Tipps-Daten vom Server "space-labs.appspot.com".
 */
package server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class NetworkConnect {

	// JSON Node names
	private static final String TAG_ID = "id";
	private static final String TAG_TITLE = "Title";
	private static final String TAG_MESSAGE = "Description";
	private static final String TAG_URL = "URL";

	// Methode um eine Verbindung zum Server aufzubauen und um gewuenschte Daten
	// zu erhalten
	public String downloadTipps() throws Exception {

		InputStream is = null;

		try {
			// Serververbindung oeffnen
			URL url = new URL(
					"http://space-labs.appspot.com/repo/2185003/budget/api/tipps.sjs");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.connect();

			// int response = conn.getResponseCode();
			is = conn.getInputStream();

			String contentAsString = convertStreamToString(is);

			// Daten auf Server abfragen
			String tipp = parseDownload(contentAsString);

			return tipp;
		}

		finally {
			if (is != null) {
				is.close();
			}

		}

	}

	// Konvertiert Server Antwort in String
	private static String convertStreamToString(InputStream is)
			throws Exception {
		BufferedReader bf = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		// solange bis es keine Zeile mehr in Server Antwort gibt
		while ((line = bf.readLine()) != null) {
			sb.append(line);
		}
		is.close();

		return sb.toString();

	}

	// einen zufaelligen Tipp aus dem Download heraussuchen
	private static String parseDownload(String rawString) {

		StringBuffer parsedString = new StringBuffer();
		String tipp = "";
		String nachricht = "Fehler";
		List al = new ArrayList<TippsListEntry>();

		try {
			JSONArray tipps = new JSONArray(rawString);

			for (int i = 0; i < tipps.length(); i++) {
				JSONObject h = tipps.getJSONObject(i);

				TippsListEntry tle = new TippsListEntry(h.optInt(TAG_ID),
						h.optString(TAG_TITLE), h.optString(TAG_MESSAGE),
						h.optString(TAG_URL));
				al.add(tle);
			}

		} catch (JSONException e) {
			Log.v("test", e.getMessage());
		}

		TippsListEntry[] a = new TippsListEntry[al.size()];
		al.toArray(a);

		// Bubble sort
		for (int n = a.length; n > 1; n--) {
			for (int i = 0; i < n - 1; i++) {
				if (a[i].getID() < a[i + 1].getID()) {
					TippsListEntry temp = a[i];
					a[i] = a[i + 1];
					a[i + 1] = temp;
				}
			}
		}
		// zufaelligen Tipp auswaehlen
		int randomTipp = (int) (Math.random() * a.length);
		// Auswechseln bei Acht, da Fehler mit acht
		if (randomTipp == 8) {
			randomTipp = 7;
		}

		parsedString.append(a[randomTipp].getTitle() + "\n\n");
		parsedString.append(a[randomTipp].getDescription());
		parsedString.append("-" + a[randomTipp].getURL().toString());

		return parsedString.toString();

	}
}
