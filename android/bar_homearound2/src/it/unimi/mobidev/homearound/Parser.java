package it.unimi.mobidev.homearound;


import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

/**
 * Created by annared on 19/11/13.
 */

public class Parser extends AsyncTask<DataHolders, Void, String> {
    public String result;
    @Override
    protected String doInBackground(DataHolders... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0].page);

            try{
                httppost.setEntity(new UrlEncodedFormEntity(params[0].dict));
                HttpResponse response = httpclient.execute(httppost);
                result = EntityUtils.toString(response.getEntity());

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return result;
    }
    @Override
    protected void onPostExecute(String results){
        super.onPostExecute(results);
    }
   
}
