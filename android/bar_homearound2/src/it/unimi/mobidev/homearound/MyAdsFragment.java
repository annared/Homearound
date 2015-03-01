package it.unimi.mobidev.homearound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MyAdsFragment extends Fragment{
	final String titleString = "Title";
    final String dateString = "Date";
    ArrayList<Object> userAds = new ArrayList<Object>();
    private  HashMap<String,String> map;
    private List<HashMap<String,String>> rowList;
	private ImageButton button;
	private SimpleAdapter simpleAdapter;
	private ImageView img;
	Globals g;
	private ListView listView;
	public static final String PREFS_NAME = "HomeAround";
	public MyAdsFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_myads, container, false);
        g = ((Globals)getActivity().getApplicationContext());
        img = (ImageView)rootView.findViewById(R.id.imageView);
        button = (ImageButton)rootView.findViewById(R.id.plus);
        listView = (ListView)rootView.findViewById(R.id.listView);
        rowList = new ArrayList<HashMap<String,String>>();
        map = null;
       
        button.setOnClickListener(new View.OnClickListener(){
	       	 @Override
	       	 public void onClick(View v) {
	       		 addAdd();
	       	 }
        });
       
        return rootView;
    }
	
    private void addAdd(){
    	FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = new AddAdFragment();
        ft.replace(R.id.fragmentmyads, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }
    
    private void populateList(){
		
        String[] from = { titleString, dateString };
        int[] to = { R.id.title, R.id.detail };
        userAds = g.getUserAds();
        
        if (userAds.size()<1){
            img.setVisibility(View.VISIBLE);
            int resID = getResources().getIdentifier(getString(R.string.addads), "drawable", getActivity().getPackageName());
            img.setImageResource(resID);
        }
        
        for (Object obj : userAds){
            map = new HashMap<String, String>();
            if (obj.getClass().equals(Ad_homeoffer.class)){
                Ad_homeoffer ad = (Ad_homeoffer)obj;
                map.put(titleString, ad.getOffer_home_title());
                map.put(dateString, ad.getOffer_home_date());
                rowList.add(map);
            }
            if (obj.getClass().equals(Ad_homesearch.class)){
                Ad_homesearch ad = (Ad_homesearch)obj;
                map.put(titleString, ad.getSearch_home_title());
                map.put(dateString, ad.getSearch_home_date());
                rowList.add(map);
            }
            if (obj.getClass().equals(Ad_roomoffer.class)){
                Ad_roomoffer ad = (Ad_roomoffer)obj;
                map.put(titleString, ad.getOffer_room_title());
                map.put(dateString, ad.getOffer_room_date());
                rowList.add(map);
            }
            if (obj.getClass().equals(Ad_roomsearch.class)){
                Ad_roomsearch ad = (Ad_roomsearch)obj;
                map.put(titleString, ad.getSearch_room_title());
                map.put(dateString, ad.getSearch_room_date());
                rowList.add(map);
            }
        }

        simpleAdapter = new SimpleAdapter(getActivity(), rowList, R.layout.list_row, from, to);
        
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
            	
            		 showDetails(position);
            	 
              
            }
        });
        
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long arg3) {
                     if (removeAds(position)){
                    	 g.getUserAds().remove(position);
                    	 rowList.remove(position);
                         simpleAdapter.notifyDataSetChanged();
                         simpleAdapter.notifyDataSetInvalidated();
                     }
            		
                     
                return false;
            }

        });
       
    }
    public boolean removeAds(int p){
    	String ad_id = new String();
    	String cat = new String();
    	if (g.getUserAds().get(p).getClass().equals(Ad_homeoffer.class)){
    		Ad_homeoffer obj = (Ad_homeoffer) g.getUserAds().get(p);
    		ad_id = obj.getOffer_home_id();
    		cat = "offer_home";
    	} else if (g.getUserAds().get(p).getClass().equals(Ad_roomoffer.class)){
    		Ad_roomoffer obj = (Ad_roomoffer) g.getUserAds().get(p);
    		ad_id = obj.getOffer_room_id();
    		cat = "offer_room";
    	} else if (g.getUserAds().get(p).getClass().equals(Ad_homesearch.class)){
    		Ad_homesearch obj = (Ad_homesearch) g.getUserAds().get(p);
    		ad_id = obj.getSearch_home_id();
    		cat = "search_home";
    	} else if (g.getUserAds().get(p).getClass().equals(Ad_roomsearch.class)){
    		Ad_roomsearch obj = (Ad_roomsearch) g.getUserAds().get(p);
    		ad_id = obj.getSearch_room_id();
    		cat = "search_room";
    	} 

    	SharedPreferences userData = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    	ArrayList<NameValuePair> data = new ArrayList<NameValuePair>(2);
        data.add(new BasicNameValuePair(getResources().getString(R.string.EMAIL_USER), userData.getString(getString(R.string.EMAIL_USER), null)));
        data.add(new BasicNameValuePair(getResources().getString(R.string.PSW_USER), userData.getString(getString(R.string.PSW_USER), null)));
        data.add(new BasicNameValuePair("ad_id", ad_id));
        data.add(new BasicNameValuePair("categoria", cat));
        String url = getResources().getString(R.string.HOST) + getResources().getString(R.string.DELETE_ADS);
        
        try {
            String res = new Parser().execute(new DataHolders(url, data)).get();
            JsonElement msg = new JsonParser().parse(res);
            JsonObject obj = msg.getAsJsonObject();

            if(!obj.get("stato").getAsBoolean()){
            	Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.EMPTY_REG_MSG), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            } else {
            	Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.delete), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    	return true;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
    }
    
    @Override
    public void onResume(){
    	super.onResume();  	
    	populateList();
    }
    
    private void showDetails(int index) {
    	
    	Fragment fragment = new DetailFragment();
    	FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
    	Bundle args = new Bundle();
    	args.putString("Cat", g.getUserAds().get(index).getClass().toString());
    	args.putInt("index", index);
    	args.putString("segue", "myads");
    	fragment.setArguments(args);
    	ft.replace(R.id.fragmentmyads, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

}
