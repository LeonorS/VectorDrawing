package drawing.drawing.vectordrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import drawing.drawing.model.Figure;
import drawing.drawing.model.Intersection;
import drawing.drawing.model.Iso;
import drawing.drawing.model.Line;
import drawing.drawing.model.Model;
import drawing.drawing.model.PointFigure;
import drawing.drawing.model.Selector;
import drawing.drawing.model.StraightLine;

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
    public static final     int     INTER_ACTION          = 6;
    public                  int     current_action      = DEFAULT_ACTION;

    private Figure touched = null;
    private Selector selector = null;
    private Point  anchor;

    private ArrayList<Figure> selected;

    private Model model;
    private Designer designer;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawingCacheEnabled(true);
        selected = new ArrayList<>();
        model = new Model();
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
                if (current_action == POINT_ACTION) {
                    makeFigure(event.getX(), event.getY());
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
                    invalidate();
                }
                else if (current_action == SEG_ACTION || current_action == LINE_ACTION){
                    makeFigure(event.getX(), event.getY());
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
    public void onDraw(Canvas canvas){

        super.onDraw(canvas);

        ArrayList<Figure> figures = model.getFigures();

        for (Figure f : figures) {

            if (f instanceof Iso){
                Iso iso = (Iso) f;
                designer.onDrawIso(canvas, iso, model.findFiguesById(iso.getIdsLinked()));
            }
            else if (f instanceof StraightLine || f instanceof Line){
                designer.onDrawSegment(canvas, (Line) f);
            }
            else if (f instanceof Intersection){
                designer.onDrawInter(canvas, (Intersection) f);
            }
            else if (f instanceof PointFigure){
                designer.onDrawPointFigure(canvas, (PointFigure) f);
            }
        }

        if (selector != null){
            designer.onDrawSelector(canvas, selector);
        }
    }

    private void makeFigure(float x, float y){
        switch (current_action){
            case POINT_ACTION:
                model.makePoint(x, y);
                break;

            case SEG_ACTION:
                model.makeLine(current_action, x, y, anchor);
                break;

            case LINE_ACTION:
                model.makeLine(current_action, x, y, anchor);
                break;

            case ISO_ACTION:
                Log.d("CustomView", "makeFigure entry");
                model.makeIso(selected);
                current_action = DEFAULT_ACTION;
                Log.d("CustomView", "makeFigure exit");
                break;

//            case INTER_ACTION:
//                model.makeIntersection(selected);
//                current_action = DEFAULT_ACTION;
//                break;
        }
    }

    private void makeSelector(float x, float y){
        resetSelection();
        selector = new Selector(anchor, (int) x, (int) y);
        selected = model.selectFigure(selector);
    }

    private  void makeAchor(float x, float y){
        anchor = new Point((int) x, (int) y);
    }

    private void cleanFigure(){
        model.cleanCurrentFigure();
    }

    private void cleanSelector(){
        current_action = DEFAULT_ACTION;
        selector = null;
        invalidate();
    }

    protected void resetSelection(){
        model.uncheckFigure();
        selected = new ArrayList<>();
        invalidate();
    }

    protected void makeIso(){
        Log.d("CustomView", "makeIso entry");
        makeFigure(0,0);
        resetSelection();
        invalidate();
        Log.d("CustomView", "makeIso exit");
    }

    protected void makeInter(){
        makeFigure( 0,0);
        resetSelection();
        invalidate();
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
        invalidate();
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
        invalidate();
    }

    public Bitmap getPreview() {
        return Bitmap.createBitmap(getDrawingCache());
    }
}
