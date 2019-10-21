package com.example.app_trying;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Trip implements Serializable {

    private de.hdodenhof.circleimageview.CircleImageView mImageprofile;

    private String idTrip;

    private String idChauffeur;

    private String mName;

    private String mInitLocat;

    private String mFinalLocat;

    private String mDate;

    private String mTime;

    private String mPrice;

    private String mSeatsAvail;

    private String mComment;


    public Trip() {
    }

    public Trip(String idTrip,String idChauffeur, String mName, String mInitLocat, String mFinalLocat, String mDate, String mTime, String mPrice, String mSeatsAvail, String mComment) {
       // this.mImageprofile = mImageprofile;
        this.idTrip = idTrip;
        this.idChauffeur = idChauffeur;
        this.mName = mName;
        this.mInitLocat = mInitLocat;
        this.mFinalLocat = mFinalLocat;
        this.mDate = mDate;
        this.mTime = mTime;
        this.mPrice = mPrice;
        this.mSeatsAvail = mSeatsAvail;
        this.mComment = mComment;
    }


    public de.hdodenhof.circleimageview.CircleImageView getmImageprofile() {
        return mImageprofile;
    }

    public String getIdChauffeur() {
        return idChauffeur;
    }

    public void setIdChauffeur(String idChauffeur) {
        this.idChauffeur = idChauffeur;
    }

    public String getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(String idTrip) {
        this.idTrip = idTrip;
    }

    public String getmName() {
        return mName;
    }

    public String getmInitLocat() {
        return mInitLocat;
    }

    public String getmFinalLocat() {
        return mFinalLocat;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public String getmPrice() {
        return mPrice;
    }

    public String getmSeatsAvail() {
        return mSeatsAvail;
    }

   public void setmImageprofile(de.hdodenhof.circleimageview.CircleImageView mImageprofile) {
        this.mImageprofile = mImageprofile;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmInitLocat(String mInitLocat) {
        this.mInitLocat = mInitLocat;
    }

    public void setmFinalLocat(String mFinalLocat) {
        this.mFinalLocat = mFinalLocat;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public void setmSeatsAvail(String mSeatsAvail) {
        this.mSeatsAvail = mSeatsAvail;
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }

}
