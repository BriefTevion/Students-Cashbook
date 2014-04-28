/*
 * Diese Klasse startet die Einstellungen und gibt die eingestellten Praeferenzen zurueck.
 */
package einstellungen;

import login.PasswordZurueckActivity;
import uebersicht.MainActivity;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.example.studentcashbook.R;

public class EinstellungenActivity extends PreferenceActivity {

	// Preferences Keys der Einstellmoeglichkeiten
	final static String keyTippAuto = "pref_key_tipps_auto";
	final static String keyTippNotifications = "pref_key_tipps_notifications";
	final static String keySpracheChange = "pref_key_sprache_change";
	final static String keyPswdChange = "pref_key_passwort_change";

	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.activity_einstellungen);
		Preference newPswd = (Preference) findPreference(keyPswdChange);
		newPswd.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {

				// Activity PasswordZurueck aufrufen
				Intent intent = new Intent(getApplicationContext(),
						PasswordZurueckActivity.class);
				startActivity(intent);

				return true;
			}
		});

	}

	// Check ob automatisch tipps angezeigt werden duerfen
	public static Boolean getKeyTippAuto() {

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.getContext());
		Boolean syncConnPref = sharedPref.getBoolean(keyTippAuto, false);

		return syncConnPref;
	}

	// Check ob tipps benachrichtigungen zugelassen sind
	public static Boolean getKeyTippNotifications() {

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.getContext());
		Boolean syncConnPref = sharedPref.getBoolean(keyTippNotifications,
				false);

		return syncConnPref;
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.einstellungen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
