package bll;

import java.util.regex.Pattern;

/**
 *
 */
public class DiagramOperations {
    ///^[0-9]+\.[0-9]+$
    private Pattern pattern = Pattern.compile("\\d+");
    public double[] getArray(String string) {
        int i=0;
        String[] splittedText = string.split(",");
        double[] result = new double[splittedText.length];
        for(String e: splittedText){
            e.replaceAll(" ", "");
           // if(isNumeric(e)){
                result[i++] = Double.parseDouble(e);
                System.out.println(e);}
       // }
        return result;
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }
}
