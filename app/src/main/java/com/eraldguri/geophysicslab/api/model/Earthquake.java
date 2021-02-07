package com.eraldguri.geophysicslab.api.model;

import java.util.List;

public class Earthquake {
    private String type;
    private List<Features> features;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Features> getFeatures() {
        return features;
    }

    public void setFeatures(List<Features> features) {
        this.features = features;
    }
}
