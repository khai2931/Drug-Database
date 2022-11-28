package javabackend;

import java.sql.*;

public class User {
    private final String loginName;
    private final String password;

    private User(UserBuilder builder) {
        this.loginName = builder.loginName;
        this.password = builder.password;
    }

    private User(UserGetter getter) {
        this.loginName = getter.loginName;
        this.password = getter.password;
    }

    // Getters
    public String getLoginName() {
        return loginName;
    }

    public String getPassword() {
        return password;
    }

    public void saveToDB() throws SQLException {
        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();

        String addUser = "INSERT INTO User VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = con.prepareStatement(addUser);
            statement.setString(1, this.loginName);
            statement.setString(2, this.password);
            statement.setBoolean(3, false);
            statement.setString(4, "null");
            statement.setString(5, "null");
            statement.setString(6, "null");
            statement.setString(7, "null");
            statement.setString(8, "null");
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException();
        } finally {
            cm.closeConnection();
        }
    }

    public static class UserBuilder {
        private final String loginName;
        private final String password;

        public UserBuilder(String loginName, String password) {
            this.loginName = loginName;
            this.password = password;
        }

        public User build() {
            return new User(this);
        }
    }

    public static class UserGetter {
        private final String loginName;
        private String password;

        public UserGetter(String loginName, String password) {
            this.loginName = loginName;
            this.password = password;
        }

        public User get() throws SQLException {
            ConnectionManager cm = new ConnectionManager();
            Connection con = cm.createConnection();

            String getUser = "SELECT password FROM User WHERE loginName = ?";
            try {
                PreparedStatement statement = con.prepareStatement(getUser);
                statement.setString(1, this.loginName);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String password = resultSet.getString("password");
                    if (!this.password.equals(password)) {
                        return null;
                    } else {
                        this.password = password;
                        return new User(this);
                    }
                }
                return null;
            } catch (SQLException e) {
                throw new SQLException();
            } finally {
                cm.closeConnection();
            }
        }
    }

}
