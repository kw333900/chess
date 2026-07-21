package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDAOinterface {

    void clear() throws DataAccessException;

    int createGame (String gameName) throws DataAccessException;

    Collection<GameData> getGames() throws DataAccessException;

    GameData getGameData (int gameID) throws DataAccessException;

    void updateGameData(GameData gameData) throws DataAccessException;









}
