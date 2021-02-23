package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import app.model.Account;
import app.dao.AccountDAO;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountDAOGetRegistrationEntriesTest extends AccountDAOTestTemplate {

    // Checks that accounts with correct user names are returned
    // Only checks that username is same
    @Test
    void getRegistrationEntries_AllEntries_WhenEntriesExist(){
        
        Account[] accounts = DAOTestUtils.getUnregisteredAccounts();
        List<Account> returnedAccounts = AccountDAO.getRegistrationEntries();
        boolean containsAll = true;

        DAOTestUtils.useNewDB();
        for (int i = 0; containsAll && i < accounts.length && returnedAccounts.size() > 0; i++){
            Account account = accounts[i];
            boolean foundAccount = false;
            for (int r = 0; !foundAccount && r < returnedAccounts.size(); r++){
                if (account.getUsername().equals(returnedAccounts.get(r).getUsername())){
                    foundAccount = true;
                    returnedAccounts.remove(r);
                }
            }
            assertEquals(true, foundAccount);
        }
    }

}