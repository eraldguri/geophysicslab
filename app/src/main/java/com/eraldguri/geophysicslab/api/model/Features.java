package com.eraldguri.geophysicslab.api.model;

import java.io.Serializable;

public class Features implements Serializable {
    public String type;
    public Properties properties;
    public Geometry geometry;

    public String getType() {
        return type;
    }

    public Properties getProperties() {
        return properties;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
