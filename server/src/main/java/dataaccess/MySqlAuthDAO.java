package dataaccess;

import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySqlAuthDAO implements AuthDAOinterface{
    public MySqlAuthDAO (){
        configureDatabase();
    }


    public String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    public void addAuthData(AuthData a) throws DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO Auth (username, authToken) VALUES (?, ?)")) {


                preparedStatement.setString(1, a.username());
                preparedStatement.setString(2, a.authToken());

                preparedStatement.executeUpdate();     /* IMPORTANT: executeUpdate vs. executeQuery
                                                                        executeQuery returns a result set
                */


            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }





    }

    public void deleteAuthData(AuthData a) throws DataAccessException{


        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM Auth (username, authToken) WHERE authToken=?")) {
                preparedStatement.setString(1, a.authToken());

                preparedStatement.executeUpdate();     /* IMPORTANT: executeUpdate vs. executeQuery
                                                                        executeQuery returns a result set
                */


            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }





    }

    public AuthData getAuthDataByToken(String token) throws DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM Auth WHERE authToken=?")) {
                preparedStatement.setString(1, token);
                try (ResultSet rs = preparedStatement.executeQuery()){

                    if (rs.next()){
                        // return AuthData
                        var username1 = rs.getString("username");
                        var authToken = rs.getString("authToken");
                        return new AuthData(authToken, username1);
                    }

                }

            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: authdata could not be retrieved", e);
        }

        return null;
    }




    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE Auth")) {

                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: failed to get connection");
        }

    }



    private final String[] createStatements = {"""
                CREATE TABLE IF NOT EXISTS  Auth (
               username varchar(256),
               authToken varchar(256)
               )
    """};


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
