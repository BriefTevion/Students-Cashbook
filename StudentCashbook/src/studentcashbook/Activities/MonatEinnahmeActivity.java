package studentcashbook.Activities;

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

public class MonatEinnahmeActivity extends Activity  {

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monat_einnahme);
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
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.monat_einnahme, menu);
		return true;
	}
	
	public void addMonatlicheEinnahme(View view){
		EditText name = (EditText) findViewById(R.id.editText_Name);
		EditText betrag = (EditText) findViewById(R.id.editText_betrag);
		EditText tag = (EditText) findViewById(R.id.editText_Tag);
		
		
		String datum = DateFormat.getDateInstance().format(new Date());
		try{
		putMonatlicheEinnahmeInTable(name.getText().toString(), betrag.getText().toString(), datum, tag.getText().toString());
		
		//Nachricht ueber erfolgreiches speichern
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("Gesch??ft angelegt");
		alert.setTitle("Erfolgreich");
		alert.setNegativeButton("OK",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				
				dialog.cancel();
				
				changeToMonatlichUebersicht();
				
			}
		});

		alert.setCancelable(true);
		alert.create().show();

		
		}
		catch(Exception e){
			
		}
	}
	
	//neue Transaktion der Tabelle TransaktionenList hinzufuegen
	public void putMonatlicheEinnahmeInTable(String name, String betrag, String datum, String tag){
		//Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(getApplicationContext());	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(transEntry.M_COLUMN_NAME_BEZEICHNER, name);
		cv.put(transEntry.M_COLUMN_NAME_BETRAG, betrag);
		cv.put(transEntry.M_COLUMN_NAME_DATUM, datum);
		cv.put(transEntry.M_COLUMN_NAME_TAG, tag);

		
		long newRowID;
		newRowID = db.insert(transEntry.TABLE_NAME_AUTOMATIC, null, cv);
	}
	
	
	
	public void changeToMonatlichUebersicht(){
		Intent intent = new Intent(getApplicationContext(), MonatlichesActivity.class);	
		startActivity(intent);
		
	}
	
	
	public void abbrechen(View view){
		EditText name = (EditText) findViewById(R.id.editText_Name);
		EditText budget = (EditText) findViewById(R.id.editText_betrag);
		
		name.setText("");
		budget.setText("");
		
		changeToMonatlichUebersicht();
		
		
	}

	
}
