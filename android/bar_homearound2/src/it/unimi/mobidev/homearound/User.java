package it.unimi.mobidev.homearound;

public class User {

    private String user_firstname, user_lastname, user_avatar;
    public User(String user_firstname, String user_lastname, String user_avatar){
        this.user_avatar = user_avatar;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
    }

    public String getUser_firstname(){
        return user_firstname;
    }
    public String getUser_lastname(){
        return user_lastname;
    }
    public String getUser_avatar(){
        return user_avatar;
    }

}
