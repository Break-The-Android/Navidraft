package com.example.navidraft.Model;

public class TravelHistoryModel {

    String TripID;
    String Date;
    String Time;
    String Address;
    String StartPoint;
    String PlaceName;
    String TravelTime;
    String TravelDistance;
    String TravelMedium;

    public TravelHistoryModel(){}

    public TravelHistoryModel(String tripID, String date, String time, String address, String startPoint, String placeName, String travelTime, String travelDistance, String travelMedium) {
        TripID = tripID;
        Date = date;
        Time = time;
        Address = address;
        StartPoint = startPoint;
        PlaceName = placeName;
        TravelTime = travelTime;
        TravelDistance = travelDistance;
        TravelMedium = travelMedium;
    }

    public String getTripID() {
        return TripID;
    }

    public void setTripID(String tripID) {
        TripID = tripID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getStartPoint() {
        return StartPoint;
    }

    public void setStartPoint(String startPoint) {
        StartPoint = startPoint;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }

    public String getTravelTime() {
        return TravelTime;
    }

    public void setTravelTime(String travelTime) {
        TravelTime = travelTime;
    }

    public String getTravelDistance() {
        return TravelDistance;
    }

    public void setTravelDistance(String travelDistance) {
        TravelDistance = travelDistance;
    }

    public String getTravelMedium() {
        return TravelMedium;
    }

    public void setTravelMedium(String travelMedium) {
        TravelMedium = travelMedium;
    }
}
