package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import app.dao.AccountDAO;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountDAOCheckIfUserExistsTest extends AccountDAOTestTemplate {
    
    // Tests that false is correctly returned when user doesn't exist
    @Test
    void checkIfUserExists_False_WhenUserDoesntExist(){
        String invalidUser = "not valid";

        DAOTestUtils.useNewDB();

        boolean exists = AccountDAO.checkIfUserExists(invalidUser);
        assertEquals(false, exists);
    }

    
    // Tests that true is correctly returned when user exists
    @Test
    void checkIfUserExists_True_WhenUsersExist(){
    	boolean exists = true;
        String[] usernames = {"user1", "critic1", "pco1"};

        DAOTestUtils.useNewDB();

        for (int i = 0; i < usernames.length; i++){
            exists = AccountDAO.checkIfUserExists(usernames[i]);
            
        }
        assertEquals(true, exists);
    }


}