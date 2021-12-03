package com.example.screentofacedistance;

public class ScreenPoints {

    private float distanceEye;
    private float distanceDevice;

    public ScreenPoints(float dE, float dD) {
        this.distanceEye = dE;
        this.distanceDevice = dD;
    }

    public float distanceDeviceGet() {
        return distanceDevice;
    }
    public float distanceEyesGet() {
        return distanceEye;
    }



}
