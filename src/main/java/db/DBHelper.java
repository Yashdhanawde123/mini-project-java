package db;

import java.sql.*;

public class DBHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/expense_tracker"; // your DB name
    private static final String USER = "root";  // your DB user
    private static final String PASS = "123";      // your DB password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // global variable to store logged-in user
    public static int loggedInUserId = -1;

    public static boolean login(String username, String password) {
        String sql = "SELECT user_id FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                loggedInUserId = rs.getInt("user_id");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
