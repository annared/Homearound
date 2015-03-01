package it.unimi.mobidev.homearound;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AddMapsFragment extends Fragment implements LocationListener {
	private EditText address;
	private Button okButton, button1;
	private String category;
	private double lat, lng;
	private ProgressBar pb;
	public static final String PREFS_NAME = "HomeAround";
	
	final int RQS_GooglePlayServices = 1;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    protected Location location; 
	private SupportMapFragment fragment;
    private GoogleMap map;
    private FragmentManager fm;
	
	public AddMapsFragment(){
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_addmap, container, false);
        
        fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map3);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map3, fragment).commit();
    	}
    	
        
        okButton = (Button)rootView.findViewById(R.id.button);
        address = (EditText)rootView.findViewById(R.id.editText);
        category = getArguments().getString("Cat");
        button1 = (Button)rootView.findViewById(R.id.button1);
        pb = (ProgressBar)rootView.findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address.getText().toString().length()>1){
                    ArrayList<NameValuePair> coordinate = new ArrayList<NameValuePair>();
                    coordinate.add(new BasicNameValuePair("address", address.getText().toString()));
                    String city = new String();
                    Globals g = ((Globals)getActivity().getApplicationContext());
                    if (category.equals(getString(R.string.offer_home))){
                        city = g.getHomeoffer().getOffer_home_city();
                    } else if (category.equals(getString(R.string.offer_room))){
                        city = g.getRoomoffer().getOffer_room_city();
                    } else if (category.equals(getString(R.string.search_home))){
                        city = g.getHomesearch().getSearch_home_city();
                    } else if (category.equals(getString(R.string.search_room))){
                        city = g.getRoomsearch().getSearch_room_city();
                    }
                    String url = getResources().getString(R.string.HOST) + getResources().getString(R.string.TRANSLATE);
                    coordinate.add(new BasicNameValuePair("city", city));
                    String response = null;
                    try {
                    	Gson gson = new Gson();
                        response = new Parser().execute(new DataHolders(url, coordinate)).get();
                        JsonElement msg = new JsonParser().parse(response);
                        
                        JsonObject obj = msg.getAsJsonObject();
                        lat = gson.fromJson(obj.get("lat"), double.class);
                        lng = gson.fromJson(obj.get("lng"), double.class);
                        
                        addMarker(lat, lng);
                        
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        
        button1.setOnClickListener(new OnClickListener() {
        	
      	  public void onClick(View view) {
      	    sendData();
      	  }
      	});
        return rootView;
    }
	public void addMarker(double lt, double lg){
		if (map!=null){
    		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lt, lg), 14));
            
            map.addMarker(new MarkerOptions()
    	 	.position(new LatLng(lt, lg))
    	 	.visible(true));
            
            map.animateCamera(CameraUpdateFactory.zoomTo(14), 1000, null);
    	}
	}
	
	public void sendData(){
		
		ConnectionDetector cd = new ConnectionDetector(getActivity().getApplicationContext());
	  	if(cd.isConnectingToInternet()){
	  		Globals g = ((Globals)getActivity().getApplicationContext());
	    	SharedPreferences userData = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	    	Gson gson = new Gson();
	    	if (category.equals(getString(R.string.offer_home))){
	    		g.getHomeoffer().setOffer_home_address(address.getText().toString());
	    		g.getHomeoffer().setOffer_home_lat(Double.toString(lat));
	    		g.getHomeoffer().setOffer_home_lng(Double.toString(lng));
	    	} else {
	    		g.getRoomoffer().setOffer_room_address(address.getText().toString());
	    		g.getRoomoffer().setOffer_room_lat(Double.toString(lat));
	    		g.getRoomoffer().setOffer_room_lng(Double.toString(lng));
	    	}
	    	
	    	ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
	        String json = new String();
	        String url = getResources().getString(R.string.HOST) + getResources().getString(R.string.INSERISCI);
	        
	        if (category.equals(getString(R.string.offer_home))){
	            JsonElement jsonElement = gson.toJsonTree(g.getHomeoffer());
	            jsonElement.getAsJsonObject().addProperty(getString(R.string.PSW_USER), userData.getString(getString(R.string.PSW_USER), null));
	            jsonElement.getAsJsonObject().addProperty("categoria", category);
	            json = gson.toJson(jsonElement);

	        } else if (category.equals(getString(R.string.offer_room))){
	            JsonElement jsonElement = gson.toJsonTree(g.getRoomoffer());
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
	                if (category.equals(getString(R.string.offer_home))){
	                	g.getUserAds().add(g.getHomeoffer());
	                    g.setHomeoffer(null);
	                } else if (category.equals(getString(R.string.offer_room))){
	                    g.getUserAds().add(g.getRoomoffer());
	                    g.setRoomoffer(null);
	                }

	                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	                
	            } else {
	                Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.EMPTY_REG_MSG), Toast.LENGTH_SHORT);
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
	
	 public void onDestroyView() {
	    	super.onDestroyView();

	    	try {
	    	    Fragment fragment = (getFragmentManager().findFragmentById(R.id.map3));  
	    	    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
	    	    ft.remove(fragment);
	    	    ft.commit();
	    	} catch (Exception e) {
	    	    e.printStackTrace();
	    	}
	    }
	    
	    private void setUpMap(){
	    	map = fragment.getMap();
	    	checkProvider();
	        
	        Criteria criteria = new Criteria();
	        location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
	        if (location!=null){
	        	 map.setMyLocationEnabled(true);
	 	        
	 	        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
	 	        map.animateCamera(CameraUpdateFactory.zoomTo(14), 1000, null);
	        }
	       

	    }
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
	    public void onResume() {
	        super.onResume();
	        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext());
	        
	        if (resultCode == ConnectionResult.SUCCESS){
		         if (map == null) {
		             map = fragment.getMap();            
		         }
		         setUpMap();
	        }else{
	        	GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), RQS_GooglePlayServices).show();
	        }
	    }
	 
	 private void checkProvider(){
	    	
    	  boolean gps_enabled = false;
    	  boolean network_enabled = false;
    	        if(locationManager == null)
    	        	locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    	        try{
    	        	gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    	        }catch(Exception ex){
    	        	
    	        }
    	        try{
    	        	network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    	        }catch(Exception ex){
    	        	
    	        }

    	       if(!gps_enabled && !network_enabled){
    	            Builder dialog = new AlertDialog.Builder(context);
    	            dialog.setMessage("errore");
    	            dialog.setPositiveButton("open location", new DialogInterface.OnClickListener() {

    	                @Override
    	                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
    	                    // TODO Auto-generated method stub
    	                    Intent myIntent = new Intent( Settings.ACTION_SECURITY_SETTINGS );
    	                    context.startActivity(myIntent);
    	                   
    	                }
    	            });
    	            dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

    	                @Override
    	                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
    	                    // TODO Auto-generated method stub

    	                }
    	            });
    	            dialog.show();
    	       }
    }
	    
    


}
