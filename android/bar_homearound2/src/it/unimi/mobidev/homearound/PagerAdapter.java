package it.unimi.mobidev.homearound;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class PagerAdapter extends FragmentPagerAdapter {
 
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {

        switch (index) {
        case 0:
            return new BachecaFragment();
        case 1:
            return new GeobachecaFragment();
        case 2:
            return new ProfiloFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        return 3;
    }
    
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
