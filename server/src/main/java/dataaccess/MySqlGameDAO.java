package dataaccess;

import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public class MySqlGameDAO implements GameDAOinterface{


    public MySqlGameDAO(){
        configureDatabase();
    }



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




    private final String[] createStatements = {"""
                CREATE TABLE IF NOT EXISTS  Games (
               gameID int NOT NULL AUTO_INCREMENT,
               whiteUsername varchar(256),
               blackUsername varchar(256),
               gameName varchar(256),
               PRIMARY KEY (gameID)
               )
    """};
/* ^Still need to add game ChessGame?^ */

    private void configureDatabase() {
        try {
            DatabaseManager.createDatabase();

            try (Connection conn = DatabaseManager.getConnection()) {
                for (String statement : createStatements) {
                    try (var preparedStatement = conn.prepareStatement(statement)) {
                        preparedStatement.executeUpdate();
                    }
                }
            }

        }catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }







}
