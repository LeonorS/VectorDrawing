package com.sirottigmail.leonor.vectordrawing;

import android.graphics.Point;
import android.view.MotionEvent;

import java.util.Vector;

/**
 * Created by leo on 03/12/17.
 */

public abstract class Figure {

    private Vector<Point> points;

    protected Figure(){
        points = new Vector<>();
    }

    public void addPoint(Point point){
        points.add(point);
    }


}
