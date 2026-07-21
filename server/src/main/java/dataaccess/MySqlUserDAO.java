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
        configureDatabase();
    }
    
    
    
    public void addUser(UserData u) {

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO Users (username, password, email) VALUES (?, ?, ?)")) {

                preparedStatement.setString(1, u.username());
                preparedStatement.setString(2, u.password());
                preparedStatement.setString(3, u.email());

                var rs = preparedStatement.executeQuery();
                rs.next();
                System.out.println(rs.getInt(1));
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }




    }

    public UserData getUser(String username) {
        return null;
    }

    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE Users")) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                System.out.println(rs.getInt(1));
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }

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

    private final String[] createStatements = {"""
                CREATE TABLE IF NOT EXISTS  Users (
               username varchar(256),
               password varchar(256),
               email varchar(256),
               PRIMARY KEY (username)
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
