package com.example.studentcashbook;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NeuesSparzielActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neues_sparziel);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.neues_sparziel, menu);
		return true;
	}

}
