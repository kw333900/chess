package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator {

    PawnMovesCalculator (){

    }





    public Collection<ChessMove> calculatePawnMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {

        List<ChessMove> pawnValidMoves = new ArrayList<>();

        // validate (make sure it's on the board)
        if (myPosition.getRow() < 1 || myPosition.getRow() > 8
                || myPosition.getColumn() < 1 || myPosition.getColumn() > 8) {
            return List.of();
        }

        PieceMovesCalculator p = new PieceMovesCalculator();

        int direction;
        int startRow;

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            direction = 1;
            startRow = 2;
        } else {
            direction = -1;
            startRow = 7;
        }

        // Capture diagonally right
        p.pawnOneSpaceCapture(board, myPosition, piece, direction, 1, pawnValidMoves);

        // Capture diagonally left
        p.pawnOneSpaceCapture(board, myPosition, piece, direction, -1, pawnValidMoves);

        // Move one square forward
        p.pawnOneSpace(board, myPosition, piece, direction, 0, pawnValidMoves);

        // Check the square directly in front
        ChessPosition position_next = new ChessPosition(
                myPosition.getRow() + direction,
                myPosition.getColumn());

        ChessPiece piece_next = board.getPiece(position_next);

        // Starting two-square move
        if (myPosition.getRow() == startRow && piece_next == null) {

            position_next = new ChessPosition(
                    myPosition.getRow() + (2 * direction),
                    myPosition.getColumn());

            if ((position_next.getRow() < 9 && position_next.getRow() > 0)
                    && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {

                piece_next = board.getPiece(position_next);

                if (piece_next == null) {
                    pawnValidMoves.add(new ChessMove(myPosition, position_next, null));
                }
            }
        }

        return pawnValidMoves;
    }


}
