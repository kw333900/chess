package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator {

    PawnMovesCalculator (){

    }


// implementation of PAWN movement:
    // white_pawn:
    // - Only can move up the board (up twice if on row2, up once if
    // - If pawn is on row2 then it can move up 2 spaces
    // - If row of next position is row8 then promotion is not null
    // - Only can capture up_right(row+1,col+1) or up_left(row+1,col-1)
    // black_pawn:
    // - Only can move down the board
    // - If pawn is on row7 then it can move down 2 spaces
    // - If row of next position is row1 then promotion is not null
    // - Only can capture down_right(row-1,col+1) or down_left(row-1,col-1)

    public Collection<ChessMove> calculatePawnMoves (ChessBoard board, ChessPosition myPosition, ChessPiece piece){
        List<ChessMove> pawnValidMoves = new ArrayList<>();
        // validate (make sure it's on the board) the myPosition given as parameter
        if (myPosition.getRow() < 1 || myPosition.getRow() > 8 || myPosition.getColumn() < 1 || myPosition.getColumn() > 8) {
            return List.of();
        }

        // WHITE_PAWN:
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPiece piece_next;

            PieceMovesCalculator p = new PieceMovesCalculator();

            // UP_RIGHT direction for capture (row+1, col+1):
            p.pawnOneSpaceCapture(board, myPosition, piece, 1, 1, pawnValidMoves);


            // UP_LEFT direction for capture (row+1, col-1):
            p.pawnOneSpaceCapture(board, myPosition, piece, 1, -1, pawnValidMoves);

            // UP direction (row+1, col):
            p.pawnOneSpace(board, myPosition, piece, 1, 0, pawnValidMoves);



            // starting UP direction (row+2, col):
//            p.pawnStarting(board, myPosition, piece, 1, 0, pawnValidMoves);
//
            ChessPosition position_next = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
            if (myPosition.getRow() == 2 && piece_next == null) {
                // initialize direction:
                position_next = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                    piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                    if (piece_next == null) {
                        // if space is empty, add move to list
                        pawnValidMoves.add(new ChessMove(myPosition, position_next, null));
                    }
                }

            }


        }


        // BLACK_PAWN:
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {




//            ChessPiece piece_next;

//            PieceMovesCalculator p = new PieceMovesCalculator();
//
//            // DOWN_RIGHT direction for capture (row-1, col+1):
//            p.pawnOneSpaceCapture(board, myPosition, piece, -1, 1, pawnValidMoves);
//
//
//            // DOWN_LEFT direction for capture (row-1, col-1):
//            p.pawnOneSpaceCapture(board, myPosition, piece, -1, -1, pawnValidMoves);
//
//            // DOWN direction (row-1, col):
//            p.pawnOneSpace(board, myPosition, piece, -1, 0, pawnValidMoves);













            // initialize direction:
            ChessPosition position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            ChessPiece piece_next;
            if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (position_next.getRow() == 1 && piece_next != null && piece.getTeamColor() != piece_next.getTeamColor()) {
                    pawnValidMoves.add(new ChessMove(myPosition, position_next, ChessPiece.PieceType.QUEEN));
                    pawnValidMoves.add(new ChessMove(myPosition, position_next, ChessPiece.PieceType.BISHOP));
                    pawnValidMoves.add(new ChessMove(myPosition, position_next, ChessPiece.PieceType.ROOK));
                    pawnValidMoves.add(new ChessMove(myPosition, position_next, ChessPiece.PieceType.KNIGHT));
                } else if (piece_next != null && piece.getTeamColor() != piece_next.getTeamColor()) {
                    // if space isn't empty and enemy piece, add move to list
                    pawnValidMoves.add(new ChessMove(myPosition, position_next, null));
                }
            }





            // initialize direction:
            position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (position_next.getRow() == 1 && piece_next != null && piece.getTeamColor() != piece_next.getTeamColor()) {
                    pawnValidMoves.add(new ChessMove(myPosition, position_next, ChessPiece.PieceType.QUEEN));
                    pawnValidMoves.add(new ChessMove(myPosition, position_next, ChessPiece.PieceType.BISHOP));
                    pawnValidMoves.add(new ChessMove(myPosition, position_next, ChessPiece.PieceType.ROOK));
                    pawnValidMoves.add(new ChessMove(myPosition, position_next, ChessPiece.PieceType.KNIGHT));
                } else if (piece_next != null && piece.getTeamColor() != piece_next.getTeamColor()) {
                    // if space isn't empty and enemy piece, add move to list
                    pawnValidMoves.add(new ChessMove(myPosition, position_next, null));
                }
            }




            // initialize direction:
            position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
            if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                if (position_next.getRow() == 1 && piece_next == null){
                    pawnValidMoves.add(new ChessMove(myPosition, position_next, ChessPiece.PieceType.QUEEN));
                    pawnValidMoves.add(new ChessMove(myPosition, position_next, ChessPiece.PieceType.BISHOP));
                    pawnValidMoves.add(new ChessMove(myPosition, position_next, ChessPiece.PieceType.ROOK));
                    pawnValidMoves.add(new ChessMove(myPosition, position_next, ChessPiece.PieceType.KNIGHT));
                }
                else if (piece_next == null) {
                    // if space is empty, add move to list
                    pawnValidMoves.add(new ChessMove(myPosition, position_next, null));
                }
            }


            // starting DOWN direction (row-2, col):
//            PieceMovesCalculator p = new PieceMovesCalculator();
//            p.pawnStarting(board, myPosition, piece, -2, 0, pawnValidMoves);

            if (myPosition.getRow() == 7 && piece_next == null) {
                // initialize direction:
                position_next = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                    piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                    if (piece_next == null) {
                        // if space is empty, add move to list
                        pawnValidMoves.add(new ChessMove(myPosition, position_next, null));
                    }
                }
            }


        }


        return pawnValidMoves;
    }


}
