package drawing.drawing.vectordrawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import drawing.drawing.model.Figure;
import drawing.drawing.model.Iso;
import drawing.drawing.model.PointFigure;
import drawing.drawing.model.Segment;
import drawing.drawing.model.Selector;

/**
 * Created by leo on 03/12/17.
 */

public class CustomView extends View {

    public  static final    int     DEFAULT_ACTION      = -1;
    public  static final    int     POINT_ACTION        = 1;
    public  static final    int     SELECT_ACTION       = 2;
    public  static final    int     SEG_ACTION          = 3;
    public                  int     current_action      = DEFAULT_ACTION;
    private Figure touched = null;
    private Selector selector = null;
    protected ArrayList<Figure> figures, selected;
    protected Point  anchor;
    private Figure currentFigure;
    private int point_margin, seg_margin;

    public CustomView(Context context, int point_margin, int seg_margin) {
        super(context);
        figures     = new ArrayList<>();
        selected    = new ArrayList<>();
        this.point_margin = point_margin;
        this.seg_margin = seg_margin;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            touched = null;
            makeAchor(event.getX(), event.getY());
            if (current_action == DEFAULT_ACTION) {
                touched = getFigure(event.getX(), event.getY());
            } if (current_action == POINT_ACTION || (current_action == DEFAULT_ACTION && touched == null)) {
                makePoint(event.getX(), event.getY());
            } break;
        case MotionEvent.ACTION_MOVE:
            if (current_action == DEFAULT_ACTION) {
                moveFigure(event.getX(), event.getY());
            } else if (current_action == SELECT_ACTION){
                makeSelector(event.getX(), event.getY());
            } else if (current_action == SEG_ACTION){
                makeLine(event.getX(), event.getY());
            } break;
        case MotionEvent.ACTION_UP:
            if (current_action == DEFAULT_ACTION) {
                resetSelection();
            } else if (current_action == SELECT_ACTION) {
                cleanSelector();
            } else if (current_action == SEG_ACTION){
                cleanFigure();
            } break;
        case MotionEvent.ACTION_CANCEL:{break;}
        }
        return (true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Figure f : figures) {
            f.onDraw(canvas);
        }
        if (selector != null){
           selector.onDraw(canvas);
        }
    }

    public Figure getFigure(float x, float y) {
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

    public void moveFigure(float x, float y){
        if (touched != null) {
            anchor = touched.move((int) x, (int) y, anchor);
            invalidate();
        }
    }

    public void makeSelector(float x, float y){
        resetSelection();
        selector = new Selector(anchor, (int) x, (int) y);
        makeSelectetion();
        invalidate();
    }

    public void makeSelectetion(){
        for (Figure f : figures) {
            if (f.intersects(selector)) {
                selected.add(f);
                f.selected = true;
            }
        }
        invalidate();
    }

    public void resetSelection(){
        for(Figure f: figures){
            f.selected = false;
            selected = new ArrayList<>();
        }
        invalidate();
    }

    public  void makeAchor(float x, float y){
        anchor = new Point((int) x, (int) y);
    }

    public void makePoint(float x, float y){
        figures.add(new PointFigure((int) x, (int) y, point_margin));
        current_action = DEFAULT_ACTION;
        invalidate();
    }

    public void makeIso(){
        if (selected == null || selected.size() < 2)
            return;
        int sx = 0;
        int sy = 0;
        /*for(int i = 0; i < selected.size(); i++){
            if (selected.get(i) instanceof Intersection){
                selected.remove(i);
                i--;
            }
        }*/
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
        resetSelection();
        invalidate();
    }

    public void makeLine(float x, float y){
        if (currentFigure == null || currentFigure instanceof Segment) {
            figures.remove(currentFigure);
            currentFigure = new Segment(anchor.x, anchor.y, x, y, (double)seg_margin);
            figures.add(currentFigure);
            invalidate();
        }
    }

    public void cleanFigure(){
        currentFigure = null;
        current_action = DEFAULT_ACTION;
    }

    public void cleanSelector(){
        selector = null;
        current_action = DEFAULT_ACTION;
        invalidate();
    }

    public void undo(){
        figures.remove(figures.size() - 1);
        invalidate();
    }
}
