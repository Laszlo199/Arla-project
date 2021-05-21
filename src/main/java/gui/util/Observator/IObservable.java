package gui.util.Observator;

import be.Screen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 */
public interface IObservable {
    //public final static
    List<ObserverMany> observersMany = new ArrayList<>();
    List<ObserverSingle> observersSingle= new ArrayList<>();

    default void attachManyObserver( ObserverMany observer) {
        observersMany.add(observer);
    }
    default void detachManyObserver(ObserverMany observer) {
        observersMany.remove(observer);
    }

    default void attachSingleObserver( ObserverSingle observer) {
        observersSingle.add(observer);
    }
    default void detachSingleObserver(ObserverSingle observer) {
        observersSingle.remove(observer);
    }

    //by default its public and abstract
    void notifyManyObservers(List<Screen> added,
                         List<Screen> deleted, List<Screen> modified);
    void notifySingleObservers(List<Screen> modified);
}
