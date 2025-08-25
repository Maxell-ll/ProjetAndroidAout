package be.helha.gestiondepoints.models;

import java.util.UUID;

public class Class {
    private long id;
    private String name;

    public Class(){

    }
    public Class(String name) {
        this.name = name;
    }
    public Class(long _id, String _name) {
        this.id = _id;
        this.name = _name;
    }

    public long getId() {return id;}

    public void setId(long _id) {
        this.id = _id;
    }

    public void setName(String _name) {this.name = _name;}

    public String getName() {
        return this.name;
    }
}
