package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class MySqlAuthDAO implements AuthDAOinterface{

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

}
