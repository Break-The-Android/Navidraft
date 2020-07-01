package com.example.navidraft.Model;

public class DataStructure {

    Integer ID;
    String DName;
    String UName;
    String PWord;
    String Phone;
    String Unit;
    String TravelType;
    String ImageURL;
    String Theme;

    public DataStructure(){}

    public DataStructure(Integer ID, String DName, String UName, String PWord, String phone, String unit, String travelType, String imageURL, String theme) {
        this.ID = ID;
        this.DName = DName;
        this.UName = UName;
        this.PWord = PWord;
        Phone = phone;
        Unit = unit;
        TravelType = travelType;
        ImageURL = imageURL;
        Theme = theme;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getDName() {
        return DName;
    }

    public void setDName(String DName) {
        this.DName = DName;
    }

    public String getUName() {
        return UName;
    }

    public void setUName(String UName) {
        this.UName = UName;
    }

    public String getPWord() {
        return PWord;
    }

    public void setPWord(String PWord) {
        this.PWord = PWord;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getTravelType() {
        return TravelType;
    }

    public void setTravelType(String travelType) {
        TravelType = travelType;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getTheme() {
        return Theme;
    }

    public void setTheme(String theme) {
        Theme = theme;
    }
}