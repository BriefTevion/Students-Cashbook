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

public class KategorienActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void startAct(){
		
		setContentView(R.layout.activity_kategorien);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
		Toast.makeText(this, "Kategorien", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kategorien, menu);
		return true;
	}
	
	//Button pressed, neue Kategorie hinzufuegen
	public void createNewKategorie(){
		String name = "";
		String budget = "";
		String restbetrag = "";
		String datum = DateFormat.getDateInstance().format(new Date());
		
		addKategorie(name, budget, restbetrag, datum);
	}
	
	//neue Transaktion der Tabelle TransaktionenList hinzufuegen
	public void addKategorie(String name, String budget, String restbetrag, String datum){
		//Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(getApplicationContext());	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(transEntry.K_COLUMN_NAME_BEZEICHNER, name);
		cv.put(transEntry.K_COLUMN_NAME_BUDGET, budget);
		cv.put(transEntry.K_COLUMN_NAME_RESTBETRAG, restbetrag);
		cv.put(transEntry.K_COLUMN_NAME_LAST_UPDATED, datum);
		
		long newRowID;
		newRowID = db.insert(transEntry.TABLE_NAME_Kategorie, null, cv);
	}

}
