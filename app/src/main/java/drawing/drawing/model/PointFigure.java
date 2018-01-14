package drawing.drawing.model;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

import drawing.drawing.testdrawing.Personalization;
import drawing.drawing.vectordrawing.VectorDrawing;

/**
 * Created by leo on 03/12/17.
 */

public class PointFigure extends Figure {

    protected int             widthPoint = 10;
    private ArrayList<Iso>    barycenters;
    private int margin = 0;

    public PointFigure(int x, int y, int margin) {
        super();
        addPoint(new Point(x, y));
        barycenters = new ArrayList<>();
        this.margin = margin;
    }

    Point getPoint(){
        return super.getPoints().get(0);
    }

    @Override
    public void onDraw(Canvas canvas) {
        myPaint.setColor(Color.BLUE);
        myPaint.setStyle(Paint.Style.FILL);
        if(selected == true){
            myPaint.setColor(Color.RED);
            myPaint.setStyle(Paint.Style.FILL);
        }
        canvas.drawCircle(getPoint().x, getPoint().y, widthPoint, myPaint);
    }

    @Override
    public boolean contains(float x, float y) {
         if ((getPoint().x - x) * (getPoint().x - x) + (getPoint().y - y) * (getPoint().y - y) <= (margin + widthPoint) * (margin + widthPoint)) {
            return true;
        }
        return false;
    }

    @Override
    public Point move(int x, int y, Point anchor) {
        for(Iso i : barycenters){
            i.movePoint(anchor);
        }
        changePoint(new Point(x, y), 0);
        return anchor;
    }

    @Override
    public boolean intersects(Selector rect) {
        return rect != null && rect.contains(getPoint().x, getPoint().y);
    }

    public void addBarycenter(Iso i){
        barycenters.add(i);
    }

    public void setMargin(int value){
        margin = value;
    }
}
