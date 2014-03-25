package Charts;

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
			return new ChartPie();
		case 1:
			//Fragment for Ios Tab
			return new ChartBar();
		case 2:
			//Fragment for Windows Tab
			return new ChartMisc();
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
        	ret="Pie Chart";
        }else if(position==1){
        	ret="Bar Chart";
        }else if(position==2){
        	ret="Sonstiges";
        }else{
        	
        }
        		
		return ret;
    }
}

