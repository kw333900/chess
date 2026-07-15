package service;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;

public class UserService {


    private static final Logger log = LoggerFactory.getLogger(UserService.class);
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


    public void logout(LogoutRequest logoutRequest) throws InvalidLogoutException {

        // see if logout is valid:

        AuthData authData = authDAO.getAuthDataByToken(logoutRequest.authToken());

        if (authData == null){
            throw new InvalidLogoutException("Error: unauthorized");
        }

        authDAO.deleteAuthData(authData);


    }




    public CreateGameResult createGame (CreateGameRequest createGameRequest) throws InvalidLoginException{
        AuthData authData = authDAO.getAuthDataByToken(createGameRequest.authToken());

        // if BadRequest: throw exception
        if (createGameRequest.gameName() == null){
            throw new BadRequestException("Error: bad request");
        }


        if (authData == null){
            throw new InvalidLoginException("Error: unauthorized");
        }
        // call createGame(gameName)
        int gameID = gameDAO.createGame(createGameRequest.gameName());

        return new CreateGameResult(gameID);
    }





    public ListGamesResult listGames (ListGamesRequest listGamesRequest) throws InvalidGameIDException{
        AuthData authData = authDAO.getAuthDataByToken(listGamesRequest.authToken());


        if (authData == null){
            throw new InvalidGameIDException("Error: unauthorized");
        }

        Collection<GameData> games = gameDAO.getGames();


        return new ListGamesResult(games);
    }



    public void joinGame (JoinGameRequest joinGameRequest) throws BadRequestException, InvalidGameIDException, GameAlreadyTakenException{

        AuthData authData = authDAO.getAuthDataByToken(joinGameRequest.authToken());

        // if BadRequest: throw exception
        if (/* joinGameRequest.gameID() == null || */ joinGameRequest.playerColor() == null){
            throw new BadRequestException("Error: bad request");
        }

        if (authData == null){
            throw new InvalidGameIDException("Error: unauthorized");
        }



        GameData gameData = gameDAO.getGameData(joinGameRequest.gameID());

        // if color already taken in gamedata: throw gamealreadytaken exception
        if (Objects.equals(joinGameRequest.playerColor(), "WHITE") && gameData.whiteUsername() != null){
            throw new GameAlreadyTakenException("Error: already taken");
        }
        else if (Objects.equals(joinGameRequest.playerColor(), "BLACK") && gameData.blackUsername() != null){
            throw new GameAlreadyTakenException("Error: already taken");
        }

        // join and update game:



    }






    public void clear (){


        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();

    }



}
