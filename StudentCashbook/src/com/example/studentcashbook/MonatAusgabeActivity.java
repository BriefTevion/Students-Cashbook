package com.example.studentcashbook;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class MonatAusgabeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void startAct(){
		
		setContentView(R.layout.activity_monat_ausgabe);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
		Toast.makeText(this, "Monatl. Ausgaben", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.monat_ausgabe, menu);
		return true;
	}

}
