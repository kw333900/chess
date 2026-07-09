package chess;

import java.util.Collection;
import java.util.List;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard chessGameBoard;
    int turn_count_variable;
//    Collection<ChessMove> validMovesList;


//    ChessBoard chess_game_board;


    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        /* pseudocode:
            if count_var is even:
                return white
            else if count_var is odd:
                return odd */
        if (turn_count_variable%2 == 0){
            return TeamColor.WHITE;
        } else {
            return TeamColor.BLACK;
        }
    }




    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {

        if (team == TeamColor.WHITE){
            turn_count_variable = 0;
        } else {
            turn_count_variable = 1;
        }


    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        // White always starts the game. Use even/odd numbers
        // to determine whose turn it is. even=white,odd=black.
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        /* pseudocode:
            candidateMoves = pieceMoves(...)

            for each move
                copy the board
                make the move on the copy

                if king is NOT in check on copied board
                    keep move

        */

        if (startPosition != null){
            piece_moves_calculator piece_moves = new piece_moves_calculator();
            Collection<ChessMove> validMovesList = piece_moves.calculate_piece_moves(chessGameBoard, startPosition);
            // iterate through list and remove moves that cause are in check (or cause check?):
            for (ChessMove move : validMovesList){

                // if in check:
                if (isInCheck(getTeamTurn())){   // WHY IS THE PARAMETER TO isInCheck THE TEAMCOLOR?!
                    validMovesList.remove(move);
                }
            }
            return validMovesList;


        } else {
            return null;
        }


    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        /*
        move is illegal if move is invalid for piece at startPosition, or it's not the corresponding team's turn

        if move is illegal:
            throw InvalidMoveException
        else:
            execute move
            increment turn counter
         */

        if (!validMoves(move.getStartPosition()).contains(move)){
//            throw InvalidMoveException;
        } else {
            ChessPiece chess_piece = chessGameBoard.getPiece(move.getStartPosition());
//            if (move.getPromotionPiece()==null){
//                ChessPiece.PieceType piece_type = chess_piece.getPieceType();
//            }

            // move piece:
            chessGameBoard.addPiece(move.getEndPosition(), chess_piece);
            // set to null where it moved from:
            chessGameBoard.addPiece(move.getStartPosition(), null);
            // change turn:
            turn_count_variable++;
        }




    }
    // ^this involves a whole turn (movement, rules, changing team turn)

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");

        /*
        Find that team's king.

        For every enemy piece:

            Calculate its piece moves.

            If one attacks the king's square

                return true

        return false
         */

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");

        /*
        if isInCheck is true for all the team's pieces on the board:
            return true
        else:
            return false
         */


    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");


        /*
        if valid moves is empty and isInCheck is false:
            return true
        else:
            return false
         */
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        chessGameBoard = board;

    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessGameBoard;

    }


//    @Override
//    public String toString() {
//        return "ChessGame{" +
//                "chessGameBoard=" + chessGameBoard +
//                ", turn_count_variable=" + turn_count_variable +
//                '}';
//    }

    // Overrides - I'm not sure if these are correct:
    @Override
    public int hashCode() {
        return super.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
