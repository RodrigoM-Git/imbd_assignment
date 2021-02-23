package dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

// This class is the base for all the testing classes, the test classes extend this one.
// All tests were initially in one class, but was changed as the current format is what is expected.
// This inheritence helps mitigate code repetition.
public class AccountDAOTestTemplate {
    
    // This method will clear all tables of data, and create the needed tables for the tests
    // Some entries will be added into the db in the resetTestDatabase method, others may be added in the tests
    // Also uses its own schema, named test_accountdao
    @BeforeAll
    void init(){
        DAOTestUtils.setUpTestDatabase();
    }


    // Clears all tables and re-inputs the base test data. Some tests may add or remove stuff, but they will be
    // reset before the next test here.
    @BeforeEach
    void beforeEach(){
        DAOTestUtils.resetTestDatabase();
    }

    // Removes test schema
    @AfterAll
    void afterAll(){
        DAOTestUtils.removeTestDatabase();
    }
}