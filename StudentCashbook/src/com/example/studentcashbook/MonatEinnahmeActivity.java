package com.example.studentcashbook;

import java.text.DateFormat;
import java.util.Date;

import DB.TransaktionenDBHelper;
import DB.TransaktionenContract.transEntry;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class MonatEinnahmeActivity extends BaseActivity  {

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}
	@Override
	protected void onStart(){
		super.onStart();
		
		Toast.makeText(this, "Monatl. Einnahmen", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void startAct(){
		
		setContentView(R.layout.activity_monat_einnahme);
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.monat_einnahme, menu);
		return true;
	}
	
	public void addMonatlicheEinnahme(){
		String name = "";
		String betrag = "";
		String datum = DateFormat.getDateInstance().format(new Date());
		putMonatlicheEinnahmeInTable(name, betrag, datum);
		
	}
	
	//neue Transaktion der Tabelle TransaktionenList hinzufuegen
	public void putMonatlicheEinnahmeInTable(String name, String betrag, String datum){
		//Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(getApplicationContext());	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(transEntry.M_COLUMN_NAME_BEZEICHNER, name);
		cv.put(transEntry.M_COLUMN_NAME_BETRAG, betrag);
		cv.put(transEntry.M_COLUMN_NAME_DATUM, datum);

		
		long newRowID;
		newRowID = db.insert(transEntry.TABLE_NAME_AUTOMATIC, null, cv);
	}
	

	
}
