package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator {

    public BishopMovesCalculator (){

    }


    public Collection<ChessMove> calculateBishopMoves (ChessBoard board, ChessPosition myPosition, ChessPiece piece){
        List<ChessMove> bishop_valid_moves = new ArrayList<>();
        // validate (make sure it's on the board) the myPosition given as parameter
        if (myPosition.getRow() < 1 || myPosition.getRow() > 8 || myPosition.getColumn() < 1 || myPosition.getColumn() > 8) {
            return List.of();
        }

        // UP_RIGHT direction (row+1, col+1):
        bishopDirection(board, myPosition, piece, 1, 1, bishop_valid_moves);

        // DOWN_RIGHT direction (row-1, col+1):
        bishopDirection(board, myPosition, piece, -1, 1, bishop_valid_moves);

        // DOWN_LEFT direction (row-1, col-1):
        bishopDirection(board, myPosition, piece, -1, -1, bishop_valid_moves);

        // UP_LEFT direction (row+1, col-1):
        bishopDirection(board, myPosition, piece, 1, -1, bishop_valid_moves);

        return bishop_valid_moves;


    }




    public void bishopDirection (ChessBoard board, ChessPosition myPosition, ChessPiece piece, int row, int col, Collection<ChessMove> bishop_valid_moves){
        // initialize direction for while loop:
        ChessPosition position_next = new ChessPosition(myPosition.getRow() + row, myPosition.getColumn() + col);
        while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
            ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
            if (piece_next == null) {
                // if space is empty, add move to list
                bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
            } else {
                // check enemy or friend, add to list if enemy:
                if (piece.getTeamColor() != piece_next.getTeamColor()) {
                    bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                }
                // break out of this direction loop bc space is blocked
                break;
            }
            // increment to next position in that direction
            position_next = new ChessPosition(position_next.getRow() + row, position_next.getColumn() + col);
        }
    }



}
