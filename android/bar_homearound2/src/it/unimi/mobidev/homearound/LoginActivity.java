package it.unimi.mobidev.homearound;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.internal.u;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LoginActivity extends Activity {
    public static final String PREFS_NAME = "HomeAround";
    private EditText email, psw;
    private ProgressBar pb;
    private Switch aSwitch;
    private Button lostpsw;
    private Globals g; 
    private SharedPreferences userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText)findViewById(R.id.email);
        psw = (EditText)findViewById(R.id.psw);
        pb = (ProgressBar)findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
        lostpsw = (Button)findViewById(R.id.lostpsw);
        userData = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        aSwitch = (Switch)findViewById(R.id.switch1);
        email.setText(userData.getString(getResources().getString(R.string.EMAIL_USER), null));
        psw.setText(userData.getString(getResources().getString(R.string.PSW_USER), null));
        lostpsw.setOnClickListener(new OnClickListener() {
      	  public void onClick(View view) {
      		lostPsw();
      	  }
      	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                CheckData();
                return true;
            case R.id.register:
                Intent j = new Intent(LoginActivity.this, NewAccountActivity.class);
                LoginActivity.this.startActivity(j);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void CheckData() {
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        g = ((Globals)getApplicationContext());
        g.reset();
        g.setUserRes(new ArrayList<Research>());
        g.setUserAds(new ArrayList<Object>());
        g.setUserFavs(new ArrayList<Object>());
        if(cd.isConnectingToInternet()){
            if (email.getText().toString().length()>0 && psw.getText().toString().length()>0){

                ArrayList<NameValuePair> user = new ArrayList<NameValuePair>(2);
                user.add(new BasicNameValuePair(getResources().getString(R.string.EMAIL_USER), email.getText().toString()));
                user.add(new BasicNameValuePair(getResources().getString(R.string.PSW_USER), psw.getText().toString()));

                String url = getResources().getString(R.string.HOST) + getResources().getString(R.string.PHP_LOGIN);

                try {
                    pb.setVisibility(View.VISIBLE);
                    String res = new Parser().execute(new DataHolders(url, user)).get();
                    pb.setVisibility(View.GONE);

                    JsonElement msg = new JsonParser().parse(res);
                    JsonObject obj = msg.getAsJsonObject();
                    
                    if (obj.has("stato")){
                        Toast toast = Toast.makeText(this, getResources().getString(R.string.error_login_msg), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    } else {
                    	
                        SharedPreferences.Editor editor = userData.edit();
                        editor.putString(getResources().getString(R.string.EMAIL_USER), email.getText().toString());
                        editor.putString(getResources().getString(R.string.PSW_USER), psw.getText().toString());
                        Gson gson = new Gson();
                        JsonArray jsonUser = obj.getAsJsonArray("user");

                        for (JsonElement e : jsonUser){
                            User u = gson.fromJson(e, User.class);
                            editor.putString("user_firstname", u.getUser_firstname());
                            editor.putString("user_lastname", u.getUser_lastname());
                            editor.putString("user_avatar", u.getUser_avatar());
                            g.setS(u.getUser_avatar());
                        }
                        editor.commit();
                        
                        if (aSwitch.isChecked()){
                        	g.setSave(true);
                            
                        } else {
                        	g.setSave(false);
                            
                        }

                     
                        new SetObject().setUserData(obj, this.getApplicationContext());
                        //Intent i = new Intent(LoginActivity.this, BachecaActivity.class);
                        //LoginActivity.this.startActivity(i);
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } /*catch (JSONException e) {
                    e.printStackTrace();
                }*/

            } else {
                Toast toast = Toast.makeText(this, getResources().getString(R.string.error_empty_login), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        } else {

            Toast toast = Toast.makeText(this, getResources().getString(R.string.NO_INTERNET), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

    }
    private void lostPsw(){
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);

        alertDialog.setTitle(getResources().getString(R.string.newpassword));
        alertDialog.setMessage(getResources().getString(R.string.enteremail));
        final EditText input = new EditText(LoginActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
          input.setLayoutParams(lp);
          alertDialog.setView(input);
          alertDialog.setPositiveButton(getResources().getString(R.string.yes),
                  new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog,int which) {
	                       if (input.getText().length()>0){

	                           ArrayList<NameValuePair> newpsw = new ArrayList<NameValuePair>(2);
	                           newpsw.add(new BasicNameValuePair(getResources().getString(R.string.EMAIL_USER), email.getText().toString()));
	                           String url = getResources().getString(R.string.HOST) + getResources().getString(R.string.USER_LOST_PSW);
	                           try {
	                               pb.setVisibility(View.VISIBLE);
	                               //String res = new Parser().execute(new DataHolders(url, newpsw)).get();
	                               new Parser().execute(new DataHolders(url, newpsw)).get();
	                               pb.setVisibility(View.GONE);
	                
	                           } catch (InterruptedException e) {
	                               e.printStackTrace();
	                           } catch (ExecutionException e) {
	                               e.printStackTrace();
	                           } /*catch (JSONException e) {
	                               e.printStackTrace();
	                           }*/
	                       }  else {
	                    	   dialog.cancel();
	                       }

                      }
                  });
          
          alertDialog.setNegativeButton(getResources().getString(R.string.no),
                  new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int which) {
                         
                          dialog.cancel();
                      }
                  });
          alertDialog.show();
    }
    
    @Override
    public void onBackPressed(){
    	finish();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
   
}
