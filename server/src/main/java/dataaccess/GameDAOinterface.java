package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAOinterface {

    void clear();

    int createGame (String gameName);

    Collection<GameData> getGames();

    GameData getGameData (int gameID);

    void updateGameData(GameData gameData);









}
