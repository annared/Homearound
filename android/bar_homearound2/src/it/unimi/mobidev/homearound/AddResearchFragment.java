package it.unimi.mobidev.homearound;

import java.util.ArrayList;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddResearchFragment extends Fragment implements OnItemSelectedListener{
	private TextView labelSpinner2, labelSpinner3, labelSpinner4, labelSpinner5;
	private Spinner spinner1, spinner2, spinner3, spinner4, spinner5;
	
	private String [] category, area;
	public static final String PREFS_NAME = "HomeAround";
	private Button buttonres;
	private ProgressBar pb;
	private String category_res;
	public AddResearchFragment(){}
	 

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	}
    

	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		 View rootView = inflater.inflate(R.layout.fragment_addres, container, false);
		 	pb = (ProgressBar)rootView.findViewById(R.id.progressBar1);
		 	pb.setVisibility(View.GONE);
	        buttonres = (Button)rootView.findViewById(R.id.button1);
	        
	        labelSpinner2 = (TextView)rootView.findViewById(R.id.labelSpinner2);
	        labelSpinner3 = (TextView)rootView.findViewById(R.id.labelSpinner3);
	        labelSpinner4 = (TextView)rootView.findViewById(R.id.labelSpinner4);
	        labelSpinner5 = (TextView)rootView.findViewById(R.id.labelSpinner5);

	        spinner1=(Spinner)rootView.findViewById(R.id.spinner1);
	        spinner2=(Spinner)rootView.findViewById(R.id.spinner2);
	        spinner3=(Spinner)rootView.findViewById(R.id.spinner3);
	        spinner4=(Spinner)rootView.findViewById(R.id.spinner4);
	        spinner5=(Spinner)rootView.findViewById(R.id.spinner5);
	        
	        labelSpinner2.setVisibility(View.GONE);
	        labelSpinner3.setVisibility(View.GONE);
	        labelSpinner4.setVisibility(View.GONE);
	        labelSpinner5.setVisibility(View.GONE);
	        
	        spinner2.setVisibility(View.GONE);
	        spinner3.setVisibility(View.GONE);
	        spinner4.setVisibility(View.GONE);
	        spinner5.setVisibility(View.GONE);
	        
	        String [] arrayCity = getResources().getStringArray(R.array.City);
	        String [] arrayCategory = getResources().getStringArray(R.array.arrayCat);
	        String [] price = getResources().getStringArray(R.array.Price);
	        spinner1.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, arrayCategory));
	        spinner2.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, arrayCity));
	        spinner4.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, price));
	        spinner1.setOnItemSelectedListener(this);
	        spinner2.setOnItemSelectedListener(this);
	        buttonres.setOnClickListener(new OnClickListener() {
	        	
	        	  public void onClick(View view) {
	        	    saveRes();
	        	  }
	        	});
	   return rootView;
	 }
	 
	 @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);   
	    }

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
		Spinner spinner = (Spinner) parent;
        if(spinner.getId() == spinner1.getId()){
            if (position!=0){
            	labelSpinner2.setVisibility(View.VISIBLE);
      	        labelSpinner3.setVisibility(View.VISIBLE);
      	        labelSpinner4.setVisibility(View.VISIBLE);
      	        
      	        spinner2.setVisibility(View.VISIBLE);
      	        spinner3.setVisibility(View.VISIBLE);
      	        spinner4.setVisibility(View.VISIBLE);
      	        
            	switch(position){
            	case 1:	
            		category = getResources().getStringArray(R.array.Home);
            		category_res = getString(R.string.offer_home);
            		break;
            	case 2: 
            		category = getResources().getStringArray(R.array.Room);
            		category_res = getString(R.string.offer_room);
            		break;
            	case 3: 
            		category = getResources().getStringArray(R.array.Home);
            		category_res = getString(R.string.search_home);
            		break;
            	case 4: 
            		category = getResources().getStringArray(R.array.Room);
            		category_res = getString(R.string.search_room);
            		break;
            	}
            	
            	spinner3.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, category));
            } else {
            	labelSpinner2.setVisibility(View.GONE);
    	        labelSpinner3.setVisibility(View.GONE);
    	        labelSpinner4.setVisibility(View.GONE);
    	        labelSpinner5.setVisibility(View.GONE);
    	        spinner2.setVisibility(View.GONE);
    	        spinner3.setVisibility(View.GONE);
    	        spinner4.setVisibility(View.GONE);
    	        spinner5.setVisibility(View.GONE);
            }
        }
        if(spinner.getId() == spinner2.getId()){
        	if (position!=0){
        		int res = getResources().getIdentifier(spinner2.getSelectedItem().toString(), "array", getActivity().getPackageName());
                area = getResources().getStringArray(res);
                spinner5.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, area));
                labelSpinner5.setVisibility(View.VISIBLE);
                spinner5.setVisibility(View.VISIBLE);
        	} else {
    	        labelSpinner5.setVisibility(View.GONE);
    	        spinner5.setVisibility(View.GONE);
        	}
        }
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> spinner) {
		
		
	}
	
	private void saveRes(){
		ConnectionDetector cd = new ConnectionDetector(getActivity().getApplicationContext());
  	  if(cd.isConnectingToInternet()){
  		if (spinner1.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
            Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.EMPTY_REG_MSG), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } else {
        	Gson gson = new Gson();
        	Globals g = ((Globals)getActivity().getApplicationContext());
            SharedPreferences userData = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
            String json = new String();
            String email = userData.getString(getResources().getString(R.string.EMAIL_USER), null);
            
            String area = "none";
            if (!spinner2.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
            	area = spinner5.getSelectedItem().toString();
            }
            String url = getResources().getString(R.string.HOST) + getResources().getString(R.string.USER_RESEARCH);
            Research research = new Research(null, email, spinner2.getSelectedItem().toString(), category_res, spinner4.getSelectedItem().toString(), area,
                    spinner3.getSelectedItem().toString());
            
            JsonElement jsonElement = gson.toJsonTree(research);
            jsonElement.getAsJsonObject().addProperty(getString(R.string.PSW_USER), userData.getString(getString(R.string.PSW_USER), null));
            jsonElement.getAsJsonObject().addProperty(getString(R.string.EMAIL_USER), userData.getString(getString(R.string.EMAIL_USER), null));

            json = gson.toJson(jsonElement);
            data.add(new BasicNameValuePair(getResources().getString(R.string.json), json));
            try {
                pb.setVisibility(View.VISIBLE);
                String res = new Parser().execute(new DataHolders(url, data)).get();
                pb.setVisibility(View.GONE);
                JsonElement msg = new JsonParser().parse(res);
                JsonObject obj = msg.getAsJsonObject();
                if(obj.get("stato").getAsBoolean()){
                    g.getUserRes().add(research);

                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    
                } else {
                    Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.ERROR_INS_MSG), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        
        } 
  	  } else {

            Toast toast = Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.NO_INTERNET), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
		
        
	}

}
