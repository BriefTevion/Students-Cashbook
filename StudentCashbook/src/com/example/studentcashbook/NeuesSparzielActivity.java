package com.example.studentcashbook;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class NeuesSparzielActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
	protected void onStart(){
		super.onStart();
		
		Toast.makeText(this, "Neues Sparziel", Toast.LENGTH_SHORT).show();
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

}
