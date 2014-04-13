/*
 * Diese Klasse laed die Diagramme, um sie den jeweiligen Fragmenten in der MainActivity zu zuweisen.
 */
package charts;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
	
	public TabPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
		}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
		case 0:
			//Fragement for Android Tab
			return new ChartEinAusgaben();
		case 1:
			//Fragment for Ios Tab
			return new ChartMonatliches();
		case 2:
			//Fragment for Windows Tab
			return new ChartRestbudgets();
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}
	
	@Override
    public CharSequence getPageTitle(int position) {
       String ret = "";
		
		if(position==0){
        	ret="Einmalig diesen Monat";
        }else if(position==1){
        	ret="Monatliches";
        }else if(position==2){
        	ret="Restbudgets";
        }else{
        	
        }
        		
		return ret;
    }
}

