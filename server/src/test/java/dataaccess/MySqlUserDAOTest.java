package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class MySqlUserDAOTest {
//
//        Structure for tests:
//        - Arrange
//        - Act
//        - Assert
//

    UserDAOinterface userDAO = new MySqlUserDAO();
    AuthDAOinterface authDAO = new MySqlAuthDAO();
    GameDAOinterface gameDAO = new MySqlGameDAO();


    @BeforeEach
    void setUp() throws DataAccessException {

        userDAO.clear();

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addUserPositive() throws DataAccessException {

//        - Arrange
        UserData expectedUser = new UserData("testusername", "testpass", "testemail");
//        - Act
        userDAO.addUser(expectedUser);
        UserData actualUser = userDAO.getUser(expectedUser.username());
//        - Assert
        assertEquals(expectedUser.username(), actualUser.username());
    }


    @Test
    void addUserNegative() throws DataAccessException {

//        - Arrange
        UserData expectedUser = new UserData("testusername", "testpass", "testemail");
//        - Act
        userDAO.addUser(expectedUser);
//        - Assert
        assertThrows(DataAccessException.class, () -> userDAO.addUser(expectedUser));
    }



    @Test
    void getUserPositive() throws DataAccessException{

        //        - Arrange
        UserData expectedUser = new UserData("testusername", "testpass", "testemail");
//        - Act
        userDAO.addUser(expectedUser);
        UserData actualUser = userDAO.getUser(expectedUser.username());
//        - Assert
        assertEquals(expectedUser.username(), actualUser.username());

    }


    @Test
    void getUserNegative() throws DataAccessException{

//        - Arrange
        UserData expectedUser = new UserData("testusername", "testpass", "testemail");
//        - Act
        userDAO.addUser(expectedUser);
//        - Assert
        assertNull(userDAO.getUser("testwrongusername"));

    }



    @Test
    void clear() throws DataAccessException {
        //        - Arrange
        UserData expectedUser = new UserData("testusername", "testpass", "testemail");
//        - Act
        userDAO.addUser(expectedUser);
        userDAO.clear();
//        - Assert
        assertNull(userDAO.getUser("testwrongusername"));

    }



}