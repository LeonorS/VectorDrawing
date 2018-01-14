package drawing.drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by leo on 03/12/17.
 */

public class PointFigure extends Figure {

    protected   int               widthPoint = 10;
    private     ArrayList<Iso>    barycenters;

    PointFigure(int x, int y) {
        super();
        addPoint(new Point(x, y));
        barycenters = new ArrayList<>();
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
        if ((getPoint().x - x) * (getPoint().x - x) + (getPoint().y - y) * (getPoint().y - y) <= widthPoint * widthPoint) {
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
}
