package dataaccess;

import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;



public class AuthDAO implements AuthDAOinterface{
    final Collection<AuthData> authList = new ArrayList<>();

    public  String generateAuthToken() {
        return UUID.randomUUID().toString();
    }



    public void addAuthData (AuthData a){
        authList.add(a);
    }


    public AuthData getAuthData (String username){
        for (AuthData a : authList){
            if (a.username().equals(username)){
//                throw AlreadyTakenException;
                return a;
            }
        }
        return null;
    }

}
