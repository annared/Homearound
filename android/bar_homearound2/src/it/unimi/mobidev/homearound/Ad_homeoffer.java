package it.unimi.mobidev.homearound;

public class Ad_homeoffer {
	private String offer_home_id, offer_home_title, offer_home_description, offer_home_price, offer_home_mq, offer_home_type, offer_home_area,
    offer_home_city, offer_home_address,  offer_home_lat, offer_home_lng, offer_home_date, user_email;

public Ad_homeoffer(){
this(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
}
public Ad_homeoffer(String offer_home_id, String offer_home_title, String offer_home_description, String offer_home_price,
                 String offer_home_mq, String offer_home_address, String offer_home_lat, String offer_home_lng, String offer_home_type, String offer_home_area, String offer_home_city, String offer_home_date, String offer_home_active, String user_email){
this.offer_home_id = offer_home_id;
this.offer_home_title = offer_home_title;
this.offer_home_description = offer_home_description;
this.offer_home_price = offer_home_price;
this.offer_home_mq = offer_home_mq;
this.offer_home_address = offer_home_address;
this.offer_home_lat = offer_home_lat;
this.offer_home_lng = offer_home_lng;
this.offer_home_type = offer_home_type;
this.offer_home_area = offer_home_area;
this.offer_home_city = offer_home_city;
this.offer_home_date = offer_home_date;
this.user_email = user_email;

}
public String getOffer_home_id(){
return offer_home_id;
}
public String getOffer_home_title(){
return offer_home_title;
}
public String getOffer_home_description(){
return offer_home_description;
}
public String getOffer_home_price(){
return offer_home_price;
}
public String getOffer_home_mq(){
return offer_home_mq;
}
public String getOffer_home_address(){
return offer_home_address;
}
public String getOffer_home_lat(){
return offer_home_lat;
}
public String getOffer_home_lng(){
return offer_home_lng;
}
public String getOffer_home_type(){
return offer_home_type;
}
public String getOffer_home_area(){
return offer_home_area;
}
public String getOffer_home_city(){
return offer_home_city;
}
public String getOffer_home_date(){
return offer_home_date;
}
public String getUser_email(){
return user_email;
}

public void setOffer_home_id(String offer_home_id) {
this.offer_home_id = offer_home_id;
}

public void setOffer_home_title(String offer_home_title) {
this.offer_home_title = offer_home_title;
}

public void setOffer_home_description(String offer_home_description) {
this.offer_home_description = offer_home_description;
}

public void setOffer_home_price(String offer_home_price) {
this.offer_home_price = offer_home_price;
}

public void setOffer_home_mq(String offer_home_mq) {
this.offer_home_mq = offer_home_mq;
}

public void setOffer_home_type(String offer_home_type) {
this.offer_home_type = offer_home_type;
}

public void setOffer_home_area(String offer_home_area) {
this.offer_home_area = offer_home_area;
}

public void setOffer_home_city(String offer_home_city) {
this.offer_home_city = offer_home_city;
}

public void setOffer_home_address(String offer_home_address) {
this.offer_home_address = offer_home_address;
}

public void setOffer_home_lat(String offer_home_lat) {
this.offer_home_lat = offer_home_lat;
}

public void setOffer_home_lng(String offer_home_lng) {
this.offer_home_lng = offer_home_lng;
}

public void setOffer_home_date(String offer_home_date) {
this.offer_home_date = offer_home_date;
}

public void setUser_email(String user_email) {
this.user_email = user_email;
}
}
