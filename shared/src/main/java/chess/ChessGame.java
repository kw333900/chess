package chess;

import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard chessGameBoard;
    int turn_count_variable;

//    ChessBoard chess_game_board;


    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
//        throw new RuntimeException("Not implemented");
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
//        throw new RuntimeException("Not implemented");

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
//        throw new RuntimeException("Not implemented");

        /* pseudocode:
            if there is a piece at startPosition:
                do piece_moves_calculator
                helper function to remove invalid check moves?
                return valid_moves_list
            else:
                return null
        */

        if (startPosition != null){
            piece_moves_calculator piece_moves = new piece_moves_calculator();
            return piece_moves.calculate_piece_moves(chessGameBoard, startPosition);


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
//        throw new RuntimeException("Not implemented");


        turn_count_variable++;

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
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
//        throw new RuntimeException("Not implemented");
        chessGameBoard = board;

    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
//        throw new RuntimeException("Not implemented");
        return chessGameBoard;

    }













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
