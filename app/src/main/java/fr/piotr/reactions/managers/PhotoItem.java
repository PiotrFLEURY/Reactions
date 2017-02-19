package fr.piotr.reactions.managers;

/**
 * Created by piotr_000 on 03/12/2016.
 *
 */

public class PhotoItem {

    private String path;
    private Integer imageID;

    public PhotoItem(String path, Integer imageID) {
        this.path = path;
        this.imageID = imageID;
    }

    public String getPath() {
        return path;
    }

    public Integer getImageID() {
        return imageID;
    }
}
