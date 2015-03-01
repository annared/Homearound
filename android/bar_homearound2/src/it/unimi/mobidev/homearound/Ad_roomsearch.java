package it.unimi.mobidev.homearound;

public class Ad_roomsearch {
	private String search_room_id, search_room_title, search_room_description, search_room_minprice, search_room_maxprice, search_room_type, search_room_area,
    search_room_city, search_room_date, user_email;
public Ad_roomsearch(){
this(null, null, null, null, null, null, null, null, null, null, null);
}
public Ad_roomsearch(String search_room_id, String search_room_title, String search_room_description, String search_room_minprice,
                 String search_room_maxprice, String search_room_type, String search_room_area, String search_room_city, String search_room_date, String search_room_active, String user_email){
this.search_room_id = search_room_id;
this.search_room_title = search_room_title;
this.search_room_description = search_room_description;
this.search_room_minprice = search_room_minprice;
this.search_room_maxprice = search_room_maxprice;
this.search_room_type = search_room_type;
this.search_room_area = search_room_area;
this.search_room_city = search_room_city;
this.search_room_date = search_room_date;
this.user_email = user_email;
}
public String getSearch_room_id(){
return search_room_id;
}
public String getSearch_room_title(){
return search_room_title;
}
public String getSearch_room_description(){
return search_room_description;
}
public String getSearch_room_minprice(){
return search_room_minprice;
}
public String getSearch_room_maxprice(){
return search_room_maxprice;
}
public String getSearch_room_type(){
return search_room_type;
}
public String getSearch_room_area(){
return search_room_area;
}
public String getSearch_room_city(){
return search_room_city;
}
public String getSearch_room_date(){
return search_room_date;
}
public String getUser_email(){
return user_email;
}

public void setSearch_room_id(String search_room_id) {
this.search_room_id = search_room_id;
}

public void setSearch_room_title(String search_room_title) {
this.search_room_title = search_room_title;
}

public void setSearch_room_description(String search_room_description) {
this.search_room_description = search_room_description;
}

public void setSearch_room_minprice(String search_room_minprice) {
this.search_room_minprice = search_room_minprice;
}

public void setSearch_room_maxprice(String search_room_maxprice) {
this.search_room_maxprice = search_room_maxprice;
}

public void setSearch_room_type(String search_room_type) {
this.search_room_type = search_room_type;
}

public void setSearch_room_area(String search_room_area) {
this.search_room_area = search_room_area;
}

public void setSearch_room_city(String search_room_city) {
this.search_room_city = search_room_city;
}

public void setSearch_room_date(String search_room_date) {
this.search_room_date = search_room_date;
}


public void setUser_email(String user_email) {
this.user_email = user_email;
}
}
