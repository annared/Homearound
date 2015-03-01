package it.unimi.mobidev.homearound;

public class Ad_homesearch {
	private String search_home_id, search_home_title, search_home_description, search_home_minprice, search_home_maxprice, search_home_type, search_home_area,
    search_home_city, search_home_date, user_email;

   public Ad_homesearch(){
       this(null, null, null, null, null, null, null, null, null, null, null);
   }
   public Ad_homesearch(String search_home_id, String search_home_title, String search_home_description, String search_home_minprice,
    String search_home_maxprice, String search_home_type, String search_home_area, String search_home_city, String search_home_date, String search_home_active, String user_email){
       this.search_home_id = search_home_id;
       this.search_home_title = search_home_title;
       this.search_home_description = search_home_description;
       this.search_home_minprice = search_home_minprice;
       this.search_home_maxprice = search_home_maxprice;
       this.search_home_type = search_home_type;
       this.search_home_area = search_home_area;
       this.search_home_city = search_home_city;
       this.search_home_date = search_home_date;
       this.user_email = user_email;
   }
   public String getSearch_home_id(){
       return search_home_id;
   }
   public String getSearch_home_title(){
       return search_home_title;
   }
   public String getSearch_home_description(){
       return search_home_description;
   }
   public String getSearch_home_minprice(){
       return search_home_minprice;
   }
   public String getSearch_home_maxprice(){
       return search_home_maxprice;
   }
   public String getSearch_home_type(){
       return search_home_type;
   }
   public String getSearch_home_area(){
       return search_home_area;
   }
   public String getSearch_home_city(){
       return search_home_city;
   }
   public String getSearch_home_date(){
       return search_home_date;
   }
   public String getUser_email(){
       return user_email;
   }

   public void setSearch_home_id(String search_home_id) {
       this.search_home_id = search_home_id;
   }

   public void setSearch_home_title(String search_home_title) {
       this.search_home_title = search_home_title;
   }

   public void setSearch_home_description(String search_home_description) {
       this.search_home_description = search_home_description;
   }

   public void setSearch_home_minprice(String search_home_minprice) {
       this.search_home_minprice = search_home_minprice;
   }

   public void setSearch_home_maxprice(String search_home_maxprice) {
       this.search_home_maxprice = search_home_maxprice;
   }

   public void setSearch_home_type(String search_home_type) {
       this.search_home_type = search_home_type;
   }

   public void setSearch_home_area(String search_home_area) {
       this.search_home_area = search_home_area;
   }

   public void setSearch_home_city(String search_home_city) {
       this.search_home_city = search_home_city;
   }

   public void setSearch_home_date(String search_home_date) {
       this.search_home_date = search_home_date;
   }

   public void setUser_email(String user_email) {
       this.user_email = user_email;
   }
}
