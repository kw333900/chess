package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator {

    public BishopMovesCalculator (){

    }


    public Collection<ChessMove> calculateBishopMoves (ChessBoard board, ChessPosition myPosition, ChessPiece piece){
        List<ChessMove> bishopValidMoves = new ArrayList<>();
        // validate (make sure it's on the board) the myPosition given as parameter
        if (myPosition.getRow() < 1 || myPosition.getRow() > 8 || myPosition.getColumn() < 1 || myPosition.getColumn() > 8) {
            return List.of();
        }

        PieceMovesCalculator p = new PieceMovesCalculator();
        // UP_RIGHT direction (row+1, col+1):
        p.toEndOfBoard(board, myPosition, piece, 1, 1, bishopValidMoves);
        // DOWN_RIGHT direction (row-1, col+1):
        p.toEndOfBoard(board, myPosition, piece, -1, 1, bishopValidMoves);
        // DOWN_LEFT direction (row-1, col-1):
        p.toEndOfBoard(board, myPosition, piece, -1, -1, bishopValidMoves);
        // UP_LEFT direction (row+1, col-1):
        p.toEndOfBoard(board, myPosition, piece, 1, -1, bishopValidMoves);

        return bishopValidMoves;


    }


}
