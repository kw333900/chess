package ui;
import Exceptions.ResponseException;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.State;
import Exceptions.GameAlreadyTakenException;
import model.GameData;
import service.*;

import java.util.*;

public class REPL {
    private final ServerFacade server;
    private State state = State.LOGGED_OUT;
    private String activeAuthToken = null;


    public REPL (String serverURL) throws ResponseException {
        server = new ServerFacade(serverURL);

    }


    public void run(){
        System.out.println("Welcome to 240 chess. Type Help to get started.");
//        System.out.print(help());



        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();




    }

    private void printPrompt() {
        System.out.printf(" [%s] >>> ", state);
    }


    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

/* Prompts the user to input registration information. Calls the server register API to register and login the user.
If successfully registered, the client should be logged in and transition to the Postlogin UI. */
    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            RegisterResult result = server.register(new RegisterRequest(params[0], params[1], params[2]));
            if (result == null){
                return "failed to register";
            }
            activeAuthToken = result.authToken();
            state = State.LOGGED_IN;
            return String.format("You signed in as %s.\n", params[0]);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <USERNAME> <PASSWORD> <EMAIL>\n");

    }



/* Prompts the user to input login information. Calls the server login API to
login the user. When successfully logged in, the client should transition to the Postlogin UI. */
    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            LoginResult result = server.login(new LoginRequest(params[0], params[1]));
            if (result == null){
                return "failed to login";
            }
            activeAuthToken = result.authToken();
            state = State.LOGGED_IN;
            return String.format("You logged in as %s.\n", params[0]);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <USERNAME> <PASSWORD>\n");

    }



/* Logs out the user. Calls the server logout API to logout the user.
After logging out with the server, the client should transition to the Prelogin UI. */
    public String logout() throws ResponseException {
            LogoutResult result = server.logout(new LogoutRequest(activeAuthToken));

            if (state == State.LOGGED_IN){
                activeAuthToken = null;
                state = State.LOGGED_OUT;
                return "You logged out\n";
            }


        throw new ResponseException(ResponseException.Code.ClientError, "Not logged in\n");

    }



    public String createGame(String... params) throws ResponseException {
        if (params.length == 1 && state == State.LOGGED_IN) {
            CreateGameResult result = server.createGame(new CreateGameRequest(activeAuthToken, params[0]));
            if (result == null){
                return "failed to create game";
            }
            return String.format("You created a new game called: %s.\n", params[0]);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <NAME>\n");

    }



    public String observeGame(String... params) throws ResponseException {
        if (state != State.LOGGED_IN){
            throw new ResponseException(ResponseException.Code.ClientError, "Not logged in\n");
        }
        if (params.length == 1) {
            int gameID;
            try {
                gameID = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                throw new ResponseException(ResponseException.Code.ClientError, "<ID> must be an integer (e.g. two is invalid)\n");
            }
            if (gameID <= getGamesListSize() && gameID>0){
                ChessBoard board = new ChessBoard();
                board.resetBoard();

                return printGameWhite(board);
            } else {
                throw new ResponseException(ResponseException.Code.ClientError, "Game does not exist.\n");
            }

        }

        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <ID>\n");
    }






    public int getidFromUserNum (int userNum) throws ResponseException {
        if (state == State.LOGGED_IN){
            ListGamesResult result = server.listGames(new ListGamesRequest(activeAuthToken));
            Collection<GameData> list = result.games();
            ArrayList<GameData> arrayList = new ArrayList<>(list);
            GameData gameData = arrayList.get(userNum-1);
            return gameData.gameID();
        }



        throw new ResponseException(ResponseException.Code.ClientError, "Not logged in\n");
    }





    public String joinGame(String... params) throws ResponseException {
        // based on player color, print white or blacks perspective (create a method for each)

        if (state != State.LOGGED_IN){
            throw new ResponseException(ResponseException.Code.ClientError, "Not logged in\n");
        }

        if (params.length == 2) {
//            int gameID2 = Integer.parseInt(params[0]);



            ChessBoard board = new ChessBoard();
            board.resetBoard();

            // FOR PHASE 6?:
            int userNum;
            try {
                userNum = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                throw new ResponseException(ResponseException.Code.ClientError, "<ID> must be an integer (e.g. two is invalid)\n");
            }
//            int userNum = Integer.parseInt(params[0]);
            // match gameid from user with list of games
            int gameID = getidFromUserNum(userNum);

            if (!(gameID <= getGamesListSize() && gameID > 0)) {

                throw new ResponseException(ResponseException.Code.ClientError, "Game does not exist.\n");
            }

            try {
                server.joinGame(new JoinGameRequest(activeAuthToken, params[1], gameID));
            } catch (GameAlreadyTakenException e) {
                throw new ResponseException(ResponseException.Code.ClientError, "Color already taken in that game\n");
            }


            if (Objects.equals(params[1], "white") || Objects.equals(params[1], "WHITE")) {
                return printGameWhite(board);
            } else if (Objects.equals(params[1], "black") || Objects.equals(params[1], "BLACK")) {
                return printGameBlack(board);
            }
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <ID> [WHITE|BLACK]\n");

    }
// uppercase = white, lowercase = black


    public String printGameWhite(ChessBoard board) {
        // loop through board and print out pieces
        StringBuilder boardString = new StringBuilder();
        int k = 8;
        for (int i = 1; i < 9; i++) {
            boardString.append(String.format("%s", k));

            for (int j = 1; j < 9; j++) {

                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if ((i+j)%2 == 0){
                    boardString.append(String.format("%s", EscapeSequences.SET_BG_COLOR_WHITE));
                } else {
                    boardString.append(String.format("%s", EscapeSequences.SET_BG_COLOR_BLACK));
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



                if (j==8){
//                    boardString.append(String.format("%s\n", k));
                    boardString.append("\n");
                }

            }
            k--;

        }
        boardString.append("  a   b   c  d   e  f   g   h\n");

        return boardString.toString();


    }




    private String printGameBlack(ChessBoard board) {


        StringBuilder boardString = new StringBuilder();
        int k=1;
        for (int i = 8; i > 0; i--) {
            boardString.append(String.format("%s", k));
            for (int j = 8; j > 0; j--) {

                ChessPiece piece = board.getPiece(new ChessPosition(i,j));

                if ((i+j)%2 == 0){
                    boardString.append(String.format("%s", EscapeSequences.SET_BG_COLOR_WHITE));
                } else {
                    boardString.append(String.format("%s", EscapeSequences.SET_BG_COLOR_BLACK));
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


                if (j==1){
                    boardString.append("\n");
                }

            }
            k++;

        }

        boardString.append("  h   g   f  e   d  c   b   a\n");

        return boardString.toString();





    }


    public int getGamesListSize () throws ResponseException {
        if (state == State.LOGGED_IN){
            ListGamesResult result = server.listGames(new ListGamesRequest(activeAuthToken));
            return result.games().size();

        }



        throw new ResponseException(ResponseException.Code.ClientError, "Not logged in\n");
    }




    public String listGames() throws ResponseException {
        if (state == State.LOGGED_IN){
            ListGamesResult result = server.listGames(new ListGamesRequest(activeAuthToken));
            if (result == null){
                return "failed to list games";
            }

            return printGames(result);
        }


        throw new ResponseException(ResponseException.Code.ClientError, "Not logged in\n");

    }



    public String printGames (ListGamesResult result){
        StringBuilder gamesString = new StringBuilder();
        int gameCounter = 1;
        for (var game : result.games()){
            gamesString.append(String.format("%s) Game name: %s, White user: %s, ", gameCounter, game.gameName(), game.whiteUsername()));
            gamesString.append(String.format("Black user: %s\n", game.blackUsername()));
            gameCounter++;
        }

        return gamesString.toString();

    }




    /* Displays text informing the user what actions they can take.*/
    public String help() {
        if (state == State.LOGGED_OUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
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
