package fse.SuggestionObserverPattern;

import fse.Repositories.ContentSuggestionRepo;
import fse.Repositories.PathSuggestionRepo;

import java.sql.SQLException;

public interface Observer {
    public void update(String query, PathSuggestionRepo pathRepo, ContentSuggestionRepo contentRepo) throws SQLException;
}
