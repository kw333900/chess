package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator {


    QueenMovesCalculator (){

    }


    public Collection<ChessMove> calculateQueenMoves (ChessBoard board, ChessPosition myPosition, ChessPiece piece){
        List<ChessMove> queenValidMoves = new ArrayList<>();
        // validate (make sure it's on the board) the myPosition given as parameter
        if (myPosition.getRow() < 1 || myPosition.getRow() > 8 || myPosition.getColumn() < 1 || myPosition.getColumn() > 8) {
            return List.of();
        }

        PieceMovesCalculator p = new PieceMovesCalculator();
        // UP_RIGHT direction (row+1, col+1):
        p.toEndOfBoard(board, myPosition, piece, 1, 1, queenValidMoves);
        // DOWN_RIGHT direction (row-1, col+1):
        p.toEndOfBoard(board, myPosition, piece, -1, 1, queenValidMoves);
        // DOWN_LEFT direction (row-1, col-1):
        p.toEndOfBoard(board, myPosition, piece, -1, -1, queenValidMoves);
        // UP_LEFT direction (row+1, col-1):
        p.toEndOfBoard(board, myPosition, piece, 1, -1, queenValidMoves);
        // UP direction (row+1, col):
        p.toEndOfBoard(board, myPosition, piece, 1, 0, queenValidMoves);
        // RIGHT direction (row, col+1):
        p.toEndOfBoard(board, myPosition, piece, 0, 1, queenValidMoves);
        // DOWN direction (row-1, col):
        p.toEndOfBoard(board, myPosition, piece, -1, 0, queenValidMoves);
        // LEFT direction (row, col-1):
        p.toEndOfBoard(board, myPosition, piece, 0, -1, queenValidMoves);

        return queenValidMoves;
    }

}
