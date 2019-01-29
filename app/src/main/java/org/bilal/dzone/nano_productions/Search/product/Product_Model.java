package org.bilal.dzone.nano_productions.Search.product;

/**
 * Created by DzonE on 11-Aug-18.
 */

public class Product_Model {

    String id,
    title, edition, features, image, technical_data, information, use, safety, created_at
            , updated_at;

    public Product_Model(String id, String title, String edition, String features, String image, String technical_data, String information, String use, String safety, String created_at, String updated_at) {
        this.id = id;
        this.title = title;
        this.edition = edition;
        this.features = features;
        this.image = image;
        this.technical_data = technical_data;
        this.information = information;
        this.use = use;
        this.safety = safety;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTechnical_data() {
        return technical_data;
    }

    public void setTechnical_data(String technical_data) {
        this.technical_data = technical_data;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getSafety() {
        return safety;
    }

    public void setSafety(String safety) {
        this.safety = safety;
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
