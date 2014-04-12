package studentcashbook.activities;

import java.text.DateFormat;
import java.util.Date;

import com.example.studentcashbook.R;

import DB.TransaktionenContract.transEntry;
import DB.TransaktionenDBHelper;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class NeueKategorieActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neue_kategorie);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.neue_kategorie, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
			alert.setNegativeButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
					dialog.cancel();
					
					changeToKategorien();
					
				}
			});

			alert.setCancelable(true);
			alert.create().show();
	
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
			
			db.close();
		}
		
		
	

		public void changeToKategorien(){
			Intent intent = new Intent(getApplicationContext(), KategorienActivity.class);	
			startActivity(intent);
			
		}
		
		
		public void abbrechen(View view){
			EditText name = (EditText) findViewById(R.id.editText_Name);
			EditText budget = (EditText) findViewById(R.id.editText_budget);
			
			name.setText("");
			budget.setText("");
			
			changeToKategorien();
			
			
		}

}
