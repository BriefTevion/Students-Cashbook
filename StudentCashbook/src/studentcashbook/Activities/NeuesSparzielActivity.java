package studentcashbook.Activities;

import DB.TransaktionenDBHelper;
import DB.TransaktionenContract.transEntry;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.studentcashbook.R;

public class NeuesSparzielActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//seekbar onChangeListener setzen
		SeekBar seekFeld = (SeekBar) findViewById(R.id.seekBar_Betrag);
		TextView sparbetragFeld = (TextView) findViewById(R.id.textView_Sparbetrag);
		
		
    	seekFeld.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
    		
			int progressChanged = 0;
		
    		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
            }

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				TextView sparbetragFeld = (TextView) findViewById(R.id.textView_Sparbetrag);
				sparbetragFeld.setText(progressChanged*5 + "€");
				
			}


        });
	}
	@Override
	protected void onStart(){
		super.onStart();
		
		//Radiobuttons checked und unchecked setzen
		RadioButton radioFest = (RadioButton) findViewById(R.id.radioButton_fest);
		RadioButton radioUebrig = (RadioButton) findViewById(R.id.radioButton_uebrig);

		radioFest.setChecked(false);
		radioUebrig.setChecked(true);
		
		//seekbar verstecken
		SeekBar seekFeld = (SeekBar) findViewById(R.id.seekBar_Betrag);
		TextView sparbetragFeld = (TextView) findViewById(R.id.textView_Sparbetrag);
		
		seekFeld.setVisibility(View.GONE);
    	sparbetragFeld.setVisibility(View.GONE);

	}
	
	@Override
	public void startAct(){
		
		setContentView(R.layout.activity_neues_sparziel);
	}

	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.neues_sparziel, menu);
		return true;
	}
	
	//Button abbrechen gedrueckt
	public void abbrechen(View view){
		EditText titelFeld = (EditText) findViewById(R.id.editText_Titel);
		EditText datumFeld = (EditText) findViewById(R.id.editText_datumEingabe);
		EditText betragFeld = (EditText) findViewById(R.id.editText_BetragInput);
		RadioButton uebrigFeld = (RadioButton) findViewById(R.id.radioButton_uebrig);
		RadioButton festFeld = (RadioButton) findViewById(R.id.radioButton_fest);
		SeekBar seekFeld = (SeekBar) findViewById(R.id.seekBar_Betrag);
		TextView sparbetragFeld = (TextView) findViewById(R.id.textView_Sparbetrag);
		
		titelFeld.setText("");
		datumFeld.setText("");
		betragFeld.setText("");
		uebrigFeld.setText("");
		festFeld.setText("");
		seekFeld.setProgress(0);
		sparbetragFeld.setText("");
		
		zurUebersicht();
	
	}
	
	//Button Anlegen gedrueckt
	public void zielAnlegen(View view){
		//Elemente bekommen
		EditText titelFeld = (EditText) findViewById(R.id.editText_Titel);
		EditText datumFeld = (EditText) findViewById(R.id.editText_datumEingabe);
		EditText betragFeld = (EditText) findViewById(R.id.editText_BetragInput);
		RadioButton uebrigFeld = (RadioButton) findViewById(R.id.radioButton_uebrig);
		RadioButton festFeld = (RadioButton) findViewById(R.id.radioButton_fest);
		SeekBar seekFeld = (SeekBar) findViewById(R.id.seekBar_Betrag);
		TextView sparbetragFeld = (TextView) findViewById(R.id.textView_Sparbetrag);
		
		try{
			
		//Daten in Tabelle speichern
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(getApplicationContext());	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(transEntry.T_COLUMN_NAME_BEZEICHNER, titelFeld.getText().toString());
		cv.put(transEntry.T_COLUMN_NAME_BETRAG, betragFeld.getText().toString());
		cv.put(transEntry.T_COLUMN_NAME_DATUM, datumFeld.getText().toString());
		cv.put(transEntry.T_COLUMN_NAME_SPARBETRAG, sparbetragFeld.getText().toString());
		cv.put(transEntry.T_COLUMN_NAME_GUTHABEN, "0");

		
		long newRowID;
		newRowID = db.insert(transEntry.TABLE_NAME_TARGET, null, cv);

			//Nachricht ueber erfolgreiches speichern
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Sparziel gepeichert");
			alert.setTitle("Erfolgreich");
			alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
					dialog.cancel();
					
					zurUebersicht();
					
				}
			});
			alert.setCancelable(true);
			alert.create().show();
			
			//Elemente zuruecksetzen
			titelFeld.setText("");
			datumFeld.setText("");
			betragFeld.setText("");
			uebrigFeld.setText("");
			festFeld.setText("");
			seekFeld.setProgress(0);
			sparbetragFeld.setText("");
			
				}
			
				catch(Exception e){
				e.printStackTrace();
				
				//Nachricht ueber NICHT erfolgreiches speichern
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setMessage("Fehler");
				alert.setTitle("Sparziel konnte nicht gespeichert werde.");
				alert.setNegativeButton("OK", null);
				alert.setCancelable(true);
				alert.create().show();
			}
	}
	
	//Zur Uebersicht View wechseln
	private void zurUebersicht() {
		Intent intent = new Intent(getApplicationContext(), SparzielActivity.class);	
		startActivity(intent);		
	}
	
	//Methode fuer die RadioButtons
	public void onRadioButtonClicked(View view) {
		SeekBar seekFeld = (SeekBar) findViewById(R.id.seekBar_Betrag);
		TextView sparbetragFeld = (TextView) findViewById(R.id.textView_Sparbetrag);
		RadioButton radioFest = (RadioButton) findViewById(R.id.radioButton_fest);
		RadioButton radioUebrig = (RadioButton) findViewById(R.id.radioButton_uebrig);
		
		// Ist der Button bereits gecheckt?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Checken welcher Button gecheckt
	    switch(view.getId()) {
	        case R.id.radioButton_uebrig:
	        	seekFeld.setVisibility(View.GONE);
            	sparbetragFeld.setVisibility(View.GONE);
            	radioUebrig.setChecked(false);

	            break;
	        case R.id.radioButton_fest:

	            	seekFeld.setVisibility(View.VISIBLE);
	            	sparbetragFeld.setVisibility(View.VISIBLE);	  
	            	sparbetragFeld.setText("0€");
	            	radioFest.setChecked(false);
	                
	            break;
	    }
	}

}
