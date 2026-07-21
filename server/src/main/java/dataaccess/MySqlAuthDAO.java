package dataaccess;

import model.AuthData;
import model.UserData;

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

    public void addAuthData(AuthData a) {

    }

    public void deleteAuthData(AuthData a) {

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
                        return new AuthData(username1, authToken);
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
                var rs = preparedStatement.executeQuery();
                rs.next();
                System.out.println(rs.getInt(1));
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
