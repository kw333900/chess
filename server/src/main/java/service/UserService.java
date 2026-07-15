package service;
import dataaccess.*;
import io.javalin.http.BadRequestResponse;
import model.AuthData;
import model.UserData;

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



    public LoginResult login(LoginRequest loginRequest) throws InvalidLoginException {
        // see if login is valid:
        if (userDAO.getUser(loginRequest.username()) == null){
            throw new InvalidLoginException("Error: unauthorized");
        }
        // if does exist, createAuth:
        String authToken = authDAO.generateAuthToken();
        authDAO.addAuthData(new AuthData(authToken, loginRequest.username()));

        // assemble and return LoginResult
        return new LoginResult(loginRequest.username(), authToken);


    }


    public void logout(LogoutRequest logoutRequest) {


    }




    public void clear (){
        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();

        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();

    }



}
