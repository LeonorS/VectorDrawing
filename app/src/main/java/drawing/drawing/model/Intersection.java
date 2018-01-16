package drawing.drawing.model;

import android.graphics.Point;

/**
 * Created by leo on 16/01/18.
 */

public class Intersection extends PointFigure {

    Line line1, line2;

    public Intersection(int x, int y, Line line1, Line line2, int margin) {
        super(x, y, margin);
        this.line1 = line1;
        this.line2 = line2;
    }

    /**
     * Ajuste l'intersection aux lignes correpondantes
     * @return
     * vrai si l'intersection est conservee, faux si elle est supprimee
     */
    public boolean setIntersection(){

        int x1 = line1.getP1().x;
        int y1 = line1.getP1().y;
        int x2 = line1.getP2().x;
        int y2 = line1.getP2().y;

        int x3 = line2.getP1().x;
        int y3 = line2.getP1().y;
        int x4 = line2.getP2().x;
        int y4 = line2.getP2().y;

        int x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
        int y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));

        Point p = new Point(x, y);

        if(!(line1.contains(p.x, p.y) && line2.contains(p.x, p.y))){
            line1.getInter().remove(this);
            line2.getInter().remove(this);
            return false;
        }

        super.changePoint(p, 0);
        return true;
    }

    /**
     * Retourne la premiere ligne liee a l'intersection
     * @return
     * une ligne
     */
    public Line getL1(){
        return line1;
    }

    /**
     * Retourne la seconde ligne liee a l'intersection
     * @return
     * une ligne
     */
    public Line getL2(){
        return line2;
    }

    @Override
    public Point move(int x, int y, Point anchor) {

        Point p = new Point(this.getPoint());
        super.move(x, y, anchor);

        line1.getP1().x += x - p.x;
        line1.getP1().y += y - p.y;

        line1.getP2().x += x - p.x;
        line1.getP2().y += y - p.y;

        line2.getP1().x += x - p.x;
        line2.getP1().y += y - p.y;

        line2.getP2().x += x - p.x;
        line2.getP2().y += y - p.y;


        line1.setDep();
        line2.setDep();

        return anchor;
    }
}
