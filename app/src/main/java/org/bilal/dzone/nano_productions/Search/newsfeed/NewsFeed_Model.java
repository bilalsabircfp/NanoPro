package org.bilal.dzone.nano_productions.Search.newsfeed;

/**
 * Created by DzonE on 06-Aug-18.
 */

public class NewsFeed_Model {

    String name, image, content, id, created_at, updated_at;


    public NewsFeed_Model(String name, String image, String content, String id, String created_at, String updated_at) {
        this.name = name;
        this.image = image;
        this.content = content;
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}