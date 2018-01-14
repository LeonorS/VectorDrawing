package drawing.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import java.util.Vector;

/**
 * Created by leo on 03/12/17.
 */

public abstract class Figure {

    protected static    Paint           myPaint;
    private             Vector<Point>   points;
    protected           boolean         selected = false;

    public Figure() {
        points = new Vector<>();
        if (myPaint == null) {
            myPaint = new Paint();
            myPaint.setAntiAlias(true);
        }
    }

    public void addPoint(Point point){
        points.add(point);
    }

    public Vector<Point> getPoints(){
        return points;
    }

    public void changePoint(Point p, int index){
        points.remove(index);
        points.insertElementAt(p, index);
    }

    public abstract boolean contains(float x, float y);
    public abstract void onDraw(Canvas canvas);
    public abstract Point move(int x, int y, Point anchor);
    public abstract boolean intersects(Selector selector);
}
