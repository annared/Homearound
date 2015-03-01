package it.unimi.mobidev.homearound;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


/**
 * Created by annared on 04/12/13.
 */
public class SetObject {
private String [] cat = {"offer_home", "offer_room", "search_home", "search_room"};
private Context c;
    public void setUserData(JsonObject response, Context context){
    	c = context;
        JsonArray userRes = response.getAsJsonArray("user_research");
        JsonObject userAds = response.getAsJsonObject("Ads");
        JsonElement j = response.get("favourite");
        
        JsonObject userFavs = new JsonObject();
        if (j.isJsonObject()){
            userFavs = response.getAsJsonObject("favourite");
        }

        Gson gson = new Gson();
        Globals g = ((Globals) context);
        try {
			Bitmap res = new getImage().execute(g.getS()).get();
			g.setAvatar(res);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        for (JsonElement e : userRes){
            Research r = gson.fromJson(e, Research.class);
            g.getUserRes().add(r);
            //g.addToUserRes(r);
        }
       
        for (String s : cat){
            if (!userAds.getAsJsonArray(s).isJsonNull()){
                for (JsonElement e : userAds.getAsJsonArray(s)){
                    if (s.equals("offer_home")){
                        Ad_homeoffer ho = gson.fromJson(e, Ad_homeoffer.class);
                        g.getUserAds().add(ho);
                        //g.addToUserAds(ho);
                    }
                    if (s.equals("offer_room")){
                        Ad_roomoffer hr = gson.fromJson(e, Ad_roomoffer.class);
                        g.getUserAds().add(hr);
                        //g.addToUserAds(hr);
                    }
                    if (s.equals("search_home")){
                        Ad_homesearch sh = gson.fromJson(e, Ad_homesearch.class);
                        g.getUserAds().add(sh);
                        //g.addToUserAds(sh);
                    }
                    if (s.equals("search_room")){
                        Ad_roomsearch sr = gson.fromJson(e, Ad_roomsearch.class);
                        g.getUserAds().add(sr);
                        //g.addToUserAds(sr);
                    }
                }
            }
            
            if (j.isJsonObject()){
                if (!userFavs.getAsJsonArray(s).isJsonNull()){
                    for (JsonElement e : userFavs.getAsJsonArray(s)){
                        if (s.equals("offer_home")){
                            Ad_homeoffer ho = gson.fromJson(e, Ad_homeoffer.class);
                            g.getUserFavs().add(ho);
                            //g.addToUserFavs(ho);
                        }
                        if (s.equals("offer_room")){
                            Ad_roomoffer hr = gson.fromJson(e, Ad_roomoffer.class);
                            g.getUserFavs().add(hr);
                            //g.addToUserFavs(hr);
                        }
                        if (s.equals("search_home")){
                            Ad_homesearch sh = gson.fromJson(e, Ad_homesearch.class);
                            g.getUserFavs().add(sh);
                            //g.addToUserFavs(sh);
                        }
                        if (s.equals("search_room")){
                            Ad_roomsearch sr = gson.fromJson(e, Ad_roomsearch.class);
                            g.getUserFavs().add(sr);
                            //g.addToUserFavs(sr);
                        }
                    }
                }
            }
        }
    }

    public ArrayList<Object> setResearch(JsonObject response, String c){
        Gson gson = new Gson();
        
        JsonArray resAds = response.getAsJsonArray(c);
        ArrayList<Object> ads = new ArrayList<Object>();
        for (JsonElement e : resAds){
            if (c.equals("offer_home")){
                Ad_homeoffer ho = gson.fromJson(e, Ad_homeoffer.class);
                ads.add(ho);
            }
            if (c.equals("offer_room")){
                Ad_roomoffer hr = gson.fromJson(e, Ad_roomoffer.class);
                ads.add(hr);
            }
            if (c.equals("search_home")){
                Ad_homesearch sh = gson.fromJson(e, Ad_homesearch.class);
                ads.add(sh);
            }
            if (c.equals("search_room")){
                Ad_roomsearch sr = gson.fromJson(e, Ad_roomsearch.class);
                ads.add(sr);
            }
        }
   
       
        return ads;
    }

    private class getImage extends AsyncTask<String, Void, Bitmap> {
    	@Override
        protected Bitmap doInBackground(String... string) {
            
    		InputStream in = null;
            try {
               URL url2 = new URL(c.getResources().getString(R.string.HOST) + "images/"+ string[0]);
               URLConnection urlConn = url2.openConnection();

               HttpURLConnection httpConn = (HttpURLConnection) urlConn;

               httpConn.connect();
               in = httpConn.getInputStream();

               } catch (MalformedURLException ex) {
            	   ex.printStackTrace();
               } catch (IOException ex) {
                   ex.printStackTrace();
               }
            return BitmapFactory.decodeStream(in);
        }

        protected void onPostExecute(Bitmap img) {
            
        }
    }

}

