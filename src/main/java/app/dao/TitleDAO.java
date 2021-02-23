package app.dao;

import app.dao.utils.DatabaseUtils;
import app.model.CreditsRoll;
import app.model.Image;
import app.model.Person;
import app.model.ProductionCompany;
import app.model.Show;
import app.model.UserReview;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TitleDAO {

	public static List<Show> getTitleQueryResult(String userInput) {
		// create a new array list to store the result from the database
		List<Show> results = new ArrayList<>();

		try {
			// query to be passed to the database
			String sql = "SELECT * FROM imbd.show " + "WHERE LOWER(show_title) LIKE LOWER('%" + userInput + "%');";

			// Establishing connection to the database
			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			// As long as there is result
			while (result.next()) {
				// Instantiate a new show object
				Show show = createShow(result, connection);

				// add it to the array list
				results.add(show);
			}
			// Disconnect the connection to the database
			DatabaseUtils.closeConnection(connection);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!results.isEmpty()) {
			// return all results from database
			return results;
		}
		// else, return null
		return null;
	}

	public static Show getTitleFromId(int show_id) {

		Show show;

		try {
			// Query to be passed to the SQL
			String sql = "SELECT * FROM imbd.show " + "WHERE showid LIKE " + show_id + ";";

			// Establishing connection
			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			if (result.next()) {
				show = createShow(result, connection);
			} else {
				show = null;
			}
			// Disconnect the connection to the database
			DatabaseUtils.closeConnection(connection);

		} catch (Exception e) {
			show = null;
		}

		return show;
	}

	private static Show createShow(ResultSet result_set, Connection connection) throws SQLException {
		// store the result from database
		String showID = result_set.getString("showid");
		String showTitle = result_set.getString("show_title");
		String length = result_set.getString("length");
		String movie = result_set.getString("movie");
		String series = result_set.getString("series");
		String genre = result_set.getString("genre");
		int proco_id = result_set.getInt("proco_id");
		String year = result_set.getString("year");
		String description = result_set.getString("description");
		int yearInt = Integer.parseInt(year);

		Double lengthDbl = Double.parseDouble(length);
		int showIDInt = Integer.parseInt(showID);

		boolean isMovie = false;
		boolean isSeries = false;

		if (movie.contentEquals("1")) {
			isMovie = true;
		} else if (series.contentEquals("1")) {
			isSeries = true;
		}
		// Instantiate a new show object
		Show show = new Show(showIDInt, showTitle, lengthDbl, isMovie, isSeries, genre, proco_id,yearInt, description);
		show.setCreditsRolls(getCreditsRoll(showIDInt));

		List<Image> show_images = getImagesForShow(show.getShowID(), connection);
		for (int i = 0; i < show_images.size(); i++) {
			show.addImage(show_images.get(i));
		}
		
		List<UserReview> userReviewList = ReviewDAO.getMovieReviews(showIDInt);
		show.setReviews(userReviewList);
		
		ProductionCompany PCO = getPCO(showIDInt);
		show.setPCO(PCO);

		return show;
	}

	private static List<Image> getImagesForShow(int showIDInt, Connection connection) {
		List<Image> show_images = new ArrayList<Image>();
		try {
			// query to be passed to the database
			String sql = String.format(
					"SELECT image.is_person, image.file_id FROM imbd.image, imbd.show_image WHERE show_image.show_id = %d AND image.image_id = show_image.image_id;",
					showIDInt);
			// Establishing connection to the database
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			if (result.next()) {
				Image image = new Image(result.getInt("file_id"), result.getBoolean("is_person"));
				show_images.add(image);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return show_images;
	}

	public static List<CreditsRoll> getCreditsRoll(int show_id) {
		// create a new array list to store the result from the database
		List<CreditsRoll> credits_roll = new ArrayList<>();

		try {
			// query to be passed to the database
			String sql = "SELECT * FROM imbd.credits_roll " + "WHERE show_id LIKE " + show_id + ";";

			// Establishing connection to the database
			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			// As long as there is result
			while (result.next()) {
				// Instantiate a new show object
				Person person = ActorDAO.getActorFromId(Integer.parseInt(result.getString("person_id")));
				CreditsRoll roll = new CreditsRoll(person, result.getString("role"),
						Integer.parseInt(result.getString("start_year")));
				roll.setCharacter(result.getString("character_name"));
				roll.setEndYear(Integer.parseInt(result.getString("end_year")));

				// add it to the array list
				credits_roll.add(roll);
			}
			// Disconnect the connection to the database
			DatabaseUtils.closeConnection(connection);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!credits_roll.isEmpty()) {
			// return all results from database
			return credits_roll;
		}
		// else, return null
		return null;
	}

	public static ProductionCompany getPCO(int show_id) {
		ProductionCompany PCO = null;
		try {
			// query to be passed to the database
			String sql = "SELECT proco_name FROM production_company join imbd.show where production_company.proco_id = imbd.show.proco_id and showid =  " + show_id+ ";";

			// Establishing connection to the database
			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			
			while(result.next()) {
				PCO = new ProductionCompany(result.getString("proco_name"));
			}

			// Disconnect the connection to the database
			DatabaseUtils.closeConnection(connection);
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return PCO;
	}

	public static void insertShow(Show ns) {
		try {
			int isMovie = 0;
			int isSeries = 0;
			if(ns.getIsMovie()) {
				isMovie = 1;
			}
			else if (ns.getIsSeries()) {
				isSeries = 1;
			}
			String sql = "INSERT INTO imbd.show VALUES (" + ns.getShowID() + ", '" + ns.getShowTitle() +"','" + ns.getGenre() 
			+ "'," + ns.getLength() + "," + isMovie + "," + isSeries + "," + ns.getProco_id() + "," + ns.getYear() + ",'" +
			ns.getDescription()	 + "');";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			statement.execute(sql);

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void editShow(int id, String title, String genre, String length, String year, String desc) {
		try {
			String sql = "UPDATE imbd.show SET show_title = '" + title + "', genre = '" + genre + "', length = '" +
					length + "', year = '" + year + "', description = '" + desc + "' WHERE showid = " + id + ";"; 
			
			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			statement.execute(sql);
			DatabaseUtils.closeConnection(connection);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}

