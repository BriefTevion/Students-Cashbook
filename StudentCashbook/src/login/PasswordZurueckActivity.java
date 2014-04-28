/*
 * Dies Klasse beinhaltet alle Methoden zum zuruecksetzen des Passwortes.
 */
package login;

import uebersicht.MainActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.example.studentcashbook.R;

import crypt.CryptHelper;

public class PasswordZurueckActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_zurueck);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.password_zurueck, menu);
		return true;
	}

	// Sicherheitsfrage bestaetigt, Button geklickt
	public void frageBestaetigen(View view) {
		EditText eingabe = (EditText) findViewById(R.id.secureFrage_Eingabe);

		String namenEingabe = eingabe.getText().toString();

		String secureInput = LoginLoader
				.getSecureInput(getApplicationContext());

		// verschlusseln der Eingabe
		String nameInputEncrypt = "";
		try {
			nameInputEncrypt = CryptHelper.toHex(namenEingabe);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Daten vergleichen
		int resultPswd = nameInputEncrypt.compareTo(secureInput);

		// Wenn Passwort uebereinstimmend
		if (resultPswd == 0) {

			setContentView(R.layout.alert_neues_password);
		} else {
			// Error Message aufrufen
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Name falsch");
			alert.setNegativeButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();

			// Passworteingabe zuruecksetzen
			eingabe.setText("");
		}

	}

	// Neues Passwort in key-value-store schreiben, Button geklickt
	public void neuesPasswordSetzen(View view) {
		// Zugriff auf UI Element
		EditText PasswortFeld = (EditText) findViewById(R.id.passwordNeu_Eingabe);
		EditText PasswortWdh = (EditText) findViewById(R.id.passwordNeu_EingabeWdh);

		// Werte der UI Elemente holen
		String Passwort = PasswortFeld.getText().toString();
		String PasswortWdhString = PasswortWdh.getText().toString();

		// Wenn die Eingaben uebereinstimmen
		if (Passwort.contains(PasswortWdhString)) {

			// Eingabe verschluesseln
			String passwortEncrypt = "";
			try {
				passwortEncrypt = CryptHelper.toHex(Passwort);

			} catch (Exception e) {
				e.printStackTrace();
			}

			// verschluesselter Wert im key-value-store speichern;
			LoginLoader.setPassword(getApplicationContext(), passwortEncrypt);

			// Erfolgsmeldung
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Erfolgreich geaendert!");
			alert.setNegativeButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							finish();
							// Zur Mainactivity wechseln
							Intent intent = new Intent(getApplicationContext(),
									MainActivity.class);
							startActivity(intent);

						}
					});
			alert.setCancelable(true);
			alert.create().show();

		}

		// Wenn die Eingaben nicht uebereinstimmen
		else {

			// Fehler melden
			// Error Message aufrufen
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Passwoerter stimmen nicht ueberein");
			alert.setNegativeButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();

			// UI-Elemente zuruecksetzen
			PasswortFeld.setText("");
			PasswortWdh.setText("");

		}
	}

}
