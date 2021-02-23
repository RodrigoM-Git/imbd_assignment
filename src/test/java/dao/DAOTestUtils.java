package dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;

import app.dao.utils.DatabaseUtils;
import app.model.Account;
import app.model.ProductionCompany;


// These static methods are used by the AccountDAO tests
public class DAOTestUtils {

    // Sets up the database, called before the tests start
    static void setUpTestDatabase(){
        String[] sqlQueries = {"DROP TABLE IF EXISTS test_accountdao.account;",
            "DROP TABLE IF EXISTS test_accountdao.registration;",
            "DROP TABLE IF EXISTS test_accountdao.production_company;",
            "CREATE SCHEMA test_accountdao;",
            "USE test_accountdao;",
            
            "CREATE TABLE `production_company` (`proco_id` int(11) NOT NULL "
                + "AUTO_INCREMENT,`proco_name` varchar(45) NOT NULL, PRIMARY KEY (`proco_id`));",
            
            "CREATE TABLE `account` ("
                + "`type` varchar(10) NOT NULL,"
                + "`username` varchar(45) NOT NULL,"
                + "`password` varchar(255) NOT NULL,"
                + "`email` varchar(45) NOT NULL,"
                + "`country` varchar(45) NOT NULL,"
                + "`zip` int(10) NOT NULL,"
                + "`gender` varchar(10) NOT NULL,"
                + "`first_name` varchar(45) NOT NULL,"
                + "`last_name` varchar(45) NOT NULL,"
                + "`birth_year` int(4) NOT NULL,"
                + "`orgName` varchar(45) DEFAULT NULL,"
                + "`orgPhone` varchar(10) DEFAULT NULL,"
                + "`proco_id` int(11) DEFAULT NULL,"
                + "PRIMARY KEY (`username`),"
                + "UNIQUE KEY `username_UNIQUE` (`username`),"
                + "FOREIGN KEY(`proco_id`) REFERENCES `production_company` (`proco_id`));",

            "CREATE TABLE `registration`("
                + "`type` varchar(10) NOT NULL,"
                + "`first_name` varchar(45) NOT NULL,"
                + "`last_name` varchar(45) NOT NULL,"
                + "`gender` varchar(10) NOT NULL,"
                + "`birthYear` int(4) NOT NULL,"
                + "`country` varchar(45) NOT NULL,"
                + "`zip` int(10) NOT NULL,"
                + "`username` varchar(45) NOT NULL,"
                + "`email` varchar(45) NOT NULL,"
                + "`password` varchar(100) NOT NULL,"
                + "`orgName` varchar(45) DEFAULT NULL,"
                + "`orgPhone` varchar(10) DEFAULT NULL,"
                + "PRIMARY KEY(`username`));"
        };

        runQueries(sqlQueries);

    }

    // Resets the database, called before each individual test
    static void resetTestDatabase(){
        String[] sqlQueries = {
                                "SET FOREIGN_KEY_CHECKS = 0;",
                                "TRUNCATE account;",
                                "TRUNCATE registration;",
                                "TRUNCATE production_company;",
                                "SET FOREIGN_KEY_CHECKS = 1;",
                                "INSERT INTO production_company VALUES (1, 'proco1'), (2, 'proco2'), (3, 'proco3');",
                                "INSERT INTO account VALUES "
                                    + "('User','user1','$2a$10$h.dl5J86rGH7I8bD9bZeZeci0pDt0.VwFTGujlnEaZXPf/q7vM5wO','user1@gmail.com','Australia', 1234, 'Female','User','1', 1996, NULL, NULL, NULL),"
                                    + "('Critic','critic1','$2a$10$h.dl5J86rGH7I8bD9bZeZeci0pDt0.VwFTGujlnEaZXPf/q7vM5wO','critic1@gmail.com','Australia', 1234, 'Female','Critic','1', 1996, 'critorg1', 23456, NULL),"
                                    + "('PCO','pco1','$2a$10$h.dl5J86rGH7I8bD9bZeZeci0pDt0.VwFTGujlnEaZXPf/q7vM5wO','pco1@gmail.com','Australia', 1234, 'Female','Pco','1', 1996, 'proco1', 12345, 1);",

                                "INSERT INTO registration VALUES "
                                    + "('Critic','Critic','2','Female', 1996,'Australia', 1234,'critic2','critic2@gmail.com','$2a$10$h.dl5J86rGH7I8bD9bZeZeci0pDt0.VwFTGujlnEaZXPf/q7vM5wO', 'critorg1', '23456'),"
                                    + "('PCO','Pco','2','Female',1996,'Australia',1234,'pco2','pco2@gmail.com','$2a$10$h.dl5J86rGH7I8bD9bZeZeci0pDt0.VwFTGujlnEaZXPf/q7vM5wO',  'proco2', '12345');"

                                };
            
            // This method sets the schema to the test one, need to call before using any db method
            useNewDB();
            // Run queries
            runQueries(sqlQueries);
    }

    // Removes the database, called at the end of testing
    static void removeTestDatabase(){
        String[] sqlQueries = {
            "SET FOREIGN_KEY_CHECKS = 0;",
            "DROP TABLE IF EXISTS account;",
            "DROP TABLE IF EXISTS registration;",
            "DROP TABLE IF EXISTS production_company;",
            "DROP SCHEMA IF EXISTS test_accountdao;",
            "SET FOREIGN_KEY_CHECKS = 1;"};
        useNewDB();
        runQueries(sqlQueries);

        // Sets schema back to normal, probably not needed, just here to be safe
        try{
            Field reader = DatabaseUtils.class.getDeclaredField("schemaName");
            reader.setAccessible(true);
            reader.set(DatabaseUtils.class, "imbd");
        } catch (Exception e){
                e.printStackTrace();
        }
    }

    // Runs queries using executeBatch()
    static void runQueries(String[] sqlQueries){
        Connection connection = null;
        try {
            connection = DatabaseUtils.connectToDatabase();
            Statement statement = connection.createStatement();
            for (int i = 0; i < sqlQueries.length; i++){
                statement.addBatch(sqlQueries[i] + ';');
            }
            statement.executeBatch();
        } catch (Exception e){
            e.printStackTrace();
        }
        DatabaseUtils.closeConnection(connection);

        
    }

    // Sets the private variable in DatabaseUtils for schema to use the test one
    // Needs to be called before using databaseutils in order to use test schema
    static void useNewDB(){
        try{
            Field reader = DatabaseUtils.class.getDeclaredField("schemaName");
            reader.setAccessible(true);
            reader.set(DatabaseUtils.class, "test_accountdao");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    // Returns the registered accounts (ones in account table) that are in the db
    static Account[] getRegisteredAccounts(){
        String[] usernames = {"user1", "critic1", "pco1"};
        String[] expectedTypes = {"User", "Critic", "PCO"};
        String expectedPassword = "$2a$10$h.dl5J86rGH7I8bD9bZeZeci0pDt0.VwFTGujlnEaZXPf/q7vM5wO";
        String[] expectedEmails = {"user1@gmail.com", "critic1@gmail.com", "pco1@gmail.com"};
        String expectedCountry = "Australia";
        String expectedZip = "1234";
        String expectedGender = "Female";
        String[] expectedFirstNames = {"User", "Critic", "Pco"};
        String expectedLastName = "1";
        String expectedBirthYear = "1996";
        String[] expectedOrgNames = {null, "critorg1", "proco1"};
        String[] expectedOrgNumbers = {null, "23456", "12345"};

        Account[] accounts = new Account[usernames.length];
        for (int i = 0; i < usernames.length; i++){
            accounts[i] = new Account(expectedTypes[i], expectedFirstNames[i],
                expectedLastName, expectedGender, expectedBirthYear,expectedCountry,
                expectedZip, usernames[i], expectedEmails[i], expectedPassword,
                expectedOrgNames[i], expectedOrgNumbers[i]);
        }

        return accounts;
    }

    // Returns the unregistered accounts (ones in registration table) that are in the db
    static Account[] getUnregisteredAccounts(){
        String[] usernames = {"critic2", "pco2"};
        String[] expectedTypes = {"Critic", "PCO"};
        String expectedPassword = "$2a$10$h.dl5J86rGH7I8bD9bZeZeci0pDt0.VwFTGujlnEaZXPf/q7vM5wO";
        String[] expectedEmails = {"critic2@gmail.com", "pco2@gmail.com"};
        String expectedCountry = "Australia";
        String expectedZip = "1234";
        String expectedGender = "Female";
        String[] expectedFirstNames = {"Critic", "Pco"};
        String expectedLastName = "2";
        String expectedBirthYear = "1996";
        String[] expectedOrgNames = {"critorg2", "proco2"};
        String[] expectedOrgNumbers = {"23456", "12345"};

        Account[] accounts = new Account[usernames.length];
        for (int i = 0; i < usernames.length; i++){
            accounts[i] = new Account(expectedTypes[i], expectedFirstNames[i],
                expectedLastName, expectedGender, expectedBirthYear,expectedCountry,
                expectedZip, usernames[i], expectedEmails[i], expectedPassword,
                expectedOrgNames[i], expectedOrgNumbers[i]);
        }

        return accounts;
    }
    
    // Returns accounts that have not been used in the db
    static Account[] getUnusedAccounts(){
        String[] usernames = {"testuser", "testcritic", "testpco"};
        String[] expectedTypes = {"User", "Critic", "PCO"};
        String expectedPassword = "$2a$10$h.dl5J86rGH7I8bD9bZeZeci0pDt0.VwFTGujlnEaZXPf/q7vM5wO";
        String[] expectedEmails = {"user1@gmail.com", "critic1@gmail.com", "pco1@gmail.com"};
        String expectedCountry = "Australia";
        String expectedZip = "1234";
        String expectedGender = "Female";
        String[] expectedFirstNames = {"User", "Critic", "Pco"};
        String expectedLastName = "1";
        String expectedBirthYear = "1996";
        String[] expectedOrgNames = {null, "testcritorg1", "proco1"};
        String[] expectedOrgNumbers = {null, "23456", "12345"};

        Account[] accounts = new Account[usernames.length];
        for (int i = 0; i < usernames.length; i++){
            accounts[i] = new Account(expectedTypes[i], expectedFirstNames[i],
                expectedLastName, expectedGender, expectedBirthYear,expectedCountry,
                expectedZip, usernames[i], expectedEmails[i], expectedPassword,
                expectedOrgNames[i], expectedOrgNumbers[i]);
        }

        return accounts;
    }


    static ProductionCompany[] getRegisteredProcos(){
        String[] procoNames = {"proco1", "proco2", "proco3"};
        ProductionCompany[] procos = new ProductionCompany[3];

        for (int i = 0; i < procoNames.length; i++){
            procos[i] = new ProductionCompany(procoNames[i]);
        }

        return procos;
    }
}