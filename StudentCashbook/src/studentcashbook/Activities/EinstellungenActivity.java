package studentcashbook.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.example.studentcashbook.R;

public class EinstellungenActivity extends PreferenceActivity {
    
	 final static String keyTippAuto = "pref_key_tipps_auto";
	 final static String keySpracheChange = "pref_key_sprache_change";
	 final static String keyPswdChange = "pref_key_passwort_change";
	
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.activity_einstellungen);
    }
	
	
	
	//Check ob automatisch tipps angezeigt werden duerfen
	public static Boolean getKeyTippAuto(){
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
		Boolean syncConnPref = sharedPref.getBoolean(keyTippAuto, false);
		
		return syncConnPref;
	}

}
