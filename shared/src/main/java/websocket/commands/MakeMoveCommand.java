package websocket.commands;


import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

    private final ChessMove move;

    MakeMoveCommand(ChessMove move, CommandType commandType, String authToken, Integer gameID){
        super(commandType, authToken, gameID);

        this.move = move;
    }

    public ChessMove getMove (){
        return move;
    }

}
