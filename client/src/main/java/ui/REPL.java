package ui;

import client.State;

public class REPL {
    private final ServerFacade server;
    private State state = State.PRELOGIN;


    public REPL (String serverURL) throws exception.ResponseException {
        server = new ServerFacade(serverURL);

    }








}
