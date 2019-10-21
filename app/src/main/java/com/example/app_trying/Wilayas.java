package com.example.app_trying;

public class Wilayas {

    public String code;
    public String id;
    public String nom;

    public Wilayas() {
    }

    public Wilayas(String code, String id, String nom) {
        this.code = code;
        this.id = id;
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
}
