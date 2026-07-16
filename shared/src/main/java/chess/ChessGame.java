package chess;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard chessGameBoard = new ChessBoard();
    TeamColor currTeamColor = TeamColor.WHITE;




    public ChessGame() {
        chessGameBoard.resetBoard();
    }








    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currTeamColor;
    }




    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currTeamColor = team;

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
            PieceMovesCalculator piece_moves = new PieceMovesCalculator();
            Collection<ChessMove> potential_validMovesList = piece_moves.calculatePieceMoves(chessGameBoard, startPosition);
            Collection<ChessMove> final_validMovesList = new java.util.ArrayList<>(List.of());
            // iterate through list and remove moves that cause are in check (or cause check?):
            for (ChessMove move : potential_validMovesList){


                ChessBoard copy_board = makeMoveOnCopy(chessGameBoard, move, startPosition);

                curr_piece = copy_board.getPiece(move.getEndPosition());







                if (!helperIsInCheck(curr_piece.getTeamColor(), copy_board)){
                    final_validMovesList.add(move);
                }
            }
            return final_validMovesList;


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
        My words:
            - if move is illegal (or not in validmoves list), throw exception. else, execute it.

            execute it means




        move is illegal if move is invalid for piece at startPosition, or it's not the corresponding team's turn

        if move is illegal:
            throw InvalidMoveException
        else:
            execute move
            increment turn counter
         */
        ChessPiece chess_piece = chessGameBoard.getPiece(move.getStartPosition());
        if (chess_piece == null){
            throw new InvalidMoveException("");
        }


        if (chess_piece.getPieceType() != ChessPiece.PieceType.KING && (validMoves(move.getStartPosition()).isEmpty() || !validMoves(move.getStartPosition()).contains(move))){
            throw new InvalidMoveException("");
        } else {
            chess_piece = chessGameBoard.getPiece(move.getStartPosition());
            if (chess_piece.getTeamColor() != getTeamTurn()){
                throw new InvalidMoveException("");
            }
            if (move.getPromotionPiece()!=null){
                ChessPiece.PieceType promo_piece_type = move.getPromotionPiece();
                // move piece:
                chessGameBoard.addPiece(move.getEndPosition(), new ChessPiece(chess_piece.getTeamColor(), promo_piece_type));
                // set to null where it moved from:
                chessGameBoard.addPiece(move.getStartPosition(), null);
                // change turn:
                if (getTeamTurn() == TeamColor.WHITE){
                    setTeamTurn(TeamColor.BLACK);
                } else{
                    setTeamTurn(TeamColor.WHITE);
                }
            } else{
                // move piece:
                chessGameBoard.addPiece(move.getEndPosition(), chess_piece);
                // set to null where it moved from:
                chessGameBoard.addPiece(move.getStartPosition(), null);
                // change turn:
                if (getTeamTurn() == TeamColor.WHITE){
                    setTeamTurn(TeamColor.BLACK);
                } else{
                    setTeamTurn(TeamColor.WHITE);
                }
            }


        }




    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
       return helperIsInCheck(teamColor, chessGameBoard);
    }








    public boolean helperIsInCheck(TeamColor teamColor, ChessBoard board){
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
        ChessPosition curr_team_kings_position = null;
        for (int i=1; i<9; i++){
            for (int j=1; j<9; j++){
                // check piece type and teamColor:
                ChessPosition curr_position = new ChessPosition(i,j);
                ChessPiece curr_piece = board.getPiece(curr_position);
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
                ChessPiece curr_piece = board.getPiece(curr_position);
                if (curr_piece != null) {
                    // if position contains enemy piece:
                    if (curr_piece.getTeamColor() != teamColor) {
                        // generate the piece's moves:
                        PieceMovesCalculator piece_moves = new PieceMovesCalculator();
                        Collection<ChessMove> curr_piece_moves = piece_moves.calculatePieceMoves(board, curr_position);
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
//        throw new RuntimeException("Not implemented");

        /*
        My words:
            - A team's king is in checkmate if it's currently in check and any move that it makes
            results in the king still being in check.

            First, call isInCheck. If that returns true, then we need to see if any move from one of our own pieces
            will make isInCheck be false. If not return true.

            How to see if any friendly move will make isInCheck be false?



            Other concept:
            if the king is in check mate, see if any other friendly pieces moves can change that, if not return true
         */


        // if isincheck is true and validmoves is empty

        // iterate through board to find and store team's king (2d array):
        ChessPosition curr_team_kings_position = null;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                // check piece type and teamColor:
                ChessPosition curr_position = new ChessPosition(i, j);
                ChessPiece curr_piece = chessGameBoard.getPiece(curr_position);
                if (curr_piece != null) {
                    if (curr_piece.getPieceType() == ChessPiece.PieceType.KING && curr_piece.getTeamColor() == teamColor) {
                        curr_team_kings_position = new ChessPosition(i, j);
                        break;
                    }
                }
            }
        }


        ChessBoard copy_board = null;
        if (isInCheck(teamColor) && validMoves(curr_team_kings_position).isEmpty()) {
            // iterate through all friendly pieces and see if their moves will change check:


            // iterate through all positions on board:
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition curr_position = new ChessPosition(i, j);
                    ChessPiece curr_piece = chessGameBoard.getPiece(curr_position);
                    if (curr_piece != null) {
                        // if position contains friendly piece:
                        if (curr_piece.getTeamColor() == teamColor) {
                            // generate the piece's moves:
                            PieceMovesCalculator piece_moves = new PieceMovesCalculator();
                            Collection<ChessMove> curr_piece_moves = piece_moves.calculatePieceMoves(chessGameBoard, curr_position);
                            // for each move:
                            for (ChessMove m : curr_piece_moves) {
                                // if move makes isincheck be false:

                                copy_board = makeMoveOnCopy(chessGameBoard, m, curr_position);

                                curr_piece = copy_board.getPiece(m.getEndPosition());


                                if (!helperIsInCheck(curr_piece.getTeamColor(), copy_board)) {
                                    return false;
                                }


                            }
                        }
                    }
                }
            }


        }

        System.out.println(copy_board);


        return isInCheck(teamColor) && validMoves(curr_team_kings_position).isEmpty();


    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        // iterate through board to find and store team's king (2d array):
        ChessPosition curr_team_kings_position = null;
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


        return !isInCheck(teamColor) && validMoves(curr_team_kings_position).isEmpty() && (curr_team_kings_position.getRow() != 1 && curr_team_kings_position.getColumn() != 5);


    }









    private ChessBoard makeMoveOnCopy(ChessBoard board,
                                      ChessMove move,
                                      ChessPosition startPosition) {

        ChessBoard copy = new ChessBoard(board);

        ChessPiece piece = copy.getPiece(startPosition);

        copy.addPiece(move.getEndPosition(), piece);
        copy.addPiece(move.getStartPosition(), null);

        return copy;
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
        return Objects.equals(chessGameBoard, chessGame.chessGameBoard) && currTeamColor == chessGame.currTeamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessGameBoard, currTeamColor);
    }
}
