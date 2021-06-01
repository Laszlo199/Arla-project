package bll.util;

import be.Screen;
import be.ScreenElement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import junit.framework.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DetectOtherScreensTest {

    @Test
    void getNewScreens() {
        DetectOtherScreens detectOtherScreens = new DetectOtherScreens();
        List<Screen> newScreens = new ArrayList<>();
        ObservableList<Screen> oldScreens = FXCollections.observableArrayList();

        Screen screen1 = new Screen(1, "aa", 5);
        Screen screen2 = new Screen(2, "bb", 4);
        Screen screen3 = new Screen(3, "cc", 5);
        Screen screen4 = new Screen(4, "aa", 3);

        newScreens.add(screen1);
        newScreens.add(screen2);
        newScreens.add(screen3);
        newScreens.add(screen4);

        oldScreens.add(screen2);
        oldScreens.add(screen3);

        List<Screen> expected = new ArrayList<>();
        expected.add(screen1);
        expected.add(screen4);

        List<Screen> actual = detectOtherScreens.getNewScreens(newScreens, oldScreens);

        Assert.assertEquals(expected, actual);

    }
}