package GUI.util.Observator;

import be.DefaultScreen;

import java.util.HashSet;

/**
 *
 */
public interface IObservableDefault {
    //public final static
    HashSet<IObserver<DefaultScreen>> observersDefault = new HashSet<>();
    default void attachObserverDefault(IObserver observer) {
        observersDefault.add(observer);
    }
    default void detachObserverDefault(IObserver observer) {
        observersDefault.remove(observer);
    }

    //by default its public and abstract
    void notifyObservers(DefaultScreen added,
                                         DefaultScreen deleted, DefaultScreen modified);
}
