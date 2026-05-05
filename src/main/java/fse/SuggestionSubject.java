package fse;

import java.util.List;

public class SuggestionSubject implements Subject{
    private List<Observer> observers;

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String query) {
        for (Observer o : observers) {
            o.update(query);
        }
    }
}
