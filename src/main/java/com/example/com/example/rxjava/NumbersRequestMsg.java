package com.example.com.example.rxjava;

/**
 * Created by robertwood on 7/4/17.
 */
public class NumbersRequestMsg {
    private int numValues;

    public NumbersRequestMsg(int numValues) {
        this.numValues = numValues;
    }

    public int getNumValues() {
        return numValues;
    }

    public void setNumValues(int numValues) {
        this.numValues = numValues;
    }
}
