package com.ion.iondriving.dpconverter;

/**
 * Created by sarith.vasu on 29-12-2016.
 */

public class VectorBase {
    //Region property
    private double X;
    private double Y;
    private double Z;

    public double getX() {
        return X;
    }
    public void setX(double x) {
        X = x;
    }
    public double getY() {
        return Y;
    }
    public void setY(double y) {
        Y = y;
    }
    public double getZ() {
        return Z;
    }
    public void setZ(double z) {
        Z = z;
    }
    //End of Region

    //Default Constructor
    public VectorBase() {
        // TODO Auto-generated constructor stub
    }



  /*  // Overloaded constructor
    public VectorBase(double x, double y, double z)
    {
        X = DPConverter.RoundUpNumber(x);
        Y = DPConverter.RoundUpNumber(y);
        Z = DPConverter.RoundUpNumber(z);
    }*/


    //end of  class
}