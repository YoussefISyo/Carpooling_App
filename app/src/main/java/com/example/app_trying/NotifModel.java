package com.example.app_trying;

import android.widget.Button;

import java.util.ArrayList;

public class NotifModel {

    private String idAttente;

    private String idTrip;

    private String idChauff;

    private String idPass;

    private String type;

    private String nameChauff;

    private String namePass;

    private String InitLocat;

    private String FinalLocat;

    private String date;

    private String Time;

    private String places;

    private String prix;

    private String comment;

    private String idDemands;

    public NotifModel() {
    }

    public NotifModel(String idAttente,String idTrip, String idChauff, String idPass, String type,String nameChauff, String namePass, String initLocat, String finalLocat) {
        this.idAttente = idAttente;
        this.idTrip = idTrip;
        this.idChauff = idChauff;
        this.idPass = idPass;
        this.type = type;
        this.nameChauff = nameChauff;
        this.namePass = namePass;
        InitLocat = initLocat;
        FinalLocat = finalLocat;
    }

    public NotifModel(String idAttente, String idTrip, String idChauff, String idPass, String type, String nameChauff, String namePass, String initLocat, String finalLocat, String date, String time, String places, String prix, String comment) {
        this.idAttente = idAttente;
        this.idTrip = idTrip;
        this.idChauff = idChauff;
        this.idPass = idPass;
        this.type = type;
        this.nameChauff = nameChauff;
        this.namePass = namePass;
        InitLocat = initLocat;
        FinalLocat = finalLocat;
        this.date = date;
        Time = time;
        this.places = places;
        this.prix = prix;
        this.comment = comment;
    }

    public NotifModel(String idAttente, String idTrip, String idChauff, String idPass, String type, String nameChauff, String namePass, String initLocat, String finalLocat, String date, String time, String places, String prix, String comment, String idDemands) {
        this.idAttente = idAttente;
        this.idTrip = idTrip;
        this.idChauff = idChauff;
        this.idPass = idPass;
        this.type = type;
        this.nameChauff = nameChauff;
        this.namePass = namePass;
        InitLocat = initLocat;
        FinalLocat = finalLocat;
        this.date = date;
        Time = time;
        this.places = places;
        this.prix = prix;
        this.comment = comment;
        this.idDemands = idDemands;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIdTrip() {
        return idTrip;
    }

    public String getIdDemands() {
        return idDemands;
    }

    public void setIdDemands(String idDemands) {
        this.idDemands = idDemands;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public void setIdTrip(String idTrip) {
        this.idTrip = idTrip;
    }

    public String getIdChauff() {
        return idChauff;
    }

    public void setIdChauff(String idChauff) {
        this.idChauff = idChauff;
    }

    public String getIdPass() {
        return idPass;
    }

    public void setIdPass(String idPass) {
        this.idPass = idPass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNamePass() {
        return namePass;
    }

    public void setNamePass(String namePass) {
        this.namePass = namePass;
    }

    public String getInitLocat() {
        return InitLocat;
    }

    public void setInitLocat(String initLocat) {
        InitLocat = initLocat;
    }

    public String getFinalLocat() {
        return FinalLocat;
    }

    public void setFinalLocat(String finalLocat) {
        FinalLocat = finalLocat;
    }

    public String getIdAttente() {
        return idAttente;
    }

    public void setIdAttente(String idAttente) {
        this.idAttente = idAttente;
    }

    public String getNameChauff() {
        return nameChauff;
    }

    public void setNameChauff(String nameChauff) {
        this.nameChauff = nameChauff;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }
}
