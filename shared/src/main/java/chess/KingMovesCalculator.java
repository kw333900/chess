package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator {

    KingMovesCalculator (){

    }


    public Collection<ChessMove> calculateKingMoves (ChessBoard board, ChessPosition myPosition, ChessPiece piece){
        List<ChessMove> kingValidMoves = new ArrayList<>();
        // validate (make sure it's on the board) the myPosition given as parameter
        if (myPosition.getRow() < 1 || myPosition.getRow() > 8 || myPosition.getColumn() < 1 || myPosition.getColumn() > 8) {
            return List.of();
        }

        PieceMovesCalculator p = new PieceMovesCalculator();
        // UP_RIGHT direction (row+1, col+1):
        p.oneSpace(board, myPosition, piece, 1, 1, kingValidMoves);
        // DOWN_RIGHT direction (row-1, col+1):
        p.oneSpace(board, myPosition, piece, -1, 1, kingValidMoves);
        // DOWN_LEFT direction (row-1, col-1):
        p.oneSpace(board, myPosition, piece, -1, -1, kingValidMoves);
        // UP_LEFT direction (row+1, col-1):
        p.oneSpace(board, myPosition, piece, 1, -1, kingValidMoves);
        // UP direction (row+1, col):
        p.oneSpace(board, myPosition, piece, 1, 0, kingValidMoves);
        // RIGHT direction (row, col+1):
        p.oneSpace(board, myPosition, piece, 0, 1, kingValidMoves);
        // DOWN direction (row-1, col):
        p.oneSpace(board, myPosition, piece, -1, 0, kingValidMoves);
        // LEFT direction (row, col-1):
        p.oneSpace(board, myPosition, piece, 0, -1, kingValidMoves);

        return kingValidMoves;
    }

}
