package drawing.drawing.vectordrawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import drawing.drawing.model.Figure;
import drawing.drawing.model.Iso;
import drawing.drawing.model.Line;
import drawing.drawing.model.Model;
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
    public  static final    int     LINE_ACTION         = 4;
    public static final     int     ISO_ACTION          = 5;
    public                  int     current_action      = DEFAULT_ACTION;

    private Figure touched = null;
    private Selector selector = null;
    private Point  anchor;

    private ArrayList<Figure> selected;

    private Model model;
    private Designer designer;

    public CustomView(Context context, int point_margin, int seg_margin, double width, double height) {
        super(context);
        selected = new ArrayList<>();
        model = new Model(width, height, point_margin, seg_margin);
        designer = new Designer();
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                touched = null;
                makeAchor(event.getX(), event.getY());

                if (current_action == DEFAULT_ACTION) {
                    touched = model.findFigure(event.getX(), event.getY());
                }
                if (current_action == POINT_ACTION || (current_action == DEFAULT_ACTION && touched == null)) {
                    makeFigure(POINT_ACTION, event.getX(), event.getY(), null, null);
                    current_action = DEFAULT_ACTION;
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (current_action == DEFAULT_ACTION) {
                    anchor = model.moveFigure(event.getX(), event.getY(), touched, anchor);
                    invalidate();
                }
                else if (current_action == SELECT_ACTION){
                    makeSelector(event.getX(), event.getY());
                }
                else if (current_action == SEG_ACTION || current_action == LINE_ACTION){
                    makeFigure(current_action, event.getX(), event.getY(), null, anchor);
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:

                if (current_action == DEFAULT_ACTION) {
                    resetSelection();
                }
                else if (current_action == SELECT_ACTION) {
                    cleanSelector();
                }
                else if (current_action == SEG_ACTION  || current_action == LINE_ACTION){
                    cleanFigure();
                }
                break;
        }

        return (true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ArrayList<Figure> figures = model.getFigures();
        designer.onDraw(canvas, figures, selector);
    }

    public void makeFigure(int action, float x, float y, ArrayList<Figure> selected, Point anchor){
        switch (action){
            case CustomView.POINT_ACTION:
                model.makePoint(x, y);
                break;

            case CustomView.SEG_ACTION:
                model.makeLine(action, x, y, anchor);
                break;

            case CustomView.LINE_ACTION:
                model.makeLine(action, x, y, anchor);
                break;

            case CustomView.ISO_ACTION:
                model.makeIso(selected);
                break;
        }
    }

    private void makeSelector(float x, float y){
        resetSelection();
        selector = new Selector(anchor, (int) x, (int) y);
        selected = model.selectFigure(selector);
        invalidate();
    }

    private  void makeAchor(float x, float y){
        anchor = new Point((int) x, (int) y);
    }

    private void cleanFigure(){
        model.cleanCurrentFigure();
        current_action = DEFAULT_ACTION;
    }

    private void cleanSelector(){
        selector = null;
        current_action = DEFAULT_ACTION;
        invalidate();
    }

    protected void resetSelection(){
        model.uncheckFigure();
        selected = new ArrayList<>();
        invalidate();
    }

    protected void makeIso(){
        makeFigure(ISO_ACTION, 0,0, selected, null);
        resetSelection();
        invalidate();
        current_action = DEFAULT_ACTION;
    }

    protected void undo(){
        if(model.sizeFigures() == 0){
            Toast.makeText(this.getContext(), "Nothing to cancel.", Toast.LENGTH_LONG).show();
            return;
        }
        model.removeLastFigure();
        invalidate();
    }

    protected void redo(){
        if (model.sizeCanceled() == 0){
            Toast.makeText(this.getContext(), "No figure available.", Toast.LENGTH_LONG).show();
            return;
        }
        model.addLastCanceled();
        invalidate();
    }

    protected void reset(){
        current_action = DEFAULT_ACTION;
        resetSelection();
        model.reset();
    }
}
