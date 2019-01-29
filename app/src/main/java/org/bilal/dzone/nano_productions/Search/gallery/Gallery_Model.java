package org.bilal.dzone.nano_productions.Search.gallery;

/**
 * Created by DzonE on 12-Aug-18.
 */

public class Gallery_Model {

    String id, title, image, created_at, updated_at, gallery_images, no_of_images;

    public Gallery_Model(String id, String title, String image, String created_at, String updated_at, String gallery_images, String no_of_images) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.gallery_images = gallery_images;
        this.no_of_images = no_of_images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getGallery_images() {
        return gallery_images;
    }

    public void setGallery_images(String gallery_images) {
        this.gallery_images = gallery_images;
    }

    public String getNo_of_images() {
        return no_of_images;
    }

    public void setNo_of_images(String no_of_images) {
        this.no_of_images = no_of_images;
    }
}
