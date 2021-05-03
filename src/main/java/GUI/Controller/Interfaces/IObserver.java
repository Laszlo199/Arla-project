package GUI.Controller.Interfaces;

/**
 *
 */
public interface IObserver<TUpdateType> {
    void update(Observable observable, TUpdateType info);
}
