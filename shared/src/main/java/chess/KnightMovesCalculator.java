package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator {

    KnightMovesCalculator (){

    }


    public Collection<ChessMove> calculateKnightMoves (ChessBoard board, ChessPosition myPosition, ChessPiece piece){
        List<ChessMove> knight_valid_moves = new ArrayList<>();
        // validate the myPosition parameter (make sure it's on the board)
        if (myPosition.getRow() < 1 || myPosition.getRow() > 8 || myPosition.getColumn() < 1 || myPosition.getColumn() > 8) {
            return List.of();
        }

        // UP_RIGHT direction (row+2, col+1):


        // initialize direction:
        ChessPosition position_next = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1);
        ChessPiece piece_next;
        if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
            piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
            if (piece_next == null) {
                // if space is empty, add move to list
                knight_valid_moves.add(new ChessMove(myPosition, position_next, null));
            } else {
                // check enemy or friend, add to list if enemy:
                if (piece.getTeamColor() != piece_next.getTeamColor()) {
                    knight_valid_moves.add(new ChessMove(myPosition, position_next, null));
                }
            }
        }


        // DOWN_RIGHT direction (row-2, col+1):


        // initialize direction:
        position_next = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1);
        if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
            piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
            if (piece_next == null) {
                // if space is empty, add move to list
                knight_valid_moves.add(new ChessMove(myPosition, position_next, null));
            } else {
                // check enemy or friend, add to list if enemy:
                if (piece.getTeamColor() != piece_next.getTeamColor()) {
                    knight_valid_moves.add(new ChessMove(myPosition, position_next, null));
                }
            }
        }

        // DOWN_LEFT direction (row-2, col-1):


        // initialize direction:
        position_next = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1);
        if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
            piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
            if (piece_next == null) {
                // if space is empty, add move to list
                knight_valid_moves.add(new ChessMove(myPosition, position_next, null));
            } else {
                // check enemy or friend, add to list if enemy:
                if (piece.getTeamColor() != piece_next.getTeamColor()) {
                    knight_valid_moves.add(new ChessMove(myPosition, position_next, null));
                }
            }

        }
        // UP_LEFT direction (row+2, col-1):


        // initialize direction:
        position_next = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1);
        if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
            piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
            if (piece_next == null) {
                // if space is empty, add move to list
                knight_valid_moves.add(new ChessMove(myPosition, position_next, null));
            } else {
                // check enemy or friend, add to list if enemy:
                if (piece.getTeamColor() != piece_next.getTeamColor()) {
                    knight_valid_moves.add(new ChessMove(myPosition, position_next, null));
                }
            }
        }

//-----------------------------division between movement directions of knight--------------------------------


        // RIGHT_UP direction (row+1, col+2):


        // initialize direction:
        position_next = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2);
        if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
            piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
            if (piece_next == null) {
                // if space is empty, add move to list
                knight_valid_moves.add(new ChessMove(myPosition, position_next, null));
            } else {
                // check enemy or friend, add to list if enemy:
                if (piece.getTeamColor() != piece_next.getTeamColor()) {
                    knight_valid_moves.add(new ChessMove(myPosition, position_next, null));
                }
            }
        }

        // RIGHT_DOWN direction (row-1, col+2):


        // initialize direction:
        position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2);
        if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
            piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
            if (piece_next == null) {
                // if space is empty, add move to list
                knight_valid_moves.add(new ChessMove(myPosition, position_next, null));
            } else {
                // check enemy or friend, add to list if enemy:
                if (piece.getTeamColor() != piece_next.getTeamColor()) {
                    knight_valid_moves.add(new ChessMove(myPosition, position_next, null));
                }
            }
        }

        // LEFT_DOWN direction (row-1, col-2):


        // initialize direction:
        position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2);
        if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
            piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
            if (piece_next == null) {
                // if space is empty, add move to list
                knight_valid_moves.add(new ChessMove(myPosition, position_next, null));
            } else {
                // check enemy or friend, add to list if enemy:
                if (piece.getTeamColor() != piece_next.getTeamColor()) {
                    knight_valid_moves.add(new ChessMove(myPosition, position_next, null));
                }
            }
        }

        // LEFT_UP direction (row+1, col-2):


        // initialize direction:
        position_next = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2);
        if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
            piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
            if (piece_next == null) {
                // if space is empty, add move to list
                knight_valid_moves.add(new ChessMove(myPosition, position_next, null));
            } else {
                // check enemy or friend, add to list if enemy:
                if (piece.getTeamColor() != piece_next.getTeamColor()) {
                    knight_valid_moves.add(new ChessMove(myPosition, position_next, null));
                }
            }
        }

        return knight_valid_moves;

    }



}
