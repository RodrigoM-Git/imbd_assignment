package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import app.model.Account;
import app.dao.AccountDAO;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountDAOGetAccountByUsernameTest extends AccountDAOTestTemplate {
    
    // Tests that null is returned when an invalid username is used
    @Test
    void getAccountByUsername_Null_WhenInvalidUsername(){
        String invalidUser = "not valid";
        
        DAOTestUtils.useNewDB();

        Account account = AccountDAO.getAccountByUsername(invalidUser);
        assertEquals(null, account);
    }


    // Tests that the correct account is returned when a valid username is passed
    // Checks all values of returned account
    @Test
    void getAccountByUsername_Correct_WhenValidUserPassedIn(){
        Account[] accounts = DAOTestUtils.getRegisteredAccounts();
        DAOTestUtils.useNewDB();
        for (int i = 0; i < accounts.length; i++){
            Account account = accounts[i];
            Account returnedAccount = AccountDAO.getAccountByUsername(account.getUsername());
            boolean accountsMatch = false;
            // Test if account inputted matches what is expected
            if (account.getUsername().equals(returnedAccount.getUsername())
                && account.getType().equals(returnedAccount.getType())
                && account.getFirstName().equals(returnedAccount.getFirstName())
                && account.getLastName().equals(returnedAccount.getLastName())
                && account.getGender().equals(returnedAccount.getGender())
                && account.getBirthYear().equals(returnedAccount.getBirthYear())
                && account.getCountry().equals(returnedAccount.getCountry())
                && account.getZip().equals(returnedAccount.getZip())
                && account.getEmail().equals(returnedAccount.getEmail())
                && account.getPassword().equals(returnedAccount.getPassword())
                && ((account.getOrg() == null && returnedAccount.getOrg() == null) || account.getOrg().equals(returnedAccount.getOrg()))
                && ((account.getNum() == null && returnedAccount.getNum() == null) || account.getNum().equals(returnedAccount.getNum()))){
                    accountsMatch = true;
                }
            assertTrue(accountsMatch);
        }
    }

}