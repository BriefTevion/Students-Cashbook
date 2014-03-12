package com.example.studentcashbook;

import java.text.DateFormat;
import java.util.Date;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class AusgabeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
	protected void onStart(){
		super.onStart();
		
		//kleine Nachricht beim oeffnen der Activity
		Toast.makeText(this, "Neue Ausgabe", Toast.LENGTH_SHORT).show();
		
		//Elemente erkennen
		TextView FeldZeit = (TextView) findViewById(R.id.textView_Uhrzeit);
		TextView FeldDatum = (TextView) findViewById(R.id.textView_Datum);
		
		//Datum erkennen und setzen
		String AktuellesDatum = DateFormat.getDateInstance().format(new Date());
		FeldDatum.setText(AktuellesDatum);
		
		//Uhrzeit erkennen und setzen
		String AktuelleUhrzeit = DateFormat.getTimeInstance().format(new Date());
		FeldZeit.setText(AktuelleUhrzeit);
	}
	
	@Override
	public void startAct(){
		
		setContentView(R.layout.activity_ausgabe);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ausgabe, menu);
		return true;
	}

}
