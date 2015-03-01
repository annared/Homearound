package it.unimi.mobidev.homearound;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetailFragment extends Fragment {
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected Location location; 
	private TextView title1, title2, title3, title4, title5, title6, title7, title8;
	private TextView text1, text2, text3, text4, text5, text6, text7, text8;
	private String user_email;
	private ImageButton favs, mail;
	private Button edit;
	private Ad_roomsearch sr = new Ad_roomsearch();
	private Ad_roomoffer ro = new Ad_roomoffer();
	private Ad_homesearch sh = new Ad_homesearch();
	private Ad_homeoffer ho = new Ad_homeoffer();
	private boolean fav = false;
	private Globals g;
	private String cat, segue, ad_id;
	private SharedPreferences userData;
	private int index;
	private View rootView; 
	public static final String PREFS_NAME = "HomeAround";
	
	
	public DetailFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_detail, container, false);
		
		userData = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        favs = (ImageButton)rootView.findViewById(R.id.favs);
        mail = (ImageButton)rootView.findViewById(R.id.mail);
        edit = (Button)rootView.findViewById(R.id.edit);
        
        title1 = (TextView)rootView.findViewById(R.id.title1);
        title2 = (TextView)rootView.findViewById(R.id.title2);
        title3 = (TextView)rootView.findViewById(R.id.title3);
        title4 = (TextView)rootView.findViewById(R.id.title4);
        title5 = (TextView)rootView.findViewById(R.id.title5);
        title6 = (TextView)rootView.findViewById(R.id.title6);
        title7 = (TextView)rootView.findViewById(R.id.title7);
        title8 = (TextView)rootView.findViewById(R.id.title8);

        text1 = (TextView)rootView.findViewById(R.id.text1);
        text2 = (TextView)rootView.findViewById(R.id.text2);
        text3 = (TextView)rootView.findViewById(R.id.text3);
        text4 = (TextView)rootView.findViewById(R.id.text4);
        text5 = (TextView)rootView.findViewById(R.id.text5);
        text6 = (TextView)rootView.findViewById(R.id.text6);
        text7 = (TextView)rootView.findViewById(R.id.text7);
        text8 = (TextView)rootView.findViewById(R.id.text8);
        
        g = ((Globals)getActivity().getApplicationContext());
        cat = getArguments().getString("Cat");
        segue = getArguments().getString("segue");
        index = getArguments().getInt("index");
        setView(); 
       
        if (segue.equals("myads")) {
        	edit.setVisibility(View.VISIBLE);
        	mail.setVisibility(View.GONE);
        	favs.setVisibility(View.GONE);
        	edit.setOnClickListener(new View.OnClickListener(){
	        	 @Override
		            public void onClick(View v) {
	        		 edit();
	        	 }
	        });
        }
        
        mail.setOnClickListener(new View.OnClickListener(){
        	 @Override
	            public void onClick(View v) {
        		 openEmail();
        	 }
        });
        
        favs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	ConnectionDetector cd = new ConnectionDetector(getActivity().getApplicationContext());
          	  	if(cd.isConnectingToInternet()){
	          	  	ArrayList<NameValuePair> data = new ArrayList<NameValuePair>(2);
	                data.add(new BasicNameValuePair(getResources().getString(R.string.EMAIL_USER), userData.getString(getString(R.string.EMAIL_USER), null)));
	                data.add(new BasicNameValuePair(getResources().getString(R.string.PSW_USER), userData.getString(getString(R.string.PSW_USER), null)));
	                data.add(new BasicNameValuePair("ad_id", ad_id));
	                String url = getResources().getString(R.string.HOST) + getResources().getString(R.string.FAVS_INSERT);
	                
	                try {
	                    String res = new Parser().execute(new DataHolders(url, data)).get();
	                    JsonElement msg = new JsonParser().parse(res);
	                    
	                    JsonObject obj = msg.getAsJsonObject();
	                    JsonElement je = obj.get("messaggio");
	                    String s = je.getAsString();
	                    if(!obj.get("stato").getAsBoolean()){
	                    	Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.EMPTY_REG_MSG), Toast.LENGTH_SHORT);
	                        toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
	                        toast.show();
	                    } else {
	                    	check(s);

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
        });
        
    	return rootView;
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }
	
	private void edit(){
		 FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
         Fragment fragment = new EditAd();
         Bundle args = new Bundle();
         args.putString("Cat", cat);
         args.putInt("index", index);
         fragment.setArguments(args);
         ft.replace(R.id.linear, fragment);
         ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
         ft.addToBackStack(null);
         ft.commit();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	}
	
	private void check(String string){
		
    	if (string.equals("notfavs")){

    		if (cat.equals(getResources().getString(R.string.search_room))||(cat.equals(Ad_roomsearch.class.toString()))){
    			g.getUserFavs().add(sr);
        	} 
    		if (cat.equals(getResources().getString(R.string.search_home))||(cat.equals(Ad_homesearch.class.toString()))){
    			g.getUserFavs().add(sh);
        	}
    		if (cat.equals(getResources().getString(R.string.offer_home))||(cat.equals(Ad_homeoffer.class.toString()))){
    			g.getUserFavs().add(ho);
        	} 
    		if (cat.equals(getResources().getString(R.string.offer_room))||(cat.equals(Ad_roomoffer.class.toString()))){
    			g.getUserFavs().add(ro);
        	}
        	fav=true;
        	
    	} else if (string.equals("favs")){
    		
    		for (Object o : g.getUserFavs()){
    			
    			if (o.getClass().equals(sr.getClass())){
            		if (((Ad_roomsearch)o).getSearch_room_id().equals(sr.getSearch_room_id())) {
            			g.getUserFavs().remove(o);
            			break;
            		}
    			} else if (o.getClass().equals(sh.getClass())){
    				if (((Ad_homesearch)o).getSearch_home_id().equals(sh.getSearch_home_id())){
    					g.getUserFavs().remove(o);
    					break;
    				} 
    			} else if (o.getClass().equals(ho.getClass())){
    				if (((Ad_homeoffer)o).getOffer_home_id().equals(ho.getOffer_home_id())) {
    					g.getUserFavs().remove(o);
    					break;
    				}
    			} else if (o.getClass().equals(ro.getClass())){
    				if (((Ad_roomoffer)o).getOffer_room_id().equals(ro.getOffer_room_id())) {
    					g.getUserFavs().remove(o);
    					break;
    				}
    			}
    		}
    		
    	
    		fav=false;
    	}
    	
    	setFavs();
    }
	  
    private void openEmail(){
        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { user_email });
        i.setType("text/plain");
        startActivity(Intent.createChooser(i, "Send Email"));
    }
    
    private void setFavs(){
    	if (!fav){
    		favs.setBackgroundResource(R.drawable.empty);
    		if (segue.equals("favs")){
    			if (g.getUserFavs().size()==0) {
    				getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);	
    			}	else {
    				FragmentTransaction fm = getFragmentManager().beginTransaction();
    				fm.remove(this).commit();
    				getFragmentManager().popBackStack();
    				
    			}     			
    		}
    	} else {
    		favs.setBackgroundResource(R.drawable.fullstar);
    	}
    }
    
    private void setView(){    	
    	if ((cat.equals(getResources().getString(R.string.search_room)))||(cat.equals(getResources().getString(R.string.search_home)))
                ||(cat.equals(Ad_homesearch.class.toString()))||(cat.equals(Ad_roomsearch.class.toString()))){
            title5.setVisibility(View.GONE);
            text5.setVisibility(View.GONE);
        } 	        
        title1.setText(R.string.title);
        title2.setText(R.string.description);
        title3.setText(R.string.type);
        title4.setText(R.string.area);

        if (cat.equals(getResources().getString(R.string.search_room))||(cat.equals(Ad_roomsearch.class.toString()))){
            if (segue.equals("ads")){
                sr = g.getRoom_searches().get(index);
            } else if (segue.equals("myads")){
                sr = (Ad_roomsearch)g.getUserAds().get(index);
            } else if (segue.equals("favs")){
                sr = (Ad_roomsearch)g.getUserFavs().get(index);
            } else if (segue.equals("myres")){
            	sr = g.getRes_room_searches().get(index);
            }
            title6.setText(R.string.city);
            title7.setText(R.string.min_price);
            title8.setText(R.string.max_price);
            text1.setText(sr.getSearch_room_title());
            text2.setText(sr.getSearch_room_description());
            text3.setText(sr.getSearch_room_type());
            text4.setText(sr.getSearch_room_area());
            text6.setText(sr.getSearch_room_city());
            text7.setText(sr.getSearch_room_minprice());
            text8.setText(sr.getSearch_room_maxprice());
            user_email = sr.getUser_email();
            ad_id = sr.getSearch_room_id();
            
           
            for (Object o: g.getUserFavs()){
            	if (o.getClass().equals(sr.getClass())){
            		if (((Ad_roomsearch)o).getSearch_room_id().equals(sr.getSearch_room_id())){
                		fav=true;
                	}
            	}
            }
            
            
        } else if (cat.equals(getResources().getString(R.string.search_home))||(cat.equals(Ad_homesearch.class.toString()))){
            if (segue.equals("ads")){
                sh = g.getHome_searches().get(index);
            } else if (segue.equals("myads")){
                sh = (Ad_homesearch)g.getUserAds().get(index);
            } else if (segue.equals("favs")){
                sh = (Ad_homesearch)g.getUserFavs().get(index);
            } else if (segue.equals("myres")){
            	 sh = g.getRes_home_searches().get(index);
            }
            
            ad_id = sh.getSearch_home_id();
            title6.setText(R.string.city);
            title7.setText(R.string.min_price);
            title8.setText(R.string.max_price);
            text1.setText(sh.getSearch_home_title());
            text2.setText(sh.getSearch_home_description());
            text3.setText(sh.getSearch_home_type());
            text4.setText(sh.getSearch_home_area());
            text6.setText(sh.getSearch_home_city());
            text7.setText(sh.getSearch_home_minprice());
            text8.setText(sh.getSearch_home_maxprice());
            user_email = sh.getUser_email();

            for (Object o: g.getUserFavs()){
            	if (o.getClass().equals(sh.getClass())){
            		if (((Ad_homesearch)o).getSearch_home_id().equals(sh.getSearch_home_id())){
                		fav=true;
                	}
            	}
            }


        } else if (cat.equals(getResources().getString(R.string.offer_room))||(cat.equals(Ad_roomoffer.class.toString()))){
            
        	if (segue.equals("ads")){
                ro = g.getRoom_offers().get(index);
            } else if (segue.equals("myads")){
                ro = (Ad_roomoffer)g.getUserAds().get(index);
            } else if (segue.equals("favs")){
                ro = (Ad_roomoffer)g.getUserFavs().get(index);
            } else if (segue.equals("geomaps")){
            	ro = g.getArrayRoom().get(index);
            } else if (segue.equals("myres")){
           	 	ro = g.getRes_room_offers().get(index);
            }
        	
            ad_id = ro.getOffer_room_id();
            title5.setText(R.string.city);
            title6.setText(R.string.address);
            title7.setText(R.string.price);
            title8.setText(R.string.mq);
            text1.setText(ro.getOffer_room_title());
            text2.setText(ro.getOffer_room_description());
            text3.setText(ro.getOffer_room_type());
            text4.setText(ro.getOffer_room_area());
            text5.setText(ro.getOffer_room_city());
            text6.setText(ro.getOffer_room_address());
            text7.setText(ro.getOffer_room_price());
            text8.setText(ro.getOffer_room_mq());
            
            for (Object o: g.getUserFavs()){
            	if (o.getClass().equals(ro.getClass())){
            		if (((Ad_roomoffer)o).getOffer_room_id().equals(ro.getOffer_room_id())){
                		fav=true;
                	}
            	}
            }
        } else if (cat.equals(getResources().getString(R.string.offer_home))||(cat.equals(Ad_homeoffer.class.toString()))){
            if (segue.equals("ads")){
                ho = g.getHome_offers().get(index);
            } else if (segue.equals("myads")){
                ho = (Ad_homeoffer)g.getUserAds().get(index);
            } else if (segue.equals("favs")){
                ho = (Ad_homeoffer)g.getUserFavs().get(index);
            }  else if (segue.equals("geomaps")){
            	ho = g.getArrayHome().get(index);
            } else if (segue.equals("myres")){
           	 	ho = g.getRes_home_offers().get(index);
            }
          
            ad_id = ho.getOffer_home_id();
            title5.setText(R.string.city);
            title6.setText(R.string.address);
            title7.setText(R.string.price);
            title8.setText(R.string.mq);
            text1.setText(ho.getOffer_home_title());
            text2.setText(ho.getOffer_home_description());
            text3.setText(ho.getOffer_home_type());
            text4.setText(ho.getOffer_home_area());
            text5.setText(ho.getOffer_home_city());
            text6.setText(ho.getOffer_home_address());
            text7.setText(ho.getOffer_home_price());
            text8.setText(ho.getOffer_home_mq());
            user_email = ho.getUser_email();
            
            for (Object o: g.getUserFavs()){
            	if (o.getClass().equals(ho.getClass())){
            		if (((Ad_homeoffer)o).getOffer_home_id().equals(ho.getOffer_home_id())){
                		fav=true;
                	}
            	}
            }
        }
        setFavs();
    }

    
    @Override
    public void onResume() {
        super.onResume();
        setView();
    }
    
  
 
    
}
