package com.ceking.bean;

/*
 *@author ceking
 *@description
 *@date 2020-12-16 17:18
 */
public class ExamStudent {
    private int fllowID;
    private int type;
    private String IDCard;
    private String examCard;
    private String studnetName;
    private String location;
    private int grade;

    public ExamStudent() {
    }

    public ExamStudent(int fllowID, int type, String IDCard, String examCard, String studnetName, String location, int grade) {
        this.fllowID = fllowID;
        this.type = type;
        this.IDCard = IDCard;
        this.examCard = examCard;
        this.studnetName = studnetName;
        this.location = location;
        this.grade = grade;
    }

    public int getFllowID() {
        return fllowID;
    }

    public void setFllowID(int fllowID) {
        this.fllowID = fllowID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getExamCard() {
        return examCard;
    }

    public void setExamCard(String examCard) {
        this.examCard = examCard;
    }

    public String getStudnetName() {
        return studnetName;
    }

    public void setStudnetName(String studnetName) {
        this.studnetName = studnetName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "ExamStudent{" +
                "fllowID=" + fllowID +
                ", type=" + type +
                ", IDCard='" + IDCard + '\'' +
                ", examCard='" + examCard + '\'' +
                ", studnetName='" + studnetName + '\'' +
                ", location='" + location + '\'' +
                ", grade=" + grade +
                '}';
    }
}
