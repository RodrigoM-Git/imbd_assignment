package app.model;
//Class to create Entry objects to store
public class Entry {
	//Attributes
	private int entryId;
	private String showTitle;
	private String genre;
	private String showLength;
	private String showType;
	private String prodCompany;
	private String yearOfProduction;
	private String description;
	private String username;
	private int isRejected;
	
	public Entry(int entryId,String username, String showTitle,String genre, String showLength, String showType, String prodCompany, String yearOfProduction, String description,int isRejected) {
		this.setEntryId(entryId);
		this.username = username;
		this.showTitle = showTitle;
		this.genre = genre;
		this.showLength = showLength;
		this.showType = showType;
		this.prodCompany = prodCompany;
		this.yearOfProduction = yearOfProduction;
		this.description = description;
		this.setApproved(isRejected);
		
	}
	
	//Getters and Setters
	public String getShowTitle() {
		return showTitle;
	}

	public void setShowTitle(String showTitle) {
		this.showTitle = showTitle;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getShowLength() {
		return showLength;
	}

	public void setShowLength(String showLength) {
		this.showLength = showLength;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getProdCompany() {
		return prodCompany;
	}

	public void setProdCompany(String prodCompany) {
		this.prodCompany = prodCompany;
	}

	public String getYearOfProduction() {
		return yearOfProduction;
	}

	public void setYearOfProduction(String yearOfProduction) {
		this.yearOfProduction = yearOfProduction;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int isRejected() {
		return isRejected;
	}

	public void setApproved(int isApproved2) {
		this.isRejected = isApproved2;
	}

	public int getEntryId() {
		return entryId;
	}

	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}
	
}
