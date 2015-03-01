package it.unimi.mobidev.homearound;

public class Research {
    private String user_email, research_city, research_id, research_area, research_type, research_cat, research_price;
    
    public Research(String research_id, String user_email, String research_city, String research_cat, String research_price,  String research_area,
                    String research_type){
        this.setUser_email(user_email);
        this.research_city=research_city;
        this.research_id=research_id;
        this.research_area=research_area;
        this.research_cat=research_cat;
        this.research_price=research_price;
        this.research_type=research_type;
    }
    public String getResearch_id(){
        return research_id;
    }
    public String getResearch_city(){
        return research_city;
    }
    public String getResearch_area(){
        return research_area;
    }
    public String getResearch_cat(){
        return research_cat;
    }
    public String getResearch_type(){
        return research_type;
    }
    public String getResearch_price(){
        return research_price;
    }
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
}
