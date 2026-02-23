package com.example.photolens.models;


public class CameraParameters {
    private int isoIndex;
    private int apertureIndex;
    private int shutterIndex;

    public static final int[] ISO_VALUES = {100, 200, 400, 800, 1600, 3200, 6400};
    public static final double[] APERTURE_VALUES = {1.8, 2.8, 4, 5.6, 8, 11, 16};
    public static final String[] SHUTTER_VALUES = {
            "1/1000", "1/500", "1/250", "1/125", "1/60",
            "1/30", "1/15", "1\"", "2\"", "5\"", "10\""
    };

    public CameraParameters() {
        reset();
    }

    public void reset() {
        this.isoIndex = 3; // ISO 800
        this.apertureIndex = 3; // f/5.6
        this.shutterIndex = 3; // 1/125
    }

    public int calculateExposure() {
        return isoIndex + (6 - apertureIndex) - shutterIndex;
    }

    public String getExposureStatus() {
        int exp = calculateExposure();
        if (exp < 3) return "Недосвет";
        if (exp > 8) return "Пересвет";
        return "Норма";
    }

    // Getters and setters
    public int getIsoIndex() {
        return isoIndex;
    }

    public void setIsoIndex(int isoIndex) {
        this.isoIndex = isoIndex;
    }

    public int getApertureIndex() {
        return apertureIndex;
    }

    public void setApertureIndex(int apertureIndex) {
        this.apertureIndex = apertureIndex;
    }

    public int getShutterIndex() {
        return shutterIndex;
    }

    public void setShutterIndex(int shutterIndex) {
        this.shutterIndex = shutterIndex;
    }
}