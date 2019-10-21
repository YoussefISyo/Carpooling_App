package com.example.app_trying;

public class Communes {
    public String code_postal;
    public String id;
    public String nom;
    public String wilaya_id;

    public Communes() {
    }

    public Communes(String code_postal, String id, String nom, String wilaya_id) {
        this.code_postal = code_postal;
        this.id = id;
        this.nom = nom;
        this.wilaya_id = wilaya_id;
    }

    public String getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(String code_postal) {
        this.code_postal = code_postal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getWilaya_id() {
        return wilaya_id;
    }

    public void setWilaya_id(String wilaya_id) {
        this.wilaya_id = wilaya_id;
    }
}
