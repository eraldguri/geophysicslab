package com.eraldguri.geophysicslab.api.model;

import java.io.Serializable;

public class Properties implements Serializable {
    public double mag;
    public String place;
    public String time;
    public String updated;
    private int tz;
    public String felt;
    public String cdi;
    public String mmi;
    public String alert;
    public String status;
    public int tsunami;
    public int sig;
    public String origin;
    public int nst;
    public double dmin;
    public double rms;
    public double gap;
    public String magType;
    public String type;
    public String title;

    public int getTz() {
        return tz;
    }

    public void setTz(int tz) {
        this.tz = tz;
    }

    public double getMagnitude() {
        return mag;
    }

    public void setMagnitude(double magnitude) {
        this.mag = magnitude;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getFelt() {
        return felt;
    }

    public void setFelt(String felt) {
        this.felt = felt;
    }

    public String getCdi() {
        return cdi;
    }

    public void setCdi(String cdi) {
        this.cdi = cdi;
    }

    public String getMmi() {
        return mmi;
    }

    public void setMmi(String mmi) {
        this.mmi = mmi;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTsunami() {
        return tsunami;
    }

    public void setTsunami(int tsunami) {
        this.tsunami = tsunami;
    }

    public int getSig() {
        return sig;
    }

    public void setSig(int sig) {
        this.sig = sig;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getNst() {
        return nst;
    }

    public void setNst(int nst) {
        this.nst = nst;
    }

    public double getDmin() {
        return dmin;
    }

    public void setDmin(double dmin) {
        this.dmin = dmin;
    }

    public double getRms() {
        return rms;
    }

    public void setRms(double rms) {
        this.rms = rms;
    }

    public double getGap() {
        return gap;
    }

    public void setGap(double gap) {
        this.gap = gap;
    }

    public String getMagType() {
        return magType;
    }

    public void setMagType(String magType) {
        this.magType = magType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Properties{" +
                "mag=" + mag +
                ", place='" + place + '\'' +
                ", time='" + time + '\'' +
                ", updated='" + updated + '\'' +
                ", felt='" + felt + '\'' +
                ", cdi='" + cdi + '\'' +
                ", mmi='" + mmi + '\'' +
                ", alert='" + alert + '\'' +
                ", status='" + status + '\'' +
                ", tsunami=" + tsunami +
                ", sig=" + sig +
                ", origin='" + origin + '\'' +
                ", nst='" + nst + '\'' +
                ", dmin='" + dmin + '\'' +
                ", rms='" + rms + '\'' +
                ", gap='" + gap + '\'' +
                ", magType='" + magType + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
