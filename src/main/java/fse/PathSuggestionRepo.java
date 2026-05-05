package fse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PathSuggestionRepo {

    String url = System.getenv("FSE_DB_URL");
    String user = System.getenv("FSE_DB_USER");
    String password = System.getenv("FSE_DB_PASS");

    Connection con = DriverManager.getConnection(url,user,password);

    public PathSuggestionRepo() throws SQLException {

    }

    public void insertDatabase(String item) throws SQLException {
        String sql = "INSERT INTO path_suggestions VALUES(?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);

    }
}
