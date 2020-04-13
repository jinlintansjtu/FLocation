package com.xposed.hook.location;

public class FLocation {
    private String latitude, longitude, MCC, MNC, LAC, BSSS, CID;

    public FLocation(String latitude, String longitude, String MCC, String MNC, String LAC, String BSSS, String CID){
        this.latitude = latitude;
        this.longitude = longitude;
        this.MCC = MCC;
        this.MNC = MNC;
        this.LAC = LAC;
        this.BSSS = BSSS;
        this.CID = CID;
    }

    public String getLatitude (){
        return latitude;
    }

    public void setLatitude(String latitude){
        this.latitude = latitude;
    }

    public String getLongitude (){
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMCC() {
        return MCC;
    }

    public String getBSSS() {
        return BSSS;
    }

    public String getCID() {
        return CID;
    }

    public String getLAC() {
        return LAC;
    }

    public String getMNC() {
        return MNC;
    }

    public void setBSSS(String BSSS) {
        this.BSSS = BSSS;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public void setLAC(String LAC) {
        this.LAC = LAC;
    }

    public void setMCC(String MCC) {
        this.MCC = MCC;
    }

    public void setMNC(String MNC) {
        this.MNC = MNC;
    }
}
