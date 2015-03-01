package it.unimi.mobidev.homearound;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditAd extends Fragment{
	private String category;
 
    private TextView label2, label3;
    private EditText text1, text2, text3;
    private Ad_homeoffer homeoffer = new Ad_homeoffer();
    private Ad_homesearch homesearch = new Ad_homesearch();;
    private Ad_roomoffer roomoffer = new Ad_roomoffer();
    private Ad_roomsearch roomsearch = new Ad_roomsearch();
    private Button button;
    private JsonObject res = new JsonObject();
    private Globals g;
    private int index;
    public static final String PREFS_NAME = "HomeAround";
	
    public EditAd(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_ad_edit, container, false);
      
        category = getArguments().getString("Cat");
        index = getArguments().getInt("index");
        
        label2 = (TextView)rootView.findViewById(R.id.textView2);
        label3 = (TextView)rootView.findViewById(R.id.textView3);
        text1 = (EditText)rootView.findViewById(R.id.editText);
        text2 = (EditText)rootView.findViewById(R.id.editText1);
        text3 = (EditText)rootView.findViewById(R.id.editText2);
        
        button = (Button)rootView.findViewById(R.id.button1);
        g = ((Globals)getActivity().getApplicationContext());
        
        if ((category.equals(homeoffer.getClass().toString()))||(category.equals(roomoffer.getClass().toString()))){
            label2.setText(R.string.price);
            label3.setVisibility(View.GONE);
            text3.setVisibility(View.GONE);
            if (category.equals(homeoffer.getClass().toString())){
            	homeoffer = ((Ad_homeoffer) g.getUserAds().get(index));
            	text1.setText(homeoffer.getOffer_home_description());
            	text2.setText(homeoffer.getOffer_home_price());
            	res.addProperty(getResources().getString(R.string.categoria), getResources().getString(R.string.offer_home) );
            	res.addProperty(getResources().getString(R.string.OFFER_HOME_ID), homeoffer.getOffer_home_id() );
            } else if (category.equals(roomoffer.getClass().toString())){
            	roomoffer = ((Ad_roomoffer) g.getUserAds().get(index));
            	text1.setText(roomoffer.getOffer_room_description());
            	text2.setText(roomoffer.getOffer_room_price());
            	res.addProperty(getResources().getString(R.string.OFFER_ROOM_ID), roomoffer.getOffer_room_id() );
            }
        } else {
            label2.setText(R.string.min_price);
            label3.setText(R.string.max_price);
            if (category.equals(homesearch.getClass().toString())){
            	homesearch = ((Ad_homesearch) g.getUserAds().get(index));
            	text1.setText(homesearch.getSearch_home_description());
            	text2.setText(homesearch.getSearch_home_minprice());
            	text3.setText(homesearch.getSearch_home_maxprice());
            	res.addProperty(getResources().getString(R.string.SEARCH_HOME_ID), homesearch.getSearch_home_id());
            } else if (category.equals(roomsearch.getClass().toString())){
            	roomsearch = ((Ad_roomsearch) g.getUserAds().get(index));
            	text1.setText(roomsearch.getSearch_room_description());
            	text2.setText(roomsearch.getSearch_room_minprice());
            	text3.setText(roomsearch.getSearch_room_maxprice());
            	res.addProperty(getResources().getString(R.string.SEARCH_ROOM_ID), roomsearch.getSearch_room_id() );
            }
        }
        
        button.setOnClickListener(new OnClickListener() {
        	
        	  public void onClick(View view) {
        	    save();
        	  }
        	});
        return rootView;
    }
    
   
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    
    }
    
    private void save(){
    	  ConnectionDetector cd = new ConnectionDetector(getActivity().getApplicationContext());
    	  if(cd.isConnectingToInternet()){
    		
    		  if (text1.getText().toString().length()==0||text2.getText().toString().length()==0||
    				
    				(text3.getText().toString().length()==0 && text3.getVisibility()==View.VISIBLE)){
				  	Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.EMPTY_REG_MSG), Toast.LENGTH_SHORT);
    	         	toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
    	         	toast.show();
    		  } else {
    			 
    			  boolean change = false;
    			  ArrayList<NameValuePair> json = new ArrayList<NameValuePair>();
    			  if (category.equals(roomsearch.getClass().toString())&&(!roomsearch.getSearch_room_description().equals(text1.getText().toString()))){
    				  res.addProperty(getResources().getString(R.string.SEARCH_ROOM_DESCRIPTION), text1.getText().toString());
    				  roomsearch.setSearch_room_description(text1.getText().toString());
    				  change = true;
    			  }
    			  if (category.equals(homesearch.getClass().toString())&&(!homesearch.getSearch_home_description().equals(text1.getText().toString()))){
    				  res.addProperty(getResources().getString(R.string.SEARCH_HOME_DESCRIPTION), text1.getText().toString());
    				  homesearch.setSearch_home_description(text1.getText().toString());
    				  change = true;
    			  }
    			  if (category.equals(roomoffer.getClass().toString())&&(!roomoffer.getOffer_room_description().equals(text1.getText().toString()))){
    				  res.addProperty(getResources().getString(R.string.OFFER_ROOM_DESCRIPTION), text1.getText().toString());
    				  roomoffer.setOffer_room_description(text1.getText().toString());
    				  change = true;
    			  }
    			  if (category.equals(homeoffer.getClass().toString())&&(!homeoffer.getOffer_home_description().equals(text1.getText().toString()))){
    				  res.addProperty(getResources().getString(R.string.OFFER_HOME_DESCRIPTION), text1.getText().toString());
    				  homeoffer.setOffer_home_description(text1.getText().toString());
    				  change = true;
    			  }
    			  
    			  if (category.equals(roomsearch.getClass().toString())&&(!roomsearch.getSearch_room_minprice().equals(text2.getText().toString()))){
    				  res.addProperty(getResources().getString(R.string.SEARCH_ROOM_MINPRICE), text2.getText().toString());
    				  roomsearch.setSearch_room_minprice(text2.getText().toString());
    				  change = true;
    			  }
    			  if (category.equals(homesearch.getClass().toString())&&(!homesearch.getSearch_home_minprice().equals(text2.getText().toString()))){
    				  res.addProperty(getResources().getString(R.string.SEARCH_HOME_MINPRICE), text2.getText().toString());
    				  homesearch.setSearch_home_minprice(text2.getText().toString());
    				  change = true;
    			  }
    			  if (category.equals(roomoffer.getClass().toString())&&(!roomoffer.getOffer_room_price().equals(text2.getText().toString()))){
    				  res.addProperty(getResources().getString(R.string.OFFER_ROOM_PRICE), text2.getText().toString());
    				  roomoffer.setOffer_room_price(text2.getText().toString());
    				  change = true;
    			  }
    			  if (category.equals(homeoffer.getClass().toString())&&(!homeoffer.getOffer_home_price().equals(text2.getText().toString()))){
    				  res.addProperty(getResources().getString(R.string.OFFER_HOME_PRICE), text2.getText().toString());
    				  roomoffer.setOffer_room_price(text2.getText().toString());
    				  change = true;
    			  }
    			  
    			  if (category.equals(roomsearch.getClass().toString())&&(!roomsearch.getSearch_room_maxprice().equals(text3.getText().toString()))){
    				  res.addProperty(getResources().getString(R.string.SEARCH_ROOM_MAXPRICE), text3.getText().toString());
    				  roomsearch.setSearch_room_maxprice(text3.getText().toString());
    				  change = true;
    			  }
    			  if (category.equals(homesearch.getClass().toString())&&(!homesearch.getSearch_home_maxprice().equals(text3.getText().toString()))){
    				  res.addProperty(getResources().getString(R.string.SEARCH_HOME_MAXPRICE), text3.getText().toString());
    				  homesearch.setSearch_home_maxprice(text3.getText().toString());
    				  change = true;
    			  }
    			  
    			  SharedPreferences userData = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    			  res.addProperty(getResources().getString(R.string.PSW_USER), userData.getString(getResources().getString(R.string.PSW_USER), null));
    			  res.addProperty(getResources().getString(R.string.EMAIL_USER), userData.getString(getResources().getString(R.string.EMAIL_USER), null));
    			  String url = getResources().getString(R.string.HOST) + getResources().getString(R.string.EDIT_AD);
    			  
    			  if (change){
    				  json.add(new BasicNameValuePair("json", res.toString()));
        			  try {
        		            String res2 = new Parser().execute(new DataHolders(url, json)).get();
        		            JsonElement msg = new JsonParser().parse(res2);
        		            JsonObject obj = msg.getAsJsonObject();
        		            if(obj.get("stato").getAsBoolean()){
        		            	if (category.equals(homeoffer.getClass().toString())){
        		            		g.getUserAds().set(index, homeoffer);
        		                } else if (category.equals(roomoffer.getClass().toString())){
        		                	g.getUserAds().set(index, roomoffer);
        		                } else if (category.equals(roomsearch.getClass().toString())){
        		                	g.getUserAds().set(index, roomsearch);
          		                } else if (category.equals(homesearch.getClass().toString())){
          		                	g.getUserAds().set(index, homesearch);
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
    				  getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    			  }
    			 
    		  }
    		  
    		  
    	  } else {

              Toast toast = Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.NO_INTERNET), Toast.LENGTH_SHORT);
              toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
              toast.show();
          }
    
    }
}
