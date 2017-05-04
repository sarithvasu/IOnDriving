package com.ion.iondriving.core_logic;

import com.ion.iondriving.dpconverter.VectorBase;

/**
 * Created by sarith.vasu on 29-12-2016.
 */

public class ABCCalculator {
    // Singleton instance for Filter.
    private static volatile ABCCalculator _singletonInstance;
    private static Object _syncRoot = new Object();
    private  ABCCalculator(){

    }

    public static ABCCalculator getSingletonInstance()
    {

        if (_singletonInstance == null)
        {
            synchronized(_syncRoot)
            {
                if (_singletonInstance == null)
                {
                    _singletonInstance = new ABCCalculator();
                }
            }
        }
        return _singletonInstance;
    }
    public ABCCalculator Caculate(VectorBase accFinalData, double currentSpeed, double configSpeed) {
    return  null;
    }
    public double GetCaculatedScore()
    {
        return 0;
        //private set { m_calc
        // ulatedScore = value; }
    }
    public double GetCaculatedAcceleration()
    {
        return 0;
        //private set { m_CalculatedAcceleration = value; }
    }
    public double GetCaculatedBreaking()
    {
        return 0;
        //private set { m_CalculatedBreaking = value; }
    }

    public double GetCaculatedCornering()
    {
        return 0;
        //private set { m_CalculatedCornering = value; }
    }
    //Recently Added 14th October 2014 to support High Speed  Trip File Writing
    public double getX_AxisMagnitudeDifference() {
        return 0;
    }

    public double getY_AxisMagnitudeDifference() {
        return 0;
    }

    public double getZ_AxisMagnitudeDifference() {
        return 0;
    }
    // }} #end region Property

}
