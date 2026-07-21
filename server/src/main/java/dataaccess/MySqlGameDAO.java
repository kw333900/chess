package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MySqlGameDAO implements GameDAOinterface{
    int gameIDcounter=1;

    public MySqlGameDAO(){
        configureDatabase();
    }



    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE Games")) {

                gameIDcounter=1;
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: failed to get connection");
        }

    }



    public int createGame(String gameName) throws DataAccessException{

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO Games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)")) {


                preparedStatement.setInt(1, gameIDcounter);
                preparedStatement.setString(2, null);
                preparedStatement.setString(3, null);
                preparedStatement.setString(4, gameName);


                // serialize new ChessGame object to Json string:
                var serializer = new Gson();
                var json = serializer.toJson(new ChessGame());
                preparedStatement.setString(5, json);

                preparedStatement.executeUpdate();

                /* IMPORTANT: executeUpdate vs. executeQuery
                                                                        executeQuery returns a result set
                */


                return gameIDcounter++;

            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }


//        return 0;
    }





    public Collection<GameData> getGames() throws DataAccessException{

        Collection<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM Games")) {
                try (ResultSet rs = preparedStatement.executeQuery()){


                    while (rs.next()){
                        // create GameData object and add it to list
                        var gameID1 = rs.getInt("gameID");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var game = rs.getString("game");

                        // deserialize game (Json string) to ChessGame object:
                        var serializer = new Gson();
                        ChessGame chessGame = serializer.fromJson(game, ChessGame.class);
                        games.add(new GameData(gameID1, whiteUsername, blackUsername, gameName, chessGame));
                    }



                }

                return games;

            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: gamedata could not be retrieved", e);
        }

    }





    public GameData getGameData(int gameID) throws DataAccessException{

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM Games WHERE gameID=?")) {
                preparedStatement.setInt(1, gameID);
                try (ResultSet rs = preparedStatement.executeQuery()){

                    if (rs.next()){
                        // return GameData
                        var gameID1 = rs.getInt("gameID");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var game = rs.getString("game");

                        // deserialize game (Json string) to ChessGame object:
                        var serializer = new Gson();
                        ChessGame chessGame = serializer.fromJson(game, ChessGame.class);
                        return new GameData(gameID1, whiteUsername, blackUsername, gameName, chessGame);
                    }

                }

            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: gamedata could not be retrieved", e);
        }

        return null;
    }





    public void updateGameData(GameData gameData) throws DataAccessException{

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("UPDATE games SET gameID=?, whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?")) {
                preparedStatement.setInt(6, gameData.gameID());






                        preparedStatement.setInt(1, gameData.gameID());
                        preparedStatement.setString(2, gameData.whiteUsername());
                        preparedStatement.setString(3, gameData.blackUsername());
                        preparedStatement.setString(4, gameData.gameName());

                        // serialize new ChessGame object to Json string:
                        var serializer = new Gson();
                        var json = serializer.toJson(gameData.game());
                        preparedStatement.setString(5, json);

                        preparedStatement.executeUpdate();





            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: gamedata could not be retrieved", e);
        }







    }




    private final String[] createStatements = {"""
                CREATE TABLE IF NOT EXISTS  Games (
               gameID int NOT NULL AUTO_INCREMENT,
               whiteUsername varchar(256),
               blackUsername varchar(256),
               gameName varchar(256),
               game text,
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
