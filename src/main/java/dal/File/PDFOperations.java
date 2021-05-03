package dal.File;

import dal.exception.DALexception;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 */
public class PDFOperations {


    /*
    being provided the path to pdf convert it to html
    then save HTML and return URL to the new .html file
     */
    public String getHTML(Path pdfPath) throws DALexception {
        String fileName = getFileName(pdfPath);
        generateHTMLFromPDF(fileName, String.valueOf(pdfPath));
        return fileName;
    }

    private String getFileName(Path pdfPath) {
        return String.valueOf(pdfPath).replaceAll("PDFData", "HTMLData").replaceAll(".pdf", ".html");
    }

    private void generateHTMLFromPDF(String filename, String pdfLocation) throws DALexception {
        try{
            System.out.println(filename);
            PDDocument pdf = PDDocument.load(new File(pdfLocation));
            File file = new File(filename);
            file.createNewFile();
            Writer output = new PrintWriter(filename, "utf-8");
            new PDFDomTree().writeText(pdf, output);
            output.close();
        } catch (FileNotFoundException e) {
            throw new DALexception("couldnt generate HTML from PDF", e);
        } catch (UnsupportedEncodingException e) {
            throw new DALexception("couldnt generate HTML from PDF", e);
        } catch (IOException e) {
            throw new DALexception("couldnt generate HTML from PDF", e);
        } catch (ParserConfigurationException e) {
            throw new DALexception("couldnt generate HTML from PDF", e);
        }
    }
}
