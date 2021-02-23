package app.dao;

import app.dao.utils.DatabaseUtils;
import app.model.Account;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
	public static final String SALT = "$2a$10$h.dl5J86rGH7I8bD9bZeZe";

	/**
	 * Method to fetch users from the database. You should use this as an example
	 * for future queries, though the sql statement will change -and you are
	 * supposed to write them.
	 *
	 * Current user: caramel 6, password (the password is "password" without quotes)
	 * 
	 * @param username what the user typed in the log in form.
	 * @return Some of the user data to check on the password. Null if there no
	 *         matching user.
	 */
	public static Account getUserByUsername(String username) {
		// Fish out the results
		List<Account> accounts = new ArrayList<>();

		try {
			// Here you prepare your sql statement
			String sql = "SELECT username, password FROM account WHERE username ='" + username + "'";

			// Execute the query
			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			// If you have multiple results, you do a while
			while (result.next()) {
				// 2) Add it to the list we have prepared
				accounts.add(
						// 1) Create a new account object
						new Account(result.getString("username"), result.getString("password")));
			}

			// Close it
			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// If there is a result
		if (!accounts.isEmpty())
			return accounts.get(0);
		// If we are here, something bad happened
		return null;
	}

	public static Account getAccountByUsername(String user) {
		List<Account> accounts = new ArrayList<>();
		
		try {
			String sql = "SELECT * FROM account WHERE username = '" + user + "';";
			
			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			
			while(result.next()) {
				String type = result.getString("type");
				
				if(type.matches("Critic")) {
					accounts.add(new Account(type,result.getString("first_name"),result.getString("last_name"),
							result.getString("gender"),result.getString("birth_year"),result.getString("country")
							,result.getString("zip"),result.getString("username"),result.getString("email")
							,result.getString("password"), result.getString("orgName"), result.getString("orgPhone")));
				}else if(type.matches("PCO")) {
					accounts.add(new Account(type,result.getString("first_name"),result.getString("last_name")
							,result.getString("gender"),result.getString("birth_year"),result.getString("country")
							,result.getString("zip"),result.getString("username"),result.getString("email")
							,result.getString("password"), result.getString("orgName"), result.getString("orgPhone")
							,result.getString("proco_id")));
				}else {
					accounts.add(new Account(type,result.getString("first_name"),result.getString("last_name"),
							result.getString("gender"),result.getString("birth_year"),result.getString("country")
							,result.getString("zip"),result.getString("username"),result.getString("email")
							,result.getString("password")));
				}
			}
			
			DatabaseUtils.closeConnection(connection);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		if(!accounts.isEmpty()) return accounts.get(0);
		
		return null;
	}
	public static String getAccType(String username) {
		List<String> types = new ArrayList<>();

		try {
			String sql = "SELECT type FROM account WHERE username ='" + username + "'";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				types.add(result.getString("type"));
			}

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!types.isEmpty())
			return types.get(0);

		return null;
	}

	public static boolean checkIfUserExists(String username) {
		List<String> users = new ArrayList<>();

		try {
			String sql = "SELECT username FROM account WHERE username = '" + username + "'";
			String sql2 = "SELECT username FROM registration WHERE username = '" + username + "'";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			Statement statement2 = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			ResultSet result2 = statement2.executeQuery(sql2);

			while (result.next()) {
				users.add(result.getString("username"));
			}

			while (result2.next()) {
				users.add(result2.getString("username"));
			}

			DatabaseUtils.closeConnection(connection);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!users.isEmpty())
			return true;

		return false;

	}

	public static void addEntryToDatabase(Account user) {

		try {
			String sql = "INSERT INTO registration VALUES ('" + user.getType() + "','" + user.getFirstName() + "','"
					+ user.getLastName() + "','" + user.getGender() + "','" + user.getBirthYear() + "','"
					+ user.getCountry() + "','" + user.getZip() + "','" + user.getUsername() + "','" + user.getEmail()
					+ "','" + user.getPassword() + "','" + user.getOrg() + "','" + user.getNum() + "');";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			statement.execute(sql);

			DatabaseUtils.closeConnection(connection);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void registerUser(Account user) {

		String type = user.getType();
		String sql = "";

		if (type.matches("User")) {
			sql = "INSERT INTO account VALUES ('" + user.getType() + "','" + user.getUsername() + "','"
					+ user.getPassword() + "','" + user.getEmail() + "','" + user.getCountry() + "','" + user.getZip()
					+ "','" + user.getGender() + "','" + user.getFirstName() + "','" + user.getLastName() + "','"
					+ user.getBirthYear() + "',NULL,NULL,NULL);";
		} else if (type.matches("Critic")) {
			sql = "INSERT INTO account VALUES ('" + user.getType() + "','" + user.getUsername() + "','"
					+ user.getPassword() + "','" + user.getEmail() + "','" + user.getCountry() + "','" + user.getZip()
					+ "','" + user.getGender() + "','" + user.getFirstName() + "','" + user.getLastName() + "','"
					+ user.getBirthYear() + "','" + user.getOrg() + "','" + user.getNum() + "',NULL);";
		} else {
			String prodCo = getProdCompany(user.getOrg());
			String org = "";
			int proc_id = -1;

			if (prodCo != null) {
				org = prodCo;
				proc_id = getProcoID(org);
			}else {
				org = user.getOrg();
				registerProdCo(org);
				proc_id = getProcoID(org);
			}
			
			sql = "INSERT INTO account VALUES ('" + user.getType() + "','" + user.getUsername() + "','"
					+ user.getPassword() + "','" + user.getEmail() + "','" + user.getCountry() + "','" + user.getZip()
					+ "','" + user.getGender() + "','" + user.getFirstName() + "','" + user.getLastName() + "','"
					+ user.getBirthYear() + "','" + org + "','" + user.getNum() + "','" + proc_id + "');";
		}

		try {
			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			statement.execute(sql);
			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<Account> getRegistrationEntries() {
		List<Account> entries = new ArrayList<>();

		try {
			String sql = "SELECT * FROM registration";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				entries.add(new Account(result.getString("type"), result.getString("first_name"),
						result.getString("last_name"), result.getString("gender"), result.getString("birthYear"),
						result.getString("country"), result.getString("zip"), result.getString("username"),
						result.getString("email"), result.getString("password"), result.getString("orgName"),
						result.getString("orgPhone")));
			}

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!entries.isEmpty())
			return entries;

		return null;
	}

	public static Account getEntryByUsername(String username) {
		List<Account> entries = new ArrayList<>();

		try {
			String sql = "SELECT * FROM registration WHERE username = '" + username + "';";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				entries.add(new Account(result.getString("type"), result.getString("first_name"),
						result.getString("last_name"), result.getString("gender"), result.getString("birthYear"),
						result.getString("country"), result.getString("zip"), result.getString("username"),
						result.getString("email"), result.getString("password"), result.getString("orgName"),
						result.getString("orgPhone")));
			}

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!entries.isEmpty())
			return entries.get(0);

		return null;
	}

	public static void deleteRegistration(String username) {
		try {
			String sql = "DELETE FROM registration WHERE username = '" + username + "';";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			statement.execute(sql);

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getProdCompany(String prodCo) {
		List<String> companies = new ArrayList<>();

		try {
			String sql = "SELECT * FROM production_company WHERE LOWER(proco_name) like " + "LOWER('%" + prodCo
					+ "%');";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				companies.add(result.getString("proco_name"));
			}

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!companies.isEmpty())
			return companies.get(0);

		return null;
	}

	private static int getProcoID(String prodCo) {
		List<Integer> proco_id = new ArrayList<>();

		try {
			String sql = "SELECT * FROM production_company WHERE LOWER(proco_name) like " + "LOWER('%" + prodCo
					+ "%');";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				proco_id.add(result.getInt("proco_id"));
			}

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!proco_id.isEmpty())
			return proco_id.get(0);

		return -1;
	}
	
	private static int generateSuitableProcoID() {
		List<Integer> count = new ArrayList<>();

		try {
			String sql = "SELECT COUNT(proco_id) FROM production_company;";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				count.add(result.getInt("COUNT(proco_id)"));
			}

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		int newID = count.get(0) + 1;
		
		return newID;
	}
	
	private static void registerProdCo(String prodCo) {
		try {
			int id = generateSuitableProcoID();
			String sql = "INSERT INTO production_company VALUES ('" + id + "','" + prodCo + "');";
			
			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			statement.execute(sql);
			
			DatabaseUtils.closeConnection(connection);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
