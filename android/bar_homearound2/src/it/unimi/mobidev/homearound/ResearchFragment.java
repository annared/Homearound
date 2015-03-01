package it.unimi.mobidev.homearound;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ResearchFragment extends Fragment implements AdapterView.OnItemSelectedListener{

private String cat;
private TextView labelSpinner1, labelSpinner2, labelSpinner3, labelSpinner4, labelSpinner5;
private Spinner spinner1, spinner2, spinner3, spinner4, spinner5;
private ArrayAdapter<?> dataAdapter1, dataAdapter2, dataAdapter3, dataAdapter4, dataAdapter5;
private ProgressBar pb;
public static final String PREFS_NAME = "HomeAround";
private Button buttonres;
	public ResearchFragment(){}
	 

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}
    

	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		cat = getArguments().getString("Cat");
        View rootView = inflater.inflate(R.layout.fragment_research, container, false);
        
        pb = (ProgressBar)rootView.findViewById(R.id.progressBar1);
        buttonres = (Button)rootView.findViewById(R.id.button1);
        labelSpinner1 = (TextView)rootView.findViewById(R.id.labelSpinner1);
        labelSpinner2 = (TextView)rootView.findViewById(R.id.labelSpinner2);
        labelSpinner3 = (TextView)rootView.findViewById(R.id.labelSpinner3);
        labelSpinner4 = (TextView)rootView.findViewById(R.id.labelSpinner4);
        labelSpinner5 = (TextView)rootView.findViewById(R.id.labelSpinner5);

        spinner1=(Spinner)rootView.findViewById(R.id.spinner1);
        spinner2=(Spinner)rootView.findViewById(R.id.spinner2);
        spinner3=(Spinner)rootView.findViewById(R.id.spinner3);
        spinner4=(Spinner)rootView.findViewById(R.id.spinner4);
        spinner5=(Spinner)rootView.findViewById(R.id.spinner5);

        if(cat.equals(getResources().getString(R.string.offer_home)) || cat.equals(getResources().getString(R.string.offer_room))){
            labelSpinner1.setText(R.string.price);
            labelSpinner2.setText(R.string.mq);
            dataAdapter1 = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.Price, R.layout.spinner_item);
            dataAdapter2 = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.MQ, R.layout.spinner_item);

        } else if(cat.equals(getResources().getString(R.string.search_home))|| cat.equals(getResources().getString(R.string.search_room))){
            labelSpinner1.setText(R.string.min_price);
            labelSpinner2.setText(R.string.max_price);
            dataAdapter1 = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.Min_Price, R.layout.spinner_item);
            dataAdapter2 = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.Max_Price, R.layout.spinner_item);
        }

        spinner1.setAdapter(dataAdapter1);
        spinner2.setAdapter(dataAdapter2);

        if(cat.equals(getResources().getString(R.string.offer_home)) || cat.equals(getResources().getString(R.string.search_home))){
        	dataAdapter3 = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.Home, R.layout.spinner_item);
        } else {
        	dataAdapter3 = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.Room, R.layout.spinner_item);
        }
        labelSpinner3.setText(R.string.type);
       
        spinner3.setAdapter(dataAdapter3);

        labelSpinner4.setText(R.string.city);
        dataAdapter4 = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.City, R.layout.spinner_item);
        spinner4.setAdapter(dataAdapter4);

        labelSpinner5.setText(R.string.area);
        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        spinner3.setOnItemSelectedListener(this);
        spinner4.setOnItemSelectedListener(this);
        spinner5.setOnItemSelectedListener(this);

        pb.setVisibility(View.GONE);
        buttonres.setOnClickListener(new OnClickListener() {
        	  public void onClick(View view) {
        	    getData();
        	  }
        	});
        return rootView;
        
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);   
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		 	Spinner spinner = (Spinner) parent;
	        if(spinner.getId() == R.id.spinner4)
	        {
	            if (position!=0){
	                labelSpinner5.setVisibility(View.VISIBLE);
	                spinner5.setVisibility(View.VISIBLE);
	                int res = getResources().getIdentifier(spinner4.getSelectedItem().toString(), "array", getActivity().getPackageName());
	                dataAdapter5 = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), res, R.layout.spinner_item);
	                spinner5.setAdapter(dataAdapter5);
	            } else {
	                labelSpinner5.setVisibility(View.GONE);
	                spinner5.setVisibility(View.GONE);
	            }
	        }
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void getData(){
		
        ConnectionDetector cd = new ConnectionDetector(getActivity().getApplicationContext());
        if(cd.isConnectingToInternet()){

            SharedPreferences userData = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

            JsonObject res = new JsonObject();

            res.addProperty(getResources().getString(R.string.EMAIL_USER), userData.getString(getResources().getString(R.string.EMAIL_USER), null));
            res.addProperty(getResources().getString(R.string.PSW_USER), userData.getString(getResources().getString(R.string.PSW_USER), null));
            res.addProperty(getResources().getString(R.string.res_cat), cat);

            if (cat.equals(getResources().getString(R.string.search_room))){

                if (!spinner1.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                    res.addProperty(getResources().getString(R.string.SEARCH_ROOM_MINPRICE), spinner1.getSelectedItem().toString());
                }
                if (!spinner2.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                    res.addProperty(getResources().getString(R.string.SEARCH_ROOM_MAXPRICE), spinner2.getSelectedItem().toString());
                }
                if (!spinner3.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                    res.addProperty(getResources().getString(R.string.SEARCH_ROOM_TYPE), spinner3.getSelectedItem().toString());
                }
                if (!spinner4.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                    res.addProperty(getResources().getString(R.string.SEARCH_ROOM_CITY), spinner4.getSelectedItem().toString());
                }
                if (spinner5.getVisibility()==View.VISIBLE){
                    if (!spinner5.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                        res.addProperty(getResources().getString(R.string.SEARCH_ROOM_AREA), spinner5.getSelectedItem().toString());
                    }
                }

            } else if (cat.equals(getResources().getString(R.string.search_home))){

                if (!spinner1.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                    res.addProperty(getResources().getString(R.string.SEARCH_HOME_MINPRICE), spinner1.getSelectedItem().toString());
                }
                if (!spinner2.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                    res.addProperty(getResources().getString(R.string.SEARCH_HOME_MAXPRICE), spinner2.getSelectedItem().toString());
                }
                if (!spinner3.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                    res.addProperty(getResources().getString(R.string.SEARCH_HOME_TYPE), spinner3.getSelectedItem().toString());
                }
                if (!spinner4.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                    res.addProperty(getResources().getString(R.string.SEARCH_HOME_CITY), spinner4.getSelectedItem().toString());
                }
                if (spinner5.getVisibility()==View.VISIBLE){
                    if (!spinner5.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                        res.addProperty(getResources().getString(R.string.SEARCH_HOME_AREA), spinner5.getSelectedItem().toString());
                    }
                }

            } else if (cat.equals(getResources().getString(R.string.offer_home))){

                if (!spinner1.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                    res.addProperty(getResources().getString(R.string.OFFER_HOME_PRICE), spinner1.getSelectedItem().toString());
                }
                if (!spinner2.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                    res.addProperty(getResources().getString(R.string.OFFER_HOME_MQ), spinner2.getSelectedItem().toString());
                }
                if (!spinner3.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                    res.addProperty(getResources().getString(R.string.OFFER_HOME_TYPE), spinner3.getSelectedItem().toString());
                }
                if (!spinner4.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                    res.addProperty(getResources().getString(R.string.OFFER_HOME_CITY), spinner4.getSelectedItem().toString());
                }
                if (spinner5.getVisibility()==View.VISIBLE){
                    if (!spinner5.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                        res.addProperty(getResources().getString(R.string.OFFER_HOME_AREA), spinner5.getSelectedItem().toString());
                    }
                }


            } else if (cat.equals(getResources().getString(R.string.offer_room))){

                if (!spinner1.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                    res.addProperty(getResources().getString(R.string.OFFER_ROOM_PRICE), spinner1.getSelectedItem().toString());
                }
                if (!spinner2.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                    res.addProperty(getResources().getString(R.string.OFFER_ROOM_MQ), spinner2.getSelectedItem().toString());
                }
                if (!spinner3.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                    res.addProperty(getResources().getString(R.string.OFFER_ROOM_TYPE), spinner3.getSelectedItem().toString());
                }
                if (!spinner4.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                    res.addProperty(getResources().getString(R.string.OFFER_ROOM_CITY), spinner4.getSelectedItem().toString());
                }
                if (spinner5.getVisibility()==View.VISIBLE){
                    if (!spinner5.getSelectedItem().toString().equals(getResources().getString(R.string.none))){
                        res.addProperty(getResources().getString(R.string.OFFER_ROOM_AREA), spinner5.getSelectedItem().toString());
                    }
                }
            }

            ArrayList<NameValuePair> jsonRes = new ArrayList<NameValuePair>();
            jsonRes.add(new BasicNameValuePair("json", res.toString()));
            String url = getResources().getString(R.string.HOST) + getResources().getString(R.string.ADS_RESEARCH);

            try {
                pb.setVisibility(View.VISIBLE);
                String response = new Parser().execute(new DataHolders(url, jsonRes)).get();
                JsonElement msg = new JsonParser().parse(response);
                JsonObject obj = msg.getAsJsonObject();
                pb.setVisibility(View.GONE);
                if (!(obj.has("stato"))){
                    ArrayList <Object> ads = new SetObject().setResearch(obj, cat);

                    Globals g = ((Globals)getActivity().getApplicationContext());
                    g.reset();
                    for (Object o : ads){
                        if (cat.equals(getResources().getString(R.string.search_room))){
                        	g.getRoom_searches().add((Ad_roomsearch) o);
                            //g.setRoom_searches((Ad_roomsearch) o);
                        } else if (cat.equals(getResources().getString(R.string.search_home))){
                        	g.getHome_searches().add((Ad_homesearch) o);
                            //g.setHome_searches((Ad_homesearch) o);
                        } else if (cat.equals(getResources().getString(R.string.offer_home))){
                        	g.getHome_offers().add((Ad_homeoffer) o);
                            //g.setHome_offers((Ad_homeoffer) o);
                        } else if (cat.equals(getResources().getString(R.string.offer_room))){
                        	g.getRoom_offers().add((Ad_roomoffer) o);
                            //g.setRoom_offers((Ad_roomoffer) o);
                        }
                    }
                    g.setSelectedCat(cat);
                    gotoNext();
                    //Intent i = new Intent(ResearchActivity.this, AdsActivity.class);
                    //ResearchActivity.this.startActivity(i);

                } else {
                    Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.NO_ADS), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

	private void gotoNext(){
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = new AdsFragment();
        Bundle args = new Bundle();
        args.putString("segue", "ads");
        fragment.setArguments(args);
        ft.replace(R.id.fragmentresearch, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
	}

}
