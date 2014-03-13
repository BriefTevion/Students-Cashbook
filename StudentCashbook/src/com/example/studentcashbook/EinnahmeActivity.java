package com.example.studentcashbook;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import DB.TransaktionenDBHelper;
import DB.TransaktionenContract.transEntry;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class EinnahmeActivity extends BaseActivity {

	private Spinner kategorieSpin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Spinner laden
		kategorieSpin = (Spinner) findViewById(R.id.DropDown_Kategorien);
		List<String> list = new ArrayList<String>();
		//Daten aus der DB laden
		list.add("Test1");
		list.add("Test2");
		
		try{
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			kategorieSpin.setAdapter(dataAdapter);	
			
			kategorieSpin.setOnItemSelectedListener((OnItemSelectedListener) this);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
		
	
	@Override
	protected void onStart(){
		super.onStart();
		
		//Kleine Nachricht beim oeffnen der Activity
		Toast.makeText(this, "Neue Einnahme", Toast.LENGTH_SHORT).show();
		
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
		
		setContentView(R.layout.activity_einnahme);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.einnahme, menu);
		return true;
	}
	
	public void addEinnahme(){
		
		TextView datumFeld = (TextView) findViewById(R.id.textView_Datum);
		TextView zeitFeld = (TextView) findViewById(R.id.textView_Uhrzeit);
		EditText betragFeld = (EditText) findViewById(R.id.editText_Eingabe);
		EditText anmerkungFeld = (EditText) findViewById(R.id.editText_Anmerkungen);
		
		//Kategorie
		//ID

		try{
			addTransaktion(1, anmerkungFeld.getText().toString(), datumFeld.getText().toString(), zeitFeld.getText().toString(), 
					"Auto", betragFeld.getText().toString());
			
			//Nachricht ueber erfolgreiches speichern
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Einnahme gepeichert");
			alert.setTitle("Erfolgreich");
			alert.setNegativeButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();
		
		}
		catch(Exception e){
			
			e.printStackTrace();
			//Error Message aufrufen
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Nicht erfolgreich");
			alert.setTitle("Speicherung der Einnahme fehlgeschlagen");
			alert.setNegativeButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();
		}
		finally{
			//Intent intent = new Intent(this, MainActivity.class);	
			//startActivity(intent);
		}
	}
	
	//neue Transaktion der Tabelle TransaktionenList hinzufuegen
	public void addTransaktion(Integer id, String anmerkung, String datum, String zeit, String kategorie, String betrag){
		//Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(getApplicationContext());	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(transEntry.COLUMN_NAME_TRANSAKTION_ID, id);
		cv.put(transEntry.COLUMN_NAME_ANMEKRUNG, anmerkung);
		cv.put(transEntry.COLUMN_NAME_DATUM, datum);
		cv.put(transEntry.COLUMN_NAME_UHRZEIT, zeit);
		cv.put(transEntry.COLUMN_NAME_KATEGORIE, kategorie);
		cv.put(transEntry.COLUMN_NAME_BETRAG, betrag);
		
		long newRowID;
		newRowID = db.insert(transEntry.TABLE_NAME, null, cv);
	}

}
