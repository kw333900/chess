package server.websocket;

import com.google.gson.Gson;
import dataaccess.GameDAOinterface;
import dataaccess.MySqlAuthDAO;
import dataaccess.MySqlGameDAO;
import dataaccess.exceptions.DataAccessException;
import exceptions.ResponseException;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
//import webSocketMessages.Action;
//import webSocketMessages.Notification;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }
      // FROM LECTURE VIDEO:
    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) throws Exception {
        int gameId = -1;
        Session session = wsMessageContext.session;

        Gson Serializer = new Gson();

        try {
            UserGameCommand command = Serializer.fromJson(
                    wsMessageContext.message(), UserGameCommand.class);
            gameId = command.getGameID();
            String username = getUsername(command.getAuthToken());


            // connect -> add to connections, leave -> remove from connections

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
//                case MAKE_MOVE -> makeMove(session, username, Serializer.fromJson(
//                        wsMessageContext.message(), MakeMoveCommand.class));
                case LEAVE -> leaveGame(session, username, command);
//                case RESIGN -> resign(session, username, command);
            }

        }
//        catch (exceptions.ResponseException ex) {
//            throw new ResponseException(ResponseException.Code.ClientError, "Error: unauthorized");
////            sendMessage(session, gameId, new ErrorMessage("Error: unauthorized"));
//
//        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new ResponseException(ResponseException.Code.ClientError, "Error: " + ex.getMessage());
//            sendMessage(session, gameId, new ErrorMessage("Error: " + ex.getMessage()));
        }
    }



    private String getUsername(String authToken) throws DataAccessException {
        // get username from auth data somehow?
        MySqlAuthDAO mySqlAuthDAO = new MySqlAuthDAO();
        AuthData authData = mySqlAuthDAO.getAuthDataByToken(authToken);
        return authData.username();

    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }


    public void connect (Session session, String username, UserGameCommand command) throws IOException, DataAccessException {
        connections.add(command.getGameID(), session);

        // Server sends a LOAD_GAME message back to the root client:
        Gson Serializer = new Gson();
        MySqlGameDAO mySqlGameDAO = new MySqlGameDAO();
        GameData gameData = mySqlGameDAO.getGameData(command.getGameID());
        session.getRemote().sendString(Serializer.toJson(new LoadGame(gameData.game())));

        // Server sends a Notification message to all other clients in that game informing them the root client
        // connected to the game, either as a player (in which case their color must be specified) or as an observer:
        var message = String.format("%s has connected to the game", username);

        var notification = new Notification(message);
        connections.broadcast(session, notification);


    }



    public void leaveGame (Session session, String username, UserGameCommand command) throws IOException, DataAccessException {
        // If a player is leaving, then the game is updated to remove the root client. Game is updated in the database.
        connections.remove(command.getGameID(), session);
        MySqlGameDAO mySqlGameDAO = new MySqlGameDAO();
        GameData gameData = mySqlGameDAO.getGameData(command.getGameID());
        if (Objects.equals(gameData.whiteUsername(), username)){
            mySqlGameDAO.updateGameData(new GameData(command.getGameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game()));
        } else if (Objects.equals(gameData.blackUsername(), username)){
            mySqlGameDAO.updateGameData(new GameData(command.getGameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game()));
        }

        // Server sends a Notification message to all other clients in that game informing
        // them that the root client left. This applies to both players and observers.
        var message = String.format("%s has left the game", username);
        var notification = new Notification(message);
        if (!connections.isEmpty()){
            connections.broadcast(session, notification);
        } else {
            String msg = notification.toString();
            session.getRemote().sendString(msg);
        }



    }



}