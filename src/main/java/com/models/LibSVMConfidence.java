package com.models;

public class LibSVMConfidence {
    private double positiveConfidence;
    private double negativeConfidence;
    private double prediction;

    public LibSVMConfidence(double positiveConfidence, double negativeConfidence, double prediction) {
        this.positiveConfidence = positiveConfidence;
        this.negativeConfidence = negativeConfidence;
        this.prediction = prediction;
    }

    public double getPositiveConfidence() {
        return positiveConfidence;
    }

    public void setPositiveConfidence(double positiveConfidence) {
        this.positiveConfidence = positiveConfidence;
    }

    public double getNegativeConfidence() {
        return negativeConfidence;
    }

    public void setNegativeConfidence(double negativeConfidence) {
        this.negativeConfidence = negativeConfidence;
    }

    public double getPrediction() {
        return prediction;
    }

    public void setPrediction(double prediction) {
        this.prediction = prediction;
    }
}
