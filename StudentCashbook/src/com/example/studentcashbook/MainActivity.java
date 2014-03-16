package com.example.studentcashbook;

import DB.TransaktionenDBHelper;
import DB.TransaktionenContract.transEntry;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
		
		
		//Element bekommen
		lastTrans = (TextView) findViewById(R.id.lastTransaktionen);
		
		//Abfrage als Text des Elementes setzen
		try{
		result = getLastThreeTransaktions();
		}
		catch(Exception e){
			e.printStackTrace();
			result = "DB Verbindungsproblem";
		}
		finally{
		lastTrans.setText(result);}
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
	
	//Abfrage der letzten drei Transaktionen
	public String getLastThreeTransaktions(){
		String result = "";
	try{
			//Zugang zur Datenbank
			TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(getApplicationContext());	
			SQLiteDatabase db = dbHelper.getReadableDatabase();

		String [] projection = {
				transEntry.COLUMN_NAME_TRANSAKTION_ID,
				transEntry.COLUMN_NAME_DATUM,
				transEntry.COLUMN_NAME_BETRAG,
				transEntry.COLUMN_NAME_KATEGORIE
		};
		
		String sortOrder = transEntry.COLUMN_NAME_TRANSAKTION_ID + " DESC";
		
		Cursor c = db.query(transEntry.TABLE_NAME, projection, null, null, null, null, sortOrder, "3");
		
		
		//solange cursor liest strings zu einem brauchbaren string zusammenfassen
		while(c.moveToNext()){
			String datum = c.getString(1);
			String betrag;
			String kategorie = c.getString(3);
			
			if(c.getString(2).matches("")){
				betrag = "0";
			}
			else{
				betrag = c.getString(2);
			}
			
			result = result + datum + "			" + kategorie + "			" + betrag + "€\n";
			
		}
		
		if(result == ""){
			result = "Noch keine Transkationen bisher";
		}
		
			db.close();
			
			return result;
			
		}
		catch(Exception e){
			e.printStackTrace();
			
			return "Error";
		}
	}

	

}