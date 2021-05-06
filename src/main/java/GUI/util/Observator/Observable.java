package GUI.util.Observator;

import GUI.util.Observator.IObserver;
import be.DefaultScreen;

import java.util.HashSet;
import java.util.List;

/**
 *
 */
public abstract class Observable<TUpdateType>{
    protected HashSet<IObserver<TUpdateType>> observers = new HashSet<>();

    public void attachObserver(IObserver observer) {
        observers.add(observer);
    }
    public void detachObserver(IObserver observer) {
        observers.remove(observer);
    }

    public abstract void notifyObservers(TUpdateType added, TUpdateType deleted, TUpdateType modified);
}
