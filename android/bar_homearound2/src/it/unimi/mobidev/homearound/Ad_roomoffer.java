package it.unimi.mobidev.homearound;

public class Ad_roomoffer {
	private String offer_room_id, offer_room_title, offer_room_description, offer_room_price, offer_room_mq, offer_room_type, offer_room_area,
    offer_room_city, offer_room_address,  offer_room_lat, offer_room_lng, offer_room_date, user_email;

public Ad_roomoffer(){
this(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
}

public Ad_roomoffer(String offer_room_id, String offer_room_title, String offer_room_description, String offer_room_price,
                String offer_room_mq, String offer_room_address, String offer_room_lat, String offer_room_lng, String offer_room_type, String offer_room_area, String offer_room_city, String offer_room_date, String offer_room_active, String user_email){
this.offer_room_id = offer_room_id;
this.offer_room_title = offer_room_title;
this.offer_room_description = offer_room_description;
this.offer_room_price = offer_room_price;
this.offer_room_mq = offer_room_mq;
this.offer_room_address = offer_room_address;
this.offer_room_lat = offer_room_lat;
this.offer_room_lng = offer_room_lng;
this.offer_room_type = offer_room_type;
this.offer_room_area = offer_room_area;
this.offer_room_city = offer_room_city;
this.offer_room_date = offer_room_date;
this.user_email = user_email;
}

public String getOffer_room_id(){
return offer_room_id;
}
public String getOffer_room_title(){
return offer_room_title;
}
public String getOffer_room_description(){
return offer_room_description;
}
public String getOffer_room_price(){
return offer_room_price;
}
public String getOffer_room_mq(){
return offer_room_mq;
}
public String getOffer_room_address(){
return offer_room_address;
}
public String getOffer_room_lat(){
return offer_room_lat;
}
public String getOffer_room_lng(){
return offer_room_lng;
}
public String getOffer_room_type(){
return offer_room_type;
}
public String getOffer_room_area(){
return offer_room_area;
}
public String getOffer_room_city(){
return offer_room_city;
}
public String getOffer_room_date(){
return offer_room_date;
}
public String getUser_email(){
return user_email;
}

public void setOffer_room_id(String offer_room_id) {
this.offer_room_id = offer_room_id;
}

public void setOffer_room_title(String offer_room_title) {
this.offer_room_title = offer_room_title;
}

public void setOffer_room_description(String offer_room_description) {
this.offer_room_description = offer_room_description;
}

public void setOffer_room_price(String offer_room_price) {
this.offer_room_price = offer_room_price;
}

public void setOffer_room_mq(String offer_room_mq) {
this.offer_room_mq = offer_room_mq;
}

public void setOffer_room_type(String offer_room_type) {
this.offer_room_type = offer_room_type;
}

public void setOffer_room_area(String offer_room_area) {
this.offer_room_area = offer_room_area;
}

public void setOffer_room_city(String offer_room_city) {
this.offer_room_city = offer_room_city;
}

public void setOffer_room_address(String offer_room_address) {
this.offer_room_address = offer_room_address;
}

public void setOffer_room_lat(String offer_room_lat) {
this.offer_room_lat = offer_room_lat;
}

public void setOffer_room_lng(String offer_room_lng) {
this.offer_room_lng = offer_room_lng;
}

public void setOffer_room_date(String offer_room_date) {
this.offer_room_date = offer_room_date;
}

public void setUser_email(String user_email) {
this.user_email = user_email;
}
}
