package GUI.Controller.Interfaces;

import java.util.HashSet;

/**
 *
 */
public abstract class Observable<TUpdateType>{
    private HashSet<IObserver<TUpdateType>> observers = new HashSet<>();

    public void startObserving(IObserver observer) {
        observers.add(observer);
    }
    public void stopObserving(IObserver observer) {
        observers.remove(observer);
    }

    protected void notifySubscribers(TUpdateType update) {
        for(IObserver observer : observers) {
            observer.update(this, update);
        }
    }
}
