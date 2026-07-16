package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator {

    KnightMovesCalculator (){

    }


    public Collection<ChessMove> calculateKnightMoves (ChessBoard board, ChessPosition myPosition, ChessPiece piece){
        List<ChessMove> knightValidMoves = new ArrayList<>();
        // validate (make sure it's on the board) the myPosition given as parameter
        if (myPosition.getRow() < 1 || myPosition.getRow() > 8 || myPosition.getColumn() < 1 || myPosition.getColumn() > 8) {
            return List.of();
        }

        PieceMovesCalculator p = new PieceMovesCalculator();
        // UP_RIGHT direction (row+2, col+1):
        p.oneSpace(board, myPosition, piece, 2, 1, knightValidMoves);
        // DOWN_RIGHT direction (row-2, col+1):
        p.oneSpace(board, myPosition, piece, -2, 1, knightValidMoves);
        // DOWN_LEFT direction (row-2, col-1):
        p.oneSpace(board, myPosition, piece, -2, -1, knightValidMoves);
        // UP_LEFT direction (row+2, col-1):
        p.oneSpace(board, myPosition, piece, 2, -1, knightValidMoves);
        // RIGHT_UP direction (row+1, col+2):
        p.oneSpace(board, myPosition, piece, 1, 2, knightValidMoves);
        // RIGHT_DOWN direction (row-1, col+2):
        p.oneSpace(board, myPosition, piece, -1, 2, knightValidMoves);
        // LEFT_DOWN direction (row-1, col-2):
        p.oneSpace(board, myPosition, piece, -1, -2, knightValidMoves);
        // LEFT_UP direction (row+1, col-2):
        p.oneSpace(board, myPosition, piece, 1, -2, knightValidMoves);

        return knightValidMoves;

    }



}
