package com.example.studentcashbook;

import java.text.DateFormat;
import java.util.Date;

import DB.TransaktionenDBHelper;
import DB.TransaktionenContract.transEntry;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class MonatAusgabeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void startAct(){
		
		setContentView(R.layout.activity_monat_ausgabe);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
		Toast.makeText(this, "Monatl. Ausgaben", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.monat_ausgabe, menu);
		return true;
	}
	
	public void addMonatlicheAusgabe(){
		String name = "";
		String betrag = "";
		String datum = DateFormat.getDateInstance().format(new Date());
		putMonatlicheAusgabeInTable(name, betrag, datum);
		
	}
	
	//neue Transaktion der Tabelle TransaktionenList hinzufuegen
	public void putMonatlicheAusgabeInTable(String name, String betrag, String datum){
		//Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(getApplicationContext());	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(transEntry.MA_COLUMN_NAME_BEZEICHNER, name);
		cv.put(transEntry.MA_COLUMN_NAME_BETRAG, betrag);
		cv.put(transEntry.MA_COLUMN_NAME_DATUM, datum);

		
		long newRowID;
		newRowID = db.insert(transEntry.TABLE_NAME_AUTOMATIC_A, null, cv);
	}

}
