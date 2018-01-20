package drawing.drawing.model;

import android.graphics.Point;

import java.util.ArrayList;

import drawing.drawing.vectordrawing.DrawingView;

import static drawing.drawing.vectordrawing.DrawingView.DrawingAction.LINE_ACTION;
import static drawing.drawing.vectordrawing.DrawingView.DrawingAction.SEG_ACTION;

/**
 * Created by leo on 15/01/18.
 */

public class Model {
    private ArrayList<Figure> figures, canceled;
    private double width, height;
    private int point_margin, seg_margin;
    private Figure currentFigure;

    public Model(){
        figures = new ArrayList<>();
        canceled = new ArrayList<>(10);
    }

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
        ArrayList<Integer> ids = new ArrayList<>();
        for (Figure f : selected){
            ids.add(f.getId());
        }
        Iso i = new Iso(sx, sy, point_margin, ids);
        figures.add(i);
        for (Figure f : selected){
            ((PointFigure) f).addBarycenter(i.getId());
        }
    }

    public void makeLine(DrawingView.DrawingAction action, float x, float y, Point anchor){
        figures.remove(currentFigure);
        if (action == SEG_ACTION) {
            currentFigure = new Line(anchor.x, anchor.y, (int)x, (int)y, (double) seg_margin/*, this*/);
        } else if (action == LINE_ACTION) {
            currentFigure = new StraightLine(anchor.x, anchor.y, (int)x, (int)y, (double) seg_margin, width, height/*, this*/);
        }
        figures.add(currentFigure);
    }

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

    public void setPrecision(int point_margin, int seg_margin) {
        this.point_margin = point_margin;
        this.seg_margin = seg_margin;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
