package GUI.util.Observator;

import be.DefaultScreen;

/**
 *
 */
public interface IObserver<TUpdateType> {
    void update(TUpdateType added, TUpdateType deleted, TUpdateType modified);
}
