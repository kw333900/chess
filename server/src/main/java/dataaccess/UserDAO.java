package dataaccess;
// create interface class for each DAO class


/*
From spec:

    For the most part, the methods on your DAO classes will be CRUD operations that:

    - Create objects in the data store
    - Read objects from the data store
    - Update objects already in the data store
    - Delete objects from the data store
 */


import model.*;

import java.util.ArrayList;
import java.util.Collection;

public class UserDAO implements UserDAOinterface{
    final Collection<UserData> usersList = new ArrayList<>();


    public void addUser(UserData u) /*throws DataAccessException*/ {


        usersList.add(u);
    }



    public UserData getUser (String username){
        for (UserData u : usersList){
            if (u.username().equals(username)){
//                throw AlreadyTakenException;
                return u;
            }
        }
        return null;
    }



    public void clear () {
        usersList.clear();
    }



}
