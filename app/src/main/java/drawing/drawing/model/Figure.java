package drawing.drawing.model;

import android.graphics.Point;

import java.util.Vector;

/**
 * Created by leo on 03/12/17.
 */

public abstract class Figure {

    private Vector<Point> points;
    public boolean selected = false;

    public Figure() {
        points = new Vector<>();
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
    public abstract Point move(int x, int y, Point anchor);
    public abstract boolean intersects(Selector selector);
}
