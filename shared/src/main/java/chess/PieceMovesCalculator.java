package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PieceMovesCalculator {
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

    public PieceMovesCalculator() {

    }

    // method to return list of valid moves for a particular piece type
    public Collection<ChessMove> calculatePieceMoves(ChessBoard board, ChessPosition myPosition)  {
        ChessPiece piece = board.getPiece(myPosition);
        if (piece == null){
            return List.of();
        }


// BISHOP:
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            BishopMovesCalculator bishopMovesCalculator = new BishopMovesCalculator();
            return bishopMovesCalculator.calculateBishopMoves(board, myPosition, piece);

        }

// ROOK:
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            RookMovesCalculator rookMovesCalculator = new RookMovesCalculator();
            return rookMovesCalculator.calculateRookMoves(board, myPosition, piece);

        }

// QUEEN:
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            QueenMovesCalculator queenMovesCalculator = new QueenMovesCalculator();
            return queenMovesCalculator.calculateQueenMoves(board, myPosition, piece);

        }

// KING:
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            KingMovesCalculator kingMovesCalculator = new KingMovesCalculator();
            return kingMovesCalculator.calculateKingMoves(board, myPosition, piece);

            }


// Knight:
        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            KnightMovesCalculator knightMovesCalculator = new KnightMovesCalculator();
            return knightMovesCalculator.calculateKnightMoves(board, myPosition, piece);

        }

// PAWN:

        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            PawnMovesCalculator pawnMovesCalculator = new PawnMovesCalculator();
            return pawnMovesCalculator.calculatePawnMoves(board, myPosition, piece);
            }


        return List.of();

        }



    public void toEndOfBoard(ChessBoard board, ChessPosition myPosition, ChessPiece piece, int row, int col, Collection<ChessMove> validMoves){
        // initialize direction for while loop:
        ChessPosition position_next = new ChessPosition(myPosition.getRow() + row, myPosition.getColumn() + col);
        while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
            ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
            if (piece_next == null) {
                // if space is empty, add move to list
                validMoves.add(new ChessMove(myPosition, position_next, null));
            } else {
                // check enemy or friend, add to list if enemy:
                if (piece.getTeamColor() != piece_next.getTeamColor()) {
                    validMoves.add(new ChessMove(myPosition, position_next, null));
                }
                // break out of this direction loop bc space is blocked
                break;
            }
            // increment to next position in that direction
            position_next = new ChessPosition(position_next.getRow() + row, position_next.getColumn() + col);
        }
    }


    public void oneSpace(ChessBoard board, ChessPosition myPosition, ChessPiece piece, int row, int col, Collection<ChessMove> validMoves){
        // initialize direction:
        ChessPosition position_next = new ChessPosition(myPosition.getRow() + row, myPosition.getColumn() + col);
        ChessPiece piece_next;
        if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
            piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
            if (piece_next == null) {
                // if space is empty, add move to list
                validMoves.add(new ChessMove(myPosition, position_next, null));
            } else {
                // check enemy or friend, add to list if enemy:
                if (piece.getTeamColor() != piece_next.getTeamColor()) {
                    validMoves.add(new ChessMove(myPosition, position_next, null));
                }
            }
        }
    }


    public void pawnOneSpace(ChessBoard board, ChessPosition myPosition, ChessPiece piece, int row, int col, Collection<ChessMove> validMoves){

    }


    public void pawnStarting(ChessBoard board, ChessPosition myPosition, ChessPiece piece, int row, int col, Collection<ChessMove> validMoves){
        ChessPosition position_next = new ChessPosition(myPosition.getRow() + row, myPosition.getColumn() + col);
        ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));

        if (myPosition.getRow() == 7 && piece_next == null) {
            // initialize direction:
            position_next = new ChessPosition(myPosition.getRow() + row, myPosition.getColumn() + col);
            if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    validMoves.add(new ChessMove(myPosition, position_next, null));
                }
            }
        }
    }



    }


