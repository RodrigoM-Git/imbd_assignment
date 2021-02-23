package dao;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import app.model.ProductionCompany;
import app.dao.AccountDAO;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountDAOGetProdCompanyTest extends AccountDAOTestTemplate {

    // Tests that production companies can be found, when the proco is in the db
    @Test
    void getProdCompany_Proco_WhenNameIsValid(){
        ProductionCompany[] procos = DAOTestUtils.getRegisteredProcos();
        boolean allCorrect = true;

        for (int i = 0; allCorrect && i < procos.length; i++){
            String returnedProco = AccountDAO.getProdCompany(procos[i].getName());
            if (!procos[i].getName().equals(returnedProco)){
                allCorrect = false;
            }
        }

       assertTrue(allCorrect);
    }
    
}