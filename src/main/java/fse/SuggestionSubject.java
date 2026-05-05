package fse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SuggestionSubject implements Subject{
    private List<Observer> observers = new ArrayList<Observer>();

    public SuggestionSubject() {}

    @Override
    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String query) throws SQLException {
        for (Observer o : observers) {
            o.update(query);
        }
    }
}
