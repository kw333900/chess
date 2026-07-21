package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.AuthData;

public interface AuthDAOinterface {



    String generateAuthToken() throws DataAccessException;

    void addAuthData (AuthData a) throws DataAccessException;

    void deleteAuthData (AuthData a) throws DataAccessException;

    AuthData getAuthDataByToken(String token) throws DataAccessException;

    void clear() throws DataAccessException;






}
