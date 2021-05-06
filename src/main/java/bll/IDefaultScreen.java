package bll;

import be.DefaultScreen;
import bll.exception.BLLException;

import java.util.List;

/**
 *
 */
public interface IDefaultScreen {
    void deleteDefaultScreen(DefaultScreen defaultScreen);
    List<DefaultScreen> getAllDefaultScreens() throws BLLException;
    void deleteScreen(DefaultScreen currentScreen) throws BLLException;

    void updateScreen(int id, DefaultScreen screen) throws BLLException;
}
