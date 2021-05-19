package gui.util.Observator;

/**
 *
 */
public interface IObserver<TUpdateType> {
    void update(TUpdateType added, TUpdateType deleted, TUpdateType modified);
}
