package fse;

import java.sql.SQLException;

public interface Observer {
    public void update(String query, PathSuggestionRepo pathRepo, ContentSuggestionRepo contentRepo) throws SQLException;
}
