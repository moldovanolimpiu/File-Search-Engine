package fse;

import java.sql.*;

public class PathSuggestionRepo {

    String url = System.getenv("FSE_DB_URL");
    String user = System.getenv("FSE_DB_USER");
    String password = System.getenv("FSE_DB_PASS");

    Connection con = DriverManager.getConnection(url,user,password);

    public PathSuggestionRepo() throws SQLException {

    }

    public void insertDatabase(String item) throws SQLException {
        String sql = "INSERT INTO path_suggestions(name_suggestion, date_accessed) " +
                "VALUES (?, ?) " +
                "ON CONFLICT (name_suggestion) DO UPDATE " +
                "SET date_accessed = ?";
        try(PreparedStatement ps = con.prepareStatement(sql)) {
            String currentDateTime = java.time.LocalDateTime.now().toString();
            ps.setString(1, item);
            ps.setString(2, currentDateTime);
            ps.setString(3, currentDateTime);
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
