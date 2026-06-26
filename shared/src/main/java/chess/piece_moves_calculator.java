package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class piece_moves_calculator {

    // logic for returning collection of valid moves for a specific piece:
        /*
        1. Check piece type (e.g. if bishop, go into bishop loop)
        2. Get the current position of the piece
        3. Implement its movement (e.g. diagonal for bishop)
            - Check each possible position to see if there is a piece there already (also figure out blocked case)
            - In for loop, row&col can't go above 8 or below 1 for its movement
            - Check if piece in movement path is enemy or friend (if enemy, include move in list of valid moves)
        4. Return collection of valid moves (list of ChessMoves, use List.of()?)
         */

    // empty constructor:
    public piece_moves_calculator() {

    }

    // method to return list of valid moves for a particular piece type
    public Collection<ChessMove> calculate_piece_moves (ChessBoard board, ChessPosition myPosition){
        ChessPiece piece = board.getPiece(myPosition);




    // BISHOP:
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP){
            List<ChessMove> bishop_valid_moves = new ArrayList<>();
            // validate (make sure it's on the board) the myPosition given as parameter
            if (myPosition.getRow() < 1 || myPosition.getRow() > 8 || myPosition.getColumn() < 1 || myPosition.getColumn() > 8){
                return List.of();
            }

    // UP_RIGHT direction (row+1, col+1):


            // initialize direction for while loop:
            ChessPosition position_next = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
            while ((position_next.getRow() != 9 && position_next.getRow() != 0)  && (position_next.getColumn() != 9 && position_next.getColumn() != 0)){
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null){
                    // if space is empty, add move to list
                    bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()){
                        bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow()+1, position_next.getColumn()+1);
            }



    // DOWN_RIGHT direction (row-1, col+1):


            // initialize direction for while loop:
            position_next = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
            while ((position_next.getRow() != 9 && position_next.getRow() != 0)  && (position_next.getColumn() != 9 && position_next.getColumn() != 0)){
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null){
                    // if space is empty, add move to list
                    bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()){
                        bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow()-1, position_next.getColumn()+1);
            }





    // DOWN_LEFT direction (row-1, col-1):


            // initialize direction for while loop:
            position_next = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
            while ((position_next.getRow() != 9 && position_next.getRow() != 0)  && (position_next.getColumn() != 9 && position_next.getColumn() != 0)){
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null){
                    // if space is empty, add move to list
                    bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()){
                        bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow()-1, position_next.getColumn()-1);
            }






    // UP_LEFT direction (row+1, col-1):


            // initialize direction for while loop:
            position_next = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
            while ((position_next.getRow() != 9 && position_next.getRow() != 0)  && (position_next.getColumn() != 9 && position_next.getColumn() != 0)){
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null){
                    // if space is empty, add move to list
                    bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()){
                        bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow()+1, position_next.getColumn()-1);
            }





            return bishop_valid_moves;

        }
        return List.of();
    }
}
