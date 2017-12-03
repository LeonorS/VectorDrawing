package com.sirottigmail.leonor.vectordrawing;

import android.graphics.Point;

import com.sirottigmail.leonor.vectordrawing.Figure;

/**
 * Created by leo on 03/12/17.
 */

public class PointFigure extends Figure {

    public PointFigure(Point p){
        super();
        super.addPoint(p);
    }

    public PointFigure(int x, int y) {
        this(new Point(x, y));
    }
}
