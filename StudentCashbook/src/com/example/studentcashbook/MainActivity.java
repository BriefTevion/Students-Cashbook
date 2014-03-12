package com.example.studentcashbook;

import DB.TransaktionenDBHelper;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	TextView lastTrans;
	TransaktionenDBHelper dbHelper;
	String result = "NO DATA";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
		Toast.makeText(this, "Übersicht", Toast.LENGTH_SHORT).show();
		
		//Get die letzten drei Transaktionen
		dbHelper = new TransaktionenDBHelper(getApplicationContext());
		//Element bekommen
		lastTrans = (TextView) findViewById(R.id.lastTransaktionen);
		//Abfrage als Text des Elementes setzen
		try{
		result = dbHelper.getLastThreeTransaktions();
		}
		catch(Exception e){
			e.printStackTrace();
			result = "DB Verbindungsproblem";
		}
		
		lastTrans.setText(result);
	}
	@Override
	public void startAct(){
		
		setContentView(R.layout.activity_main);
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void openEinnahme(View view){
		Intent intent = new Intent(this, EinnahmeActivity.class);	
		startActivity(intent);
	}
	
	public void openAusgabe(View view){
		Intent intent = new Intent(this, AusgabeActivity.class);	
		startActivity(intent);
	}
	
	

}