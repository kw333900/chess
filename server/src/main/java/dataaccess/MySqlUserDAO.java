package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlUserDAO implements UserDAOinterface {
    /*
       this class is like UserDAO but it runs mySQL commands against the mySQL database
        */
    public MySqlUserDAO() {
        configureDatabase();
    }
    
    
    
    public void addUser(UserData u) throws DataAccessException{

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO Users (username, password, email) VALUES (?, ?, ?)")) {

                String hashedPassword = BCrypt.hashpw(u.password(), BCrypt.gensalt());

                preparedStatement.setString(1, u.username());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, u.email());

                preparedStatement.executeUpdate();     /* IMPORTANT: executeUpdate vs. executeQuery
                                                                        executeQuery returns a result set
                */


            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }




    }

    public UserData getUser(String username) throws DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM Users WHERE username=?")) {
                    preparedStatement.setString(1, username);
                try (ResultSet rs = preparedStatement.executeQuery()){

                    if (rs.next()){
                        // return UserData
                        var username1 = rs.getString("username");
                        var password = rs.getString("password");
                        var email = rs.getString("email");
                        return new UserData(username1, password, email);
                    }

                }

            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: user could not be retrieved", e);
        }

        return null;
    }




    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE Users")) {
                preparedStatement.executeUpdate();

            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error: failed to get connection");
        }

    }





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
