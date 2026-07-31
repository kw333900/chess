package ui;
import com.google.gson.Gson;
import service.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import Exceptions.ResponseException;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String serverUrl) {


        this.serverUrl = serverUrl;
    }

// include a method in this class for each server endpoint

    public RegisterResult register (RegisterRequest request) throws ResponseException {
        var httpRequest = buildRequest("POST", "/user", request, null);
        var response = sendRequest(httpRequest);
        return handleResponse(response, RegisterResult.class);

    }

//
//    public void clear () throws Exceptions.ResponseException {
//        var httpRequest = buildRequest("DELETE", "/db", null);
//        var response = sendRequest(httpRequest);
////        return handleResponse(response, null);
//    }


    public LoginResult login (LoginRequest request) throws ResponseException {
        var httpRequest = buildRequest("POST", "/session", request, null);
        var response = sendRequest(httpRequest);
        return handleResponse(response, LoginResult.class);
    }


    public LogoutResult logout (LogoutRequest request) throws ResponseException {

        var httpRequest = buildRequest("DELETE", "/session", request, request.authToken());
        var response = sendRequest(httpRequest);
        return handleResponse(response, LogoutResult.class);
    }


    public CreateGameResult createGame (CreateGameRequest request) throws ResponseException {
        var httpRequest = buildRequest("POST", "/game", request, request.authToken());
        var response = sendRequest(httpRequest);
        return handleResponse(response, CreateGameResult.class);
    }


    public ListGamesResult listGames (ListGamesRequest request) throws ResponseException {
        var httpRequest = buildRequest("GET", "/game", request, request.authToken());
        var response = sendRequest(httpRequest);
        return handleResponse(response, ListGamesResult.class);
    }


    public void joinGame (JoinGameRequest request) throws ResponseException {
        var httpRequest = buildRequest("PUT", "/game", request, request.authToken());
        var response = sendRequest(httpRequest);

        handleResponse(response, null);
    }



    private HttpRequest buildRequest(String method, String path, Object body, String header) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (header != null) {
            request.setHeader("authorization", header);
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }







}
