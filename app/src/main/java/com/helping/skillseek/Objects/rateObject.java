package com.helping.skillseek.Objects;

public class rateObject {
    float rate;
    float raterCount;

    public rateObject(float rate, float raterCount) {
        this.rate = rate;
        this.raterCount = raterCount;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getRaterCount() {
        return raterCount;
    }

    public void setRaterCount(float raterCount) {
        this.raterCount = raterCount;
    }
}
