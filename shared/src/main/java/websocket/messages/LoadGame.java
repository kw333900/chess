package websocket.messages;

import chess.ChessGame;
import com.google.gson.Gson;

public class LoadGame extends ServerMessage{

    private final ChessGame game;

    public LoadGame(ChessGame userGame) {
        super(ServerMessageType.LOAD_GAME);
        game = userGame;
    }

    public ChessGame getGame (){
        return game;
    }


    public String toString() {
        return new Gson().toJson(this);
    }

}
