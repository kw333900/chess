package service;
import dataaccess.*;
import model.AuthData;
import model.UserData;

public class UserService {
    public RegisterResult register(RegisterRequest registerRequest) {
        // call DAO method here

        // see if user already exists:
        UserDAO userDAO = new UserDAO();
        if (userDAO.getUser(registerRequest.username()) != null){

            throw new AlreadyTakenException("Error: username already taken");

        }
        // if not, add user to db
        userDAO.addUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));

        // call createAuth:
        AuthDAO authDAO = new AuthDAO();
        String authToken = authDAO.generateAuthToken();
        authDAO.addAuthData(new AuthData(authToken, registerRequest.username()));

        // assemble and return RegisterResult
        return new RegisterResult(registerRequest.username(), authToken);
    }



    public LoginResult login(LoginRequest loginRequest) {
        return null;
    }


    public void logout(LogoutRequest logoutRequest) {}



}
