package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class piece_moves_calculator {

    // logic for returning collection of valid moves for bishop:
        /*
        1. Check piece type (if bishop, go into bishop loop)
        2. Get the current position of the piece
        3. Implement its movement (diagonal for bishop)
            - Check each possible position to see if there is a piece there already (also figure out blocked case)
            - In for loop, row&col can't go above 8 or below 1 for its movement
        4. Return collection of valid moves (list of ChessMoves, use List.of()?)
         */

    public piece_moves_calculator() {

    }


    public Collection<ChessMove> calculate_piece_moves (ChessBoard board, ChessPosition myPosition){
        ChessPiece piece = board.getPiece(myPosition);

        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP){
            // up_right:
//            for (int ; myPosition.row; ){
//
//            }
            List<ChessMove> bishop_valid_moves = new ArrayList<>();
            if (myPosition.getRow() < 1 || myPosition.getRow() >8 || myPosition.getColumn() < 1 || myPosition.getColumn() > 8){
                return List.of();
            }
            // up_right:
            ChessPosition position_next = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
            while ((position_next.getRow() != 8 && position_next.getRow() != 0)  && (position_next.getColumn() != 8 && position_next.getColumn() != 0)){

                ChessPiece piece_next = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1));


                if (piece_next == null){
                    bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend
                    // add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()){
                        bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    break;
                }
                position_next = new ChessPosition(position_next.getRow()+1, position_next.getColumn()+1);

            }
            // down_right
////            while (){
//
//            }
        }
        return List.of();
    }
}
