package server;

import handler.UserHandler;
import io.javalin.*;

public class Server {

    private final Javalin httpHandler;

    public Server() {
        httpHandler = Javalin.create(config -> config.staticFiles.add("web"));
        UserHandler u = new UserHandler();
        httpHandler.post("/user", u::handle_register);

        httpHandler.delete("/db", u::handle_clear);

        // Register your endpoints and exception handlers here.




    }

    public int run(int desiredPort) {
        httpHandler.start(desiredPort);
        return httpHandler.port();
    }






    public void stop() {
        httpHandler.stop();
    }
}
