package GUI.util.Observator;

import be.DefaultScreen;

/**
 *
 */
public interface IObserver<TUpdateType> {
    void update(TUpdateType... addedScreens);
}
