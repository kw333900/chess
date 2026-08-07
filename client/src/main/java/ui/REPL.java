package ui;
import chess.*;
import com.google.gson.Gson;
import exceptions.ResponseException;
import client.State;
import exceptions.GameAlreadyTakenException;
import model.GameData;
import service.*;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.commands.MakeMoveCommand;
import websocket.messages.Error;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.util.*;

public class REPL implements NotificationHandler {
    private final ServerFacade server;
    private State state = State.LOGGED_OUT;
    private String activeAuthToken = null;
    private ChessBoard activeChessBoard;
    private String currentUserColor = null;
     private final WebSocketFacade ws;   // <--- FOR PHASE6?
    private int activeGameID=1;
    private ChessGame activeChessGame;

    public REPL (String serverURL) throws ResponseException {
        server = new ServerFacade(serverURL);
         ws = new WebSocketFacade(serverURL, this);   // <---PHASE 6?
    }

    public void run(){
        System.out.println("Welcome to 240 chess. Type Help to get started.");
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

    @Override
    public void notify(ServerMessage message, String jsonMessage) {
        Gson Serializer = new Gson();
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(Serializer.fromJson(jsonMessage, Notification.class));
            case ERROR -> displayError(Serializer.fromJson(jsonMessage, Error.class));
            case LOAD_GAME -> loadGame(Serializer.fromJson(jsonMessage, LoadGame.class));
        }
    }

    public void displayError (Error message){
        System.out.print(message.getErrorMessage());
        System.out.print("\n");
        printPrompt();
    }

    public void displayNotification(Notification message){
        System.out.print(message.getNotificationMessage());
        System.out.print("\n");
        printPrompt();
    }

    public void loadGame(LoadGame message){
        ChessGame chessGame = message.getGame();
        activeChessBoard = chessGame.getBoard();
        activeChessGame = chessGame;
        if (state == State.OBSERVER){
            String result  = printGameWhite(activeChessBoard, null);
            System.out.print(result);
        } else {
            if (Objects.equals(currentUserColor, "white") || Objects.equals(currentUserColor, "WHTIE")) {
                String result  = printGameWhite(activeChessBoard, null);
                System.out.print(result);
            } else if (Objects.equals(currentUserColor, "black") || Objects.equals(currentUserColor, "BLACK")) {
                String result  = printGameBlack(activeChessBoard, null);
                System.out.print(result);
            }
        }
        printPrompt();
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
                // GAMEPLAY:
                case "redraw" -> redrawChessBoard();
                case "leave" -> leaveGame();
                case "resign" -> resign();
                case "move" -> makeMoveREPL(params);
                case "highlight" -> highlightLegalMoves(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String highlightLegalMoves(String... params) throws ResponseException {
        if (state != State.GAMEPLAY && state != State.OBSERVER){
            throw new ResponseException(ResponseException.Code.ClientError, "Must be playing or observing a game to highlight a piece's legal moves\n");
        }
        if (params.length != 1){
            throw new ResponseException(ResponseException.Code.ClientError, "Expected: <PIECE POSITION> - lowercase letter and number (e.g. a7)\n");
        }
        char letter = params[0].charAt(0);
        char numberChar = params[0].charAt(1);

        if (!Character.isLetter(letter) || !Character.isLowerCase(letter) || !Character.isDigit(numberChar)){
            throw new ResponseException(ResponseException.Code.ClientError, "Expected: <PIECE POSITION> - lowercase letter and number (e.g. a7)\n");
        }
        if ( !(letter >= 'a' && letter <= 'h') ){
            throw new ResponseException(ResponseException.Code.ClientError, "<PIECE POSITION> must be in bounds of the board ([a-h][1-8])\n");
        }
        int number = Character.getNumericValue(numberChar);
        if ( !(number >= 1 && number <= 8) ){
            throw new ResponseException(ResponseException.Code.ClientError, "<PIECE POSITION> must be in bounds of the board ([a-h][1-8])\n");
        }
        int row = number;
        int col = getNumFromLetter(letter);
        Collection<ChessMove> movesToHighlight = activeChessGame.validMoves(new ChessPosition(row, col));
        if (state == State.OBSERVER){
            String result  = printGameWhite(activeChessBoard, movesToHighlight);
            System.out.print(result);
        } else {
            if (Objects.equals(currentUserColor, "white") || Objects.equals(currentUserColor, "WHTIE")) {
                String result  = printGameWhite(activeChessBoard, movesToHighlight);
                System.out.print(result);
            } else if (Objects.equals(currentUserColor, "black") || Objects.equals(currentUserColor, "BLACK")) {
                String result  = printGameBlack(activeChessBoard, movesToHighlight);
                System.out.print(result);
            }
        }
        return "";
    }

    public int getNumFromLetter (char letter){
        if (letter == 'a'){
            return 1;
        } else if (letter == 'b'){
            return 2;
        } else if (letter == 'c'){
            return 3;
        } else if (letter == 'd'){
            return 4;
        } else if (letter == 'e'){
            return 5;
        } else if (letter == 'f'){
            return 6;
        } else if (letter == 'g'){
            return 7;
        } else {
            return 8;
        }
    }

    public String makeMoveREPL(String... params) throws ResponseException {
        if (state != State.GAMEPLAY){
            throw new ResponseException(ResponseException.Code.ClientError, "Must be playing a game to make a move\n");
        }
        if (params.length != 2){
            throw new ResponseException(ResponseException.Code.ClientError, "Expected: <START POSITION> <END POSITION> - lowercase letter and number (e.g. a7 a8)\n");
        }
        // make startposition:
        char letter = params[0].charAt(0);
        char numberChar = params[0].charAt(1);
        if (!Character.isLetter(letter) || !Character.isLowerCase(letter) || !Character.isDigit(numberChar)){
            throw new ResponseException(ResponseException.Code.ClientError, "Expected: <START POSITION> <END POSITION> - lowercase letter and number (e.g. a7 a8)\n");
        }
        if ( !(letter >= 'a' && letter <= 'h') ){
            throw new ResponseException(ResponseException.Code.ClientError, "<START POSITION> <END POSITION> must be in bounds of the board ([a-h][1-8])\n");
        }
        int number = Character.getNumericValue(numberChar);
        if ( !(number >= 1 && number <= 8) ){
            throw new ResponseException(ResponseException.Code.ClientError, "<START POSITION> <END POSITION> must be in bounds of the board ([a-h][1-8])\n");
        }
        int row1 = number;
        int col1 = getNumFromLetter(letter);
        ChessPosition startPosition = new ChessPosition(row1, col1);
        // make endposition:
        char letter2 = params[1].charAt(0);
        char numberChar2 = params[1].charAt(1);
        if (!Character.isLetter(letter2) || !Character.isLowerCase(letter2) || !Character.isDigit(numberChar2)){
            throw new ResponseException(ResponseException.Code.ClientError, "Expected: <START POSITION> <END POSITION> - lowercase letter and number (e.g. a7 a8)\n");
        }
        if ( !(letter2 >= 'a' && letter2 <= 'h') ){
            throw new ResponseException(ResponseException.Code.ClientError, "<START POSITION> <END POSITION> must be in bounds of the board ([a-h][1-8])\n");
        }
        int number2 = Character.getNumericValue(numberChar2);
        if ( !(number2 >= 1 && number2 <= 8) ){
            throw new ResponseException(ResponseException.Code.ClientError, "<START POSITION> <END POSITION> must be in bounds of the board ([a-h][1-8])\n");
        }
        int row2 = number2;
        int col2 = getNumFromLetter(letter2);
        ChessPosition endPosition = new ChessPosition(row2, col2);
        ChessPiece piece = activeChessBoard.getPiece(startPosition);
        if (  (piece.getTeamColor() == ChessGame.TeamColor.WHITE && endPosition.getRow() == 8 && piece.getPieceType() == ChessPiece.PieceType.PAWN)
        || (piece.getTeamColor() == ChessGame.TeamColor.BLACK && endPosition.getRow() == 1 && piece.getPieceType() == ChessPiece.PieceType.PAWN)  ) {
            Scanner scanner = new Scanner(System.in);
            printPrompt();
            System.out.print("Enter piece you want to promote to ('queen' or 'bishop' or 'knight' or 'rook'): ");
            String line = scanner.nextLine();
            if (Objects.equals(line, "queen")){
                ws.makeMoveFacade(activeAuthToken, activeGameID, new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
            } else if (Objects.equals(line, "bishop")){
                ws.makeMoveFacade(activeAuthToken, activeGameID, new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
            } else if (Objects.equals(line, "knight")){
                ws.makeMoveFacade(activeAuthToken, activeGameID, new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
            } else if (Objects.equals(line, "rook")){
                ws.makeMoveFacade(activeAuthToken, activeGameID, new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
            }
        } else {
            ws.makeMoveFacade(activeAuthToken, activeGameID, new ChessMove(startPosition, endPosition, null));
        }
        return "";
    }

    public String resign() throws ResponseException {
        if (state == State.GAMEPLAY){
            Scanner scanner = new Scanner(System.in);
            printPrompt();
            System.out.print("Are you sure you want to resign? (yes/no): ");
            String line = scanner.nextLine();
            if (Objects.equals(line, "yes")){
                ws.resignFacade(activeAuthToken, activeGameID);
            }
        } else {
            throw new ResponseException(ResponseException.Code.ClientError, "Must be playing a game to resign from it\n");
        }
        return "";
    }

    public String redrawChessBoard() throws ResponseException {
        if (state == State.OBSERVER){
            String result  = printGameWhite(activeChessBoard, null);
            System.out.print(result);
        } else if (state == State.GAMEPLAY) {
            if (Objects.equals(currentUserColor, "white") || Objects.equals(currentUserColor, "WHTIE")) {
                String result  = printGameWhite(activeChessBoard, null);
                System.out.print(result);
            } else if (Objects.equals(currentUserColor, "black") || Objects.equals(currentUserColor, "BLACK")) {
                String result  = printGameBlack(activeChessBoard, null);
                System.out.print(result);
            }
        } else {
            throw new ResponseException(ResponseException.Code.ClientError, "Must be playing or observing a game to redraw board\n");
        }
        return "";
    }

    public String leaveGame() throws ResponseException {
        if (state == State.GAMEPLAY || state == State.OBSERVER){
            ws.leaveGameFacade(activeAuthToken, activeGameID);
            state = State.LOGGED_IN;
        } else {
            throw new ResponseException(ResponseException.Code.ClientError, "Must be playing or observing a game to leave it\n");
        }
        return "";
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
            int userNum;
            try {
                userNum = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                throw new ResponseException(ResponseException.Code.ClientError, "<ID> must be an integer (e.g. two is invalid)\n");
            }
            int gameID = getidFromUserNum(userNum);
            activeGameID = gameID;
            if (gameID <= getGamesListSize() && gameID>0){
                state = State.OBSERVER;
                ws.connectFacade(activeAuthToken, gameID);
            } else {
                throw new ResponseException(ResponseException.Code.ClientError, "Game does not exist.\n");
            }
        } else {
            throw new ResponseException(ResponseException.Code.ClientError, "Expected: <ID>\n");
        }
        return "";
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
        if (state == State.OBSERVER){
            throw new ResponseException(ResponseException.Code.ClientError, "Cannot join a game while observing\n");
        }
        if (state != State.LOGGED_IN){
            throw new ResponseException(ResponseException.Code.ClientError, "Not logged in\n");
        }
        if (params.length == 2) {
            int userNum;
            try {
                userNum = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                throw new ResponseException(ResponseException.Code.ClientError, "<ID> must be an integer (e.g. two is invalid)\n");
            }
            int gameID = getidFromUserNum(userNum);
            activeGameID = gameID;
            if (!(gameID <= getGamesListSize() && gameID > 0)) {
                throw new ResponseException(ResponseException.Code.ClientError, "Game does not exist.\n");
            }
            try {
                server.joinGame(new JoinGameRequest(activeAuthToken, params[1], gameID));
                state = State.GAMEPLAY;
                ws.connectFacade(activeAuthToken, gameID);
            } catch (GameAlreadyTakenException e) {
                throw new ResponseException(ResponseException.Code.ClientError, "Color already taken in that game\n");
            }
            currentUserColor = params[1];
        } else {
            throw new ResponseException(ResponseException.Code.ClientError, "Expected: <ID> [WHITE|BLACK]\n");
        }
        return "";
    }

    public String printGameWhite(ChessBoard board, Collection<ChessMove> movesToHighlight) {
        List<ChessPosition> positionsToHighlight = new ArrayList<>();
        if (movesToHighlight != null){
            for (var m : movesToHighlight){
                if (!positionsToHighlight.contains(m.getStartPosition())){
                    positionsToHighlight.add(m.getStartPosition());
                }
                positionsToHighlight.add(m.getEndPosition());
            }
        }
        StringBuilder boardString = new StringBuilder();
        boardString.append("\n");
        boardString.append(String.format("%s = white\n", EscapeSequences.WHITE_KING));
        boardString.append(String.format("%s = black\n", EscapeSequences.BLACK_KING));
        int k = 8;
        for (int i = 8; i > 0; i--) {
            boardString.append(String.format("%s", k));
            for (int j = 1; j < 9; j++) {
                if (positionsToHighlight.contains(new ChessPosition(i, j))){
                    printGameHelper(board, boardString, i, j, true);
                } else {
                    printGameHelper(board, boardString, i, j, false);
                }
                if (j==8){
                    boardString.append("\n");
                }
            }
            k--;
        }
        boardString.append("  a   b   c  d   e  f   g   h\n");
        return boardString.toString();
    }

    private String printGameBlack(ChessBoard board, Collection<ChessMove> movesToHighlight) {
        List<ChessPosition> positionsToHighlight = new ArrayList<>();
        if (movesToHighlight != null){
            for (var m : movesToHighlight){
                if (!positionsToHighlight.contains(m.getStartPosition())){
                    positionsToHighlight.add(m.getStartPosition());
                }
                positionsToHighlight.add(m.getEndPosition());
            }
        }
        StringBuilder boardString = new StringBuilder();
        boardString.append("\n");
        boardString.append(String.format("%s = white\n", EscapeSequences.WHITE_KING));
        boardString.append(String.format("%s = black\n", EscapeSequences.BLACK_KING));
        int k=1;
        for (int i = 1; i < 9; i++) {
            boardString.append(String.format("%s", k));
            for (int j = 8; j > 0; j--) {
                if (positionsToHighlight.contains(new ChessPosition(i, j))){
                    printGameHelper(board, boardString, i, j, true);
                } else {
                    printGameHelper(board, boardString, i, j, false);
                }
                if (j==1){
                    boardString.append("\n");
                }
            }
            k++;
        }
        boardString.append("  h   g   f  e   d  c   b   a\n");
        return boardString.toString();
    }

    private void printGameHelper(ChessBoard board, StringBuilder boardString, int i, int j, boolean willHighlight) {
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

    public String help() {
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