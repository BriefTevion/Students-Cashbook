/*
 * Diese Klasse beinhaltet alle Methoden rund um den Anmeldescreen.
 * Speicherung und Abfrage erfolgen mit den Methoden der Klasse LoginLoader
 */
package login;

import uebersicht.MainActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.example.studentcashbook.R;

import crypt.CryptHelper;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// Back Taste ausgeschalten
	@Override
	public void onBackPressed() {
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Wenn noch kein Passwort gesetzt worden ist, Umleitung zur
		// Registrierung
		if (LoginLoader.getPassword(getApplicationContext()) == "") {
			Intent intent = new Intent(this, NeuerNutzerActivity.class);
			startActivity(intent);

			finish();
		}
		// wenn die Registrierung bereits vollzogen ist
		else {
			setContentView(R.layout.activity_login);
		}

	}

	// Called when Anmelde Button clicked
	public void buttonPressed(View view) {

		// Eingaben pruefen
		// Zugriff auf UI Element
		EditText PasswortFeld = (EditText) findViewById(R.id.Passwort);

		// Werte der UI Elemente holen
		String PasswortInput = PasswortFeld.getText().toString();

		// verschlusseln der Eingabe
		String PasswortInputEncrypt = "";
		try {
			PasswortInputEncrypt = CryptHelper.toHex(PasswortInput);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Daten vergleichen
		int resultPswd = PasswortInputEncrypt.compareTo(LoginLoader
				.getPassword(getApplicationContext()));

		// Wenn Passwort uebereinstimmend
		if (resultPswd == 0) {

			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		} else {
			// Error Message aufrufen
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Passwort falsch");
			alert.setTitle("Anmeldung fehlgeschlagen");
			alert.setNegativeButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();

			// Passworteingabe zuruecksetzen
			PasswortFeld.setText("");
		}

	}

	public void PasswortZuruecksetzen(View view) {
		Intent intent = new Intent(this, PasswordZurueckActivity.class);
		startActivity(intent);

	}

}
