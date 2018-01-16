package drawing.drawing.model;

import android.graphics.Point;

/**
 * Created by leo on 16/01/18.
 */

public class Intersection extends PointFigure {

    private Segment s1, s2;

    public Intersection(int x, int y, int margin, Segment s1, Segment s2) {
        super(x, y, margin);
        this.s1=s1;
        this.s2=s2;
    }

    @Override
    public Point move(int x, int y, Point anchor) {

        Point p = new Point(this.getPoint());
        super.move(x, y, anchor);

        s1.getP1().x += x - p.x;
        s1.getP1().y += y - p.y;

        s1.getP2().x += x - p.x;
        s1.getP2().y += y - p.y;

        s2.getP1().x += x - p.x;
        s2.getP1().y += y - p.y;

        s2.getP2().x += x - p.x;
        s2.getP2().y += y - p.y;

        s1.setDep();
        s2.setDep();

        return anchor;
    }

    public boolean setIntersection(){

        int x1 = s1.getP1().x;
        int y1 = s1.getP1().y;
        int x2 = s1.getP2().x;
        int y2 = s1.getP2().y;

        int x3 = s2.getP1().x;
        int y3 = s2.getP1().y;
        int x4 = s2.getP2().x;
        int y4 = s2.getP2().y;

        int x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
        int y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));

        Point p = new Point(x, y);

        if(!(s1.contains(x, y) && s2.contains(x, y)) /*&& file.contains(p)*/){
            //file.getFigures().remove(this);
            s1.getInter().remove(this);
            s2.getInter().remove(this);
            return false;
        }

        super.changePoint(p, 0);
        return true;
    }

    public Segment getL1(){
        return s1;
    }

    public Segment getL2(){
        return s2;
    }
}
