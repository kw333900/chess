package dataaccess;

import model.UserData;

public interface UserDAOinterface {
/*
put all methods in corresponding DAO in this class
 */

    void addUser (UserData u);

    UserData getUser (String username) throws DataAccessException;

    void clear();





}
