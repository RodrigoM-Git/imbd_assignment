package app.dao;

import app.dao.utils.DatabaseUtils;
import app.model.Account;
import app.model.UserReview;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewDAO {
    
    public static List<UserReview> getMovieReviews(int showIDInt){
		List<UserReview> userReviewList = new ArrayList<>();
		
		try {
			String sql = "SELECT * FROM user_review WHERE show_id = " + showIDInt + ";";
			
			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			
			while(result.next()) {
				
				UserReview user_review = createUserReview(result, connection);
				userReviewList.add(user_review);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		if(!userReviewList.isEmpty()) {
			return userReviewList;
		}
		
		return null;
    }

    public static UserReview createUserReview(ResultSet result, Connection connection) throws SQLException, ParseException {
        int reviewID = Integer.parseInt(result.getString("reviewId"));
        String username = result.getString("user_id");
        String review = result.getString("review");
        int rating = Integer.parseInt(result.getString("rating"));
        String dateString = result.getString("date");
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
        String orgName = result.getString("orgName");
        UserReview user_review = new UserReview(review, rating);
        user_review.setDate(date);
        user_review.setReviewID(reviewID);
        user_review.setUsername(username);
        user_review.setOrgName(orgName);

        return user_review;
    }

    public static UserReview addMovieReviewBase(int showId, String userName, Account user){
    	String orgName = "None";
    	//Check if user is a critic
    	if(user.getType().matches("Critic")) {
    		//get the organization name
    		orgName = user.getOrg();
    	}
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String sql = String.format("INSERT INTO user_review(show_id, user_id, date, orgName) VALUES ('%d','%s', '%s', '%s');",
            showId, userName, df.format(date), orgName);
        UserReview userReview = null;
        try{
        Connection connection = DatabaseUtils.connectToDatabase();
        userReview = getReviewFromShowAndUser(showId, userName, connection);
        if (userReview == null){
            Statement statement = connection.createStatement();
            statement.execute(sql);
            userReview = getReviewFromShowAndUser(showId, userName, connection);
        }
        connection.close();
        } catch (Exception e){
            System.out.println("Exception occured");
            System.out.println(e.getMessage());
        }

        return userReview;
    }

    private static UserReview getReviewFromShowAndUser(int showID, String username, Connection connection){
        String sql = String.format("SELECT * FROM user_review WHERE show_id = '%d' AND user_id = '%s';",
            showID, username);
        UserReview userReview = null;
        try{
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);
        if (result.next()){
            userReview = createUserReview(result, connection);
        }
        } catch (Exception e){
            System.out.println("Error occured");
            System.out.println(e.getMessage());
        }
        return userReview;
    }
    
    public static void changeMovieReviewText(int reviewId, String reviewText){
        
        String sql = String.format("UPDATE user_review SET review = '%s' WHERE reviewId = '%d';",
            reviewText, reviewId);
        try{
        Connection connection = DatabaseUtils.connectToDatabase();
		Statement statement = connection.createStatement();
        statement.execute(sql);
        connection.close();
        } catch (Exception e){
            System.out.println("Exception occured");
            System.out.println(e.getMessage());
        }
    }
    
    public static void changeMovieRating(int reviewId, String rating) {
    	
    	String sql = String.format("UPDATE user_review SET rating = '%s' WHERE reviewId = '%d';"
    			,rating, reviewId);
    	try{
            Connection connection = DatabaseUtils.connectToDatabase();
    		Statement statement = connection.createStatement();
            statement.execute(sql);
            connection.close();
        } catch (Exception e){
            System.out.println("Exception occured");
            System.out.println(e.getMessage());
        }
    }

    public static void removeReview(int reviewID){
        String sql = String.format("DELETE FROM user_review WHERE reviewId = '%d'", reviewID);

        try{
            Connection connection = DatabaseUtils.connectToDatabase();
    		Statement statement = connection.createStatement();
            statement.execute(sql);
            connection.close();
        } catch (Exception e){
            System.out.println("Exception occured");
            System.out.println(e.getMessage());
        }
    }
}