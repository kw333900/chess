package server;

import dataaccess.*;
import handler.UserHandler;
import io.javalin.*;
import server.websocket.WebSocketHandler;

public class Server {

    private final Javalin httpHandler;
    private final WebSocketHandler webSocketHandler;

    public Server() {
        httpHandler = Javalin.create(config -> config.staticFiles.add("web"));
        // Register your endpoints and exception handlers here.

        UserDAOinterface userDAO = new MySqlUserDAO();
        AuthDAOinterface authDAO = new MySqlAuthDAO();
        GameDAOinterface gameDAO = new MySqlGameDAO();

        UserHandler u = new UserHandler(userDAO, authDAO, gameDAO);

        webSocketHandler = new WebSocketHandler();

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

        // ws:
        httpHandler.ws("/ws", ws -> {
            ws.onConnect(webSocketHandler);
            ws.onMessage(webSocketHandler);
            ws.onClose(webSocketHandler);
        });

    }

    public int run(int desiredPort) {
        httpHandler.start(desiredPort);
        return httpHandler.port();
    }






    public void stop() {
        httpHandler.stop();
    }
}
