package be;

public class Section {
    private int screenId;
    private int id;
    private int width;
    private int height;

    public Section(int screenId, int id, int width, int height) {
        this.screenId = screenId;
        this.id = id;
        this.width = width;
        this.height = height;
    }
    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int id) {
        this.screenId = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
