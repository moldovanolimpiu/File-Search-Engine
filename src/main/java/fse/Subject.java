package fse;

import java.sql.SQLException;

public interface Subject {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String query, PathSuggestionRepo pathRepo, ContentSuggestionRepo contentRepo) throws SQLException;
}
