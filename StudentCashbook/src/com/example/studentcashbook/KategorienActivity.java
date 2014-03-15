package com.example.studentcashbook;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import DB.TransaktionenContract.transEntry;
import DB.TransaktionenDBHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class KategorienActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//addKategorie("test", "12", "2", "20.03.2014");
		//addKategorie("test2", "12", "2", "20.03.2014");
		//addKategorie("test3", "12", "2", "20.03.2014");
		
		
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
		
		LinearLayout lLayout = (LinearLayout) findViewById(R.id.lLayout);
		lLayout.setOrientation(LinearLayout.VERTICAL);
		
		ListView listV = (ListView) findViewById(R.id.list);
		final ArrayList<String> list = new ArrayList<String>();
		 
		while(c.moveToNext()){
			String kName = c.getString(0);
			String kBudget = c.getString(1);
			String kRest = c.getString(2);
			
			Integer progress = Integer.parseInt(kRest) / Integer.parseInt(kBudget);
			
			list.add("Kategorie: " + kName + "\nVerbraucht: " + progress + "\nRestbetrag: " + kRest);
			
			ArrayAdapter adapter = new ArrayAdapter(this,
				        android.R.layout.simple_list_item_1, list);
			
			listV.setAdapter(adapter);
		}
		
		db.close();

	}

}
