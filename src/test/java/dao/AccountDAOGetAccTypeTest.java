package dao;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import app.model.Account;
import app.dao.AccountDAO;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountDAOGetAccTypeTest extends AccountDAOTestTemplate {
    
    // Tests that null is returned when an invalid username is passed in
    @Test
    void getAccType_Null_WhenUsernameNotValid(){
        String username = "not valid";
        DAOTestUtils.useNewDB();
        String returnedType = AccountDAO.getAccType(username);
        assertEquals(null, returnedType);
    }

    // Tests if correct account type is returned when username is valid
    @Test
    void getAccType_Type_WhenValidUserIsSearched(){
        Account[] accounts = DAOTestUtils.getRegisteredAccounts();
        // Use test db
        DAOTestUtils.useNewDB();
        // Loop through each user to determine if successful
        for (int i = 0; i < accounts.length; i++){
            Account account = accounts[i];
            assertEquals(account.getType(), AccountDAO.getAccType(account.getUsername()));
        }
        
    }
}