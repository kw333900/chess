package server.websocket;

import chess.ChessGame;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
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
import websocket.messages.Error;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

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

        Gson serializer = new Gson();

        try {
            UserGameCommand command = serializer.fromJson(
                    wsMessageContext.message(), UserGameCommand.class);
            gameId = command.getGameID();

            MySqlAuthDAO mySqlAuthDAO = new MySqlAuthDAO();
            AuthData authData = mySqlAuthDAO.getAuthDataByToken(command.getAuthToken());
            if (authData != null){
                String username = authData.username();


                // connect -> add to connections, leave -> remove from connections

                switch (command.getCommandType()) {
                    case CONNECT -> connectHandler(session, username, command);
                case MAKE_MOVE -> makeMoveHandler(session, username, serializer.fromJson(
                        wsMessageContext.message(), MakeMoveCommand.class));
                    case LEAVE -> leaveGameHandler(session, username, command);
                case RESIGN -> resignHandler(session, username, command);
                }
            } else {
                session.getRemote().sendString(serializer.toJson(new Error("Error: connection failed")));
            }


        }

        catch (Exception ex) {
            ex.printStackTrace();
            throw new ResponseException(ResponseException.Code.ClientError, "Error: " + ex.getMessage());
        }

    }



    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }


    public void connectHandler(Session session, String username, UserGameCommand command) throws IOException, DataAccessException {
        connections.add(command.getGameID(), session);

        // Server sends a LOAD_GAME message back to the root client:

        Gson Serializer = new Gson();
        MySqlGameDAO mySqlGameDAO = new MySqlGameDAO();
        GameData gameData = mySqlGameDAO.getGameData(command.getGameID());
        if (gameData != null){
            var stringJSON = Serializer.toJson(new LoadGame(gameData.game()));
            session.getRemote().sendString(stringJSON);

            // Server sends a Notification message to all other clients in that game informing them the root client
            // connected to the game, either as a player (in which case their color must be specified) or as an observer:
            var message = String.format("%s has connected to the game", username);
            var notification = new Notification(message);
            connections.broadcast(session, notification, command.getGameID());

        } else {
            session.getRemote().sendString(Serializer.toJson(new Error("Error: connection failed")));
        }





    }



    public void leaveGameHandler (Session session, String username, UserGameCommand command) throws IOException, DataAccessException {
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
            connections.broadcast(session, notification, command.getGameID());
        } else {
            String msg = notification.toString();
            session.getRemote().sendString(msg);
        }


    }



    private void makeMoveHandler(Session session, String username, MakeMoveCommand makeMoveCommand) throws InvalidMoveException, IOException, DataAccessException {
        try{

            // 1. Server verifies the validity of the move.
            MySqlGameDAO mySqlGameDAO = new MySqlGameDAO();
            GameData gameData = mySqlGameDAO.getGameData(makeMoveCommand.getGameID());
            ChessGame chessGame = gameData.game();


            if ( (chessGame.getTeamTurn() == ChessGame.TeamColor.WHITE) && !Objects.equals(username, gameData.whiteUsername()) ){
                throw new InvalidMoveException("It's not your turn");
            }
            else if ( (chessGame.getTeamTurn() == ChessGame.TeamColor.BLACK) && !Objects.equals(username, gameData.blackUsername()) ){
                throw new InvalidMoveException("It's not your turn");
            }

            if (chessGame.getOpenStatus()){
                chessGame.makeMove(makeMoveCommand.getMove());
            } else {
                throw new InvalidMoveException("Game is closed");
            }


            // 2. Game is updated to represent the move. Game is updated in the database.
            mySqlGameDAO.updateGameData(gameData);


            // 3. Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
            connections.broadcast(null, new LoadGame(gameData.game()), makeMoveCommand.getGameID());


            // 4. Server sends a Notification message to all other clients in that game informing them what move was made.
            // TODO: find a way to return piece type and position in message
            var string1 = convertPositionToString(makeMoveCommand.getMove().getStartPosition());
            var string2 = convertPositionToString(makeMoveCommand.getMove().getEndPosition());

            var message = String.format("%s moved %s to %s", username, string1, string2);
            var notification = new Notification(message);
            if (!connections.isEmpty()){
                connections.broadcast(session, notification, makeMoveCommand.getGameID());
            } else {
                String msg = notification.toString();
                session.getRemote().sendString(msg);
            }



            // 5. If the move results in check, checkmate or stalemate the server sends a Notification message to all clients.
            if (chessGame.isInCheckmate(chessGame.getTeamTurn())){
                if (Objects.equals(gameData.blackUsername(), username)){
                    username = gameData.whiteUsername();
                } else {
                    username = gameData.blackUsername();
                }
                message = String.format("%s is in checkmate", username);
                notification = new Notification(message);
                if (!connections.isEmpty()){
                    connections.broadcast(null, notification, makeMoveCommand.getGameID());
                } else {
                    String msg = notification.toString();
                    session.getRemote().sendString(msg);
                }
            } else if (chessGame.isInCheck(chessGame.getTeamTurn())){
                if (Objects.equals(gameData.blackUsername(), username)){
                    username = gameData.whiteUsername();
                } else {
                    username = gameData.blackUsername();
                }
                message = String.format("%s is in check", username);
                notification = new Notification(message);
                if (!connections.isEmpty()){
                    connections.broadcast(null, notification, makeMoveCommand.getGameID());
                } else {
                    String msg = notification.toString();
                    session.getRemote().sendString(msg);
                }
            } else if (chessGame.isInStalemate(chessGame.getTeamTurn())){
                message = String.format("%s is in stalemate", username);
                notification = new Notification(message);
                if (!connections.isEmpty()){
                    connections.broadcast(null, notification, makeMoveCommand.getGameID());
                } else {
                    String msg = notification.toString();
                    session.getRemote().sendString(msg);
                }
            }



        } catch (InvalidMoveException ex){
            Gson Serializer = new Gson();
            session.getRemote().sendString(Serializer.toJson(new Error("Error: " + ex.getMessage() + "\n")));
        }


    }






//
//
//
//    if (chessGame.isInCheck(chessGame.getTeamTurn())){
//        message = String.format("%s is in check", username);
//        notification = new Notification(message);
//        if (!connections.isEmpty()){
//            connections.broadcast(null, notification, makeMoveCommand.getGameID());
//        } else {
//            String msg = notification.toString();
//            session.getRemote().sendString(msg);
//        }
//    } else if (chessGame.isInCheckmate(chessGame.getTeamTurn())){
//        message = String.format("%s is in checkmate", username);
//        notification = new Notification(message);
//        if (!connections.isEmpty()){
//            connections.broadcast(null, notification, makeMoveCommand.getGameID());
//        } else {
//            String msg = notification.toString();
//            session.getRemote().sendString(msg);
//        }
//    }
//












    // d2 = [2, 5]
    private String convertPositionToString (ChessPosition position){
        int row = position.getRow();
        int col = position.getColumn();
//        col = flipRowValue(col);

        char letter = getLetterFromNum(col);
        String pos = "" + letter + row;
        return pos;
    }


    public int flipRowValue(int num){
        if (num == 1){
            return 8;
        } else if (num == 2){
            return 7;
        } else if (num == 3){
            return 6;
        } else if (num == 4){
            return 5;
        } else if (num == 5){
            return 4;
        } else if (num == 6){
            return 3;
        } else if (num == 7){
            return 2;
        } else {
            return 1;
        }
    }


    public char getLetterFromNum (int num){
        if (num == 1){
            return 'a';
        } else if (num == 2){
            return 'b';
        } else if (num == 3){
            return 'c';
        } else if (num == 4){
            return 'd';
        } else if (num == 5){
            return 'e';
        } else if (num == 6){
            return 'f';
        } else if (num == 7){
            return 'g';
        } else {
            return 'h';
        }

    }



    private void resignHandler(Session session, String username, UserGameCommand command) throws IOException, DataAccessException {
        // 1. Server marks the game as over (no more moves can be made). Game is updated in the database.
        MySqlGameDAO mySqlGameDAO = new MySqlGameDAO();
        GameData gameData = mySqlGameDAO.getGameData(command.getGameID());
        ChessGame chessGame = gameData.game();
        if (!username.equals(gameData.blackUsername()) && !username.equals(gameData.whiteUsername())){
            // observer:
            Gson Serializer = new Gson();
            session.getRemote().sendString(Serializer.toJson(new Error("Error: observer cannot resign from game")));
            return;
        }
        if (!chessGame.getOpenStatus()){
            Gson Serializer = new Gson();
            session.getRemote().sendString(Serializer.toJson(new Error("Error: game is closed")));
            return;
        }
        chessGame.markGameAsOver();
        mySqlGameDAO.updateGameData(new GameData(command.getGameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), chessGame));


        // 2. Server sends a Notification message to all clients in that game informing them that the root client resigned.
        // This applies to both players and observers.
        var message = String.format("%s has resigned from the game", username);
        var notification = new Notification(message);
        if (!connections.isEmpty()){
            connections.broadcast(null, notification, command.getGameID());
        } else {
            String msg = notification.toString();
            session.getRemote().sendString(msg);
        }



    }





}