package com.example.studentcashbook;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		//Zugriff auf UI Element
		EditText BenutzernameFeld = (EditText) findViewById(R.id.Benutzername);
		
		//Holen der Preferences
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		//Name auslesen
		String Benutzername = spref.getString("USERNAME", "");
		
		//Werte in UI Elemente schreiben
		BenutzernameFeld.setText(Benutzername);	
		
		if (Benutzername != ""){
			findViewById(R.id.neuerBenutzer).setVisibility(View.GONE);
			findViewById(R.id.Pswdzuruecksetzen).setVisibility(View.VISIBLE);
		}
		else{
			findViewById(R.id.neuerBenutzer).setVisibility(View.VISIBLE);
			findViewById(R.id.Pswdzuruecksetzen).setVisibility(View.GONE);
		}
				
				
	}
	
	//Called when Anmelde Button clicked
	public void buttonPressed(View view){
		
		//Eingaben prüfen
		//Zugriff auf UI Element
		EditText BenutzernameFeld = (EditText) findViewById(R.id.Benutzername);
		EditText PasswortFeld = (EditText) findViewById(R.id.Passwort);
		
		//Werte der UI Elemente holen
		String Benutzername = BenutzernameFeld.getText().toString();
		
		//<<<<<<<<<<<<<<<<<<<Verschlüsselung hinzufuegen>>>>>>>>>>>>>>>>>>>>>>>>>>
		String Passwort = PasswortFeld.getText().toString();
		
		//Holen der Preferences
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		//Daten auslesen
		String BenutzernameMemory = spref.getString("USERNAME", "");
		String PasswortMemory = spref.getString("PASSWORD", "");
		
		
		//Daten vergleichen
		int resultUser = Benutzername.compareTo(BenutzernameMemory);
		int resultPswd = Passwort.compareTo(PasswortMemory);		
		
		
		//Wenn übereinstimmend	
		if(resultUser == 0 && resultPswd == 0) {
				
				Intent intent = new Intent(this, MainActivity.class);	
				startActivity(intent);
		}
		else{
			//Error Message aufrufen
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Benutzername oder Passwort falsch");
			alert.setTitle("Login fehlgeschlagen");
			alert.setNegativeButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();

			//Passworteingabe zurücksetzen
			PasswortFeld.setText("");
		}
	
	}
	
	public void BenutzerAnlegen(View view){
		
		//Zur Neuer Nutzer Activity wechseln
		Intent intent = new Intent(this, NeuerNutzerActivity.class);	
		startActivity(intent);
		
		
	}
	
	public void PasswortZuruecksetzen(View view){
		
	}
	

}
