package studentcashbook.Activities;

import javax.crypto.KeyGenerator;

import android.inputmethodservice.Keyboard.Key;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.studentcashbook.R;

public class EinstellungenActivity extends PreferenceActivity {
    
	 final static String keyTippButton = "pref_key_tipps_button";
	 final static String keyTippAuto = "pref_key_tipps_auto";
	 final static String keySpracheChange = "pref_key_sprache_change";
	 final static String keyPswdChange = "pref_key_passwort_change";
	
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.activity_einstellungen);
    }
	

}
