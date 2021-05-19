package gui.util.Observator;

import be.DefaultScreen;
import be.Screen;

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
