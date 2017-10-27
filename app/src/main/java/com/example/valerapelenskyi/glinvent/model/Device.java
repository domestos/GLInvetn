package com.example.valerapelenskyi.glinvent.model;

/**
 * Created by valera.pelenskyi on 26.10.17.
 */

public class Device {

    private int id ;
    private String number;
    private String item;
    private String name_wks;
    private String owner;
    private String location;
    private String description;
    public Device(int id, String number, String item, String name_wks, String owner, String location, String description) {
        this.id = id;
        this.number = number;
        this.item = item;
        this.owner = owner;
        this.location = location;
        this.description = description;
        this.name_wks = name_wks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName_wks() {
        return name_wks;
    }

    public void setName_wks(String name_wks) {
        this.name_wks = name_wks;
    }
}
