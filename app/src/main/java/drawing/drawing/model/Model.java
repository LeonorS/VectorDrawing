package drawing.drawing.model;

import android.graphics.Point;

import java.util.ArrayList;

import drawing.drawing.vectordrawing.CustomView;

/**
 * Created by leo on 15/01/18.
 */

public class Model {

    private ArrayList<Figure> figures, canceled;
    private double width, height;
    private int point_margin, seg_margin;
    private Figure currentFigure;

    public Model(double width, double height, int point_margin, int seg_margin){
        figures = new ArrayList<>();
        canceled = new ArrayList<>(10);
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

    public int sizeFigures(){
        return figures.size();
    }

    public int sizeCanceled(){
        return canceled.size();
    }

    public void removeLastFigure(){
        Figure f = figures.remove(sizeFigures() - 1);
        if (canceled.size() == 10){
            canceled.remove(0);
        }
        canceled.add(f);
    }

    public void addLastCanceled(){
        Figure f = canceled.remove(canceled.size() - 1);
        figures.add(f);
    }

    public void reset(){
        figures = new ArrayList<Figure>();
        canceled = new ArrayList<>(10);
    }

    public void cleanCurrentFigure(){
        currentFigure = null;
    }

    public ArrayList<Figure> selectFigure(Selector selector){
        ArrayList<Figure> selected = new ArrayList<>();
        for (Figure f : figures) {
            if (f.intersects(selector)) {
                selected.add(f);
                f.selected = true;
            }
        }
        return selected;
    }

    public void uncheckFigure(){
        for(Figure f: figures){
            f.selected = false;
        }
    }

    public void makePoint(float x, float y){
        figures.add(new PointFigure((int) x, (int) y, point_margin));
    }

    public void makeIso(ArrayList<Figure> selected){
        if (selected == null || selected.size() < 2)
            return;
        int sx = 0;
        int sy = 0;
        for(int i = 0; i < selected.size(); i++){
            if (selected.get(i) instanceof Intersection){
                selected.remove(i);
                i--;
            }
        }
        for(Figure f : selected){
            if (f instanceof PointFigure){
                sx += f.getPoints().get(0).x;
                sy += f.getPoints().get(0).y;
            }
            else
                return;
        }
        sx /= selected.size();
        sy /= selected.size();
        figures.add(new Iso(sx, sy, point_margin, selected));
    }

    public void makeLine(int action, float x, float y, Point anchor){
            figures.remove(currentFigure);
            if (action == CustomView.SEG_ACTION) {
                currentFigure = new Line(anchor.x, anchor.y, (int)x, (int)y, (double) seg_margin, this);
            } else if (action == CustomView.LINE_ACTION) {
                currentFigure = new StraightLine(anchor.x, anchor.y, (int)x, (int)y, (double) seg_margin, width, height, this);
            }
            figures.add(currentFigure);
    }

    public void makeIntersection(ArrayList<Figure> selected){
        if (selected.size() != 2)
            return;

        for(Figure f : selected)
            if(!(f instanceof Line))
                return;

        Line s1, s2;
        s1 = (Line)selected.get(0);
        s2 = (Line)selected.get(1);

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

        if(s1.contains(p.x, p.y) && s2.contains(p.x, p.y)){
            Intersection i = new Intersection(p.x, p.y, s1, s2, point_margin);
            figures.add(i);
            s1.getInter().add(i);
            s2.getInter().add(i);
        }
    }

    public Point moveFigure(float x, float y, Figure f, Point anchor){
        if (f != null) {
            anchor = f.move((int) x, (int) y, anchor);
        }
        return anchor;
    }

    public void removeInter(Intersection i){
        figures.remove(i);
    }
}
