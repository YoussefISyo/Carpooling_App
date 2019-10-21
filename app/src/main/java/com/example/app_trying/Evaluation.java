package com.example.app_trying;

public class Evaluation {

    private String idEvaluation;

    private String idEvaluer;

    private String level;

    private String Comment;

    private String time;

    public Evaluation() {
    }

    public Evaluation(String idEvaluation, String idEvaluer, String level, String comment, String time) {
        this.idEvaluation = idEvaluation;
        this.idEvaluer = idEvaluer;
        this.level = level;
        Comment = comment;
        this.time = time;
    }

    public String getIdEvaluation() {
        return idEvaluation;
    }

    public void setIdEvaluation(String idEvaluation) {
        this.idEvaluation = idEvaluation;
    }

    public String getIdEvaluer() {
        return idEvaluer;
    }

    public void setIdEvaluer(String idEvaluer) {
        this.idEvaluer = idEvaluer;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
