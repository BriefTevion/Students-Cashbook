package com.example.studentcashbook;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

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
		EditText BenutzernameFeld = (EditText) findViewById(R.id.neuerBenutzernameEingabe);
		EditText PasswortFeld = (EditText) findViewById(R.id.neuesPasswortEingabe);
		
		//Werte der UI Elemente holen
		String Benutzername = BenutzernameFeld.getText().toString();
		
		//<<<<<<<<<<<<<<<<<<<Verschlüsselung hinzufuegen>>>>>>>>>>>>>>>>>>>>>>>>>>
		String Passwort = PasswortFeld.getText().toString();
		
		//Holen der Preferences
		SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = spref.edit();
		
		//Werte speichern
		editor.putString("USERNAME", Benutzername);
		editor.putString("PASSWORD", Passwort);
		editor.commit();
		
		//UI-Elemente zurücksetzen
		BenutzernameFeld.setText("");
		PasswortFeld.setText("");
		
		//Zur Mainactivity wechseln
		Intent intent = new Intent(this, MainActivity.class);	
		startActivity(intent);
		
		
	}
}
