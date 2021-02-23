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
public class AccountDAODeleteRegistrationTest extends AccountDAOTestTemplate {
    
    // Tests that registrations are successfully deleted
    @Test
    void deleteRegistration_Deleted_WhenUsernameValid(){
        Account[] accounts = DAOTestUtils.getUnregisteredAccounts();

        DAOTestUtils.useNewDB();
        for (int i = 0; i < accounts.length; i++){
            Account account = accounts[i];
            // Remove registration from db
            AccountDAO.deleteRegistration(account.getUsername());

            boolean foundAccount = false;
            // Try to find the registration in db
            Connection connection = null;
            try {
                connection = DatabaseUtils.connectToDatabase();
                Statement statement = connection.createStatement();
                String sql = String.format("SELECT * FROM registration WHERE username = '%s';", account.getUsername());
                ResultSet results = statement.executeQuery(sql);
                if (results.next()){
                    foundAccount = true;
                }

            } catch(Exception e){
                e.printStackTrace();
            }
            DatabaseUtils.closeConnection(connection);
            // If account that was deleted was not found, then test is successful
            assertEquals(false, foundAccount);
        }
    }
}