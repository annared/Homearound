package it.unimi.mobidev.homearound;

import java.util.ArrayList;

import android.app.Application;
import android.graphics.Bitmap;

public class Globals extends Application{
    private ArrayList<Ad_homeoffer> home_offers = new ArrayList<Ad_homeoffer>();
    private  ArrayList<Ad_roomoffer> room_offers = new ArrayList<Ad_roomoffer>();
    private ArrayList<Ad_homesearch> home_searches = new ArrayList<Ad_homesearch>();
    private ArrayList<Ad_roomsearch> room_searches = new ArrayList<Ad_roomsearch>();

    private ArrayList<Ad_roomoffer> arrayRoom = new ArrayList<Ad_roomoffer>();
    private ArrayList<Ad_homeoffer> arrayHome = new ArrayList<Ad_homeoffer>();
    
    private ArrayList<Object> userAds = new ArrayList<Object>();
    private ArrayList<Object> userFavs = new ArrayList<Object>();
    private ArrayList<Research> userRes = new ArrayList<Research>();
          
    private String selectedCat, selectedCatRes;
    private boolean save;
    private Ad_homeoffer homeoffer;
    private Ad_homesearch homesearch;
    private Ad_roomoffer roomoffer;
    private Ad_roomsearch roomsearch;
    private Research research;
    
    
    private Bitmap avatar;
    private String s;
    //ARRAY di appoggio per i risultati della ricerca
    private ArrayList<Ad_homeoffer> res_home_offers = new ArrayList<Ad_homeoffer>();
    private  ArrayList<Ad_roomoffer> res_room_offers = new ArrayList<Ad_roomoffer>();
    private ArrayList<Ad_homesearch> res_home_searches = new ArrayList<Ad_homesearch>();
    private ArrayList<Ad_roomsearch> res_room_searches = new ArrayList<Ad_roomsearch>();

    public Research getResearch(){
    	return research;
    }
    
    public void setResearch(Research research){
    	this.research=research;
    }
    
    public Ad_homeoffer getHomeoffer() {
        return homeoffer;
    }

    public void setHomeoffer(Ad_homeoffer homeoffer) {
        this.homeoffer = homeoffer;
    }

    public Ad_homesearch getHomesearch() {
        return homesearch;
    }

    public void setHomesearch(Ad_homesearch homesearch) {
        this.homesearch = homesearch;
    }

    public Ad_roomoffer getRoomoffer() {
        return roomoffer;
    }

    public void setRoomoffer(Ad_roomoffer roomoffer) {
        this.roomoffer = roomoffer;
    }

    public Ad_roomsearch getRoomsearch() {
        return roomsearch;
    }

    public void setRoomsearch(Ad_roomsearch roomsearch) {
        this.roomsearch = roomsearch;
    }

    public void setSelectedCat(String cat){
        this.selectedCat = cat;
    }
    
    public ArrayList<Ad_homeoffer> getHome_offers(){
        return home_offers;
    }
    public ArrayList<Ad_homesearch> getHome_searches(){
        return home_searches;
    }
    public ArrayList<Ad_roomoffer> getRoom_offers(){
        return room_offers;
    }
    public ArrayList<Ad_roomsearch> getRoom_searches(){
        return room_searches;
    }
    public String getSelectedCat(){
        return selectedCat;
    }

    public void reset(){
        home_offers = new ArrayList<Ad_homeoffer>();
        room_offers = new ArrayList<Ad_roomoffer>();
        home_searches = new ArrayList<Ad_homesearch>();
        room_searches = new ArrayList<Ad_roomsearch>();

    }

	public ArrayList<Ad_roomoffer> getArrayRoom() {
		return arrayRoom;
	}

	public void setArrayRoom(ArrayList<Ad_roomoffer> arrayRoom) {
		this.arrayRoom = arrayRoom;
	}

	public ArrayList<Ad_homeoffer> getArrayHome() {
		return arrayHome;
	}

	public void setArrayHome(ArrayList<Ad_homeoffer> arrayHome) {
		this.arrayHome = arrayHome;
	}

	public ArrayList<Object> getUserAds() {
		return userAds;
	}

	public void setUserAds(ArrayList<Object> userAds) {
		this.userAds = userAds;
	}

	public ArrayList<Object> getUserFavs() {
		return userFavs;
	}

	public void setUserFavs(ArrayList<Object> userFavs) {
		this.userFavs = userFavs;
	}
	
	public ArrayList<Research> getUserRes() {
		return userRes;
	}

	public void setUserRes(ArrayList<Research> userRes) {
		this.userRes = userRes;
	}

	public boolean getSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
	}

	public String getSelectedCatRes() {
		return selectedCatRes;
	}

	public void setSelectedCatRes(String selectedCatRes) {
		this.selectedCatRes = selectedCatRes;
	}
	
	
	//getter e setter per array appoggio ricerca
	public ArrayList<Ad_homeoffer> getRes_home_offers() {
		return res_home_offers;
	}

	public void setRes_home_offers(ArrayList<Ad_homeoffer> res_home_offers) {
		this.res_home_offers = res_home_offers;
	}

	public ArrayList<Ad_roomoffer> getRes_room_offers() {
		return res_room_offers;
	}

	public void setRes_room_offers(ArrayList<Ad_roomoffer> res_room_offers) {
		this.res_room_offers = res_room_offers;
	}

	public ArrayList<Ad_homesearch> getRes_home_searches() {
		return res_home_searches;
	}

	public void setRes_home_searches(ArrayList<Ad_homesearch> res_home_searches) {
		this.res_home_searches = res_home_searches;
	}

	public ArrayList<Ad_roomsearch> getRes_room_searches() {
		return res_room_searches;
	}

	public void setRes_room_searches(ArrayList<Ad_roomsearch> res_room_searches) {
		this.res_room_searches = res_room_searches;
	}

	public Bitmap getAvatar() {
		return avatar;
	}

	public void setAvatar(Bitmap avatar) {
		this.avatar = avatar;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}
	
}
