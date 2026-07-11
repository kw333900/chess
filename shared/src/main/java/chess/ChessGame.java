package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard chessGameBoard = new ChessBoard();
//    int turn_count_variable=0;
    ChessPosition curr_team_kings_position;
    TeamColor curr_team_color = TeamColor.WHITE;



    //    Collection<ChessMove> validMovesList;


//    ChessBoard chess_game_board;


    public ChessGame() {
        chessGameBoard.resetBoard();
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
//        if (turn_count_variable%2 == 0){
//            return TeamColor.WHITE;
//        } else {
//            return TeamColor.BLACK;
//        }
        return curr_team_color;
    }




    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {

//        if (team == TeamColor.WHITE){
//            turn_count_variable = 0;
//        } else {
//            turn_count_variable = 1;
//        }
        curr_team_color = team;

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
            ChessPiece curr_piece = chessGameBoard.getPiece(startPosition);
            piece_moves_calculator piece_moves = new piece_moves_calculator();
            Collection<ChessMove> validMovesList = piece_moves.calculate_piece_moves(chessGameBoard, startPosition);
            // iterate through list and remove moves that cause are in check (or cause check?):
            for (ChessMove move : validMovesList){

                // if in check:
                if (isInCheck(curr_piece.getTeamColor())){
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
//            turn_count_variable++;
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
        /*

        My words:
            - To determine if White is in check: First, find the white king. Iterate through all positions on the board and if that position
            contains an enemy, generate its moves and see if one of those moves is the white king's position. If you find one, return true.
            Otherwise, return false.

        My words (critiqued from Chat to be more precise):
            - To determine if White is in check: First, find and store the white king's position. Iterate through all positions on the board and if that position
            contains an enemy, generate its moves and for each move, compare the move's endPosition to white king's current position.
            If any enemy move ends on king's position, return true.
            Otherwise, return false.

            - How will I find and store the white king's position: Iterate through the board (2d array) and check piece type (Queen, Rook etc.)
            and check teamColor (White or Black). If King and White, store the ChessPosition (row, col) and exit loop.
         */



        // iterate through board to find and store team's king (2d array):
//        ChessPosition curr_team_kings_position;
        for (int i=1; i<9; i++){
            for (int j=1; j<9; j++){
                // check piece type and teamColor:
                ChessPosition curr_position = new ChessPosition(i,j);
                ChessPiece curr_piece = chessGameBoard.getPiece(curr_position);
                if (curr_piece != null) {
                    if (curr_piece.getPieceType() == ChessPiece.PieceType.KING && curr_piece.getTeamColor() == teamColor) {
                        curr_team_kings_position = new ChessPosition(i, j);
                        break;
                    }
                }
            }
        }


        // iterate through all positions on board:
        for (int i=1; i<9; i++){
            for (int j=1; j<9; j++){
                ChessPosition curr_position = new ChessPosition(i,j);
                ChessPiece curr_piece = chessGameBoard.getPiece(curr_position);
                if (curr_piece != null) {
                    // if position contains enemy piece:
                    if (curr_piece.getTeamColor() != teamColor) {
                        // generate the piece's moves:
                        piece_moves_calculator piece_moves = new piece_moves_calculator();
                        Collection<ChessMove> curr_piece_moves = piece_moves.calculate_piece_moves(chessGameBoard, curr_position);
                        // for each move:
                        for (ChessMove m : curr_piece_moves) {
                            // if m lands on kings_position:
                            if (m.getEndPosition().equals(curr_team_kings_position)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }


        return false;
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
        My words:
            - A team's king is in checkmate if it's currently in check and any move that it makes
            results in the king still being in check.

            First, call isInCheck. If that returns true, then we need to see if any move from one of our own pieces
            will make isInCheck be false. If not return true.

            How to see if any friendly move will make isInCheck be false?
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(chessGameBoard, chessGame.chessGameBoard) && Objects.equals(curr_team_kings_position, chessGame.curr_team_kings_position) && curr_team_color == chessGame.curr_team_color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessGameBoard, curr_team_kings_position, curr_team_color);
    }





}
