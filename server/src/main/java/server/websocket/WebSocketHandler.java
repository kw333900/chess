package server.websocket;

import com.google.gson.Gson;
import exceptions.ResponseException;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
//import webSocketMessages.Action;
//import webSocketMessages.Notification;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.Notification;

import java.io.IOException;

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
//                case LEAVE -> leaveGame(session, username, command);
//                case RESIGN -> resign(session, username, command);
            }

        } catch (exceptions.ResponseException ex) {
            throw new ResponseException(ResponseException.Code.ClientError, "Error: unauthorized");
//            sendMessage(session, gameId, new ErrorMessage("Error: unauthorized"));

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ResponseException(ResponseException.Code.ClientError, "Error: " + ex.getMessage());
//            sendMessage(session, gameId, new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    private String getUsername(String authToken) {
        // get username from auth data somehow?

        return authToken;
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }


    public void connect (Session session, String username, UserGameCommand command) throws IOException {
        connections.add(command.getGameID(), session);
        var message = String.format("%s has connected to the game", username);
        var notification = new Notification(Notification.Type.ALL_BUT_ROOT, message);
        connections.broadcast(session, notification);
    }







}