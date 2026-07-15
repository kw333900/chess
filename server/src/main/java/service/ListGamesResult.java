package service;

import dataaccess.GameDAO;
import model.GameData;

import java.util.Collection;

//public record ListGamesResult(int gameID, String whiteUsername, String blackUsername, String gameName) {
//}
public record ListGamesResult(Collection<GameData> games) {
}
