package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public class MySqlGameDAO implements GameDAOinterface{

    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE Games")) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                System.out.println(rs.getInt(1));
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }

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
