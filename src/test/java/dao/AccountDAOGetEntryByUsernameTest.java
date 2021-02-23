package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import app.model.Account;
import app.dao.AccountDAO;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountDAOGetEntryByUsernameTest extends AccountDAOTestTemplate {

    
    // Checks that username of found account matches expected
    @Test
    void getEntryByUsername_Account_WhenUsernameIsValid(){
        Account[] accounts = DAOTestUtils.getUnregisteredAccounts();

        DAOTestUtils.useNewDB();
        for (int i = 0; i < accounts.length; i++){
            Account account = accounts[i];
            Account returnedAccount = AccountDAO.getEntryByUsername(account.getUsername());

            boolean foundAccount = false;
            if (returnedAccount != null && account.getUsername().equals(returnedAccount.getUsername())){
                foundAccount = true;
            }

            assertEquals(true, foundAccount);
        }
    }
    
}