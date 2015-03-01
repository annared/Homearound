package it.unimi.mobidev.homearound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class MyResFragment extends Fragment {
	final String titleString = "Title";
	final String subtitleString = "Date";
	ArrayList<Research> userRes = new ArrayList<Research>();
	private ListView listView;
	public static final String PREFS_NAME = "HomeAround";
	private SimpleAdapter simpleAdapter;
	private List<HashMap<String,String>> rowList = new ArrayList<HashMap<String,String>>();
	private HashMap<String,String> map = new HashMap<String,String>();
	Globals g;
	private int count;
	private ImageButton button;
	private ImageView img;
	boolean alreadyloaded = false;
	public MyResFragment(){
			
	}
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_myres, container, false);
        listView = (ListView)rootView.findViewById(R.id.listView);
        img = (ImageView)rootView.findViewById(R.id.imageView);
        g = ((Globals)getActivity().getApplicationContext());
        button = (ImageButton)rootView.findViewById(R.id.plus);
       

        rowList = new ArrayList<HashMap<String,String>>();
        count = g.getUserRes().size();
 		
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                downloadAds(position);
            }
        });
        
        if (g.getUserRes().size()<1){
            int resID = getResources().getIdentifier(getString(R.string.addres), "drawable", getActivity().getPackageName());
            img.setImageResource(resID);
            img.setVisibility(View.VISIBLE);
        }
        button.setOnClickListener(new View.OnClickListener(){
	       	 @Override
	       	 public void onClick(View v) {
	       		 addRes();
	       	 }
       });
        
        /*getFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                    	if(g.getUserRes().size()>count){
               
                    		rowList = new ArrayList<HashMap<String,String>>();
                    		populateList();
                    	}
                    }
                });*/
        return rootView;
    }
    
	private void populateList(){
		
        userRes = g.getUserRes();
        if (userRes.size()==0) img.setVisibility(View.VISIBLE);
        else img.setVisibility(View.GONE);
        for (Research r : userRes){
            Research res = (Research)r;
            map = new HashMap<String, String>();
            String sub = "";
            map.put(titleString, res.getResearch_cat());
            if (res.getResearch_city().length()>0){
                sub = res.getResearch_city();
            }
            if (res.getResearch_area().length()>0){
                sub = sub + " " + res.getResearch_area();
            }
            if (res.getResearch_price().length()>0){
                sub = sub + " " + res.getResearch_price();
            }
            map.put(subtitleString, sub);
            rowList.add(map);
        }
        
    	String[] from = { titleString, subtitleString };
        int[] to = { R.id.title, R.id.detail };
        simpleAdapter = new SimpleAdapter(getActivity(), rowList, R.layout.list_row, from, to);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long arg3) {
                     if (removeAds(position)){
                    	 g.getUserRes().remove(position);
                    	 rowList.remove(position);
                    	 if (g.getUserRes().size()==0) img.setVisibility(View.VISIBLE);
                         simpleAdapter.notifyDataSetChanged();
                         simpleAdapter.notifyDataSetInvalidated();
                        
                     }
            		
                     
                return false;
            }

        });
	}
	
	
	private void addRes(){
    	FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = new AddResearchFragment();
        ft.replace(R.id.myres, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }
	
    private void downloadAds(int position){
        Research r = g.getUserRes().get(position);
        g.setSelectedCatRes(r.getResearch_cat()); 
        SharedPreferences userData = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
        String json = new String();
        String url = getResources().getString(R.string.HOST) + getResources().getString(R.string.RESEARCH_FIND);
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(r);
        jsonElement.getAsJsonObject().addProperty(getString(R.string.PSW_USER), userData.getString(getString(R.string.PSW_USER), null));
        json = gson.toJson(jsonElement);
        data.add(new BasicNameValuePair(getResources().getString(R.string.json), json));
        try {
            String res = new Parser().execute(new DataHolders(url, data)).get();
            JsonElement msg = new JsonParser().parse(res);
            JsonObject obj = msg.getAsJsonObject();

            if (obj.has("stato")){
            	 Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.NO_ADS), Toast.LENGTH_SHORT);
                 toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                 toast.show();
            } else {
            	ArrayList <Object> ads = new SetObject().setResearch(obj, g.getSelectedCatRes());
            	
            	g.setRes_room_searches(new ArrayList<Ad_roomsearch>());
            	g.setRes_home_searches(new ArrayList<Ad_homesearch>());
            	g.setRes_home_offers(new ArrayList<Ad_homeoffer>());
            	g.setRes_room_offers(new ArrayList<Ad_roomoffer>());
                
            	for (Object o : ads){
                    if (g.getSelectedCatRes().toString().equals(getResources().getString(R.string.search_room))){
                    	g.getRes_room_searches().add((Ad_roomsearch) o);
                    	
                    } else if (g.getSelectedCatRes().toString().equals(getResources().getString(R.string.search_home))){
                    	g.getRes_home_searches().add((Ad_homesearch) o);
                    	
                    } else if (g.getSelectedCatRes().toString().equals(getResources().getString(R.string.offer_home))){
                    	g.getRes_home_offers().add((Ad_homeoffer) o);
                    	
                    } else if (g.getSelectedCatRes().toString().equals(getResources().getString(R.string.offer_room))){
                    	g.getRes_room_offers().add((Ad_roomoffer) o);
                    	
                    }
                }
            	
            	FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = new AdsFragment();
                Bundle args = new Bundle();
                args.putString("segue", "myres");
                fragment.setArguments(args);
                ft.replace(R.id.myres, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    
    }
    
    public boolean removeAds(int p){
    	String ad_id = new String();
    	   	
    	Research r = g.getUserRes().get(p);
    	ad_id = r.getResearch_id();
    		
    	
    	SharedPreferences userData = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    	ArrayList<NameValuePair> data = new ArrayList<NameValuePair>(2);
        data.add(new BasicNameValuePair(getResources().getString(R.string.EMAIL_USER), userData.getString(getString(R.string.EMAIL_USER), null)));
        data.add(new BasicNameValuePair(getResources().getString(R.string.PSW_USER), userData.getString(getString(R.string.PSW_USER), null)));
        data.add(new BasicNameValuePair("research_id", ad_id));
        
        String url = getResources().getString(R.string.HOST) + getResources().getString(R.string.DELETE_RES);
        try {
            String res = new Parser().execute(new DataHolders(url, data)).get();
            JsonElement msg = new JsonParser().parse(res);
            JsonObject obj = msg.getAsJsonObject();

            if(!obj.get("stato").getAsBoolean()){
            	Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.EMPTY_REG_MSG), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                return false;
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
    public void onResume(){
    	super.onResume();
    	populateList();
    }
}
