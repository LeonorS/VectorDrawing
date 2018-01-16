package drawing.drawing.model;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by leo on 06/12/17.
 */

public class Iso extends PointFigure {

    private final ArrayList<PointFigure> points_linked;

    public Iso(int x, int y, int margin, ArrayList<Figure> figures) {
        super(x, y, margin);
        this.points_linked = new ArrayList<>();
        for(Figure f : figures){
            points_linked.add((PointFigure)f);
            ((PointFigure) f).addBarycenter(this);
        }
    }

    @Override
    public Point move(int x, int y, Point anchor){
        Point p = new Point(this.getPoint());
        super.move(x, y, anchor);
        for(PointFigure pf : points_linked){
            pf.move(pf.getPoint().x + x - p.x, pf.getPoint().y + y - p.y, anchor);
        }
        return anchor;
    }

    public void movePoint(Point anchor){
        int sx = 0;
        int sy = 0;
        for(PointFigure p : points_linked){
            sx += p.getPoint().x;
            sy += p.getPoint().y;
        }
        sx /= points_linked.size();
        sy /= points_linked.size();
        super.move(sx, sy, anchor);
    }

    public ArrayList<PointFigure> getPointsFigure(){
        return points_linked;
    }
}
