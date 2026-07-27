package client;

import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    /* From spec: Make sure you clear your database between each test.
    You can do this in a method that has the @BeforeEach annotation. */
    @BeforeEach
    public void clearDatabase(){
//        facade.clear();
        // IDK if this is correct^
    }

//    @Test
//    public void sampleTest() {
//        Assertions.assertTrue(true);
//    }



    // FROM SPEC:
//    @Test
//    void register() throws Exception {
//        var authData = facade.register("player1", "password", "p1@email.com");
//        assertTrue(authData.authToken().length() > 10);
//    }



}
