package it.unimi.mobidev.homearound;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class GeobachecaFragment extends Fragment implements LocationListener, InfoWindowAdapter {
	
	private boolean home=false;
    private boolean room=false;
    final int RQS_GooglePlayServices = 1;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    protected Location location; 
    private ImageButton btnHome, btnRoom, refresh;
    
    private ArrayList<Ad_roomoffer> arrayRoom = new ArrayList<Ad_roomoffer>();
    private ArrayList<Ad_homeoffer> arrayHome = new ArrayList<Ad_homeoffer>();
    private ArrayList<Marker> markerListHome = new ArrayList<Marker>();
    private ArrayList<Marker> markerListRoom = new ArrayList<Marker>();
    private Marker marker;
    public static final String PREFS_NAME = "HomeAround";

	private SupportMapFragment fragment;
    private GoogleMap map;
    private FragmentManager fm;
    
    public GeobachecaFragment(){
		
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.fragment_geobacheca, container, false); 
    	btnHome = (ImageButton)rootView.findViewById(R.id.home);
        btnRoom = (ImageButton)rootView.findViewById(R.id.bed);
        refresh = (ImageButton)rootView.findViewById(R.id.refresh);
        fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
    	
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
    	}
        
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showHome();
            }
        });
        btnRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showRoom();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	btnRoom.setBackgroundResource(R.drawable.imagebutton);
            	btnHome.setBackgroundResource(R.drawable.imagebutton);
            	home=false; room=false;
                setUpMap();
            }
        });


        return rootView;
        
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       
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
    	   AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

           alertDialog.setTitle(getResources().getString(R.string.gps_title));

           alertDialog.setMessage(getResources().getString(R.string.gps_msg));
    
           alertDialog.setPositiveButton(getResources().getString(R.string.gps_settings), new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog,int which) {
                   Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                   getActivity().startActivity(intent);
               }
           });
     
           alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
               dialog.cancel();
               }
           });
     
           alertDialog.show();
       }
    }
    
    
    private void setUpMap(){
    	map = fragment.getMap();
    	checkProvider();
        Criteria criteria = new Criteria();
        location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        
        map.setMyLocationEnabled(true);
        if (location!=null){
        	 map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
             map.animateCamera(CameraUpdateFactory.zoomTo(14), 1000, null);
             downloadAds();
        }
        map.setOnInfoWindowClickListener(
         		new OnInfoWindowClickListener(){
     			    public void onInfoWindowClick(Marker marker){
     			    	if (markerListHome.contains(marker)){
     						showDetails(markerListHome.indexOf(marker), getString(R.string.offer_home));
     					} else if (markerListRoom.contains(marker)){
     						showDetails(markerListRoom.indexOf(marker), getString(R.string.offer_room));
     					}
     			    }
     			  }
         		);
        
       
    }
    
    private void downloadAds(){
    
    	markerListHome = new ArrayList<Marker>();;
    	markerListRoom = new ArrayList<Marker>();;
    	map.clear();
    	SharedPreferences userData = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        ArrayList<NameValuePair> data = new ArrayList<NameValuePair>(2);
        data.add(new BasicNameValuePair(getResources().getString(R.string.EMAIL_USER),  userData.getString(getResources().getString(R.string.EMAIL_USER), null)));
        data.add(new BasicNameValuePair(getResources().getString(R.string.PSW_USER),  userData.getString(getResources().getString(R.string.PSW_USER), null)));
        
        data.add(new BasicNameValuePair(getResources().getString(R.string.LAT), Double.toString(location.getLatitude())));
        data.add(new BasicNameValuePair(getResources().getString(R.string.LNG), Double.toString(location.getLongitude())));
        String url = getResources().getString(R.string.HOST) + getResources().getString(R.string.DOWNLOAD_GEO_BACHECA);
        try {
        	 Globals g = ((Globals)getActivity().getApplicationContext());
        	 
             Gson gson = new Gson();
             String res = new Parser().execute(new DataHolders(url, data)).get();
             JsonElement msg = new JsonParser().parse(res);
             JsonObject obj = msg.getAsJsonObject();
             JsonArray array = obj.getAsJsonArray("offer_home");
             JsonArray array2 = obj.getAsJsonArray("offer_room");
             if (obj.has("stato")){
            	 /*Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.error_login_msg), Toast.LENGTH_SHORT);
                 toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                 toast.show();*/
             } else {
            	 
            	 if (array.size()>0 && !array.isJsonNull()){
            		 arrayHome= new ArrayList<Ad_homeoffer>();
                	 for (JsonElement e : array){
                         Ad_homeoffer ho = gson.fromJson(e, Ad_homeoffer.class);
                         arrayHome.add(ho);
                         marker = map.addMarker(new MarkerOptions()
                         	.position(new LatLng(Double.parseDouble(ho.getOffer_home_lat()), Double.parseDouble(ho.getOffer_home_lng())))
                         	.title(ho.getOffer_home_type())
                         	.snippet(ho.getOffer_home_area())
                         	.visible(false));
                 		 markerListHome.add(marker);
                     }
                	 g.setArrayHome(arrayHome);
                 }
                 
                 if (array2.size()>0  && !array2.isJsonNull()){
                	 arrayRoom= new ArrayList<Ad_roomoffer>();
                	 for (JsonElement e : array2){
                		 Ad_roomoffer ro = gson.fromJson(e, Ad_roomoffer.class);
                		 arrayRoom.add(ro);
                		 marker = map.addMarker(new MarkerOptions()
                		 	.position(new LatLng(Double.parseDouble(ro.getOffer_room_lat()), Double.parseDouble(ro.getOffer_room_lng())))
                		 	.title(ro.getOffer_room_type())
                		 	.snippet(ro.getOffer_room_area())
                		 	.visible(false));
                 		 markerListRoom.add(marker);
                	 }
                	 g.setArrayRoom(arrayRoom);
                 }
                 

             }
             
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    
    private void showRoom(){
    	 if(!room){
         	btnRoom.setBackgroundResource(R.drawable.imagebuttonpressed);
         	for (Marker marker: markerListRoom){	
         		marker.setVisible(true);
         	}
         } else {
         	btnRoom.setBackgroundResource(R.drawable.imagebutton);
         	for (Marker m : markerListRoom){
         		m.setVisible(false);
         	}
         }
         room=!room;
    }
    private void showHome(){
    	if(!home){
            btnHome.setBackgroundResource(R.drawable.imagebuttonpressed);
            for (Marker marker: markerListHome){	
        		marker.setVisible(true);
        	}
        } else {
        	btnHome.setBackgroundResource(R.drawable.imagebutton);
        	for (Marker m : markerListHome){
        		m.setVisible(false);
        	}
        }
        home=!home;
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
    
    void showDetails(int index, String cat) {
    	Fragment fragment = new DetailFragment();
    	FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
    	Bundle args = new Bundle();
    	args.putString("Cat", cat);
    	args.putInt("index", index);
    	args.putString("segue", "geomaps");
    	fragment.setArguments(args);
    	ft.replace(R.id.mapcontainer, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
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
	public void onDetach() {
	    super.onDetach();

	    try {
	        Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
	        childFragmentManager.setAccessible(true);
	        childFragmentManager.set(this, null);

	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public void onDestroyView() {
	    super.onDestroyView();
	    try {
	        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));  
	        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
	        ft.remove(fragment);
	        ft.commit();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


}
