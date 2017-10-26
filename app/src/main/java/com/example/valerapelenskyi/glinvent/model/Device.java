package com.example.valerapelenskyi.glinvent.model;

/**
 * Created by valera.pelenskyi on 26.10.17.
 */

public class Device {

    private int id ;
    private String number;

    public Device(int id, String number) {
        this.id = id;
        this.number = number;
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
}
