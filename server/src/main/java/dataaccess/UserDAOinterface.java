package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.UserData;

public interface UserDAOinterface {
/*
put all methods in corresponding DAO in this class
 */

    void addUser (UserData u) throws DataAccessException;

    UserData getUser (String username) throws DataAccessException;

    void clear() throws  DataAccessException;





}
