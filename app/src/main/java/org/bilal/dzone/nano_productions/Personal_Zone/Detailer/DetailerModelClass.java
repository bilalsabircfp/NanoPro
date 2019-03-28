package org.bilal.dzone.nano_productions.Personal_Zone.Detailer;

public class DetailerModelClass {

    String name,
    phone_number,
    done_date,
    model,
    year,
    color,
    remarks,
    title,
    edition,
    email,
    warranty_code,
    license_plate_no;


    public DetailerModelClass(String name, String phone_number, String done_date, String model, String year, String color, String remarks, String title, String edition, String email, String warranty_code, String license_plate_no) {
        this.name = name;
        this.phone_number = phone_number;
        this.done_date = done_date;
        this.model = model;
        this.year = year;
        this.color = color;
        this.remarks = remarks;
        this.title = title;
        this.edition = edition;
        this.email = email;
        this.warranty_code = warranty_code;
        this.license_plate_no = license_plate_no;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getDone_date() {
        return done_date;
    }

    public void setDone_date(String done_date) {
        this.done_date = done_date;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWarranty_code() {
        return warranty_code;
    }

    public void setWarranty_code(String warranty_code) {
        this.warranty_code = warranty_code;
    }

    public String getLicense_plate_no() {
        return license_plate_no;
    }

    public void setLicense_plate_no(String license_plate_no) {
        this.license_plate_no = license_plate_no;
    }
}
