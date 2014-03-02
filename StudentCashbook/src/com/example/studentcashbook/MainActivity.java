package com.example.studentcashbook;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
		Toast.makeText(this, "Übersicht", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void startAct(){
		
		setContentView(R.layout.activity_main);
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}}