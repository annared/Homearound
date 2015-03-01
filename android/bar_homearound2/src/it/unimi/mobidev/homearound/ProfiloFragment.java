package it.unimi.mobidev.homearound;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfiloFragment extends Fragment {
	public static final String PREFS_NAME = "HomeAround";

	protected Context context;
	private ListView listView;
	private ImageView imageView;
	private TextView textViewName;
	private Fragment fragment;
	private Button change, delete;
    private FragmentTransaction ft;
    private Globals g; 
    
    public ProfiloFragment(){
		
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profilo, container, false);
		g = ((Globals)getActivity().getApplicationContext());
		imageView = (ImageView)rootView.findViewById(R.id.imageView);
        textViewName = (TextView)rootView.findViewById(R.id.textView);
        listView = (ListView)rootView.findViewById(R.id.listView);
        change = (Button)rootView.findViewById(R.id.buttonMod);
        delete = (Button)rootView.findViewById(R.id.buttonDel);
        imageView.setImageBitmap(g.getAvatar());
		SharedPreferences userData = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		textViewName.setText(userData.getString("user_firstname", null)+" "+userData.getString("user_lastname", null));
		String [] list = getResources().getStringArray(R.array.profilo_list);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);
        change.setOnClickListener(new OnClickListener() {
        	  public void onClick(View view) {
        		  fragment = new ChangeProfileFragment();
                  ft = getActivity().getSupportFragmentManager().beginTransaction();
                  ft.replace(R.id.fragmentprofilo, fragment);
                  ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                  ft.addToBackStack(null);
                  ft.commit();
        	  }
        	});
        
        delete.setOnClickListener(new OnClickListener() {
      	  public void onClick(View view) {
      		 deleteUser();
      	  }
      	});
        
       
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        

        
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                	case 0:
	                	fragment = new MyAdsFragment();
	                    ft = getActivity().getSupportFragmentManager().beginTransaction();
	                    ft.replace(R.id.fragmentprofilo, fragment);
	                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	                    ft.addToBackStack(null);
	                    ft.commit();
	                    break;
                    
                    case 1:
                    	if (g.getUserFavs().size()<1){
                    		 Toast toast = Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.NO_ADS), Toast.LENGTH_SHORT);
                             toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                             toast.show();
                    	} else {
                    		fragment = new MyFavsFragment();
                            ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragmentprofilo, fragment);
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            ft.addToBackStack(null);
                            ft.commit();
                    	}
                        break;
                    case 2:
                    	fragment = new MyResFragment();
                        ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragmentprofilo, fragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                    case 3:
                       
                        break;
                }

            }
        });
        
		return rootView;
	
	}
	
	private void deleteUser(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle(getResources().getString(R.string.del_user));

        alertDialog.setMessage(getResources().getString(R.string.del_user_msg));
 
        alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	String url = getResources().getString(R.string.HOST) + getResources().getString(R.string.DELETE_USER);
                ArrayList<NameValuePair> del = new ArrayList<NameValuePair>(2);
                SharedPreferences userData = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                del.add(new BasicNameValuePair(getResources().getString(R.string.EMAIL_USER), userData.getString(getResources().getString(R.string.EMAIL_USER), null) ));
                del.add(new BasicNameValuePair(getResources().getString(R.string.PSW_USER), userData.getString(getResources().getString(R.string.PSW_USER), null)));
                
                try {
                    //pb.setVisibility(View.VISIBLE);
                    //String res = new Parser().execute(new DataHolders(url, newpsw)).get();
                    new Parser().execute(new DataHolders(url, del)).get();
                    userData.edit().clear().commit();
        		
        				Intent i = new Intent(getActivity(), LoginActivity.class);
        				getActivity().startActivity(i);
     
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } /*catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        });
  
        alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
  
        alertDialog.show();
	                 
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
	
	@Override
	public void onResume(){
		super.onResume();
		
		
	}
}
