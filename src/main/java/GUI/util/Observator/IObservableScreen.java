package GUI.util.Observator;

import be.DefaultScreen;
import be.Screen;

import java.util.HashSet;

/**
 *
 */
public interface IObservableScreen {
    //public final static
    HashSet<IObserver<Screen>> observersScreen = new HashSet<>();
    default void attachObserverScreen(IObserver observer) {
        observersScreen.add(observer);
    }
    default void detachObserverScreen(IObserver observer) {
        observersScreen.remove(observer);
    }

    //by default its public and abstract
    void notifyObservers(Screen added,
                         Screen deleted, Screen modified);
}
