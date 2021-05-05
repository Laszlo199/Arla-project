package be;

public class ScreenFile {
    private int screenId;
    private int sectionId;
    private int fileId;

    public ScreenFile(int screenId, int sectionId, int fileId) {
        this.screenId = screenId;
        this.sectionId = sectionId;
        this.fileId = fileId;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }
}
