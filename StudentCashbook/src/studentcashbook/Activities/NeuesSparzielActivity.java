package studentcashbook.Activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateMidnight;
import org.joda.time.Months;

import DB.TransaktionenContract.transEntry;
import DB.TransaktionenDBHelper;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
		String sparBetrag="0";
		//Elemente bekommen
		EditText titelFeld = (EditText) findViewById(R.id.editText_Titel);
		EditText datumFeld = (EditText) findViewById(R.id.editText_datumEingabe);
		EditText betragFeld = (EditText) findViewById(R.id.editText_BetragInput);
		RadioButton uebrigFeld = (RadioButton) findViewById(R.id.radioButton_uebrig);
		RadioButton festFeld = (RadioButton) findViewById(R.id.radioButton_fest);
		SeekBar seekFeld = (SeekBar) findViewById(R.id.seekBar_Betrag);
		TextView sparbetragFeld = (TextView) findViewById(R.id.textView_Sparbetrag);
		

		//Ueberpruefung ob alle Felder ausgefuelt
		if(titelFeld.getText().toString().isEmpty() || datumFeld.getText().toString().isEmpty() || betragFeld.getText().toString().isEmpty()){

			//Nachricht ueber leeres Feld
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Achtung");
			alert.setMessage("Bitte alle Felder ausfüllen.");
			alert.setNegativeButton("OK", null);
			alert.setCancelable(true);
			alert.create().show();
		}
		
		else{
			
			//Pruefen, ob RadioButton Uebrig gewaehlt
			if(uebrigFeld.isChecked()){
				sparBetrag = "ueber";
			}
			else{
				sparBetrag=String.valueOf(seekFeld.getProgress()*5);
			}

			
			//Ueberpruefung, ob Rate fuer das Zieldatum reicht
				//Aktuelles Datum
				DateMidnight aktuellesDatum = new DateMidnight(new Date());
				Log.v("test", "Aktuell " + aktuellesDatum);
				DateMidnight zielDatum = null;
				//Zieldatum in DATE umwandeln;
				SimpleDateFormat sm = new SimpleDateFormat("dd.mm.yyy");
				SimpleDateFormat out = new SimpleDateFormat("yyyy-mm-dd");
				Date aD = null;
				String formatedDate=null;
				
				try {
					aD = (Date) sm.parse(datumFeld.getText().toString());
					formatedDate = out.format(aD);

				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try{
				 zielDatum =  new DateMidnight(formatedDate + "T00:00:00.000+01:00");
				}
				catch(Exception e){
					Log.v("test", e.getMessage());
				}
				Log.v("test", "1d");
				//Monate zwischen den Daten bekommen
				int diffMonths = Months.monthsBetween(aktuellesDatum, zielDatum).getMonths();
				
				Log.v("test", "2  " + diffMonths);
				
			//wenn zu der monatliche Sparbetrag mal der Laufzeit
			//kleiner ist als der Zielbetrag, muss eine Warnung angezeigt werden
			if(diffMonths*Integer.parseInt(sparBetrag)<Integer.parseInt(betragFeld.getText().toString())){
				
				int mindSparbetrag = Integer.parseInt(betragFeld.getText().toString())/diffMonths;
				
				//Nachricht
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Achtung");
				alert.setMessage("Monatlicher Sparbetrag nicht ausreichend.\nMindestens: " + mindSparbetrag);
				alert.setNegativeButton("OK", null);
				alert.setCancelable(true);
				alert.create().show();
				
				//Setzen der SeekBar auf Minimum
					seekFeld.setProgress(mindSparbetrag/5);
			}
			
			else{

					//Daten in Tabelle speichern
					try{
		
					TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(getApplicationContext());	
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					
					ContentValues cv = new ContentValues();
					cv.put(transEntry.T_COLUMN_NAME_BEZEICHNER, titelFeld.getText().toString());
					cv.put(transEntry.T_COLUMN_NAME_BETRAG, betragFeld.getText().toString());
					cv.put(transEntry.T_COLUMN_NAME_DATUM, datumFeld.getText().toString());
					cv.put(transEntry.T_COLUMN_NAME_SPARBETRAG, sparBetrag);
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
							alert.setTitle("Fehler");
							alert.setMessage("Sparziel konnte nicht gespeichert werde.");
							alert.setNegativeButton("OK", null);
							alert.setCancelable(true);
							alert.create().show();
						}
			}
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
		

	    
	    // Checken welcher Button gecheckt
	    switch(view.getId()) {
	        case R.id.radioButton_uebrig:
	        	seekFeld.setVisibility(View.GONE);
            	sparbetragFeld.setVisibility(View.GONE);
            	radioUebrig.setChecked(true);
            	radioFest.setChecked(false);

	            break;
	        case R.id.radioButton_fest:

	            	seekFeld.setVisibility(View.VISIBLE);
	            	sparbetragFeld.setVisibility(View.VISIBLE);	  
	            	sparbetragFeld.setText("0€");
	            	radioFest.setChecked(true);
	            	radioUebrig.setChecked(false);
	                
	            break;
	    }
	}

}
