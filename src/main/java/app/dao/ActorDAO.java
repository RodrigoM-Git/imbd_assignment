package app.dao;

import app.dao.utils.DatabaseUtils;
import app.model.Image;
import app.model.Person;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;  

public class ActorDAO {
	
	public static List<Person> getActorQueryResult(String userInput) {
		//Array list of result from database
		List<Person> results = new ArrayList<>();
		
		try {
			//Query to be passed to the SQL
			String sql = "SELECT * FROM imbd.person "
					+ "WHERE LOWER(fullname) LIKE LOWER('%" + userInput + "%');";
			
			//Establishing connection
			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			
			while(result.next()) {
				// Create person through method
				Person person = createPerson(result, connection);
				//Add it to the arraylist
				results.add(person);
			}
			//close the connection
			DatabaseUtils.closeConnection(connection);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!results.isEmpty()) {
			//return all results from the database
			return results;
		}
		
		return null;
	}
	
	public static List<String> getShowFromActor(String userInput) {
		List<String> results = new ArrayList<>();
		
		try {
			//Query to be passed to SQL
			String sql = "SELECT show_id FROM imbd.credits_roll JOIN imbd.person " + 
					"WHERE credits_roll.person_id = person.person_id AND " + 
					"LOWER(fullName) LIKE LOWER('%" + userInput + "%');";
			
			
			
			//Establishing connection
			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			
			while(result.next()) {
				results.add(result.getString("show_id"));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	

	public static Person getActorFromId(int person_id){
		
		Person person;

		try{
			//Query to be passed to the SQL
			String sql = "SELECT * FROM imbd.person "
					+ "WHERE person_id LIKE " + person_id + ";";
			
			//Establishing connection
			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			if (result.next()){
				// create person through method
				person = createPerson(result, connection);
			} else {
				person = null;
			}

			
		} catch (Exception e){
			person = null;
		}

		return person;
	}

	private static Person createPerson(ResultSet result_set, Connection connection) throws SQLException, ParseException{
		//Store the data gained from SQL to each field/attribute
		String ID = result_set.getString("person_id");
		int personID = Integer.parseInt(ID);
		String fullName = result_set.getString("fullname");
		String role = result_set.getString("role");
		String bd = result_set.getString("birthdate");
		Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(bd);
		String bio = result_set.getString("bio");
		
		//Instantiate a person object
		Person person = new Person(personID, fullName, role, birthDate, bio);

		List<Image> person_images = getImagesForShow(person.getPersonId(), connection);
		for (int i = 0; i < person_images.size(); i++){
			person.addImage(person_images.get(i));
		}
		return person;
	} 

	private static List<Image> getImagesForShow(int personIDInt, Connection connection){
		List<Image> person_images = new ArrayList<Image>();
		try{
			//query to be passed to the database
			String sql = String.format("SELECT image.is_person, image.file_id FROM imbd.image, imbd.person_image WHERE person_image.person_id = %d AND image.image_id = person_image.image_id;",
			personIDInt);
			//Establishing connection to the database
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			if (result.next()){
				Image image = new Image(result.getInt("file_id"), result.getBoolean("is_person"));
				person_images.add(image);
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		return person_images;
	}
}
