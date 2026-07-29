package ui;
import com.google.gson.Gson;
import service.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String serverUrl) {


        this.serverUrl = serverUrl;
    }

// include a method in this class for each server endpoint

    public RegisterResult register (RegisterRequest request) throws exception.ResponseException {
        var httpRequest = buildRequest("POST", "/user", request);
        var response = sendRequest(httpRequest);
        return handleResponse(response, RegisterResult.class);

    }


    public void clear () throws exception.ResponseException {
        var httpRequest = buildRequest("DELETE", "/db", null);
        var response = sendRequest(httpRequest);
//        return handleResponse(response, null);
    }


    public LoginResult login (LoginRequest request) throws exception.ResponseException {
        var httpRequest = buildRequest("POST", "/session", request);
        var response = sendRequest(httpRequest);
        return handleResponse(response, LoginResult.class);
    }


    public LogoutResult logout (LogoutRequest request) throws exception.ResponseException {
        var httpRequest = buildRequest("DELETE", "/session", request);
        var response = sendRequest(httpRequest);
        return handleResponse(response, LogoutResult.class);
    }


    public CreateGameResult createGame (CreateGameRequest request) throws exception.ResponseException {
        var httpRequest = buildRequest("POST", "/game", request);
        var response = sendRequest(httpRequest);
        return handleResponse(response, CreateGameResult.class);
    }


    public ListGamesResult listGames (ListGamesRequest request) throws exception.ResponseException {
        var httpRequest = buildRequest("GET", "/game", request);
        var response = sendRequest(httpRequest);
        return handleResponse(response, ListGamesResult.class);
    }


    public void joinGame (JoinGameRequest request) throws exception.ResponseException {
        var httpRequest = buildRequest("PUT", "/game", request);
        var response = sendRequest(httpRequest);
//        return handleResponse(response, null);
    }



    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
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

    private HttpResponse<String> sendRequest(HttpRequest request) throws exception.ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new exception.ResponseException(exception.ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws exception.ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw exception.ResponseException.fromJson(body);
            }

            throw new exception.ResponseException(exception.ResponseException.fromHttpStatusCode(status), "other failure: " + status);
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
