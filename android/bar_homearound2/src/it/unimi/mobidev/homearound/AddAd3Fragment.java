package it.unimi.mobidev.homearound;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
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
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddAd3Fragment extends Fragment{
	private String category;
    private Spinner spinner1;
    private TextView label2, label3;
    private EditText text2, text3;
    private ArrayAdapter<?> dataAdapter1;
    private String[] array1;
    private Button button;
    ProgressBar pb;
    public static final String PREFS_NAME = "HomeAround";
	public AddAd3Fragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_addad3, container, false);
        category = getArguments().getString("Cat");
        label2 = (TextView)rootView.findViewById(R.id.textView2);
        label3 = (TextView)rootView.findViewById(R.id.textView3);
        text2 = (EditText)rootView.findViewById(R.id.editText);
        text3 = (EditText)rootView.findViewById(R.id.editText2);
        spinner1 = (Spinner)rootView.findViewById(R.id.spinner1);
        pb = (ProgressBar)rootView.findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
        button = (Button)rootView.findViewById(R.id.button1);
        
        if (category.equals(getString(R.string.offer_home))||category.equals(getString(R.string.offer_room))){
            label2.setText(R.string.price);
            label3.setText(R.string.mq);

        } else {
            label2.setText(R.string.min_price);
            label3.setText(R.string.max_price);
        }

        if (category.equals(getString(R.string.offer_home))||category.equals(getString(R.string.search_home))){
            array1 = getResources().getStringArray(R.array.Home);

        } else {
            array1 = getResources().getStringArray(R.array.Room);
        }
        dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, array1);
        spinner1.setAdapter(dataAdapter1);
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
    
    private void goToNext(){
        if ((spinner1.getSelectedItem().toString().equals(getResources().getString(R.string.none)))||(text2.getText().toString().length()<1)||(text3.getText().toString().length()<1)){
            Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.EMPTY_REG_MSG), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } else {
            Globals g = ((Globals)getActivity().getApplicationContext());

            SharedPreferences userData = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

            if (category.equals(getString(R.string.offer_home))){
                g.getHomeoffer().setOffer_home_type(spinner1.getSelectedItem().toString());
                g.getHomeoffer().setOffer_home_price(text2.getText().toString());
                g.getHomeoffer().setOffer_home_mq(text3.getText().toString());
                g.getHomeoffer().setUser_email(userData.getString(getResources().getString(R.string.EMAIL_USER), null));
                g.getHomeoffer().setOffer_home_date(new SimpleDateFormat("EEE, dd-MM-yyyy").format(new Date()));
                g.getHomeoffer().setOffer_home_id("oc_"+ UUID.randomUUID().toString());
            } else if (category.equals(getString(R.string.offer_room))){
                g.getRoomoffer().setOffer_room_type(spinner1.getSelectedItem().toString());
                g.getRoomoffer().setOffer_room_price(text2.getText().toString());
                g.getRoomoffer().setOffer_room_mq(text3.getText().toString());
                g.getRoomoffer().setUser_email(userData.getString(getResources().getString(R.string.EMAIL_USER), null));
                g.getRoomoffer().setOffer_room_date(new SimpleDateFormat("EEE, dd-MM-yyyy").format(new Date()));
                g.getRoomoffer().setOffer_room_id("os_" + UUID.randomUUID().toString());
            } else if (category.equals(getString(R.string.search_home))){
                g.getHomesearch().setSearch_home_type(spinner1.getSelectedItem().toString());
                g.getHomesearch().setSearch_home_minprice((text2.getText().toString()));
                g.getHomesearch().setSearch_home_maxprice((text3.getText().toString()));
                g.getHomesearch().setUser_email(userData.getString(getResources().getString(R.string.EMAIL_USER), null));
                g.getHomesearch().setSearch_home_date(new SimpleDateFormat("EEE, dd-MM-yyyy").format(new Date()));
                g.getHomesearch().setSearch_home_id("cc_"+ UUID.randomUUID().toString());
            } else if (category.equals(getString(R.string.search_room))){
                g.getRoomsearch().setSearch_room_type(spinner1.getSelectedItem().toString());
                g.getRoomsearch().setSearch_room_minprice((text2.getText().toString()));
                g.getRoomsearch().setSearch_room_maxprice((text3.getText().toString()));
                g.getRoomsearch().setUser_email(userData.getString(getResources().getString(R.string.EMAIL_USER), null));
                g.getRoomsearch().setSearch_room_date(new SimpleDateFormat("EEE, dd-MM-yyyy").format(new Date()));
                g.getRoomsearch().setSearch_room_id("cs_" + UUID.randomUUID().toString());
            }
            if (category.equals(getString(R.string.offer_home))||category.equals(getString(R.string.offer_room))){
            	 
            	 FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                 Fragment fragment = new AddMapsFragment();
                 Bundle args = new Bundle();
                 args.putString("Cat", category);
                 fragment.setArguments(args);
                 ft.replace(R.id.addad3, fragment);
                 ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                 ft.addToBackStack(null);
                 ft.commit();
                 
            } else {
            	
            	ConnectionDetector cd = new ConnectionDetector(getActivity().getApplicationContext());
            	if(cd.isConnectingToInternet()){
            		Gson gson = new Gson();
                    ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
                    String json = new String();
                    String url = getResources().getString(R.string.HOST) + getResources().getString(R.string.INSERISCI);
                    if (category.equals(getString(R.string.search_home))){
                        JsonElement jsonElement = gson.toJsonTree(g.getHomesearch());
                        jsonElement.getAsJsonObject().addProperty(getString(R.string.PSW_USER), userData.getString(getString(R.string.PSW_USER), null));
                        jsonElement.getAsJsonObject().addProperty("categoria", category);
                        json = gson.toJson(jsonElement);

                    } else if (category.equals(getString(R.string.search_room))){
                        JsonElement jsonElement = gson.toJsonTree(g.getRoomsearch());
                        jsonElement.getAsJsonObject().addProperty(getString(R.string.PSW_USER), userData.getString(getString(R.string.PSW_USER), null));
                        jsonElement.getAsJsonObject().addProperty("categoria", category);
                        json = gson.toJson(jsonElement);
                    }


                    data.add(new BasicNameValuePair(getResources().getString(R.string.json), json));
                    try {
                        pb.setVisibility(View.VISIBLE);
                        String res = new Parser().execute(new DataHolders(url, data)).get();
                        pb.setVisibility(View.GONE);
                        JsonElement msg = new JsonParser().parse(res);
                        JsonObject obj = msg.getAsJsonObject();
                        if(obj.get("stato").getAsBoolean()){
                            if (category.equals(getString(R.string.search_home))){
                            	g.getUserAds().add(g.getHomesearch());
                                g.setHomesearch(null);
                            } else if (category.equals(getString(R.string.search_room))){
                            	g.getUserAds().add(g.getRoomsearch());
                                g.setRoomsearch(null);
                            }
                            
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
           	  	} else {

                     Toast toast = Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.NO_INTERNET), Toast.LENGTH_SHORT);
                     toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                     toast.show();
                 }
                
            }

        }

    }
}
