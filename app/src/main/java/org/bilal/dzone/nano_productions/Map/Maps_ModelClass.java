package org.bilal.dzone.nano_productions.Map;

/**
 * Created by DzonE on 05-Aug-18.
 */

public class Maps_ModelClass {

    String name, address, phone_number, image, latitude, longitude;

    public Maps_ModelClass(String name, String address, String phone_number, String image, String latitude, String longitude) {
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
