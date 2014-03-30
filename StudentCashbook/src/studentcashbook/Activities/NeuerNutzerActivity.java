package studentcashbook.Activities;

import Crypt.CryptHelper;
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
	
	

public void BenutzerNeuAnlegen(View view){
		
		//Zugriff auf UI Element
		EditText PasswortFeld = (EditText) findViewById(R.id.neuesPasswortEingabe);
		EditText PasswortWdh = (EditText) findViewById(R.id.passwortWdh);
		
	
		//Werte der UI Elemente holen
		
		//<<<<<<<<<<<<<<<<<<<Verschl??sselung hinzufuegen>>>>>>>>>>>>>>>>>>>>>>>>>>
		String Passwort = PasswortFeld.getText().toString();
		String PasswortWdhString = PasswortWdh.getText().toString();
		
		//Wenn die Eingaben uebereinstimmen
		if(Passwort.contains(PasswortWdhString)){
		
		//Holen der Preferences
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = spref.edit();
		
		
		//Eingabe verschluesseln		
		String passwortEncrypt="";
		try {
			passwortEncrypt = CryptHelper.toHex(Passwort);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//verschluesselter Wert speichern;
		editor.putString("PASSWORD", passwortEncrypt);
		editor.commit();
		
		
		//UI-Elemente zur??cksetzen
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
			
			//UI-Elemente zur??cksetzen
			PasswortFeld.setText("");
			PasswortWdh.setText("");
		
		}
		
	}
}
