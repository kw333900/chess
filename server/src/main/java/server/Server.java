package server;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import handler.UserHandler;
import io.javalin.*;

public class Server {

    private final Javalin httpHandler;

    public Server() {
        httpHandler = Javalin.create(config -> config.staticFiles.add("web"));
        // Register your endpoints and exception handlers here.


        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();
        UserHandler u = new UserHandler(userDAO, authDAO, gameDAO);


        // Endpoint: Register
        httpHandler.post("/user", u::handle_register);
        // Endpoint: Clear
        httpHandler.delete("/db", u::handle_clear);
        // Endpoint: Login
        httpHandler.post("/session", u::handle_login);
        // Endpoint: Logout
        httpHandler.delete("/session", u::handle_logout);
        // Endpoint: Create Game
        httpHandler.post("/game", u::handle_create_game);
        // Endpoint: List Games
        httpHandler.get("/game", u::handle_list_games);
        // Endpoint: Join Game
        httpHandler.put("/game", u::handle_join_game);





    }

    public int run(int desiredPort) {
        httpHandler.start(desiredPort);
        return httpHandler.port();
    }






    public void stop() {
        httpHandler.stop();
    }
}
