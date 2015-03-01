package it.unimi.mobidev.homearound;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddAd2Fragment extends Fragment implements OnItemSelectedListener{
	private Spinner spinner1, spinner2;
	private EditText edittext;
    private String category;
    private TextView area;
    private String[] array, array2;
    private Button button;
	public AddAd2Fragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_addad2, container, false);
        category = getArguments().getString("Cat");
        button = (Button)rootView.findViewById(R.id.button1);
        
        spinner1 = (Spinner)rootView.findViewById(R.id.spinner1);
        spinner2 = (Spinner)rootView.findViewById(R.id.spinner2);
        
        edittext = (EditText)rootView.findViewById(R.id.editText);
        
        array = getResources().getStringArray(R.array.City);
        area = (TextView)rootView.findViewById(R.id.textView2);
        area.setVisibility(View.GONE);
        spinner2.setVisibility(View.GONE);
        ArrayAdapter<?> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, array);
        spinner1.setAdapter(dataAdapter);
        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        button.setOnClickListener(new OnClickListener() {
        	
        	  public void onClick(View view) {
        	    goToNext();
        	  }
        	});
        return rootView;
    }
    
    public void Research(String c){
     
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    
    }

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		Spinner spinner = (Spinner) parent;
        if(spinner.getId() == spinner1.getId())
        {

            if (position!=0){

                area.setVisibility(View.VISIBLE);
                spinner2.setVisibility(View.VISIBLE);
                int res = getResources().getIdentifier(spinner1.getSelectedItem().toString(), "array", getActivity().getPackageName());
                array2 = getResources().getStringArray(res);
                ArrayAdapter<?> dataAdapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, array2);
                spinner2.setAdapter(dataAdapter2);
            } else {
                area.setVisibility(View.GONE);
                spinner2.setVisibility(View.GONE);
            }
        }
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void goToNext(){
        if ((spinner1.getSelectedItem().toString().equals(getResources().getString(R.string.none)))||(edittext.getText().toString().length()<1)){
            Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.EMPTY_REG_MSG), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } else {
            Globals g = ((Globals)getActivity().getApplicationContext());
            if (category.equals(getString(R.string.offer_home))){
                g.getHomeoffer().setOffer_home_description(edittext.getText().toString());
                g.getHomeoffer().setOffer_home_city(spinner1.getSelectedItem().toString());
                g.getHomeoffer().setOffer_home_area(spinner2.getSelectedItem().toString());
            } else if (category.equals(getString(R.string.offer_room))){
                g.getRoomoffer().setOffer_room_description(edittext.getText().toString());
                g.getRoomoffer().setOffer_room_city(spinner1.getSelectedItem().toString());
                g.getRoomoffer().setOffer_room_area(spinner2.getSelectedItem().toString());
            } else if (category.equals(getString(R.string.search_home))){
                g.getHomesearch().setSearch_home_description(edittext.getText().toString());
                g.getHomesearch().setSearch_home_city(spinner1.getSelectedItem().toString());
                g.getHomesearch().setSearch_home_area(spinner2.getSelectedItem().toString());
            } else if (category.equals(getString(R.string.search_room))){
                g.getRoomsearch().setSearch_room_description(edittext.getText().toString());
                g.getRoomsearch().setSearch_room_city(spinner1.getSelectedItem().toString());
                g.getRoomsearch().setSearch_room_area(spinner2.getSelectedItem().toString());
            }
            
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment fragment = new AddAd3Fragment();
            Bundle args = new Bundle();
            args.putString("Cat", category);
            fragment.setArguments(args);
            ft.add(R.id.addad2, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }

    }
}
