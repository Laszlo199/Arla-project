package be;

import java.nio.file.Path;

/**
 * Class encapsulated properties saved in default template
 */
public class DefaultTemplate {
    private String name;
    private Path destinationPathCSV;
    private Path destinationPathPDF;
    private String insertedWebsite;

    public DefaultTemplate(String name, Path destinationPathCSV,
                           Path destinationPathPDF, String insertedWebsite) {
        this.name = name;
        this.destinationPathCSV = destinationPathCSV;
        this.destinationPathPDF = destinationPathPDF;
        this.insertedWebsite = insertedWebsite;
    }

    public String getName() {
        return name;
    }

    public Path getDestinationPathCSV() {
        return destinationPathCSV;
    }

    public Path getDestinationPathPDF() {
        return destinationPathPDF;
    }

    public String getInsertedWebsite() {
        return insertedWebsite;
    }
}
