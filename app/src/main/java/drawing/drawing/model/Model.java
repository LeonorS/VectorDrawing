package drawing.drawing.model;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by leo on 15/01/18.
 */

public class Model {
    private ArrayList<Figure> figures;
    private double width, height;
    private int point_margin, seg_margin;

    public Model(){
        figures = new ArrayList<>();
    }

    public Model(double width, double height, int point_margin, int seg_margin){
        figures = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.point_margin = point_margin;
        this.seg_margin = seg_margin;
    }

    public ArrayList<Figure> getFigures(){
        return figures;
    }

    public Figure findFigure(float x, float y) {
        Figure f = null;
        for (int i = 0; i < figures.size(); i++) {
            if (figures.get(i).contains(x, y)) {
                f = figures.get(i);
                f.selected = true;
                break;
            }
        }
        return f;
    }

    public void removeLastFigure(){
        figures.remove(figures.size() - 1);
    }

    public void addFigure(Figure figure) {
        figures.add(figure);
    }

    public void removeFigure(Figure figure) {
        figures.remove(figure);
    }


    public void reset(){
        figures = new ArrayList<Figure>();
    }

    public ArrayList<Figure> selectFigure(Selector selector){
        ArrayList<Figure> selected = new ArrayList<>();
        for (Figure f : figures) {
            if (f.intersects(selector)) {
                selected.add(f);
                f.selected = true;
            } else {
                f.selected = false;
            }
        }
        return selected;
    }

    public ArrayList<Figure> getSelected() {
        ArrayList<Figure> selected = new ArrayList<>();
        for (Figure f : figures) {
            if (f.selected)
                selected.add(f);
        }
        return selected;
    }

    public void uncheckFigure(){
        for(Figure f: figures){
            f.selected = false;
        }
    }

    public Figure makePoint(float x, float y){
        final Figure figure = new PointFigure((int) x, (int) y, point_margin);
        figures.add(figure);
        return figure;
    }

    public Figure makeIso(ArrayList<Figure> selected){
        if (selected == null || selected.size() < 2)
            return null;
        int sx = 0;
        int sy = 0;
//        for(int i = 0; i < selected.size(); i++){
//            if (selected.get(i) instanceof Intersection){
//                selected.remove(i);
//                i--;
//            }
//        }
        Log.d("Make iso !!!!!!!", "calcul ...");
        for(Figure f : selected){
            if (f instanceof PointFigure){
                sx += f.getPoints().get(0).x;
                sy += f.getPoints().get(0).y;
            }
            else
                return null;
        }
        sx /= selected.size();
        sy /= selected.size();

        Log.d("Make iso !!!!!!!", "calcul done");

        ArrayList<Integer> ids = new ArrayList<>();
        for (Figure f : selected){
            ids.add(f.getId());
        }

        Log.d("Make iso !!!!!!!", "ids done");

        Iso i = new Iso(sx, sy, point_margin, ids);
        figures.add(i);

        Log.d("Make iso !!!!!!!", "iso done");

        for (Figure f : selected){
            ((PointFigure) f).addBarycenter(i.getId());
        }

        Log.d("Make iso !!!!!!!", "deps done");

        return i;
    }

    public Figure makeLine(float x, float y, Point anchor){
        final Figure figure =new StraightLine(anchor.x, anchor.y, (int)x, (int)y, (double) seg_margin, width, height/*, this*/);
        figures.add(figure);
        return figure;
    }

    public Figure makeSegment(float x, float y, Point anchor){
        final Figure figure = new Line(anchor.x, anchor.y, (int)x, (int)y, (double) seg_margin/*, this*/);
        figures.add(figure);
        return figure;
    }

//    public void makeIntersection(ArrayList<Figure> selected){
//        if (selected.size() != 2)
//            return;
//
//        for(Figure f : selected)
//            if(!(f instanceof Line))
//                return;
//
//        Line s1, s2;
//        s1 = (Line)selected.get(0);
//        s2 = (Line)selected.get(1);
//
//        int x1 = s1.getP1().x;
//        int y1 = s1.getP1().y;
//        int x2 = s1.getP2().x;
//        int y2 = s1.getP2().y;
//        int x3 = s2.getP1().x;
//        int y3 = s2.getP1().y;
//        int x4 = s2.getP2().x;
//        int y4 = s2.getP2().y;
//
//        int x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
//        int y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
//
//        Point p = new Point(x, y);
//
//        if(s1.contains(p.x, p.y) && s2.contains(p.x, p.y)){
//            Intersection i = new Intersection(p.x, p.y, s1, s2, point_margin);
//            figures.add(i);
//            s1.getInter().add(i);
//            s2.getInter().add(i);
//        }
//    }

    private Figure findFigureById(int i){
        for (Figure f : figures){
            if (f.getId() == i)
                return f;
        }
        return null;
    }

    public ArrayList<Figure> findFiguesById(ArrayList<Integer> ids){
        ArrayList<Figure> figures = new ArrayList<>();
        for (Integer i : ids){
            Log.d("findFiguesById", ""+i);
            Figure f = findFigureById(i);
            figures.add(f);
        }
        return figures;
    }

    public Point moveFigure(float x, float y, Figure f, Point anchor){

        if (f != null) {

            if (f instanceof Iso){

                Iso iso = (Iso) f;
                ArrayList<Figure> points_linked = new ArrayList<>();
                points_linked.addAll(findFiguesById(iso.getIdsLinked()));

                Point p = new Point(iso.getPoint());

                for(Figure ff : points_linked){
                    PointFigure pf = (PointFigure) ff;
                    pf.move(pf.getPoint().x + (int) x - p.x, pf.getPoint().y + (int) y - p.y, anchor);
                }
            }

            else if (f instanceof PointFigure){

                PointFigure pf = (PointFigure) f;
                ArrayList<Figure> iso_linked = new ArrayList<>();
                iso_linked.addAll(findFiguesById(pf.getBarycenterIds()));

                for(Figure ff : iso_linked){
                    Iso iso = (Iso) ff;

                    ArrayList<Figure> points_linked = new ArrayList<>();
                    points_linked.addAll(findFiguesById(iso.getIdsLinked()));

                    int sx = 0;
                    int sy = 0;
                    for(Figure fff : points_linked){
                        PointFigure pff = (PointFigure) fff;
                        sx += pff.getPoint().x;
                        sy += pff.getPoint().y;
                    }
                    sx /= points_linked.size();
                    sy /= points_linked.size();
                    iso.move(sx, sy, anchor);
                }
            }

            anchor = f.move((int) x, (int) y, anchor);
        }

        return anchor;
    }

    public void removeInter(Intersection i){
        figures.remove(i);
    }

    public void setPrecision(int point_margin, int seg_margin) {
        this.point_margin = point_margin;
        this.seg_margin = seg_margin;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void select(ArrayList<Figure> selection) {
        for (Figure figure: selection) {
            figure.selected = true;
        }
    }

    public void unselect(ArrayList<Figure> selection) {
        for (Figure figure: selection) {
            figure.selected = false;
        }
    }
}
