package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlUserDAO implements UserDAOinterface {

    public MySqlUserDAO() {
//        configureDatabase();
    }
    
    
    
    public void addUser(UserData u) {
        
    }

    public UserData getUser(String username) {
        return null;
    }

    public void clear() {
        var statement = "TRUNCATE Users";
//        executeUpdate(statement);
    }



    /*
    this class is like UserDAO but it runs mySQL commands against the mySQL database


     methods from MySqldataAccess in petshop:

         

     */



//    private int executeUpdate(String statement, Object... params) throws ResponseException {
//        try (Connection conn = DatabaseManager.getConnection()) {
//            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
//                for (int i = 0; i < params.length; i++) {
//                    Object param = params[i];
//                    if (param instanceof String p) ps.setString(i + 1, p);
//                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
//                    else if (param instanceof PetType p) ps.setString(i + 1, p.toString());
//                    else if (param == null) ps.setNull(i + 1, NULL);
//                }
//                ps.executeUpdate();
//
//                ResultSet rs = ps.getGeneratedKeys();
//                if (rs.next()) {
//                    return rs.getInt(1);
//                }
//
//                return 0;
//            }
//        } catch (SQLException e) {
//            throw new ResponseException(ResponseException.Code.ServerError, String.format("unable to update database: %s, %s", statement, e.getMessage()));
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//    private final String[] createStatements = {
//            """
//            CREATE TABLE IF NOT EXISTS  pet (
//              `id` int NOT NULL AUTO_INCREMENT,
//              `name` varchar(256) NOT NULL,
//              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
//              `json` TEXT DEFAULT NULL,
//              PRIMARY KEY (`id`),
//              INDEX(type),
//              INDEX(name)
//            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
//            """
//    };
//
//
//    private void configureDatabase() throws DataAccessException {
//        DatabaseManager.createDatabase();
//        try (Connection conn = DatabaseManager.getConnection()) {
//            for (String statement : createStatements) {
//                try (var preparedStatement = conn.prepareStatement(statement)) {
//                    preparedStatement.executeUpdate();
//                }
//            }
//        } catch (SQLException ex) {
////            throw new DataAccessException(DataAccessException.Code.ServerError, String.format("Unable to configure database: %s", ex.getMessage()));
//        }
//    }







}
