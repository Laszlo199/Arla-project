package GUI.util;

import java.io.File;

/**
 *
 */
public class ValidateExtension {
    private final static String jpeg = "jpeg";
    private final static String jpg = "jpg";
    private final static String gif = "gif";
    private final static String tiff = "tiff";
    private final static String tif = "tif";
    private final static String png = "png";
    private final static String pdf = "pdf";
    private final static String csv = "csv";
    private final static String xlsx = "xlsx";

    /*
     * Get the extension of a file.
     */
    private static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    public static boolean validateCSV(File f){
        String extension = getExtension(f);
        if (extension != null) {
            if(extension.equals(csv))
                return true;
            } else {
                return false;
            }
            return false;
    }

    public static boolean validatePDF(File f){
        String extension = getExtension(f);
        if (extension != null) {
            if(extension.equals(pdf))
                return true;
        } else {
            return false;
        }
        return false;
    }

    public static boolean validateXLSX(File f){
        String extension = getExtension(f);
        if (extension != null) {
            if(extension.equals(xlsx))
                return true;
        }
        return false;
    }



}
