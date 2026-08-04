package websocket.messages;

import chess.ChessGame;

public class LoadGame extends ServerMessage{

    private ChessGame game;

    public LoadGame(ChessGame userGame) {
        super(ServerMessageType.LOAD_GAME);
        game = userGame;
    }



}
