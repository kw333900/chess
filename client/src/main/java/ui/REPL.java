package ui;

import chess.ChessBoard;
import client.State;
import service.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class REPL {
    private final ServerFacade server;
    private State state = State.LOGGED_OUT;
    private String activeAuthToken = null;


    public REPL (String serverURL) throws exception.ResponseException {
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
//                case "observe" -> observeGame(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (exception.ResponseException ex) {
            return ex.getMessage();
        }
    }

/* Prompts the user to input registration information. Calls the server register API to register and login the user.
If successfully registered, the client should be logged in and transition to the Postlogin UI. */
    public String register(String... params) throws exception.ResponseException{
        if (params.length == 3) {
            RegisterResult result = server.register(new RegisterRequest(params[0], params[1], params[2]));
            if (result == null){
                return "failed to register";
            }
            activeAuthToken = result.authToken();
            state = State.LOGGED_IN;
            return String.format("You signed in as %s.\n", params[0]);
        }
        throw new exception.ResponseException(exception.ResponseException.Code.ClientError, "Expected: <USERNAME> <PASSWORD> <EMAIL>\n");

    }



/* Prompts the user to input login information. Calls the server login API to
login the user. When successfully logged in, the client should transition to the Postlogin UI. */
    public String login(String... params) throws exception.ResponseException{
        if (params.length == 2) {
            LoginResult result = server.login(new LoginRequest(params[0], params[1]));
            if (result == null){
                return "failed to login";
            }
            activeAuthToken = result.authToken();
            state = State.LOGGED_IN;
            return String.format("You logged in as %s.\n", params[0]);
        }
        throw new exception.ResponseException(exception.ResponseException.Code.ClientError, "Expected: <USERNAME> <PASSWORD>\n");

    }



/* Logs out the user. Calls the server logout API to logout the user.
After logging out with the server, the client should transition to the Prelogin UI. */
    public String logout() throws exception.ResponseException{
            LogoutResult result = server.logout(new LogoutRequest(activeAuthToken));
//            if (result == null){
//                return "failed to logout";
//            }
            if (state == State.LOGGED_IN){
                activeAuthToken = null;
                state = State.LOGGED_OUT;
                return "You logged out\n";
            }


        throw new exception.ResponseException(exception.ResponseException.Code.ClientError, "Not logged in\n");

    }



    public String createGame(String... params) throws exception.ResponseException{
        if (params.length == 1 && state == State.LOGGED_IN) {
            CreateGameResult result = server.createGame(new CreateGameRequest(activeAuthToken, params[0]));
            if (result == null){
                return "failed to create game";
            }
            return String.format("You created a new game called: %s.\n", params[0]);
        }
        throw new exception.ResponseException(exception.ResponseException.Code.ClientError, "Expected: <NAME>\n");

    }




    public String joinGame(String... params) throws exception.ResponseException{
        // based on player color, print white or blacks perspective (create a method for each)

        if (state != State.LOGGED_IN){
            throw new exception.ResponseException(exception.ResponseException.Code.ClientError, "Not logged in\n");
        }

        if (params.length == 2) {
            ChessBoard board = new ChessBoard();
            board.resetBoard();

            // FOR PHASE 6?:
            int gameID = Integer.parseInt(params[0]);

            // match gameid from user with list of games

                                                                //  v put matched gameid here
            server.joinGame(new JoinGameRequest(activeAuthToken, params[1], gameID));

            if (Objects.equals(params[1], "white") || Objects.equals(params[1], "WHITE")){
                return printGameWhite(board);
            }
            else if (Objects.equals(params[1], "black") || Objects.equals(params[1], "BLACK")){
                return printGameBlack(board);
            }
        }
        throw new exception.ResponseException(exception.ResponseException.Code.ClientError, "Expected: <ID> [WHITE|BLACK]\n");

    }
// uppercase = white, lowercase = black


    public String printGameWhite(ChessBoard board) {
        // loop through board and print out pieces

        return ("""
                uppercase = white
                lowercase = black
                   a b c d e f g h
                8 |r|n|b|q|k|b|n|r| 8
                7 |p|p|p|p|p|p|p|p| 7
                6 | | | | | | | | | 6
                5 | | | | | | | | | 5
                4 | | | | | | | | | 4
                3 | | | | | | | | | 3
                2 |P|P|P|P|P|P|P|P| 2
                1 |R|N|B|Q|K|B|N|R| 1
                   a b c d e f g h
                """);
    }




    private String printGameBlack(ChessBoard board) {
        return ("""
                uppercase = white
                lowercase = black
                   h g f e d c b a
                1 |R|N|B|K|Q|B|N|R| 1
                2 |P|P|P|P|P|P|P|P| 2
                3 | | | | | | | | | 3
                4 | | | | | | | | | 4
                5 | | | | | | | | | 5
                6 | | | | | | | | | 6
                7 |p|p|p|p|p|p|p|p| 7
                8 |r|n|b|k|q|b|n|r| 8
                   h g f e d c b a
                """);

    }



    public String listGames() throws exception.ResponseException{
        if (state == State.LOGGED_IN){
            ListGamesResult result = server.listGames(new ListGamesRequest(activeAuthToken));
            if (result == null){
                return "failed to list games";
            }

            return printGames(result); // <-------does this work? create a method to loop through and list the games?
        }


        throw new exception.ResponseException(exception.ResponseException.Code.ClientError, "Not logged in\n");

    }



    public String printGames (ListGamesResult result){
        StringBuilder gamesString = new StringBuilder();
        int gameCounter = 1;
        for (var game : result.games()){
            gamesString.append(String.format("%s) Game name: %s, White user: %s, Black user: %s\n", gameCounter, game.gameName(), game.whiteUsername(), game.blackUsername()));
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
