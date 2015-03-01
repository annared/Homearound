package it.unimi.mobidev.homearound;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddAdFragment extends Fragment{
	private Spinner spinnerType;
	private EditText title;
	private String category;
	private Button button;
	public AddAdFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_addad, container, false);
        title = (EditText)rootView.findViewById(R.id.editText);
        spinnerType = (Spinner)rootView.findViewById(R.id.spinner1);
        String[] cat = getResources().getStringArray(R.array.arrayCat);
        ArrayAdapter<?> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cat);
        spinnerType.setAdapter(dataAdapter1);
        button = (Button)rootView.findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener() {
        	
	      	  public void onClick(View view) {
	      	    goToNext();
	      	  }
      	});
        return rootView;
    }
    
	private void goToNext(){
        if ((spinnerType.getSelectedItem().toString().equals(getResources().getString(R.string.none)))||(title.getText().toString().length()<1)){
            Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.EMPTY_REG_MSG), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } else {
            Globals g = ((Globals)getActivity().getApplicationContext());
            if (spinnerType.getSelectedItem().toString().equals("Offer home")||(spinnerType.getSelectedItem().toString().equals("Offro casa"))){
                category = getString(R.string.offer_home);
                g.setHomeoffer(new Ad_homeoffer());
                g.getHomeoffer().setOffer_home_title(title.getText().toString());
            } else if (spinnerType.getSelectedItem().toString().equals("Offer room")||(spinnerType.getSelectedItem().toString().equals("Offro stanza"))){
                category = getString(R.string.offer_room);
                g.setRoomoffer(new Ad_roomoffer());
                g.getRoomoffer().setOffer_room_title(title.getText().toString());
            } else if (spinnerType.getSelectedItem().toString().equals("Search home")||(spinnerType.getSelectedItem().toString().equals("Cerco casa"))){
                category = getString(R.string.search_home);
                g.setHomesearch(new Ad_homesearch());
                g.getHomesearch().setSearch_home_title(title.getText().toString());
            } else if (spinnerType.getSelectedItem().toString().equals("Search room")||(spinnerType.getSelectedItem().toString().equals("Cerco stanza"))){
                category = getString(R.string.search_room);
                g.setRoomsearch(new Ad_roomsearch());
                g.getRoomsearch().setSearch_room_title(title.getText().toString());
            }


            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment fragment = new AddAd2Fragment();
            Bundle args = new Bundle();
            args.putString("Cat", category);
            fragment.setArguments(args);
            ft.replace(R.id.addad, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();

        }

    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    
    }


}

