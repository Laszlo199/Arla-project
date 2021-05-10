package GUI.util.Observator;

import GUI.util.Observator.IObserver;
import be.DefaultScreen;
import be.Screen;

import java.util.HashSet;
import java.util.List;

/**
 *
 */
public abstract class Observable<TUpdateType> implements IObservableDefault,
IObservableScreen
{

    @Override
    public void notifyObservers(DefaultScreen added, DefaultScreen deleted, DefaultScreen modified) {

    }

    @Override
    public void notifyObservers(Screen added, Screen deleted, Screen modified) {

    }
}
