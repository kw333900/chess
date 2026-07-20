package dataaccess;

import model.GameData;

import java.util.Collection;

public class MySqlGameDAO implements GameDAOinterface{

    public void clear (){

    }

    public int createGame(String gameName) {
        return 0;
    }

    public Collection<GameData> getGames() {
        return null;
    }

    public GameData getGameData(int gameID) {
        return null;
    }

    public void updateGameData(GameData gameData) {

    }
}
