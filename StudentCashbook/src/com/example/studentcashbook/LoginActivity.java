package com.example.studentcashbook;

import Crypt.CryptHelper;
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
		
		
		//Holen der Preferences
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		//Passwort auslesen & keine Entschluesselung noetig
		String Password = spref.getString("PASSWORD", "");
		
		
		//Wenn noch kein Passwort gesetzt worden ist, Umleitung zur Registrierung
		if (Password == ""){
			Intent intent = new Intent(this, NeuerNutzerActivity.class);	
			startActivity(intent);
		}
		//wenn die Registrierung bereits vollzogen ist
		else{
			setContentView(R.layout.activity_login);
		}
				
				
	}
	
	//Called when Anmelde Button clicked
	public void buttonPressed(View view){
		
		//Eingaben prüfen
		//Zugriff auf UI Element
		EditText PasswortFeld = (EditText) findViewById(R.id.Passwort);
		
		//Werte der UI Elemente holen
		//<<<<<<<<<<<<<<<<<<<Verschlüsselung hinzufuegen>>>>>>>>>>>>>>>>>>>>>>>>>>
		String PasswortInput = PasswortFeld.getText().toString();
		
		//Holen der Preferences
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		//Daten auslesen
		String PasswortMemory = spref.getString("PASSWORD", "");
		
		//verschlusseln der Eingabe 
		String PasswortInputEncrypt="";
		try{
			PasswortInputEncrypt = CryptHelper.encrypt("PASSWORD", PasswortInput);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		//Daten vergleichen
		int resultPswd = PasswortInputEncrypt.compareTo(PasswortMemory);		
		
		
		//Wenn Passwort uebereinstimmend	
		if(resultPswd == 0) {
				
				Intent intent = new Intent(this, MainActivity.class);	
				startActivity(intent);
		}
		else{
			//Error Message aufrufen
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Passwort falsch");
			alert.setTitle("Anmeldung fehlgeschlagen");
			alert.setNegativeButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();

			//Passworteingabe zurücksetzen
			PasswortFeld.setText("");
		}
	
	}
	
	
	public void PasswortZuruecksetzen(View view){
		
	}
	

}
