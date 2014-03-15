package com.example.studentcashbook;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import DB.TransaktionenContract.transEntry;
import DB.TransaktionenDBHelper;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
		
		getKategorien();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kategorien, menu);
		return true;
	}
	
	
	
	//Button pressed, neue Kategorie hinzufuegen
	public void createNewKategorie(View view){
		EditText name = (EditText) findViewById(R.id.editText_Name);
		EditText budget = (EditText) findViewById(R.id.editText_budget);
		
		String kName= name.getText().toString();
		String kBudget;
		
		if (budget.getText().toString().matches("")){
			kBudget = "0";
		}
		else{
			kBudget = budget.getText().toString();
			
		}
		
		String restbetrag = kBudget;

		String datum = DateFormat.getDateInstance().format(new Date());
		

		addKategorie(kName, kBudget, restbetrag, datum);
		
		//Nachricht ueber erfolgreiches speichern
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("Kategorie angelegt");
		alert.setTitle("Erfolgreich");
		alert.setNegativeButton("OK", null);
		alert.setCancelable(true);
		alert.create().show();
		
		
		getKategorien();
		
		name.setText("");
		budget.setText("");
		
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
	
	
	//alle Kategorien aus DB laden
	public void getKategorien(){
		
		//Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(getApplicationContext());	
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		String [] projection = {
				transEntry.K_COLUMN_NAME_BEZEICHNER,
				transEntry.K_COLUMN_NAME_BUDGET,
				transEntry.K_COLUMN_NAME_RESTBETRAG
		};
		
		String sortOrder = transEntry.K_COLUMN_NAME_BEZEICHNER;
		
		Cursor c = db.query(transEntry.TABLE_NAME_Kategorie, projection, null, null, null, null, sortOrder);
		

		
		ListView listV = (ListView) findViewById(R.id.list);
		final ArrayList<String> list = new ArrayList<String>();
		 
		while(c.moveToNext()){
			String kName = c.getString(0);
			String kBudget;
			String kRest;
			Integer progress;
			
			if(c.getString(1).matches("")){
				kBudget = "0";
			}
			else{
				kBudget = c.getString(1);
			}
			
			
			if(c.getString(2).matches("")){
				kRest = "0";
			}
			else{
				kRest = c.getString(2);
			}
			
			
			if(Integer.parseInt(kRest) > 0 && Integer.parseInt(kBudget) > 0){
				progress = 100-((Integer.parseInt(kRest) / Integer.parseInt(kBudget))*100);
			}
			else{
				progress = 0;
			}
			
			
			
			list.add("Kategorie: " + kName + "\nVerbaucht: " + progress + "% \nRestbetrag: " + kRest);
			
			ArrayAdapter adapter = new ArrayAdapter(this,
				        android.R.layout.simple_list_item_1, list);
			
			listV.setAdapter(adapter);
		}
		
		db.close();

	}

}
