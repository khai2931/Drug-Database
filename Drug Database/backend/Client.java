import javabackend.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

public class Client {
    // objects to keep track of the currently logged-in user
    // Note: it is always true that at most one of currentCaregiver and currentUser
    // is not null
    // since only one user can be logged-in at a time
    private static User currentUser = null;

    public static void main(String[] args) {
        // printing greetings text
        System.out.println();
        System.out.println("Welcome to the Astria Health Clinic Drug Database!");
        printListOfCommands();

        // read input from user
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("> ");
            String response = "";
            try {
                response = r.readLine();
            } catch (IOException e) {
                System.out.println("Please try again!");
            }
            // split the user input by spaces
            String[] tokens = response.split(" ");
            // check if input exists
            if (tokens.length == 0) {
                System.out.println("Please try again!");
                continue;
            }
            // determine which operation to perform
            String operation = tokens[0];
            if (operation.equals("create_user")) {
                createUser(tokens);
            } else if (operation.equals("login_user")) {
                loginUser(tokens);
            } else if (operation.equals("logout")) {
                logout(tokens);
            } else if (operation.equals("quit")) {
                System.out.println("Bye!");
                return;
            } else {
                System.out.println("Invalid operation name!");
            }
            printListOfCommands();
        }
    }

    private static void printListOfCommands() {
        System.out.println("*** Please enter one of the following commands ***");
        System.out.println("> create_user <login_name> <password>"); // TODO: implement create_user (Part 1)
        System.out.println("> login_user <login_name> <password>"); // TODO: implement login_user (Part 1)
        System.out.println("> logout"); // TODO: implement logout (Part 2)
        System.out.println("> quit");
        System.out.println();
    }

    private static void createUser(String[] tokens) {
        // TODO: Part 1
        // create_user <login_name> <password>
        // check 1: the length for tokens need to be exactly 3 to include all
        // information (with the operation name)
        if (tokens.length != 3) {
            System.out.println("Failed to create user.");
            return;
        }
        String loginName = tokens[1];
        String password = tokens[2];
        // check 2: check if the loginName has been taken already
        if (loginNameExists(loginName)) {
            System.out.println("Login name taken, try again!");
            return;
        }
        // create the user
        try {
            currentUser = new User.UserBuilder(loginName, password).build();
            // save to user information to our database
            currentUser.saveToDB();
            System.out.println("Created user " + loginName);
        } catch (SQLException e) {
            System.out.println("Failed to create user.");
            e.printStackTrace();
        }
    }

    private static boolean loginNameExists(String loginName) {
        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();

        String selectLoginName = "SELECT * FROM User WHERE loginName = ?";
        try {
            PreparedStatement statement = con.prepareStatement(selectLoginName);
            statement.setString(1, loginName);
            ResultSet resultSet = statement.executeQuery();
            // returns false if the cursor is not before the first record or if there are no
            // rows in the ResultSet.
            return resultSet.isBeforeFirst();
        } catch (SQLException e) {
            System.out.println("Error occurred when checking login name");
            e.printStackTrace();
        } finally {
            cm.closeConnection();
        }
        return true;
    }

    private static void loginUser(String[] tokens) {
        // TODO: Part 1
        // login_user <login_name> <password>
        // check 1: if someone's already logged-in, they need to log out first
        if (currentUser != null) {
            System.out.println("User already logged in.");
            return;
        }
        // check 2: the length for tokens need to be exactly 3 to include all
        // information (with the operation name)
        if (tokens.length != 3) {
            System.out.println("Login failed.");
            return;
        }
        String loginName = tokens[1];
        String password = tokens[2];

        User user = null;
        try {
            user = new User.UserGetter(loginName, password).get();
        } catch (SQLException e) {
            System.out.println("Login failed.");
            e.printStackTrace();
        }
        // check if the login was successful
        if (user == null) {
            System.out.println("Login failed.");
        } else {
            System.out.println("Logged in as: " + loginName);
            currentUser = user;
        }
    }

    private static void logout(String[] tokens) {
        // TODO: Part 2
        // Check 1: at least one user must be logged in
        if (currentUser == null) {
            System.out.println("Please login first.");
            return;
        }
        if (tokens.length != 1) {
            System.out.println("Please try again!");
            return;
        }
        currentUser = null;
        System.out.println("Successfully logged out!");
    }
}
