package service;

public record JoinGameRequestBody(String playerColor, int gameID){}

//
//// deserialize
//RegisterRequest registerRequest = serializer.fromJson(context.body(), RegisterRequest.class);