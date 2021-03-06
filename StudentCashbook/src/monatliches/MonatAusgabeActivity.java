/*
 * Diese Klasse beinhaltet alle Methoden um die Activity "Anlegen einer neuen monatlichen Ausgabe" zu bearbeiten.
 */
package monatliches;

import java.text.DateFormat;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.studentcashbook.R;

import db.BudgetLoader;

public class MonatAusgabeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_monat_ausgabe);
			// Show the Up button in the action bar.
			setupActionBar();
		} catch (Exception e) {
			Log.v("test", e.getMessage());
		}

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.monat_ausgabe, menu);
		return true;
	}

	// neue monatliche Ausgabe hinzufuegen
	public void addMonatlicheAusgabe(View view) {
		EditText name = (EditText) findViewById(R.id.editText_Name);
		EditText betragF = (EditText) findViewById(R.id.editText_betrag);
		EditText tag = (EditText) findViewById(R.id.editText_Tag);

		String betrag = "-" + betragF.getText().toString();
		String datum = DateFormat.getDateInstance().format(new Date());
		String tagString;
		if (tag.getText().toString().isEmpty()
				|| tag.getText().toString() == "0") {
			tagString = "1";
		} else {
			tagString = tag.getText().toString();
		}

		try {
			// neue monatliche Ausgabe in die DB-Tabelle schreiben
			BudgetLoader.addMonatlicheTransaktion(getApplicationContext(), name
					.getText().toString(), betrag, datum, tagString);

			// Nachricht ueber erfolgreiches speichern
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Geschäft angelegt");
			alert.setTitle("Erfolgreich");
			alert.setNegativeButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							dialog.cancel();

							changeToMonatlichUebersicht();

						}
					});

			alert.setCancelable(true);
			alert.create().show();

		} catch (Exception e) {

		}
	}

	// zurueck zur Uebersicht wechseln
	public void changeToMonatlichUebersicht() {
		Intent intent = new Intent(getApplicationContext(),
				MonatlichesActivity.class);
		startActivity(intent);

	}

	// Button ABBRECHEN geklickt
	public void abbrechen(View view) {
		EditText name = (EditText) findViewById(R.id.editText_Name);
		EditText budget = (EditText) findViewById(R.id.editText_betrag);

		name.setText("");
		budget.setText("");

		changeToMonatlichUebersicht();

	}

}
