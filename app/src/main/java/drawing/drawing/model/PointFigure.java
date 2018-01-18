package drawing.drawing.model;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by leo on 03/12/17.
 */

public class PointFigure extends Figure {

    protected int             widthPoint = 10;
//    private ArrayList<Iso>    barycenters;
    private ArrayList<Integer>    barycenterIds;
    private int margin = 0;

    public PointFigure(int x, int y, int margin) {
        super();
        addPoint(new Point(x, y));
//        barycenters = new ArrayList<>();
        barycenterIds = new ArrayList<>();
        this.margin = margin;
    }

    public Point getPoint(){
        return super.getPoints().get(0);
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
//        for(Iso i : barycenters){
//            i.movePoint(anchor);
//        }
        changePoint(new Point(x, y), 0);
        return anchor;
    }

    @Override
    public boolean intersects(Selector rect) {
        return rect != null && rect.contains(getPoint().x, getPoint().y);
    }

//    public void addBarycenter(Iso i){
//        barycenters.add(i);
//    }

    public void addBarycenter(Integer i){
        barycenterIds.add(i);
    }

    public ArrayList<Integer> getBarycenterIds(){
        return barycenterIds;
    }

    public void setMargin(int value){
        margin = value;
    }

    public int getWidthPoint(){
        return widthPoint;
    }
}
