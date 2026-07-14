package dataaccess;

import java.util.UUID;



public class AuthDAO {


    public static String createAuth() {
        return UUID.randomUUID().toString();
    }

}
