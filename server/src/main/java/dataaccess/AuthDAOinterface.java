package dataaccess;

import model.AuthData;

public interface AuthDAOinterface {



    String generateAuthToken();

    void addAuthData (AuthData a);

    void deleteAuthData (AuthData a);

    AuthData getAuthDataByToken(String token);

    void clear();






}
