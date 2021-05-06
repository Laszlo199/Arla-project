package be;

import java.nio.file.Path;

/**
 * Class encapsulated properties saved in default template
 */
public class DefaultScreen {
    private int id;
    private String name;
    private Path destinationPathCSV;
    private Path destinationPathPDF;
    private String insertedWebsite;

    public DefaultScreen(int id, String name, Path destinationPathCSV,
                         Path destinationPathPDF, String insertedWebsite) {
        this.id = id;
        this.name = name;
        this.destinationPathCSV = destinationPathCSV;
        this.destinationPathPDF = destinationPathPDF;
        this.insertedWebsite = insertedWebsite;
    }

    public DefaultScreen(String name, Path destinationPathCSV,
                         Path destinationPathPDF, String insertedWebsite) {
        this.name = name;
        this.destinationPathCSV = destinationPathCSV;
        this.destinationPathPDF = destinationPathPDF;
        this.insertedWebsite = insertedWebsite;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;

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

   
    /**
     * Id will be retrived from db and set using this setter method
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultScreen that = (DefaultScreen) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
