package com.example.studentcashbook;

import java.util.ArrayList;
import java.util.List;

import DB.TransaktionenContract.transEntry;
import DB.TransaktionenDBHelper;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

	itemListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		adapter = new itemListAdapter();
        
		ListView view = (ListView) findViewById(R.id.list);
        view.setAdapter(adapter);
 
		
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
	
	}
	
	
	@Override
	public void startAct(){
		
		setContentView(R.layout.activity_main);
	}

	//wenn action button geklickt
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_nEinnahme:
	            openEinnahme();
	        case R.id.action_nAusgabe:
	        	openAusgabe();
	        default:
	            return super.onOptionsItemSelected(item);        
	    }
	    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//Wenn action button geklickt
	public void openEinnahme(){
		Intent intent = new Intent(getApplicationContext(), EinnahmeActivity.class);	
		startActivity(intent);
		
	}
	
	
	//Wenn action button geklickt
	public void openAusgabe(){
		Intent intent = new Intent(getApplicationContext(), AusgabeActivity.class);	
		startActivity(intent);
	}
	
	
	
	
	
	public class itemListData{
		String name;
		String datum;
		String betrag;
	}
	
	public List<itemListData> getDataForListiew()
	{
		
		//Daten abfragen
		//Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(getApplicationContext());	
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		String [] projection = {
				transEntry.COLUMN_NAME_TRANSAKTION_ID,
				transEntry.COLUMN_NAME_DATUM,
				transEntry.COLUMN_NAME_BETRAG,
				transEntry.COLUMN_NAME_KATEGORIE
		};
		
		String sortOrder = transEntry.COLUMN_NAME_TRANSAKTION_ID + " DESC";
		
		Cursor c = db.query(transEntry.TABLE_NAME, projection, null, null, null, null, sortOrder, "3");
		
		List<itemListData> list = new ArrayList <itemListData>();
		
			//Daten erhalten
			while(c.moveToNext()){
				String name = c.getString(3);
				String betrag;
				String datum = c.getString(1);
				
				itemListData ild = new itemListData();
				
				

					if(c.getString(2).matches("")){
						betrag = "0";
					}
					else{
						betrag = c.getString(2);
					}
							
						//Daten in Liste übergeben						
						ild.name = name;
						ild.datum = datum;
						ild.betrag = betrag;
				
					
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
				LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				arg1 = inflater.inflate(R.layout.listitemmain, arg2,false);
			}

			TextView name = (TextView)arg1.findViewById(R.id.textView1);
			TextView datum = (TextView)arg1.findViewById(R.id.textView2);
			TextView betrag = (TextView)arg1.findViewById(R.id.textView3);

			itemListData data = list.get(arg0);

			name.setText(data.name);
			datum.setText(data.datum);
			betrag.setText(data.betrag +"€");


			return arg1;
		}
		
		public itemListData getItems(int position)
		{
			return list.get(position);
		}
		
		
	}

	

}