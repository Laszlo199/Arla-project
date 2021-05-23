package be;

public class CSVInfo {

    private boolean isHeader;
    private String title;
    private CSVType type;

    public CSVInfo(boolean isHeader, String title, CSVType type) {
        this.isHeader = isHeader;
        this.title = title;
        this.type = type;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CSVType getType() {
        return type;
    }

    public void setType(CSVType type) {
        this.type = type;
    }

    public enum CSVType {
        BARCHART,
        LINECHART,
        TABLE
    }

    @Override
    public String toString() {
        return isHeader + ", " + title + ", " + type.toString();
    }
}
