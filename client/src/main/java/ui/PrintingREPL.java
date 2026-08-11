package ui;

import chess.*;
import client.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrintingREPL {

    public PrintingREPL (){

    }

    public List<ChessPosition> getListOfPositions (Collection<ChessMove> movesToHighlight){
        List<ChessPosition> positionsToHighlight = new ArrayList<>();
        if (movesToHighlight != null){
            for (var m : movesToHighlight){
                if (!positionsToHighlight.contains(m.getStartPosition())){
                    positionsToHighlight.add(m.getStartPosition());
                }
                positionsToHighlight.add(m.getEndPosition());
            }
        }
        return positionsToHighlight;
    }





    public void printGameHelperPrinter(ChessBoard board, StringBuilder boardString, int i, int j, boolean willHighlight) {
        ChessPiece piece = board.getPiece(new ChessPosition(i,j));
        if (willHighlight){
            boardString.append(String.format("%s", EscapeSequences.SET_BG_COLOR_YELLOW));
        } else {

            if ((i+j)%2 == 0){
                boardString.append(String.format("%s", EscapeSequences.SET_BG_COLOR_DARK_GREY));
            } else {
                boardString.append(String.format("%s", EscapeSequences.SET_BG_COLOR_LIGHT_GREY));
            }

        }

        if (piece != null){
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                boardString.append(String.format("%s", EscapeSequences.WHITE_PAWN));

            }
            else if (piece.getPieceType() == ChessPiece.PieceType.ROOK && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                boardString.append(String.format("%s", EscapeSequences.WHITE_ROOK));
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                boardString.append(String.format("%s", EscapeSequences.WHITE_BISHOP));
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                boardString.append(String.format("%s", EscapeSequences.WHITE_KING));
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                boardString.append(String.format("%s", EscapeSequences.WHITE_KNIGHT));
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                boardString.append(String.format("%s", EscapeSequences.WHITE_QUEEN));
            }

            if (piece.getPieceType() == ChessPiece.PieceType.PAWN && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                boardString.append(String.format("%s", EscapeSequences.BLACK_PAWN));

            }
            else if (piece.getPieceType() == ChessPiece.PieceType.ROOK && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                boardString.append(String.format("%s", EscapeSequences.BLACK_ROOK));
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                boardString.append(String.format("%s", EscapeSequences.BLACK_BISHOP));
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                boardString.append(String.format("%s", EscapeSequences.BLACK_KING));
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                boardString.append(String.format("%s", EscapeSequences.BLACK_KNIGHT));
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                boardString.append(String.format("%s", EscapeSequences.BLACK_QUEEN));
            }
        } else {
            boardString.append(String.format("%s", EscapeSequences.EMPTY));
        }
        boardString.append(String.format("%s", EscapeSequences.RESET_BG_COLOR));
    }





    public String helpPrinter (State state){

        if (state == State.LOGGED_OUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
        } else if (state == State.GAMEPLAY){
            return """
                    redraw - to redraw current chess board
                    leave - to leave game
                    move <START POSITION> <END POSITION> - to move a piece (e.g. "move c2 d3")
                    resign - to raise the white flag
                    highlight <PIECE POSITION> - to see which moves any given piece can make
                    help - with possible commands
                    """;
        } else if (state == State.OBSERVER){
            return """
                    redraw - to redraw current chess board
                    leave - to leave game
                    highlight <PIECE POSITION> - to see which moves any given piece can make
                    help - with possible commands
                    """;
        }
        return """
            create <NAME> - a game
            list - games
            join <ID> [WHITE|BLACK] - a game
            observe <ID> - a game
            logout - when you are done
            quit - playing chess
            help - with possible commands
            """;
    }




}
