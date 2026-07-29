package ui;

import client.State;

import java.util.Arrays;
import java.util.Scanner;

public class REPL {
    private final ServerFacade server;
    private State state = State.LOGGED_OUT;


    public REPL (String serverURL) throws exception.ResponseException {
        server = new ServerFacade(serverURL);

    }


    public void run(){
        System.out.println("Welcome to 240 chess. Type Help to get started.");
        System.out.print(help());



        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
//            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
//                System.out.print(BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();




    }




    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
//                case "login" -> login(params);
//                case "create" -> createGame(params);
//                case "list" -> listGames;
//                case "join" -> joinGame(params);
//                case "observe" -> observeGame(params);
//                case "logout" -> logout();
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


        return "";
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
