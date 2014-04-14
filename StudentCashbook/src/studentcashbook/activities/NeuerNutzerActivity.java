/*
 * Diese Klasse beinhaltet alle Methoden um die Activity "Anlegen eines neuen Nutzers" zu bearbeiten.
 */
package studentcashbook.activities;

import login.LoginLoader;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.example.studentcashbook.R;

import crypt.CryptHelper;

public class NeuerNutzerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neuer_nutzer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.neuer_nutzer, menu);
		return true;
	}
	
	//Back Taste ausgeschalten
	@Override
	public void onBackPressed() {
	}
	
	

public void BenutzerNeuAnlegen(View view){
		
		//Zugriff auf UI Element
		EditText PasswortFeld = (EditText) findViewById(R.id.neuesPasswortEingabe);
		EditText PasswortWdh = (EditText) findViewById(R.id.passwortWdh);
		EditText eingabeSecureFrage = (EditText) findViewById(R.id.eingabe_secureFrage);
		
	
		//Werte der UI Elemente holen
		
		//<<<<<<<<<<<<<<<<<<<Verschluesselung hinzufuegen>>>>>>>>>>>>>>>>>>>>>>>>>>
		String Passwort = PasswortFeld.getText().toString();
		String PasswortWdhString = PasswortWdh.getText().toString();
		
		//Wenn die Eingaben uebereinstimmen
		if(Passwort.contains(PasswortWdhString)){
		
		
			//Eingabe verschluesseln		
			String passwortEncrypt="";
			String eingabeSecure= eingabeSecureFrage.getText().toString();
			try {
				passwortEncrypt = CryptHelper.toHex(Passwort);
				CryptHelper.toHex(eingabeSecure);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//Eingaben im key-value-store speichern
			LoginLoader.setPasswordAndSecurefrage(getApplicationContext(), passwortEncrypt, eingabeSecure);
			
			
			//UI-Elemente zuruecksetzen
			PasswortFeld.setText("");
			PasswortWdh.setText("");
			
			//Zur Mainactivity wechseln
			Intent intent = new Intent(this, MainActivity.class);	
			startActivity(intent);
			
			finish();
		}
		
		//Wenn die Eingaben nicht uebereinstimmen
		else{
						
			//Fehler melden
			//Error Message aufrufen
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Passwoerter stimmen nicht ueberein");
			alert.setTitle("Anmeldung fehlgeschagen");
			alert.setNegativeButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();
			
			//UI-Elemente zuruecksetzen
			PasswortFeld.setText("");
			PasswortWdh.setText("");
		
		}
		
	}
}
