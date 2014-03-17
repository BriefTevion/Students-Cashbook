package com.example.studentcashbook;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import DB.TransaktionenContract.transEntry;
import DB.TransaktionenDBHelper;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
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
		//Kategorien aus der DB laden
			//Zugang zur Datenbank
			TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(getApplicationContext());	
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			
			String [] projection = { transEntry.K_COLUMN_NAME_BEZEICHNER };
			String sortOrder = transEntry.K_COLUMN_NAME_BEZEICHNER;
			
			Cursor c = db.query(transEntry.TABLE_NAME_Kategorie,projection, null, null, null, null, sortOrder);
			
			//Jeden Wert in List hinzufuegen
			while(c.moveToNext()){
				list.add(c.getString(0));
			}
			
			//Ueberpruefen ob bereits Kategorien angelegt worden sind
	        //Dies muss der erste schritt sein
			//Wenn noch keine Kategorien vorhanden sind, dann umleiten
			if(list.isEmpty()){
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setMessage("Bitte lege zunächst deine Kategorien an");
				alert.setTitle("Hinweis");
				alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						Intent intent = new Intent(getApplicationContext(), NeueKategorieActivity.class);
						startActivity(intent);
						
					}
				});
				alert.setCancelable(true);
				alert.create().show();
	
			}
		
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
	
	public void addEinnahme(View view){
		
		TextView datumFeld = (TextView) findViewById(R.id.textView_Datum);
		TextView zeitFeld = (TextView) findViewById(R.id.textView_Uhrzeit);
		EditText betragFeld = (EditText) findViewById(R.id.editText_Eingabe);
		EditText anmerkungFeld = (EditText) findViewById(R.id.editText_Anmerkungen);
		Spinner kategorieSpin = (Spinner) findViewById(R.id.DropDown_Kategorien);

		Integer id = getNewID();

		try{
			addTransaktion(id, anmerkungFeld.getText().toString(), 
					datumFeld.getText().toString(), 
					zeitFeld.getText().toString(), 
					kategorieSpin.getSelectedItem().toString(), betragFeld.getText().toString());
			
			//Betrag der Kategorie gutschreiben
			addBetragToKategorie(datumFeld.getText().toString(), kategorieSpin.getSelectedItem().toString(), betragFeld.getText().toString());
	
			
			//Nachricht ueber erfolgreiches speichern
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Einnahme gepeichert");
			alert.setTitle("Erfolgreich");
			alert.setNegativeButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();
			
			//Elemente zuruecksetzen
			betragFeld.setText("");
			anmerkungFeld.setText("");
			kategorieSpin.setSelection(0);
			
				}
		
		catch(Exception e){
			e.printStackTrace();
			
			//Nachricht ueber NICHT erfolgreiches speichern
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Fehler");
			alert.setTitle("Einnahme konnte nicht gespeichert werde.");
			alert.setNegativeButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();
		}
			
		
		
		
	}
	
	//Kategorie den Betrag gutschreiben
	private void addBetragToKategorie(String datum, String kName,
			String betrag) {
		
		//Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(getApplicationContext());	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		//GET aktuellen restbetrag der Kategorie
		String [] projection = {
				transEntry.K_COLUMN_NAME_BEZEICHNER,
				transEntry.K_COLUMN_NAME_BUDGET,
				transEntry.K_COLUMN_NAME_RESTBETRAG
		};

		String sortOrder = transEntry.K_COLUMN_NAME_BEZEICHNER;

		try{ 
			
		Cursor c = db.query(transEntry.TABLE_NAME_Kategorie, projection, transEntry.K_COLUMN_NAME_BEZEICHNER 
				+ "= '" + kName +"'", null, null, null, sortOrder);
		
		c.moveToFirst();
		String aktuellerRest = c.getString(2);
		Integer neuerBetrag = Integer.parseInt(aktuellerRest) + Integer.parseInt(betrag);
		
		
		
			
			ContentValues cv = new ContentValues();
		      cv.put(transEntry.K_COLUMN_NAME_RESTBETRAG, String.valueOf(neuerBetrag));
		      cv.put(transEntry.K_COLUMN_NAME_LAST_UPDATED, datum);
		      
			db.update(transEntry.TABLE_NAME_Kategorie, cv, transEntry.K_COLUMN_NAME_BEZEICHNER 
					+ "= '" + kName +"'", null);
		
		
		
		}
		catch(Exception e){
			e.printStackTrace();
			//Nachricht ueber NICHT erfolgreiches speichern
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Fehler2");
			alert.setTitle("Einnahme konnte nicht gespeichert werde.");
			alert.setNegativeButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();
		}
		db.close();
		
	}


	//Wenn button pressed alle Eingaben zuruecksetzen und in MainActivity wechseln
	public void abbrechen(View view){
		//Elemente aufrufen
		EditText betragFeld = (EditText) findViewById(R.id.editText_Eingabe);
		EditText anmerkungFeld = (EditText) findViewById(R.id.editText_Anmerkungen);
		Spinner kategorieSpin = (Spinner) findViewById(R.id.DropDown_Kategorien);
		
		//Elemente zuruecksetzen
		betragFeld.setText("");
		anmerkungFeld.setText("");
		kategorieSpin.setSelection(0);
		
		//zu Main wechseln
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		
	}
	
	//neue Transaktion der Tabelle TransaktionenList hinzufuegen
	public void addTransaktion(Integer id, String anmerkung, String datum, String zeit, String kategorie, String betrag){
		//Zugang zur Datenbank
		try{
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
		
		db.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	//Neue tranaktionID ermitteln
	public Integer getNewID(){
		try{
			//Zugang zur Datenbank
			TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(getApplicationContext());	
			SQLiteDatabase db = dbHelper.getReadableDatabase();

			Integer id = 0;
			
			final String SQL_getMaxID = "Select Max(" + transEntry.COLUMN_NAME_TRANSAKTION_ID + ") AS id FROM " + 
										transEntry.TABLE_NAME;	
			
			Cursor c = db.rawQuery(SQL_getMaxID, null);
			
			
				c.moveToFirst();
				id = c.getInt(0);
			
				db.close();
				
				return id+1;
			
			
		}
		catch(Exception e){
			e.printStackTrace();
			
			return 1;
		}
	}
	

}
