package drawing.drawing.model;

import android.graphics.Point;
import android.util.Log;

/**
 * Created by leo on 15/01/18.
 */

public class StraightLine extends Line {

    private double width, height;

    public StraightLine(int x1, int y1, int x2, int y2, double margin, double width, double height/*, Model model*/) {
        super(x1, y1, x2, y2, margin/*, model*/);
        this.width = width;
        this.height = height;
        setPoints();
    }

    public void setPoints(){
        Point p1, p2, pp1, pp2;
        double tmpx, tmpy;
        if (getP1().x < getP2().x) {
            p1 = getP1();
            p2 = getP2();
        }
        else if (getP1().x > getP2().x) {
            p1 = getP2();
            p2 = getP1();
            setP2(p2);
            setP1(p1);
        }
        else if (getP1().y < getP2().y) {
            p1 = getP1();
            p2 = getP2();
        }
        else {
            p1 = getP2();
            p2 = getP1();
            setP2(p2);
            setP1(p1);
        }
        double m = (double)(p1.y - p2.y) / (double)(p1.x - p2.x);
        tmpy = (double)p2.y + m * (width - (double)p2.x);
        if (tmpy < height && tmpy >= 0)
            pp2 = new Point ((int) width, (int)tmpy);
        else if (tmpy >= height){
            tmpx = (height - (double)p2.y) / m + (double)p2.x;
            pp2 = new Point ((int) tmpx, (int)height);
        }
        else {
            tmpx = (0 - (double)p2.y) / m + (double)p2.x;
            pp2 = new Point ((int) tmpx, 0);
        }
        tmpy = (double)p1.y + m * (0 - (double)p1.x);
        if (tmpy < height && tmpy >= 0)
            pp1 = new Point (0, (int)tmpy);
        else if (tmpy >= height){
            tmpx = (height - (double)p1.y) / m + (double)p1.x;
            pp1 = new Point ((int) tmpx, (int)height);
        }
        else {
            tmpx = (0 - (double)p1.y) / m + (double)p1.x;
            pp1 = new Point ((int) tmpx, 0);
        }
        setP1(pp1);
        setP2(pp2);
    }

    @Override
    public Point move(int x, int y, Point anchor) {
        anchor = super.move(x, y, anchor);
        setPoints();
        return anchor;
    }
}
