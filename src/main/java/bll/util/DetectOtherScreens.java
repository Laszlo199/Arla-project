package bll.util;

import be.Screen;
import be.ScreenElement;
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
    public List<Screen> getModifiedScreens(List<Screen> newScreens, ObservableList<Screen> mainScreens, HashSet<String> changedFiles) {
        System.out.println(changedFiles);
        List<Screen> modifiedElements = new ArrayList<>();
        for(Screen scr: newScreens)
            for(ScreenElement se: scr.getScreenElementList())
                for(String path: changedFiles)
                    if(se.getFilepath().equals(path)){
                        System.out.println("added element to modified ... BLL");
                        modifiedElements.add(scr);
                    }

        for(Screen newSc: newScreens)
            if(!mainScreens.contains(newSc) && mainScreens.contains(newSc.getId()) &&
                    !modifiedElements.contains(newSc))
                modifiedElements.add(newSc);

         return modifiedElements;
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
