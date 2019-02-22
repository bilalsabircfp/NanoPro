package org.bilal.dzone.nano_productions.Personal_Zone.Importer;

public class ImporterModelClass {

    String name,
            email,
            phone_number,
            detailer_id,
            latitude,
            longitude,
            address,
            detailer_subscriptions, used_subscriptions;

    public ImporterModelClass(String name, String email, String phone_number,
                              String detailer_id, String latitude, String longitude, String address,
                              String detailer_subscriptions, String used_subscriptions) {
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.detailer_id = detailer_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.used_subscriptions = used_subscriptions;
        this.detailer_subscriptions = detailer_subscriptions;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getDetailer_id() {
        return detailer_id;
    }

    public void setDetailer_id(String detailer_id) {
        this.detailer_id = detailer_id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailer_subscriptions() {
        return detailer_subscriptions;
    }

    public void setDetailer_subscriptions(String detailer_subscriptions) {
        this.detailer_subscriptions = detailer_subscriptions;
    }

    public String getUsed_subscriptions() {
        return used_subscriptions;
    }

    public void setUsed_subscriptions(String used_subscriptions) {
        this.used_subscriptions = used_subscriptions;
    }
}