package app.model;



public class Account {
	private String accType;
	private String username;
	private String password; //only store hashed passwords
    private String email;
    private String country;
    private String zip;
    private String gender;
    private String firstName;
    private String lastName;
    private String birthYear;
    private String orgName;
    private String orgPh;
    private String procID;

    
    //Constructor for logging in
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }


    //Constructor for regular users
    public Account(String accType, String firstName, String lastName, String gender, String birthYear,
    		String country, String zip, String username, String email, String password) {
    	this.accType = accType;
    	this.username = username;
    	this.password = password;
    	this.email = email;
    	this.country = country;
    	this.zip = zip;
    	this.gender = gender;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.birthYear = birthYear;
    }
    
    //Constructor for Critic
    public Account(String accType, String firstName, String lastName, String gender, String birthYear,
    		String country, String zip, String username, String email, String password,
    		String orgName, String orgPh) {
    	this.accType = accType;
    	this.username = username;
    	this.password = password;
    	this.email = email;
    	this.country = country;
    	this.zip = zip;
    	this.gender = gender;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.birthYear = birthYear;
    	this.orgName = orgName;
    	this.orgPh = orgPh;
    }
    
    //Constructor for PCO
    public Account(String accType, String firstName, String lastName, String gender, String birthYear,
    		String country, String zip, String username, String email, String password,
    		String orgName, String orgPh, String procID) {
    	this.accType = accType;
    	this.username = username;
    	this.password = password;
    	this.email = email;
    	this.country = country;
    	this.zip = zip;
    	this.gender = gender;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.birthYear = birthYear;
    	this.orgName = orgName;
    	this.orgPh = orgPh;
    	this.procID = procID;
    }

    public void updateDetails() {
        // TODO
    }

    public String getType() {
		return this.accType;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public String getGender() {
		return this.gender;
	}
	
	public String getBirthYear() {
		return this.birthYear;
	}
	
	public String getCountry() {
		return this.country;
	}

	public String getZip() {
		return this.zip;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getOrg() {
		return this.orgName;
	}
	
	public String getNum() {
		return this.orgPh;
	}
	
	public String getID() {
		return this.procID;
	}
}
