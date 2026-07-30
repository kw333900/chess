package ui;

import chess.ChessBoard;
import client.State;
import service.*;

import java.util.Arrays;
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
//                case "join" -> joinGame(params);
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
            if (result == null){
                return "failed to login";
            }
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
            return String.format("You logged in as %s.\n", params[0]);
        }
        throw new exception.ResponseException(exception.ResponseException.Code.ClientError, "Expected: <NAME>\n");

    }



//
//    public String joinGame(String... params) throws exception.ResponseException{
//        // based on player color, print white or blacks perspective (create a method for each)
//
//        if (params.length == 2) {
//            ChessBoard board = new ChessBoard();
//            board.resetBoard();
//            int gameID = Integer.parseInt(params[0]);
//            server.joinGame(new JoinGameRequest(activeAuthToken, params[1], gameID));
//            if (result == null){
//                return "failed to login";
//            }
//            state = State.LOGGED_IN;
//            return String.format("You logged in as %s.\n", params[0]);
//        }
//        throw new exception.ResponseException(exception.ResponseException.Code.ClientError, "Expected: <USERNAME> <PASSWORD>\n");
//
//    }



    public String listGames() throws exception.ResponseException{
        if (state == State.LOGGED_IN){
            ListGamesResult result = server.listGames(new ListGamesRequest(activeAuthToken));
            if (result == null){
                return "failed to list games";
            }

            return result.toString(); // <-------does this work? create a method to loop through and list the games?
        }


        throw new exception.ResponseException(exception.ResponseException.Code.ClientError, "Not logged in\n");

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
