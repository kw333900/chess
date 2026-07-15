package service;
import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class UserService {


    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    public UserService (UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }


    public RegisterResult register(RegisterRequest registerRequest) throws AlreadyTakenException, BadRequestException {
        // call DAO method here

        // if BadRequest: throw exception
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null){
            throw new BadRequestException("Error: bad request");
        }


        // see if user already exists:
        if (userDAO.getUser(registerRequest.username()) != null){
            throw new AlreadyTakenException("Error: username already taken");
        }
        // if not, add user to db
        userDAO.addUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));

        // call createAuth:
        String authToken = authDAO.generateAuthToken();
        authDAO.addAuthData(new AuthData(authToken, registerRequest.username()));

        // assemble and return RegisterResult
        return new RegisterResult(registerRequest.username(), authToken);
    }



    public LoginResult login(LoginRequest loginRequest) throws InvalidLoginException, BadRequestException {
        // if BadRequest: throw exception
        if (loginRequest.username() == null || loginRequest.password() == null){
            throw new BadRequestException("Error: bad request");
        }


        // see if login is valid:
        UserData userData = userDAO.getUser(loginRequest.username());

        if (userData == null || !Objects.equals(userData.password(), loginRequest.password())){
            throw new InvalidLoginException("Error: unauthorized");
        }




        // if does exist, createAuth:
        String authToken = authDAO.generateAuthToken();
        authDAO.addAuthData(new AuthData(authToken, loginRequest.username()));

        // assemble and return LoginResult
        return new LoginResult(loginRequest.username(), authToken);


    }


    public LogoutResult logout(LogoutRequest logoutRequest) {


        return null;
    }




    public void clear (){


        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();

    }



}
