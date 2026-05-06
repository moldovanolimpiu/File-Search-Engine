package fse.Repositories;

import fse.SuggestionObserverPattern.Suggestion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContentSuggestionRepo {
    String url = System.getenv("FSE_DB_URL");
    String user = System.getenv("FSE_DB_USER");
    String password = System.getenv("FSE_DB_PASS");

    Connection con = DriverManager.getConnection(url,user,password);

    public ContentSuggestionRepo() throws SQLException {
    }

    public void insertDatabase(String item) throws SQLException {
        String sql = "INSERT INTO content_suggestions(name_suggestion, date_accessed) " +
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

    public List<Suggestion> searchContentSuggestion(String item) throws SQLException {
        String sql = "SELECT * FROM content_suggestions WHERE (name_suggestion ILIKE ?)";
        List<Suggestion> results = new ArrayList<>();
        try(PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + item + "%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String name = rs.getString("name_suggestion");
                String date = rs.getString("date_accessed");
                results.add(new Suggestion(name, date));
            }
        }

        return results;


    }
}
