package com.dribbble.shotsviewer.data;



public class Shot {
    private int id;
    private String title;
    private String description;
    private String imageURL;
    private long day;
    private long unixDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public long getUnixDate() {
        return unixDate;
    }

    public void setUnixDate(long unixDate) {
        this.unixDate = unixDate;
    }

    @Override
    public String toString() {
        return "Shot{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", day=" + day +
                ", unixDate=" + unixDate +
                '}';
    }
}
