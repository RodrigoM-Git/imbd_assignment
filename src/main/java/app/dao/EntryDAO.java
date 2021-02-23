package app.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.dao.utils.DatabaseUtils;
import app.model.Entry;

public class EntryDAO {

	public static List<Entry> getShowEntries() {
		List<Entry> entries = new ArrayList<>();

		try {
			String sql = "SELECT * FROM entry";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				entries.add(
						new Entry(result.getInt("entryid"), result.getString("username"), result.getString("showTitle"),
								result.getString("genre"), result.getString("showLength"), result.getString("showType"),
								result.getString("prodCompany"), result.getString("yearOfProduction"),
								result.getString("description"), result.getInt("isRejected")));
			}

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!entries.isEmpty())
			return entries;

		return null;
	}

	public static Entry getSpecificEntry(String entryId) {
		List<Entry> entries = new ArrayList<>();

		try {
			String sql = "SELECT * FROM imbd.entry WHERE entryid =" + entryId + ";";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				entries.add(
						new Entry(result.getInt("entryid"), result.getString("username"), result.getString("showTitle"),
								result.getString("genre"), result.getString("showLength"), result.getString("showType"),
								result.getString("prodCompany"), result.getString("yearOfProduction"),
								result.getString("description"), result.getInt("isRejected")));
			}
			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!entries.isEmpty())
			return entries.get(0);

		return null;
	}

	public static int getNumberOfShows() {
		List<Integer> showsNumber = new ArrayList<>();
		int count = 0;
		try {
			String sql = "SELECT * FROM imbd.show";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				showsNumber.add(result.getInt("showid"));
				count++;
			}

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public static int getProcId(Entry entry) {
		List<String> prodCoNames = new ArrayList<>();
		try {
			String sql = "SELECT * FROM imbd.production_company";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				prodCoNames.add(result.getString("proco_name"));
			}

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int procId = 0;
		for (String s : prodCoNames) {
			if (entry.getProdCompany().toLowerCase().matches(s.toLowerCase())) {
				// Get the procId of the specific prodCompany
				procId = getProcIdFromName(s);
			}
		}
		// If not found
		if (procId == 0) {
			procId = getNumberOfProdCompany() + 1;
			registerProcId(procId, entry.getProdCompany());
		}

		return procId;
	}

	public static int getProcIdFromName(String procCompanyName) {
		int procID = 0;
		try {
			String sql = "SELECT proco_id FROM imbd.production_company WHERE LOWER(proco_name) =" + "'"
					+ procCompanyName + "'";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				procID = result.getInt("proco_id");
			}

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return procID;
	}

	// Get the total number of Production Companies
	public static int getNumberOfProdCompany() {
		int numberOfCompany = 0;
		try {
			String sql = "SELECT proco_id FROM imbd.production_company";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				numberOfCompany++;
			}

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return numberOfCompany;
	}

	// Get the number of Production Companies
	public static void registerProcId(int id, String procName) {
		// INSERT INTO `production_company` VALUES (1,'Universal Pictures')
		try {
			String sql = "INSERT INTO imbd.production_company VALUES (" + id + ",'" + procName + "')";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			statement.execute(sql);

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteEntry(String username, String showTitle) {
		try {
			String sql = "DELETE from imbd.entry WHERE LOWER(username) ='" + username.toLowerCase() + "'"
					+ "AND LOWER(showTitle) = '" + showTitle + "';";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			statement.execute(sql);

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void RejectEntry(Entry entry) {
		try {
			String sql = "UPDATE imbd.entry SET isRejected = 1 WHERE LOWER(username) ='"
					+ entry.getUsername().toLowerCase() + "' AND LOWER(showTitle) = '"
					+ entry.getShowTitle().toLowerCase() + "';";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			statement.execute(sql);

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteDuplicate(Entry entry) {
		try {

			String sql = "DELETE from imbd.entry WHERE showTitle ='" + entry.getShowTitle() + "' " + "AND genre = '"
					+ entry.getGenre() + "' AND showLength = '" + entry.getShowLength() + "' AND prodCompany = '"
					+ entry.getProdCompany() + "' AND yearOfProduction = '" + entry.getYearOfProduction()
					+ "' AND entryid  !=" + entry.getEntryId() + ";";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			statement.execute(sql);

			DatabaseUtils.closeConnection(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addEntryToDatabase(Entry entry) {

		try {
			String sql = "INSERT INTO imbd.entry VALUES (" + entry.getEntryId() + ",'" + entry.getUsername() + "','"
					+ entry.getShowTitle() + "','" + entry.getGenre() + "','" + entry.getShowLength() + "','"
					+ entry.getShowType() + "','" + entry.getProdCompany() + "','" + entry.getYearOfProduction() + "','"
					+ entry.getDescription() + "','" + entry.isRejected() + "');";

			Connection connection = DatabaseUtils.connectToDatabase();
			Statement statement = connection.createStatement();
			statement.execute(sql);

			DatabaseUtils.closeConnection(connection);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
