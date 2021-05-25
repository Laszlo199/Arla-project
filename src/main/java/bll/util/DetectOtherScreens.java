package bll.util;

import be.Screen;
import be.ScreenElement;
import bll.exception.BLLException;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 */
public class DetectOtherScreens {



    /**
     * this method is the most difficult cause we need to think very carefully how we will
     * make hashcode for the screen to detect if it has been changed or not
     *
     * also screen should be marked as modified if one of the files it contained was changed.
     * How we can detect that
     * @param newScreens
     * @param mainScreens
     * @param changedFiles
     * @return
     */
    public List<Screen> getModifiedScreens(List<Screen> newScreens, ObservableList<Screen> mainScreens,
                                           HashSet<String> changedFiles) throws BLLException {
        for(Screen screen: mainScreens){
            for(String file: changedFiles){
                for(ScreenElement se: screen.getScreenElementList()){
                    String one  = se.getFilepath().replaceAll("\\\\","/");
                    String two = file.replaceAll("\\\\","/");
                    if(one.equals(two)){
                        screen.setRefreshNow(true);
                    }
                }
            }
        }

        List<Screen> helper = new ArrayList<>();
        for(Screen m: mainScreens){
            if(m.isRefreshNow()){
                helper.add(m);
                System.out.println("screen to modifyyyy");
            }
        }
         return helper;
    }



    public List<Screen> getDeletedScreens(List<Screen> newScreens, ObservableList<Screen> mainScreens) {
        List<Screen> helper  = new ArrayList<>();
        for(Screen oldSc: mainScreens){
            boolean add  = true;
            for(Screen newSc: newScreens){
                if(oldSc.getId() == newSc.getId())
                    add=false;
            }
            if(add)
                helper.add(oldSc);
        }
        return helper;
    }

    public List<Screen> getNewScreens(List<Screen> newScreens, ObservableList<Screen> mainScreens) {
        List<Screen> helper = new ArrayList<>();
        for(Screen newSc: newScreens){
            boolean add = true;
            for(Screen oldSc: mainScreens){
                if(newSc.getId() == oldSc.getId())
                    add=false;
            }
            if(add)
                helper.add(newSc);
        }
        return helper;

    }

}
