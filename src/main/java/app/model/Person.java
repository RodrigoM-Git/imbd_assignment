package app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class Person {
    private int personId;
    private String fullName;
    private String role;
    private String bio;
    private Date birthdate;

    private List<Image> images;


    public Person(int id, String fn, String r, Date bd, String b) {
        personId = id;
        fullName = fn;
        role = r;
        birthdate = bd;
        bio = b;
        images = new ArrayList<Image>();
    }



    public String getRole() {
        return role;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public String getFullName() {
        return fullName;
    }

    public int getPersonId() {
        return personId;
    }

    public String getBio() {
        return bio;
    }
    
    public String getDetails() {
    	String output = "";
    	output += "ID: " + this.personId + "\n";
    	output += "Name: " + this.fullName + "\n";
    	output += "Role: " + this.role + "\n";
    	output += "Birthday: " + this.birthdate + "\n";
    	output += "Bio: " + this.bio;
    	
    	return output;
    }

    public boolean addImage(Image image){
        return images.add(image);
    }

    public boolean removeImage(Image image){
        return images.remove(image);
    }

    public List<Image> getImages(){
        return images;
    }
}
