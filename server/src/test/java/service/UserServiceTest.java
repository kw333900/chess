package service;

import dataaccess.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private static UserService userService;




    @BeforeAll
    static void setUp() {
        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();

        userService = new UserService(userDAO, authDAO, gameDAO);
    }

    @AfterAll
    static void tearDown() {
    }

    @Test
    void registerPositive() throws DataAccessException{
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        assertNotNull(userService.register(registerRequest));


    }

    @Test
    void registerNegative() {
        RegisterRequest registerRequestNegative = new RegisterRequest("username", "jfd", "email");
//        assertNotNull(userService.register(registerRequestNegative));
         assertThrows(AlreadyTakenException.class, () -> userService.register(registerRequestNegative) );

    }



    @Test
    void loginPositive() {
        RegisterRequest registerRequest = new RegisterRequest("loginUser", "loginPass", "email@test.com");
        assertDoesNotThrow(() -> userService.register(registerRequest));

        LoginRequest loginRequest = new LoginRequest("loginUser", "loginPass");
        assertDoesNotThrow(() -> userService.login(loginRequest));
    }

    @Test
    void loginNegative() {
        RegisterRequest registerRequest = new RegisterRequest("loginNegUser", "correctPass", "email@test.com");
        assertDoesNotThrow(() -> userService.register(registerRequest));

        LoginRequest badLogin = new LoginRequest("loginNegUser", "wrongPass");
        assertThrows(InvalidLoginException.class, () -> userService.login(badLogin));
    }



    @Test
    void logoutPositive() throws BadRequestException, AlreadyTakenException {
        RegisterRequest registerRequest = new RegisterRequest("logoutUser", "logoutPass", "email@test.com");
        var registerResult = assertDoesNotThrow(() -> userService.register(registerRequest));

        LogoutRequest logoutRequest = new LogoutRequest(registerResult.authToken());
        assertDoesNotThrow(() -> userService.logout(logoutRequest));
    }

    @Test
    void logoutNegative() {
        LogoutRequest badLogout = new LogoutRequest("tokenDNE");
        assertThrows(InvalidLogoutException.class, () -> userService.logout(badLogout));
    }

    @Test
    void createGamePositive() {
        RegisterRequest registerRequest = new RegisterRequest("createGameUser", "pass", "email@test.com");
        var registerResult = assertDoesNotThrow(() -> userService.register(registerRequest));

        CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), "myGame");
        var createGameResult = assertDoesNotThrow(() -> userService.createGame(createGameRequest));

        assertNotNull(createGameResult);
    }

    @Test
    void createGameNegative() {
        CreateGameRequest badRequest = new CreateGameRequest("tokenDNE", "myGame");
        assertThrows(InvalidLoginException.class, () -> userService.createGame(badRequest));
    }

    @Test
    void listGamesPositive() {
        RegisterRequest registerRequest = new RegisterRequest("listGamesUser", "pass", "email@test.com");
        var registerResult = assertDoesNotThrow(() -> userService.register(registerRequest));

        ListGamesRequest listGamesRequest = new ListGamesRequest(registerResult.authToken());
        var listGamesResult = assertDoesNotThrow(() -> userService.listGames(listGamesRequest));

        assertNotNull(listGamesResult);
    }

    @Test
    void listGamesNegative() {
        ListGamesRequest badRequest = new ListGamesRequest("tokenDNEe");
        assertThrows(InvalidGameIDException.class, () -> userService.listGames(badRequest));
    }

    @Test
    void joinGamePositive() {
        RegisterRequest registerRequest = new RegisterRequest("joinGameUser", "pass", "email@test.com");
        var registerResult = assertDoesNotThrow(() -> userService.register(registerRequest));

        CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), "joinableGame");
        var createGameResult = assertDoesNotThrow(() -> userService.createGame(createGameRequest));

        JoinGameRequest joinGameRequest = new JoinGameRequest(registerResult.authToken(), "WHITE", createGameResult.gameID());
        assertDoesNotThrow(() -> userService.joinGame(joinGameRequest));
    }

    @Test
    void joinGameNegative() {
        RegisterRequest registerRequest = new RegisterRequest("joinGameNegUser", "pass", "email@test.com");
        var registerResult = assertDoesNotThrow(() -> userService.register(registerRequest));

        JoinGameRequest badColorRequest = new JoinGameRequest(registerResult.authToken(), "PURPLE", 1);
        assertThrows(BadRequestException.class, () -> userService.joinGame(badColorRequest));
    }

    @Test
    void clear() {
    }
}