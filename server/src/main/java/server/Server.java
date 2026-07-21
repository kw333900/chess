package server;

import dataaccess.*;
import handler.UserHandler;
import io.javalin.*;

public class Server {

    private final Javalin httpHandler;

    public Server() {
        httpHandler = Javalin.create(config -> config.staticFiles.add("web"));
        // Register your endpoints and exception handlers here.

        UserDAOinterface userDAO = new MySqlUserDAO();
        AuthDAOinterface authDAO = new MySqlAuthDAO();
        GameDAOinterface gameDAO = new MySqlGameDAO();
        UserHandler u = new UserHandler(userDAO, authDAO, gameDAO);

//        UserDAO userDAO = new UserDAO();
//        AuthDAO authDAO = new AuthDAO();
//        GameDAO gameDAO = new GameDAO();
//        UserHandler u = new UserHandler(userDAO, authDAO, gameDAO);


        // Endpoint: Register
        httpHandler.post("/user", u::handleRegister);
        // Endpoint: Clear
        httpHandler.delete("/db", u::handleClear);
        // Endpoint: Login
        httpHandler.post("/session", u::handleLogin);
        // Endpoint: Logout
        httpHandler.delete("/session", u::handleLogout);
        // Endpoint: Create Game
        httpHandler.post("/game", u::handleCreateGame);
        // Endpoint: List Games
        httpHandler.get("/game", u::handleListGames);
        // Endpoint: Join Game
        httpHandler.put("/game", u::handleJoinGame);





    }

    public int run(int desiredPort) {
        httpHandler.start(desiredPort);
        return httpHandler.port();
    }






    public void stop() {
        httpHandler.stop();
    }
}
