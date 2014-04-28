/*
 * Diese Klasse beinhaltet alle Methoden um die Activity "Anlegen einer neuen einmaligen Ausgabe" zu bearbeiten.
 */
package uebersicht;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import weiteres.EinstellungenActivity;

import network.StartNetworkConnectAsync;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentcashbook.R;

import db.BudgetLoader;
import db.TransaktionenContract.transEntry;
import db.TransaktionenDBHelper;
import drawer.BaseActivity;

public class AusgabeActivity extends BaseActivity {

	private Spinner kategorieSpin;
	ProgressDialog progressDia;
	
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
			
			list.add("ohne Kategorie");
				
			
		
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
	
	public boolean addAusgabe(View view){
		final TextView datumFeld = (TextView) findViewById(R.id.textView_Datum);
		final TextView zeitFeld = (TextView) findViewById(R.id.textView_Uhrzeit);
		final EditText betragFeld = (EditText) findViewById(R.id.editText_Eingabe);
		final EditText anmerkungFeld = (EditText) findViewById(R.id.editText_Anmerkungen);
		final Spinner kategorieSpin = (Spinner) findViewById(R.id.DropDown_Kategorien);

		String kategorieTitel = kategorieSpin.getSelectedItem().toString();
		Integer betragPre = Integer.parseInt(betragFeld.getText().toString());
		boolean checkKategorie = BudgetLoader.checkIfKategorieEnoughMoney(getApplicationContext(), kategorieTitel, betragPre);
		Integer checkGesamt = BudgetLoader.getSumOfTransaktionen(getApplicationContext());
		final String betrag = "-" + betragPre;
		//neue Transaktion ID ermitteln
		Integer id = BudgetLoader.getNewTransaktionID(getApplicationContext());
		
		if(checkGesamt < betragPre){
			//Nachricht, da Gesamtbudget nicht ausreicht
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Gesamtbudget nicht ausreichend");
			alert.setNegativeButton("Trotzdem fortfahren", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					addAusgabeToTransaktion(id, anmerkungFeld.getText().toString(), 
							datumFeld.getText().toString(), 
							zeitFeld.getText().toString(), 
							kategorieSpin.getSelectedItem().toString(), betrag);
				}});
				
			alert.setTitle("Achtung!");
			alert.setPositiveButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();
		}
		else if(checkKategorie==false){
			//Nachricht, da Restbudget nicht ausreicht
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Restbudget der Kategorie "+ kategorieTitel + " nicht ausreichend.");
			alert.setNegativeButton("Trotzdem fortfahren", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					addAusgabeToTransaktion(id, anmerkungFeld.getText().toString(), 
							datumFeld.getText().toString(), 
							zeitFeld.getText().toString(), 
							kategorieSpin.getSelectedItem().toString(), betrag);
				}});
				
			alert.setTitle("Achtung!");
			alert.setPositiveButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();
		}	
		else{

			
	
			try{
				//neue Transaktion in DB schreiben
				addAusgabeToTransaktion(id, anmerkungFeld.getText().toString(), 
				datumFeld.getText().toString(), 
				zeitFeld.getText().toString(), 
				kategorieSpin.getSelectedItem().toString(), betrag);
				
	
				return true;
				
			}
			
			catch(Exception e){
				e.printStackTrace();
				
				//Nachricht ueber NICHT erfolgreiches speichern
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setMessage("Ausgabe konnte nicht gespeichert werde.");
				alert.setTitle("Fehler");
				alert.setNegativeButton("OK", null);
				alert.setCancelable(true);
				alert.create().show();
				
				return false;
			}
		}	
		
		return true;
		
	}
	
	private void addAusgabeToTransaktion(Integer id, String anmerkung, String datum, String zeit, String kategorie, String betrag){
		//Einmalige Ausgabe in DB-Tabelle schreiben
		BudgetLoader.addEinmaligeTransaktion(getApplicationContext(), 
				id, 
				anmerkung, 
				datum, 
				zeit, 
				kategorie, 
				betrag);
		
		//wenn kategorie 'ohne kategorie' nicht ausgewaehlt worden ist
		if(kategorieSpin.getSelectedItem().toString()!="ohne Kategorie"){
			BudgetLoader.addEinmaligeTransaktionToKategorie(getApplicationContext(),
					datum, 
					kategorie, 
					betrag);
		}
		
		//Nachricht ueber erfolgreiches speichern
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("Ausgabe gepeichert");
		alert.setTitle("Erfolgreich");
		alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				
				finish();
				
				//zu Main wechseln
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);	
				
				//Tipp anzeigen
				//Zunaechst pruefen, ob Einstellungen es zulassen
				
				if(EinstellungenActivity.getKeyTippAuto()==true){
					try{
						MainActivity.openNewTipp();
					}
					catch(Exception e){
						Log.v("test", e.getMessage());
					}
				}
				
			}
		});
		alert.setCancelable(true);
		alert.create().show();
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
		

}
