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

    public ScreenElement(int colIndex, int rowIndex, int colSpan, int rowSpan, String filepath) {
        this.colIndex = colIndex;
        this.rowIndex = rowIndex;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
        this.filepath = filepath;
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

}
