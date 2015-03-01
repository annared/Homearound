package it.unimi.mobidev.homearound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class AdsFragment extends Fragment {
	private String cat, segue;
    final String titleString = "Title";
    final String subtitleString = "Subtitle";
    private ListView listView;
    
	public AdsFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.fragment_ads, container, false);
    	List<HashMap<String,String>> rowList = new ArrayList<HashMap<String,String>>();
    	listView = (ListView)rootView.findViewById(R.id.listView);
    	segue = getArguments().getString("segue");
    	
        HashMap<String,String> map = null;
        String[] from = { titleString, subtitleString };
        int[] to = { R.id.title, R.id.detail };
        
        Globals g = ((Globals)getActivity().getApplicationContext());
       
        if (segue.equals("ads")) cat = g.getSelectedCat();
        else cat = g.getSelectedCatRes();
        
        if(cat.equals(getResources().getString(R.string.offer_home))){
        	 ArrayList<Ad_homeoffer> ads;
	    	 if (segue.equals("ads")) ads = g.getHome_offers(); 
	         else ads = g.getRes_home_offers();
        	
            for (Ad_homeoffer ad : ads){
                map = new HashMap<String, String>();
                map.put(titleString, ad.getOffer_home_title());
                map.put(subtitleString, ad.getOffer_home_area());
                rowList.add(map);
            }
            
        } else if(cat.equals(getResources().getString(R.string.offer_room))){
        	ArrayList<Ad_roomoffer> ads;
        	if (segue.equals("ads")) ads = g.getRoom_offers();
        	else ads = g.getRes_room_offers();
        	
            for (Ad_roomoffer ad : ads){
                map = new HashMap<String, String>();
                map.put(titleString, ad.getOffer_room_title());
                map.put(subtitleString, ad.getOffer_room_area());
                rowList.add(map);
            }
        } else if(cat.equals(getResources().getString(R.string.search_home))){
        	ArrayList<Ad_homesearch> ads;
        	if (segue.equals("ads")) ads = g.getHome_searches();
        	else ads = g.getRes_home_searches();
        	
            for (Ad_homesearch ad : ads){
                map = new HashMap<String, String>();
                map.put(titleString, ad.getSearch_home_title());
                map.put(subtitleString, ad.getSearch_home_area());
                rowList.add(map);
            }
        } else if(cat.equals(getResources().getString(R.string.search_room))){
        	ArrayList<Ad_roomsearch> ads;
        	if (segue.equals("ads")) ads = g.getRoom_searches();
        	else ads = g.getRes_room_searches();
        	
            for (Ad_roomsearch ad : ads){
                map = new HashMap<String, String>();
                map.put(titleString, ad.getSearch_room_title());
                map.put(subtitleString, ad.getSearch_room_area());
                rowList.add(map);
            }
        }

        
        SimpleAdapter simple = new SimpleAdapter(getActivity(), rowList, R.layout.list_row, from, to);
       
        listView.setAdapter(simple);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
               showDetails(position);
            }
        });
        return rootView;
    
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    
    }
    
    private void showDetails(int index) {
    	Fragment fragment = new DetailFragment();
    	FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
    	Bundle args = new Bundle();
    	args.putString("Cat", cat);
    	args.putInt("index", index);
    	args.putString("segue", segue);
    	fragment.setArguments(args);
    	ft.replace(R.id.fragmentads, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }
}
