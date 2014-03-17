package com.example.studentcashbook;

import java.util.ArrayList;
import java.util.List;

import DB.TransaktionenContract.transEntry;
import DB.TransaktionenDBHelper;
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

public class KategorienActivity extends BaseActivity {

	itemListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		adapter = new itemListAdapter();
        
		ListView view = (ListView) findViewById(R.id.list);
        view.setAdapter(adapter);
		
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

	//Ausgewählter Eintrag loeschen
	private void deleteListItem(String name){
		//Zugang zur Datenbank
		TransaktionenDBHelper dbHelper = new TransaktionenDBHelper(getApplicationContext());	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		try{
		db.delete(transEntry.TABLE_NAME_Kategorie, transEntry.K_COLUMN_NAME_BEZEICHNER + "= '" + name +"'" , null);

		
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
	
	 
	 
	@Override
	public void startAct(){
		
		setContentView(R.layout.activity_kategorien);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kategorien, menu);
		return true;
	}
	
	//wenn action Button geklickt
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_add:
	            openNewKategorie();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	//Zur View wechseln um eine neue Kategorie anzulegen
	public void openNewKategorie(){
		Intent intent = new Intent(getApplicationContext(), NeueKategorieActivity.class);	
		startActivity(intent);
	}
	
	
	public class itemListData{
		String name;
		String rest;
		String budget;
	}
	
	public List<itemListData> getDataForListiew()
	{
		
		//Daten abfragen
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
		
		List<itemListData> list = new ArrayList <itemListData>();
		

			//Daten erhalten
			while(c.moveToNext()){
				String kName = c.getString(0);
				String kBudget;
				String kRest;

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

				
				//Daten in Liste uebergeben
				itemListData ild = new itemListData();
				
				ild.name = kName;
				ild.rest = kRest;
				ild.budget = kBudget;

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
				LayoutInflater inflater = (LayoutInflater) KategorienActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				arg1 = inflater.inflate(R.layout.listitem, arg2,false);
			}

			ImageView img = (ImageView) arg1.findViewById(R.id.imageView1);
			TextView name = (TextView)arg1.findViewById(R.id.textView1);
			TextView rest = (TextView)arg1.findViewById(R.id.textView2);

			itemListData data = list.get(arg0);
			
			if(Integer.parseInt(data.budget)==0){
				img.setImageResource(R.drawable.ic_action_star);
				rest.setText(data.rest + "€");
			}
			else if((Integer.parseInt(data.rest)/Integer.parseInt(data.budget))>0.3){
				img.setImageResource(R.drawable.ic_action_good);
				rest.setText(data.rest + "€ von " + data.budget + "€ übrig");
			}
			else{
				img.setImageResource(R.drawable.ic_action_bad);
				rest.setText(data.rest + "€ von " + data.budget + "€ übrig");
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
