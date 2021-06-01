package bll;

import be.Screen;
import bll.exception.BLLException;
import javafx.collections.ObservableList;

import java.util.List;

/**
 *
 */
public interface IListener {
    List<Screen> getModifiedScreens(List<Screen> newScreens) throws BLLException;

    List<Screen> getDeletedScreens(List<Screen> newScreens, ObservableList<Screen> mainScreens);

    List<Screen> getNewScreens(List<Screen> newScreens, ObservableList<Screen> mainScreens);

}
