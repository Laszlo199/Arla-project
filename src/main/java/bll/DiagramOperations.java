package bll;

import java.util.regex.Pattern;

/**
 *  It has one drawback. File must follow one unified standard.
 *  Later make it robust
 */
public class DiagramOperations {
    public double[] getArray(String string) {
        int i=0;
        String[] splittedText = string.split(",");
        double[] result = new double[splittedText.length];
        for(String e: splittedText)
                result[i++] = Double.parseDouble(e);
        return result;
    }
}
