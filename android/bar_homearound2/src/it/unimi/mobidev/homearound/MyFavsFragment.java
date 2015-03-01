package it.unimi.mobidev.homearound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MyFavsFragment extends Fragment{
	final String titleString = "Title";
    final String subtitleString = "Subtitle";
    private SimpleAdapter simpleAdapter;
    private ArrayList<Object> userFavs = new ArrayList<Object>();
    private HashMap<String,String> map = new HashMap<String,String>();
    private List<HashMap<String,String>> rowList = new ArrayList<HashMap<String,String>>();
    private ListView listView;
	private int count;
	private Globals g;
    public MyFavsFragment(){
		
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_favs, container, false);
        g = ((Globals)getActivity().getApplicationContext());
       
        listView = (ListView)rootView.findViewById(R.id.listView);
        count = g.getUserFavs().size();
       
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
               showDetails(position);
            }
        });
        
        getFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                    	if(g.getUserFavs().size()!=count){
                    		rowList = new ArrayList<HashMap<String,String>>();
                    		populateList();
                    	}
                    }
                });
        return rootView;
    }
    
	public void populateList(){
		userFavs = g.getUserFavs();
        for (Object obj : userFavs){
            map = new HashMap<String, String>();
            if (obj.getClass().equals(Ad_homeoffer.class)){
                Ad_homeoffer ad = (Ad_homeoffer)obj;
                map.put(titleString, ad.getOffer_home_title());
                map.put(subtitleString, ad.getOffer_home_area());
                rowList.add(map);
            }
            
            if (obj.getClass().equals(Ad_homesearch.class)){
                Ad_homesearch ad = (Ad_homesearch)obj;
                map.put(titleString, ad.getSearch_home_title());
                map.put(subtitleString, ad.getSearch_home_area());
                rowList.add(map);
            }
            if (obj.getClass().equals(Ad_roomoffer.class)){
                Ad_roomoffer ad = (Ad_roomoffer)obj;
                map.put(titleString, ad.getOffer_room_title());
                map.put(subtitleString, ad.getOffer_room_area());
                rowList.add(map);
            }
            if (obj.getClass().equals(Ad_roomsearch.class)){
                Ad_roomsearch ad = (Ad_roomsearch)obj;
                map.put(titleString, ad.getSearch_room_title());
                map.put(subtitleString, ad.getSearch_room_area());
                rowList.add(map);
            }
        }
        String[] from = { titleString, subtitleString };
        int[] to = { R.id.title, R.id.detail };
        simpleAdapter = new SimpleAdapter(getActivity(), rowList, R.layout.list_row, from, to);
        listView.setAdapter(simpleAdapter);
	}
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    
    }
    
    private void showDetails(int index) {
    	Fragment fragment = new DetailFragment();
    	FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
    	Bundle args = new Bundle();
    	args.putString("Cat", userFavs.get(index).getClass().toString());
    	args.putInt("index", index);
    	args.putString("segue", "favs");
    	fragment.setArguments(args);
    	ft.replace(R.id.fragmentfavs, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
        
        
    }
    
    
    @Override
    public void onResume(){
    	super.onResume();
    	populateList();
    }

    
    
   
}
