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
    public Collection<ChessMove> calculate_piece_moves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);


// BISHOP:
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            List<ChessMove> bishop_valid_moves = new ArrayList<>();
            // validate (make sure it's on the board) the myPosition given as parameter
            if (myPosition.getRow() < 1 || myPosition.getRow() > 8 || myPosition.getColumn() < 1 || myPosition.getColumn() > 8) {
                return List.of();
            }

            // UP_RIGHT direction (row+1, col+1):


            // initialize direction for while loop:
            ChessPosition position_next = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow() + 1, position_next.getColumn() + 1);
            }


            // DOWN_RIGHT direction (row-1, col+1):


            // initialize direction for while loop:
            position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow() - 1, position_next.getColumn() + 1);
            }


            // DOWN_LEFT direction (row-1, col-1):


            // initialize direction for while loop:
            position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow() - 1, position_next.getColumn() - 1);
            }


            // UP_LEFT direction (row+1, col-1):


            // initialize direction for while loop:
            position_next = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        bishop_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow() + 1, position_next.getColumn() - 1);
            }


            return bishop_valid_moves;

        }


// ROOK:
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            List<ChessMove> rook_valid_moves = new ArrayList<>();
            // validate (make sure it's on the board) the myPosition given as parameter
            if (myPosition.getRow() < 1 || myPosition.getRow() > 8 || myPosition.getColumn() < 1 || myPosition.getColumn() > 8) {
                return List.of();
            }

            // UP direction (row+1, col):


            // initialize direction for while loop:
            ChessPosition position_next = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    rook_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        rook_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow() + 1, position_next.getColumn());
            }


            // RIGHT direction (row, col+1):


            // initialize direction for while loop:
            position_next = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
            while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    rook_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        rook_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow(), position_next.getColumn() + 1);
            }


            // DOWN direction (row-1, col):


            // initialize direction for while loop:
            position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    rook_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        rook_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow() - 1, position_next.getColumn());
            }


            // LEFT direction (row, col-1):


            // initialize direction for while loop:
            position_next = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
            while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    rook_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        rook_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow(), position_next.getColumn() - 1);
            }


            return rook_valid_moves;

        }


// QUEEN:
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            List<ChessMove> queen_valid_moves = new ArrayList<>();
            // validate (make sure it's on the board) the myPosition given as parameter
            if (myPosition.getRow() < 1 || myPosition.getRow() > 8 || myPosition.getColumn() < 1 || myPosition.getColumn() > 8) {
                return List.of();
            }

            // UP_RIGHT direction (row+1, col+1):


            // initialize direction for while loop:
            ChessPosition position_next = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    queen_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        queen_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow() + 1, position_next.getColumn() + 1);
            }


            // DOWN_RIGHT direction (row-1, col+1):


            // initialize direction for while loop:
            position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    queen_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        queen_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow() - 1, position_next.getColumn() + 1);
            }


            // DOWN_LEFT direction (row-1, col-1):


            // initialize direction for while loop:
            position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    queen_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        queen_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow() - 1, position_next.getColumn() - 1);
            }


            // UP_LEFT direction (row+1, col-1):


            // initialize direction for while loop:
            position_next = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    queen_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        queen_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow() + 1, position_next.getColumn() - 1);
            }


//-----------------------------division between diagonal and forward movements of queen--------------------------------


            // UP direction (row+1, col):


            // initialize direction for while loop:
            position_next = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    queen_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        queen_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow() + 1, position_next.getColumn());
            }


            // RIGHT direction (row, col+1):


            // initialize direction for while loop:
            position_next = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
            while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    queen_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        queen_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow(), position_next.getColumn() + 1);
            }


            // DOWN direction (row-1, col):


            // initialize direction for while loop:
            position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    queen_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        queen_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow() - 1, position_next.getColumn());
            }


            // LEFT direction (row, col-1):


            // initialize direction for while loop:
            position_next = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
            while ((position_next.getRow() != 9 && position_next.getRow() != 0) && (position_next.getColumn() != 9 && position_next.getColumn() != 0)) {
                ChessPiece piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    queen_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        queen_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                    // break out of this direction loop bc space is blocked
                    break;
                }
                // increment to next position in that direction
                position_next = new ChessPosition(position_next.getRow(), position_next.getColumn() - 1);
            }


            return queen_valid_moves;

        }


// KING:
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            List<ChessMove> king_valid_moves = new ArrayList<>();
            // validate the myPosition parameter (make sure it's on the board)
            if (myPosition.getRow() < 1 || myPosition.getRow() > 8 || myPosition.getColumn() < 1 || myPosition.getColumn() > 8) {
                return List.of();
            }

            // UP_RIGHT direction (row+1, col+1):


            // initialize direction:
            ChessPosition position_next = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            ChessPiece piece_next;
            if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    king_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        king_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                }
            }


            // DOWN_RIGHT direction (row-1, col+1):


            // initialize direction:
            position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if (piece_next == null) {
                    // if space is empty, add move to list
                    king_valid_moves.add(new ChessMove(myPosition, position_next, null));
                } else {
                    // check enemy or friend, add to list if enemy:
                    if (piece.getTeamColor() != piece_next.getTeamColor()) {
                        king_valid_moves.add(new ChessMove(myPosition, position_next, null));
//                    }
                    }
                }

                // DOWN_LEFT direction (row-1, col-1):


                // initialize direction:
                position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                    piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                    if (piece_next == null) {
                        // if space is empty, add move to list
                        king_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    } else {
                        // check enemy or friend, add to list if enemy:
                        if (piece.getTeamColor() != piece_next.getTeamColor()) {
                            king_valid_moves.add(new ChessMove(myPosition, position_next, null));
                        }
                    }

                }
                // UP_LEFT direction (row+1, col-1):


                // initialize direction:
                position_next = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                    piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                    if (piece_next == null) {
                        // if space is empty, add move to list
                        king_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    } else {
                        // check enemy or friend, add to list if enemy:
                        if (piece.getTeamColor() != piece_next.getTeamColor()) {
                            king_valid_moves.add(new ChessMove(myPosition, position_next, null));
                        }
                    }
                }

//-----------------------------division between diagonal and forward movements of king--------------------------------


                // UP direction (row+1, col):


                // initialize direction:
                position_next = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                    piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                    if (piece_next == null) {
                        // if space is empty, add move to list
                        king_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    } else {
                        // check enemy or friend, add to list if enemy:
                        if (piece.getTeamColor() != piece_next.getTeamColor()) {
                            king_valid_moves.add(new ChessMove(myPosition, position_next, null));
                        }
                    }
                }

                // RIGHT direction (row, col+1):


                // initialize direction:
                position_next = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
                if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                    piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                    if (piece_next == null) {
                        // if space is empty, add move to list
                        king_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    } else {
                        // check enemy or friend, add to list if enemy:
                        if (piece.getTeamColor() != piece_next.getTeamColor()) {
                            king_valid_moves.add(new ChessMove(myPosition, position_next, null));
                        }
                    }
                }

                // DOWN direction (row-1, col):


                // initialize direction:
                position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                    piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                    if (piece_next == null) {
                        // if space is empty, add move to list
                        king_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    } else {
                        // check enemy or friend, add to list if enemy:
                        if (piece.getTeamColor() != piece_next.getTeamColor()) {
                            king_valid_moves.add(new ChessMove(myPosition, position_next, null));
                        }
                    }
                }

                // LEFT direction (row, col-1):


                // initialize direction:
                position_next = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
                if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                    piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                    if (piece_next == null) {
                        // if space is empty, add move to list
                        king_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    } else {
                        // check enemy or friend, add to list if enemy:
                        if (piece.getTeamColor() != piece_next.getTeamColor()) {
                            king_valid_moves.add(new ChessMove(myPosition, position_next, null));
                        }
                    }
                }

                return king_valid_moves;

            }
        }


// Knight:
        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
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
                position_next = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()+2);
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
                position_next = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn() + 2);
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
                position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()-2);
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
                position_next = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn() - 2);
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


// PAWN:
        // pseudocode for PAWN:
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

        if (piece.getPieceType() == ChessPiece.PieceType.PAWN){
            List<ChessMove> pawn_valid_moves = new ArrayList<>();
            // validate (make sure it's on the board) the myPosition given as parameter
            if (myPosition.getRow() < 1 || myPosition.getRow() > 8 || myPosition.getColumn() < 1 || myPosition.getColumn() > 8) {
                return List.of();
            }

            // WHITE_PAWN:
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){



                // UP_RIGHT direction for capture (row+1, col+1):


                // initialize direction:
                ChessPosition position_next = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                ChessPiece piece_next;
                if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                    piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                    if (piece_next != null && piece.getTeamColor() != piece_next.getTeamColor()) {
                        // if space isn't empty and enemy piece, add move to list
                        pawn_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                }






                // UP_LEFT direction for capture (row+1, col-1):


                // initialize direction:
                 position_next = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                    piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                    if (piece_next != null && piece.getTeamColor() != piece_next.getTeamColor()) {
                        // if space isn't empty and enemy piece, add move to list
                        pawn_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                }





                // UP direction (row+1, col):


                // initialize direction:
                position_next = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
//                    piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                    if (piece_next == null) {
                        // if space is empty, add move to list
                        pawn_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                }




                // starting UP direction (row+2, col):

                if (myPosition.getRow() == 2 && piece_next == null) {
                    // initialize direction:
                    position_next = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                    if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                        piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                        if (piece_next == null) {
                            // if space is empty, add move to list
                            pawn_valid_moves.add(new ChessMove(myPosition, position_next, null));
                        }
                    }

                }






            }





            // BLACK_PAWN:
            if (piece.getTeamColor() == ChessGame.TeamColor.BLACK){




                // DOWN_RIGHT direction for capture (row-1, col+1):


                // initialize direction:
                ChessPosition position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                ChessPiece piece_next;
                if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                    piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                    if (piece_next != null && piece.getTeamColor() != piece_next.getTeamColor()) {
                        // if space isn't empty and enemy piece, add move to list
                        pawn_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                }






                // DOWN_LEFT direction for capture (row-1, col-1):


                // initialize direction:
                position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                    piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                    if (piece_next != null && piece.getTeamColor() != piece_next.getTeamColor()) {
                        // if space isn't empty and enemy piece, add move to list
                        pawn_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                }





                // DOWN direction (row-1, col):


                // initialize direction:
                position_next = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
//                    piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                    if (piece_next == null) {
                        // if space is empty, add move to list
                        pawn_valid_moves.add(new ChessMove(myPosition, position_next, null));
                    }
                }




                // starting DOWN direction (row-2, col):

                if (myPosition.getRow() == 7 && piece_next == null) {
                    // initialize direction:
                    position_next = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                    if ((position_next.getRow() < 9 && position_next.getRow() > 0) && (position_next.getColumn() < 9 && position_next.getColumn() > 0)) {
                        piece_next = board.getPiece(new ChessPosition(position_next.getRow(), position_next.getColumn()));
                        if (piece_next == null) {
                            // if space is empty, add move to list
                            pawn_valid_moves.add(new ChessMove(myPosition, position_next, null));
                        }
                    }
                }










            }


            return pawn_valid_moves;
        }








        return List.of();
    }
}
