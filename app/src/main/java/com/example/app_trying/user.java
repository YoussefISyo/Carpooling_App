package com.example.app_trying;

public class user {
    public String id;
    public String name;
    public String email;
    public String urlPhoto;
    public String phone;
    public String nationalité;
    public String dateNaissance;
    public String sexe;
    public String voiture;
    public String ambiance;
    public String cigarette;
    public String discussion;
    public String propos;




    public user() {
    }

    public user(String id, String name, String email, String urlPhoto) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.urlPhoto = urlPhoto;
    }

    public user(String id, String name, String email, String phone, String nationalité, String dateNaissance, String sexe,
                String voiture, String ambiance, String cigarette, String discussion, String propos) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.nationalité = nationalité;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.voiture = voiture;
        this.ambiance = ambiance;
        this.cigarette = cigarette;
        this.discussion = discussion;
        this.propos = propos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNationalité() {
        return nationalité;
    }

    public void setNationalité(String nationalité) {
        this.nationalité = nationalité;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getVoiture() {
        return voiture;
    }

    public void setVoiture(String voiture) {
        this.voiture = voiture;
    }

    public String getAmbiance() {
        return ambiance;
    }

    public void setAmbiance(String ambiance) {
        this.ambiance = ambiance;
    }

    public String getCigarette() {
        return cigarette;
    }

    public void setCigarette(String cigarette) {
        this.cigarette = cigarette;
    }

    public String getDiscussion() {
        return discussion;
    }

    public void setDiscussion(String discussion) {
        this.discussion = discussion;
    }

    public String getPropos() {
        return propos;
    }

    public void setPropos(String propos) {
        this.propos = propos;
    }
}
