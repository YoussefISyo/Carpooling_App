package com.example.app_trying;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

public class DemandModel implements Serializable {

    private de.hdodenhof.circleimageview.CircleImageView mImageprofile;

    private String idTrip;

    private String idDemandeur;

    private String jName;

    private String jInitLocat;

    private String jFinalLocat;

    private String jDate;

    private String jTime;

    private String jPrice;

    private String jComment;

    public DemandModel() {
    }

    public DemandModel(String idTrip, String idDemandeur, String jName, String jInitLocat, String jFinalLocat, String jDate, String jTime, String jPrice, String jComment) {
        this.idTrip = idTrip;
        this.idDemandeur = idDemandeur;
        this.jName = jName;
        this.jInitLocat = jInitLocat;
        this.jFinalLocat = jFinalLocat;
        this.jDate = jDate;
        this.jTime = jTime;
        this.jPrice = jPrice;
        this.jComment = jComment;
    }

    public CircleImageView getmImageprofile() {
        return mImageprofile;
    }

    public void setmImageprofile(CircleImageView mImageprofile) {
        this.mImageprofile = mImageprofile;
    }

    public String getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(String idTrip) {
        this.idTrip = idTrip;
    }

    public String getIdDemandeur() {
        return idDemandeur;
    }

    public void setIdDemandeur(String idDemandeur) {
        this.idDemandeur = idDemandeur;
    }

    public String getjName() {
        return jName;
    }

    public void setjName(String jName) {
        this.jName = jName;
    }

    public String getjInitLocat() {
        return jInitLocat;
    }

    public void setjInitLocat(String jInitLocat) {
        this.jInitLocat = jInitLocat;
    }

    public String getjFinalLocat() {
        return jFinalLocat;
    }

    public void setjFinalLocat(String jFinalLocat) {
        this.jFinalLocat = jFinalLocat;
    }

    public String getjDate() {
        return jDate;
    }

    public void setjDate(String jDate) {
        this.jDate = jDate;
    }

    public String getjTime() {
        return jTime;
    }

    public void setjTime(String jTime) {
        this.jTime = jTime;
    }

    public String getjPrice() {
        return jPrice;
    }

    public void setjPrice(String jPrice) {
        this.jPrice = jPrice;
    }

    public String getjComment() {
        return jComment;
    }

    public void setjComment(String jComment) {
        this.jComment = jComment;
    }
}
