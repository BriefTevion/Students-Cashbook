package com.example.studentcashbook;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;


public class EinnahmeActivity extends MainActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		//findViewById(R.id.Datum).setBackgroundColor(0xFF00FF00);
		//TextView FeldZeit = (TextView) findViewById(R.id.Uhrzeit);
		
		//Calendar c = Calendar.getInstance();
		//int AktuellesDatum = c.get(Calendar.DATE);
		//FeldDatum.setText("Heute");
		
		
		//Date AktuelleUhrzeit = c.getTime();
		//FeldZeit.setText("900");
		
		
		
	}
	
	@Override
	public void startAct(){
		
		setContentView(R.layout.activity_einnahme);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.einnahme, menu);
		return true;
	}

}
