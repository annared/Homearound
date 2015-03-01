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

public class ResResultFragment extends Fragment {
	final String titleString = "Title";
    final String dateString = "Date";
    ArrayList<Object> userResult = new ArrayList<Object>();
    private  HashMap<String,String> map;
    private List<HashMap<String,String>> rowList;
	private SimpleAdapter simpleAdapter;
	Globals g;
	private ListView listView;
	public static final String PREFS_NAME = "HomeAround";

	public ResResultFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);
        listView = (ListView)rootView.findViewById(R.id.listView);
        rowList = new ArrayList<HashMap<String,String>>();
        map = null;
        populateList();
       
        
        return rootView;
    }

    private void populateList(){
		g = ((Globals)getActivity().getApplicationContext());
        String[] from = { titleString, dateString };
        int[] to = { R.id.title, R.id.detail };
        //userAds = g.getUserAds();
        
        
        
        for (Object obj : g.getUserAds()){
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

    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
    }
    
    @Override
    public void onResume(){
    	super.onResume();  	
    	//simpleAdapter.notifyDataSetChanged();
    }
    
    private void showDetails(int index) {
    	Fragment fragment = new DetailFragment();
    	FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
    	Bundle args = new Bundle();
    	args.putString("Cat", g.getUserAds().get(index).getClass().toString());
    	args.putInt("index", index);
    	args.putString("segue", "result");
    	fragment.setArguments(args);
    	ft.replace(R.id.fragmentresults, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }


}
