package it.unimi.mobidev.homearound;

import java.util.Locale;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BachecaFragment extends Fragment{
	public BachecaFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_bacheca, container, false);
        ListView offer = (ListView)rootView.findViewById(R.id.listView);
        ListView search = (ListView)rootView.findViewById(R.id.listView2);

        final String[] array_cat = getResources().getStringArray(R.array.cat);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, array_cat);

        offer.setAdapter(arrayAdapter);
        offer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String cat = "offer_";
                switch(position){
                case 0:
                	cat+="home";
                	break;
                case 1:
                	cat+="room";
                	break;
                }
                
                Research(cat);

            }
        });

        search.setAdapter(arrayAdapter);
        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
            	 String cat = "search_";
                 switch(position){
                 case 0:
                 	cat+="home";
                 	break;
                 case 1:
                 	cat+="room";
                 	break;
                 }
                Research(cat);
            }
        });  
        
        //getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        return rootView;
    }
    
    public void Research(String c){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = new ResearchFragment();
    	Bundle args = new Bundle();
    	args.putString("Cat", c.toLowerCase(Locale.getDefault()));
    	fragment.setArguments(args);
        ft.replace(R.id.fragmentbacheca, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    
    }


}
