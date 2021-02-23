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
public class AccountDAORegisterUserTest extends AccountDAOTestTemplate {
    
    // Tests that a user of any type can be added successfully
    // Also tests that added user has correct values
    @Test
    void registerUser_Added_WhenUserIsValid(){
        Account[] accounts = DAOTestUtils.getUnusedAccounts();

        DAOTestUtils.useNewDB();
        for (int i = 0; i < accounts.length; i++){
            Account account = accounts[i];
            
            AccountDAO.registerUser(account);

            String sql = String.format("SELECT * FROM account WHERE username = '%s'", account.getUsername());
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
                        && account.getBirthYear().equals(results.getString("birth_year"))
                        && account.getCountry().equals(results.getString("country"))
                        && account.getZip().equals(results.getString("zip"))
                        && account.getEmail().equals(results.getString("email"))
                        && account.getPassword().equals(results.getString("password"))
                        && ((account.getOrg() == null && (results.getString("orgName") == null || results.getString("orgName").equals("null"))) || account.getOrg().equals(results.getString("orgName")))
                        && ((account.getNum() == null && (results.getString("orgPhone") == null || results.getString("orgName").equals("null"))) || account.getNum().equals(results.getString("orgPhone")))){

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