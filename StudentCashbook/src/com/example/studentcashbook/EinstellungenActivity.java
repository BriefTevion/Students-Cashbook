package com.example.studentcashbook;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EinstellungenActivity extends MainActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void startAct(){
		
		setContentView(R.layout.activity_einstellungen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.einstellungen, menu);
		return true;
	}

}
