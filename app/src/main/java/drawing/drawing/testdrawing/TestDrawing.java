package drawing.drawing.testdrawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import drawing.drawing.model.Figure;
import drawing.drawing.model.PointFigure;
import drawing.drawing.model.Segment;

/**
 * Created by leo on 14/01/18.
 */

public class TestDrawing extends View{

    public interface MyCustomObjectListener {
        public void endingTest(int point_margin, int seg_margin);
    }

    private MyCustomObjectListener listener;

    public static final int POINT_TEST = 0;
    public static final int SEG_TEST = 1;
    public int CURRENT_TEST = -1;

    private ArrayList<Figure> figures;
    private PointFigure p;
    private Segment s;
    private Figure touched;
    private Point anchor;

    private int point_margin;
    private int seg_margin;

    public TestDrawing(Context context) {
        super(context);
        figures = new ArrayList<>();
        p = new PointFigure(200, 200, 0);
        figures.add(p);
        CURRENT_TEST = POINT_TEST;
        invalidate();
    }

    public void setCustomObjectListener(MyCustomObjectListener listener) {
        this.listener = listener;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                makeAchor(event.getX(), event.getY());
                touched = null;
                if (CURRENT_TEST == POINT_TEST) {
                    point_margin = Math.max(Math.abs(anchor.x - p.getPoints().get(0).x), Math.abs(anchor.y - p.getPoints().get(0).y));
                    if (point_margin <= 50) {
                        p.setMargin(point_margin);
                        touched = getFigure(anchor.x, anchor.y);
                    }
                } else if (CURRENT_TEST == SEG_TEST) {
                        seg_margin = Math.abs(anchor.y - s.getPoints().get(0).y);
                        if (seg_margin <= 25) {
                            s.setMargin(seg_margin);
                            touched = getFigure(anchor.x, anchor.y);
                        }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                moveFigure(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                if (CURRENT_TEST == POINT_TEST && touched != null){
                    CURRENT_TEST = SEG_TEST;
                    figures = new ArrayList<>();
                    s = new Segment(100,300, 600, 300, 0);
                    figures.add(s);
                    invalidate();

                } else if (CURRENT_TEST == SEG_TEST && touched != null){
                    if (listener != null){
                        listener.endingTest(point_margin, seg_margin);
                    }
                }
                break;
        }
        return (true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Figure f : figures) {
            f.onDraw(canvas);
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

    public  void makeAchor(float x, float y){
        anchor = new Point((int) x, (int) y);
    }
}
