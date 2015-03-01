package it.unimi.mobidev.homearound;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener{
	private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private ActionBar actionBar;
    private String[] tabs;
    private Globals g;
    public static final String PREFS_NAME = "HomeAround";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        g = ((Globals)getApplicationContext());
        tabs = getResources().getStringArray(R.array.items);
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        
        viewPager.setAdapter(pagerAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
       
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }
        
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
         
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
         
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
         
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	 MenuInflater inflater = getMenuInflater();
    	 inflater.inflate(R.menu.main, menu);
    	 return super.onCreateOptionsMenu(menu);
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.logout:
            logout();
            return true;
        default:
            break;
        }

        return false;
    }
    
	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
		
		
	}
	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
		
	}
	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		 FragmentManager manager = getSupportFragmentManager();
		 manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); 
	}
	
	@Override
	public void onBackPressed() {
	    FragmentManager manager = getSupportFragmentManager();
	    if (manager.getBackStackEntryCount() > 0) {
	        super.onBackPressed();
	    }
	}
	
	private void logout(){
		if (!g.getSave()){
			SharedPreferences shared = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
			shared.edit().clear().commit();
		}
		Intent i = new Intent(MainActivity.this, LoginActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		MainActivity.this.startActivity(i);
	}
	


}
