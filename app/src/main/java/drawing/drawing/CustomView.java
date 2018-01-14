package drawing.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by leo on 03/12/17.
 */

class CustomView extends View {

    public  static final    int     DEFAULT_ACTION      = -1;
    public  static final    int     POINT_ACTION        = 1;
    public  static final    int     SELECT_ACTION       = 2;
    public  static final    int     SEG_ACTION          = 3;
    public                  int     current_action      = DEFAULT_ACTION;
    private     Figure              touched             = null;
    private     Selector            selector            = null;
    private     ArrayList<Figure>   figures, selected;
    protected   Point               anchor;
    private     Figure              currentFigure;

    public CustomView(Context context) {
        super(context);
        figures     = new ArrayList<>();
        selected    = new ArrayList<>();


        figures.add(new Segment(100, 1000, 300, 1000));
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            touched = null;
            makeAchor(event);
            if (current_action == DEFAULT_ACTION) {
                touched = getFigure(event);
            } if (current_action == POINT_ACTION || (current_action == DEFAULT_ACTION && touched == null)) {
                makePoint(event);
            } break;
        case MotionEvent.ACTION_MOVE:
            if (current_action == DEFAULT_ACTION) {
                moveFigure(event);
            } else if (current_action == SELECT_ACTION){
                makeSelector(event);
            } else if (current_action == SEG_ACTION){
                makeLine(event);
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

    public Figure getFigure(MotionEvent event) {
        Figure f = null;
        for (int i = 0; i < figures.size(); i++) {
            if (figures.get(i).contains(event.getX(), event.getY())) {
                f = figures.get(i);
                f.selected = true;
                break;
            }
        }
        return f;
    }

    public void moveFigure(MotionEvent event){
        if (touched != null) {
            anchor = touched.move((int) event.getX(), (int) event.getY(), anchor);
            invalidate();
        }
    }

    public void makeSelector(MotionEvent event){
        resetSelection();
        selector = new Selector(anchor, (int) event.getX(), (int) event.getY());
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

    public  void makeAchor(MotionEvent event){
        anchor = new Point((int) event.getX(), (int) event.getY());
    }

    public void makePoint(MotionEvent event){
        figures.add(new PointFigure((int) event.getX(), (int) event.getY()));
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
        figures.add(new Iso(sx, sy, selected));
        resetSelection();
        invalidate();
    }

    public void makeLine(MotionEvent event){
        if (currentFigure == null || currentFigure instanceof Segment) {
            figures.remove(currentFigure);
            currentFigure = new Segment(anchor.x, anchor.y, event.getX(), event.getY());
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

    public ArrayList<Figure> getFigures(){
        return figures;
    }
}
