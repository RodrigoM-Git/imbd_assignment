package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import app.dao.utils.DatabaseUtils;
import app.model.Account;
import app.dao.AccountDAO;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountDAOAddEntryToDatabaseTest extends AccountDAOTestTemplate {

    // Tests that registrations are correctly added
    // Also checks that added registration has correct values
    @Test
    void addEntryToDatabase_Added_WhenUserIsValid(){
        Account[] accounts = DAOTestUtils.getUnusedAccounts();
            
        DAOTestUtils.useNewDB();
        // Skip first index, as it is a basic user that will never register here
        for (int i = 1; i < accounts.length; i++){
            Account account = accounts[i];
            
            AccountDAO.addEntryToDatabase(account);

            String sql = String.format("SELECT * FROM registration WHERE username = '%s'", account.getUsername());

            boolean correctResult = false;
            Connection connection = null;
            try {
                connection = DatabaseUtils.connectToDatabase();
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(sql);
                if (results.next()){
                    // Then result has been found, check to see if values match those that were found
                    if (account.getUsername().equals(results.getString("username"))
                        && account.getType().equals(results.getString("type"))
                        && account.getFirstName().equals(results.getString("first_name"))
                        && account.getLastName().equals(results.getString("last_name"))
                        && account.getGender().equals(results.getString("gender"))
                        && account.getBirthYear().equals(results.getString("birthYear"))
                        && account.getCountry().equals(results.getString("country"))
                        && account.getZip().equals(results.getString("zip"))
                        && account.getEmail().equals(results.getString("email"))
                        && account.getPassword().equals(results.getString("password"))
                        && ((account.getOrg() == null && results.getString("orgName").equals("null")) || account.getOrg().equals(results.getString("orgName")))
                        && ((account.getNum() == null && results.getString("orgPhone").equals("null")) || account.getNum().equals(results.getString("orgPhone")))){

                        correctResult = true;
                    }
                }
            } catch(Exception e){
                e.printStackTrace();
            }
            DatabaseUtils.closeConnection(connection);
            assertEquals(true, correctResult);

        }
    }
}