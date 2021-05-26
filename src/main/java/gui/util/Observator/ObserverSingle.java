package gui.util.Observator;

import be.Screen;

/**
 *
 */
public abstract class ObserverSingle extends Observer {
    private Screen screen;

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    public abstract void update();

    public abstract void setAsObserver(Screen screen);
}