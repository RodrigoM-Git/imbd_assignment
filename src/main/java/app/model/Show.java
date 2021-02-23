package app.model;

import java.util.ArrayList;
import java.util.List;

public class Show {

	// attributes for show class
	private int showid;
	private String showTitle;
	private double length;
	private boolean isMovie;
	private boolean isSeries;
	private String genre;
	private int proco_id;
	private int year;
	private String description;

	private List<Image> images;

	private List<UserReview> userReviewList;
	private ProductionCompany productionCompany;

	private List<CreditsRoll> creditsRolls;

	// Constructor
	public Show(int showid, String showTitle, double length, boolean isMovie, boolean isSeries, String genre, int proco_id, int year,
			String description) {
		this.showid = showid;
		this.showTitle = showTitle;
		this.length = length;
		this.isMovie = isMovie;
		this.isSeries = isSeries;
		this.genre = genre;
		this.setProco_id(proco_id);
		this.year = year;
		this.description = description;
		images = new ArrayList<Image>();
	}

	// Getters and Setters for the private attributes
	public int getShowID() {
		return this.showid;
	}

	public String getShowTitle() {
		return this.showTitle;
	}

	public double getLength() {
		return this.length;
	}

	public boolean getIsMovie() {
		return this.isMovie;
	}

	public boolean getIsSeries() {
		return this.isSeries;
	}

	public String getGenre() {
		return this.genre;
	}

	public int getYear() {
		return this.year;
	}

	public String getDescription() {
		return this.description;
	}

	
	// Method to output the details of the show as a string
	public String getDetails() {
		String output = "";
		output += "ID: " + this.showid + "\n";
		output += "Title: " + this.showTitle + "\n";
		output += "Length: " + this.length + " hours\n";

		if (this.isMovie) {
			output += "Type: Movie\n";
		} else if (this.isSeries) {
			output += "Type: Series\n";
		}

		output += "Genre: " + this.genre + "\n";
		output += "Year: " + this.year;
		output += "Description: " + this.description;

		return output;
	}

	public boolean addImage(Image image) {
		return images.add(image);
	}

	public boolean removeImage(Image image) {
		return images.remove(image);
	}

	public List<Image> getImages() {
		return images;
	}

	public void setCreditsRolls(List<CreditsRoll> creditsRolls) {
		this.creditsRolls = creditsRolls;
	}
	
	public List<CreditsRoll> getCreditsRolls(){
		return this.creditsRolls;
	}
    
    public List<UserReview> getReviews(){
    	return this.userReviewList;
    }
    
    public void setReviews(List<UserReview> UserReviewList) {
    	this.userReviewList = UserReviewList;
    }
    
    public void setPCO(ProductionCompany PCO) {
    	this.productionCompany = PCO;
    }
    
    public ProductionCompany getPCO() {
    	return this.productionCompany;
    }

	public int getProco_id() {
		return proco_id;
	}

	public void setProco_id(int proco_id) {
		this.proco_id = proco_id;
	}

}
