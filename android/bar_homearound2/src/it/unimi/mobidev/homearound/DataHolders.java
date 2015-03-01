package it.unimi.mobidev.homearound;


import java.util.ArrayList;

import org.apache.http.NameValuePair;

/**
 * Created by annared on 19/11/13.
 */
public class DataHolders {
    String page;
    ArrayList<NameValuePair> dict;
    String path;
    
    public DataHolders(String page, ArrayList<NameValuePair> dict){
        this.page = page;
        this.dict = dict;

    }
    
    public DataHolders(String page, String path){
        this.page = page;
        this.path = path;

    }

}
