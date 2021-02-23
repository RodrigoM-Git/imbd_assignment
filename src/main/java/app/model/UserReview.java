package app.model;

import java.util.Date;





public class UserReview {
    private int reviewID;
    private String username;
    private String review;
    private int rating;
    private Date date;
    private String orgName;






    public UserReview(String r, int v) {
        review = r;
        rating = v;
    }



    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
    	this.date = date;
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public String getUsername() {
    	return this.username;
    }
    
    public void setReviewID(int ID) {
    	this.reviewID = ID;
    }
    
    public int getReviewID() {
    	return this.reviewID;
    }


	public String getOrgName() {
		return orgName;
	}



	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
}
