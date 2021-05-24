package be;

/**
 *
 */
public class ScreenElement {
    private int colIndex;
    private int rowIndex;
    private int colSpan;
    private int rowSpan;
    private String filepath;
    private CSVInfo csvInfo;

    public ScreenElement(int colIndex, int rowIndex, int colSpan, int rowSpan, String filepath) {
        this.colIndex = colIndex;
        this.rowIndex = rowIndex;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
        this.filepath = filepath;
    }

    public ScreenElement(int colIndex, int rowIndex, int colSpan, int rowSpan, String filepath, CSVInfo csvInfo) {
        this.colIndex = colIndex;
        this.rowIndex = rowIndex;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
        this.filepath = filepath;
        this.csvInfo = csvInfo;
    }

    public int getColIndex() {
        return colIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColSpan() {
        return colSpan;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public String getFilepath() {
        return filepath;
    }

    public CSVInfo getCsvInfo() {
        return csvInfo;
    }

    public void setCsvInfo(CSVInfo csvInfo) {
        this.csvInfo = csvInfo;
    }

    @Override
    public String toString() {
        return "ScreenElement{" +
                "colIndex=" + colIndex +
                ", rowIndex=" + rowIndex +
                ", colSpan=" + colSpan +
                ", rowSpan=" + rowSpan +
                ", filepath='" + filepath + ", " + csvInfo +
                '}';
    }
}
