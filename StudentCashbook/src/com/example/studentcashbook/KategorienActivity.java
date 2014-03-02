package com.example.studentcashbook;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class KategorienActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void startAct(){
		
		setContentView(R.layout.activity_kategorien);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
		Toast.makeText(this, "Kategorien", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kategorien, menu);
		return true;
	}

}
