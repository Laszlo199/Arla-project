package dal;

import dal.exception.DALexception;

import java.net.URL;
import java.nio.file.Path;

/**
 *
 */
public interface IPDF {
    String getHTML(Path pdfPath) throws DALexception;
}
