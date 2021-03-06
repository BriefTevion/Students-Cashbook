/*
 * Diese Klasse startet den Download der Tipps-Daten und uebergibt die Daten zur Versendung einer Notification.
 */
package server;

import android.os.AsyncTask;
import drawer.BaseActivity;

public class StartNetworkConnectForNotifications extends
		AsyncTask<String, Void, String> {

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
		BaseActivity.sendNotification(tipp);
	}

}
