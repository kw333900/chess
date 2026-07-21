package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;

public class MySqlAuthDAO implements AuthDAOinterface{
    public MySqlAuthDAO (){
        configureDatabase();
    }


    public String generateAuthToken() {
        return null;
    }

    public void addAuthData(AuthData a) {

    }

    public void deleteAuthData(AuthData a) {

    }

    public AuthData getAuthDataByToken(String token) {
        return null;
    }

    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE Auth")) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                System.out.println(rs.getInt(1));
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
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
