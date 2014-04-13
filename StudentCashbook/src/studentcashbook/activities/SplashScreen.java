/*
 * Diese Klasse laedt den ersten Screen(Splash Screen) der App fuer drei Sekunden.
 */
package studentcashbook.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.example.studentcashbook.R;

public class SplashScreen extends Activity {

	//Timer fuer den Splash Screen
	private static int SPLASH_TIME_OUT = 3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		new Handler().postDelayed(new Runnable() {
			 
            /*
             * Showing splash screen with a timer, to show the supporters.
             */
 
            @Override
            public void run() {
                
            	
                // Start LoginActivity
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
 
                // Splash schliessen
                finish();
            }
        }, SPLASH_TIME_OUT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

}
