/*
 * Diese Klasse laedt alle bereits erstellten monatlichen Transaktionen.
 * Außerdem beinhaltet sie alle Methoden zur Bearbeitung dieser Liste.
 */
package monatliches;

import java.util.ArrayList;
import java.util.List;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.studentcashbook.R;

import db.TransaktionenContract.transEntry;
import db.TransaktionenDBHelper;
import drawer.BaseActivity;

public class MonatlichesActivity extends BaseActivity {

itemListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		adapter = new itemListAdapter();
        
		ListView view = (ListView) findViewById(R.id.listMonatliches);
        view.setAdapter(adapter);
        
		}
		catch(Exception e){
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Fehler beim Laden der Daten");
			alert.setTitle("Fehlgeschlagen");
			alert.setNegativeButton("OK",null);
			alert.setCancelable(true);
			alert.create().show();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.monatliches, menu);
		return true;
	}
	
	//wenn action button geklickt
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		    // Handle presses on the action bar items

		    switch (item.getItemId()) {
		    case R.id.action_nMonatlicheEinnahme:
	            openNeuMonEinnahme();
	            return true;
		    case R.id.action_nMonatlicheAusgabe:
	            openNeuMonAusgabe();
	            return true;
		        default:
		            return super.onOptionsItemSelected(item);        

		}
		 }

	private void openNeuMonAusgabe() {
		Intent intent = new Intent(getApplicationContext(), MonatAusgabeActivity.class);	
		startActivity(intent);
			
		}

	private void openNeuMonEinnahme() {
		Intent intent = new Intent(getApplicationContext(), MonatEinnahmeActivity.class);	
		startActivity(intent);
		
	}
	
	@Override
	public void startAct(){
		setContentView(R.layout.activity_monatliches);
	}

	@Override
	protected void onStart(){
		super.onStart();
		
	}
	
	 
	
	
	
	//Wenn DELETE Button geklickt
		public void onButtonClick(View view){
			
			RelativeLayout rparent = (RelativeLayout) view.getParent();
			
			final TextView name = (TextView) rparent.getChildAt(1);
			
			//Nachfragen ob item sicher geloescht werden moechte
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Möchtest du diese Kategorie löschen?");
			alert.setTitle("Löschen bestätigen");
			alert.setNegativeButton("Nicht Löschen",null);
			alert.setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
					deleteListItem(name.getText().toString());
					
					finish();
					startActivity(getIntent());

					
				}
			});
			alert.setCancelable(true);
			alert.create().show();
			

			
		}

		//Ausgewaehlter Eintrag loeschen
		private void deleteListItem(String name){
			//Zugang zur Datenbank
			TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(getApplicationContext());	
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			
			try{
				db.delete(transEntry.TABLE_NAME_AUTOMATIC, transEntry.M_COLUMN_NAME_BEZEICHNER + "= '" + name +"'" , null);
			}
			catch(Exception e){
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setMessage("Eintrag konnte nicht gelöscht werden");
				alert.setTitle("Fehlgeschlagen");
				alert.setNegativeButton(name,null);
				alert.setCancelable(true);
				alert.create().show();
			}
			
			db.close();
			
		}
	

	
	public class itemListData{
		String name;
		String zumTag;
		String betrag;
	}
	
	public List<itemListData> getDataForListiew()
	{
		
		//Daten abfragen
		//Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(getApplicationContext());	
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		String [] projection = {
				transEntry.M_COLUMN_NAME_BEZEICHNER,
				transEntry.M_COLUMN_NAME_BETRAG,
				transEntry.M_COLUMN_NAME_DATUM,
				transEntry.M_COLUMN_NAME_TAG
		};

		String sortOrder = transEntry.M_COLUMN_NAME_BEZEICHNER;

		Cursor c = db.query(transEntry.TABLE_NAME_AUTOMATIC, projection, null, null, null, null, sortOrder);
		
		List<itemListData> list = new ArrayList <itemListData>();
		

			//Daten erhalten
			while(c.moveToNext()){
				
				String mName;
				String mBudget;
				String mTag;
				
				if(c.getString(0)==""|| c.getString(1)==""||c.getString(3)==""){
					 mName = "0";
					 mBudget = "0";
					 mTag = "0";
				}
				else{
					 mName = c.getString(0);
					 mBudget = c.getString(1);
					 mTag = c.getString(3);
				}
				
				

				//Daten in Liste uebergeben
				itemListData ild = new itemListData();
				
				ild.name = mName;
				ild.zumTag = mTag;
				ild.betrag = mBudget;

				list.add(ild);
					
					
			}	
			
		
					db.close();
					return list;
					
	}
	
	//benutzerdefinierter Adapter
	public class itemListAdapter extends BaseAdapter{
		List<itemListData> list =  getDataForListiew();
		
		@Override
		public int getCount() {
			return list.size();

		}
		
		@Override
		public itemListData getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}
		
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
		
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {

			if(arg1==null)
			{
				LayoutInflater inflater = (LayoutInflater) MonatlichesActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				arg1 = inflater.inflate(R.layout.listitemmonatlich, arg2,false);
			}

			ImageView img = (ImageView) arg1.findViewById(R.id.imageView1);
			TextView name = (TextView)arg1.findViewById(R.id.textView1);
			TextView datum = (TextView)arg1.findViewById(R.id.textView2);

			itemListData data = list.get(arg0);
			
			
			
			if(Integer.parseInt(data.betrag)>=0){
				img.setImageResource(R.drawable.ic_action_star);
				datum.setText("Zubuchen von " + data.betrag + "€ zum: " + data.zumTag + ". des Monats");
			}
			else{
				img.setImageResource(R.drawable.ic_action_not_important);
				datum.setText("Abbuchen von " + data.betrag + "€ zum: " + data.zumTag  + ". des Monats");
			}

			
			name.setText(data.name);

			

			return arg1;
		}
		
		public itemListData getItems(int position)
		{
			return list.get(position);
		}
		
		
	}

}
