package com.example.studentcashbook;

import android.os.Bundle;
import android.view.Menu;

public class MonatEinnahmeActivity extends BaseActivity  {

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}
	
	@Override
	public void startAct(){
		
		setContentView(R.layout.activity_monat_einnahme);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.monat_einnahme, menu);
		return true;
	}
	

	
}
