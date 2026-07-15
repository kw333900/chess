package handler;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import service.*;

import java.util.Map;

public class UserHandler {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserService userService;
    public UserHandler (UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userService = new UserService(userDAO, authDAO, gameDAO);
    }

    public void handle_register(@NotNull Context context) {
        // create RegisterRequest object and then pass it into service method and call it


        var serializer = new Gson();


// deserialize
        RegisterRequest registerRequest = serializer.fromJson(context.body(), RegisterRequest.class);


       try {
           RegisterResult registerResult = userService.register(registerRequest);
           var json = serializer.toJson(registerResult);
           context.result(json);
       } catch (AlreadyTakenException e){
           context.status(403);
           var json = serializer.toJson(Map.of("message", e.getMessage()));
           context.result(json);
       } catch (BadRequestException e){
           context.status(400);
           var json = serializer.toJson(Map.of("message", e.getMessage()));
           context.result(json);

       }

    }

    public void handle_clear(@NotNull Context context){

        userService.clear();


    }





    public void handle_login (@NotNull Context context) {
        // create LoginRequest object and then pass it into service method and call it


        var serializer = new Gson();



        // deserialize
        LoginRequest loginRequest = serializer.fromJson(context.body(), LoginRequest.class);

        try {
            LoginResult loginResult = userService.login(loginRequest);
            var json = serializer.toJson(loginResult);
            context.result(json);
        } catch (InvalidLoginException e){
            context.status(401);
            var json = serializer.toJson(Map.of("message", e.getMessage()));
            context.result(json);
        } catch (BadRequestException e){
            context.status(400);
            var json = serializer.toJson(Map.of("message", e.getMessage()));
            context.result(json);

        }



    }



    public void handle_logout (@NotNull Context context) {

        String authToken = context.header("authorization");
        LogoutRequest logoutRequest = new LogoutRequest(authToken);


        try {
            userService.logout(logoutRequest);
        } catch (InvalidLogoutException e){
            context.status(401);
            var serializer = new Gson();
            var json = serializer.toJson(Map.of("message", e.getMessage()));
            context.result(json);
        }



    }





    public void handle_list_games (@NotNull Context context){
        var serializer = new Gson();


        String authToken = context.header("authorization");
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);

        try {
            ListGamesResult listGamesResult = userService.listGames(listGamesRequest);
            var json = serializer.toJson(listGamesResult);
            context.result(json);
        } catch (InvalidGameIDException e){
            context.status(401);
            var json = serializer.toJson(Map.of("message", e.getMessage()));
            context.result(json);
        }


    }








    public void handle_create_game (@NotNull Context context){

        var serializer = new Gson();

        CreateGameRequestBody bodyContext = serializer.fromJson(context.body(), CreateGameRequestBody.class);
        String gameName = bodyContext.gameName();


        String authToken = context.header("authorization");
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);

        try {
            CreateGameResult createGameResult = userService.createGame(createGameRequest);
            var json = serializer.toJson(createGameResult);
            context.result(json);
        } catch (InvalidLoginException e){
            context.status(401);
            var json = serializer.toJson(Map.of("message", e.getMessage()));
            context.result(json);
        } catch (BadRequestException e){
            context.status(400);
            var json = serializer.toJson(Map.of("message", e.getMessage()));
            context.result(json);

        }


    }




    public void handle_join_game (@NotNull Context context){



        var serializer = new Gson();

        String authToken = context.header("authorization");
        JoinGameRequestBody bodyContext = serializer.fromJson(context.body(), JoinGameRequestBody.class); // <--- error hapenning here
        String playerColor = bodyContext.playerColor();
        int gameID = bodyContext.gameID();
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, playerColor, gameID);

        try {
            userService.joinGame(joinGameRequest);
//            var json = serializer.toJson(createGameResult);
//            context.result(json);
        } catch (InvalidGameIDException e){
            context.status(401);
            var json = serializer.toJson(Map.of("message", e.getMessage()));
            context.result(json);
        } catch (BadRequestException e){
            context.status(400);
            var json = serializer.toJson(Map.of("message", e.getMessage()));
            context.result(json);
        } catch (GameAlreadyTakenException e){
            context.status(403);
            var json = serializer.toJson(Map.of("message", e.getMessage()));
            context.result(json);
        }


    }











}
