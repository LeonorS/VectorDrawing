package drawing.drawing.testdrawing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import drawing.drawing.R;
import drawing.drawing.model.Figure;
import drawing.drawing.model.PointFigure;
import drawing.drawing.model.Segment;

/**
 * Created by leo on 14/01/18.
 */

public class TestDrawing extends View{

    public static final int POINT_TEST = 0;
    public static final int SEG_TEST = 1;
    public int CURRENT_TEST = -1;

    private ArrayList<Figure> figures;
    private PointFigure p;
    private Segment s;
    private Figure touched;
    protected Point anchor;

    public TestDrawing(Context context) {
        super(context);
        figures = new ArrayList<>();
        p = new PointFigure(200, 200);
        figures.add(p);
        CURRENT_TEST = POINT_TEST;
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                makeAchor(event.getX(), event.getY());
                //Log.d("DEBUG", "event : " + anchor.x + "; " + anchor.y);

                touched = null;

                if (CURRENT_TEST == POINT_TEST) {
                    int value = Math.max(Math.abs(anchor.x - p.getPoints().get(0).x), Math.abs(anchor.y - p.getPoints().get(0).y));
                    Log.d("DEBUG", "value : " + value);

                    if (value <= 50) {

                        p.setMargin(value);

                        touched = getFigure(anchor.x, anchor.y);
                        if (touched != null) {
                            Log.d("DEBUG", "touché");
                        } else {
                            Log.d("DEBUG", "null");
                        }
                    }

                } else {
                    if (CURRENT_TEST == SEG_TEST) {


                        int value = Math.abs(anchor.y - s.getPoints().get(0).y);

                        Log.d("DEBUG", "value : " + value);

                        if (value <= 25) {
                            s.setMargin(value);

                            p.setMargin(value);

                            touched = getFigure(anchor.x, anchor.y);
                            if (touched != null) {
                                Log.d("DEBUG", "touché");
                            } else {
                                Log.d("DEBUG", "null");
                            }
                        }
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
                    s = new Segment(100,300, 600, 300);
                    figures.add(s);
                    invalidate();
                    break;
                }

                if (CURRENT_TEST == SEG_TEST && touched != null){

                    this.setVisibility(View.GONE);

                }

            case MotionEvent.ACTION_CANCEL:
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
