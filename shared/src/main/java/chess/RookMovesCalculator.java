package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMovesCalculator {

    RookMovesCalculator (){

    }

    public Collection<ChessMove> calculateRookMoves (ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        List<ChessMove> rookValidMoves = new ArrayList<>();
        // validate (make sure it's on the board) the myPosition given as parameter
        if (myPosition.getRow() < 1 || myPosition.getRow() > 8 || myPosition.getColumn() < 1 || myPosition.getColumn() > 8) {
            return List.of();
        }

        PieceMovesCalculator p = new PieceMovesCalculator();
        // UP direction (row+1, col):
        p.toEndOfBoard(board, myPosition, piece, 1, 0, rookValidMoves);
        // RIGHT direction (row, col+1):
        p.toEndOfBoard(board, myPosition, piece, 0, 1, rookValidMoves);
        // DOWN direction (row-1, col):
        p.toEndOfBoard(board, myPosition, piece, -1, 0, rookValidMoves);
        // LEFT direction (row, col-1):
        p.toEndOfBoard(board, myPosition, piece, 0, -1, rookValidMoves);

        return rookValidMoves;

    }






}
