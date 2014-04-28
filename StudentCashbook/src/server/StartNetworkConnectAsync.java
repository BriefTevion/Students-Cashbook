/*
 * Diese Klasse startet den Download der Tipps-Daten und uebergibt die Daten zur Anzeige eines Tipp-Alerts.
 */
package server;

import uebersicht.MainActivity;
import android.os.AsyncTask;

public class StartNetworkConnectAsync extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... urls) {
		NetworkConnect nc = new NetworkConnect();
		try {
			return nc.downloadTipps();

		} catch (Exception e) {
			return e.getMessage();
		}

	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String tipp) {
		MainActivity.showTipp(tipp);
	}

}
