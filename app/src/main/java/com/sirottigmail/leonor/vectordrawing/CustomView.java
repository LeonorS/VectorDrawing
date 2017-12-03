package com.sirottigmail.leonor.vectordrawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 03/12/17.
 */

class CustomView extends View {

    public static int DEFAULT_ACTION     = -1;
    public static int POINT_ACTION       = 1;
    public int current_action  = DEFAULT_ACTION;

    protected List<Point> figures;

    private float offsetX;
    private float offsetY;
    private Paint myPaint;
    private Paint backgroundPaint;
    private int widthPoint = 20;
    private int touched = -1;

    public CustomView(Context context) {
        super(context);

        figures = new ArrayList<>();

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLUE);

        myPaint = new Paint();
        myPaint.setColor(Color.RED);
        myPaint.setAntiAlias(true);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        offsetX = event.getX();
        offsetY = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (current_action == POINT_ACTION) {
                    touched = -1;
                    figures.add(new Point((int) offsetX, (int) offsetY));
                    current_action = DEFAULT_ACTION;
                    break;
                }
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touched = getTouchedPoint(offsetX, offsetY);
                if (touched != -1) {
                    figures.remove(touched);
                    figures.add(new Point((int) event.getX(), (int) event.getY()));
                    break;
                }

                break;
        }
        return (true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Point p : figures) {
            canvas.drawCircle(p.x, p.y, widthPoint, myPaint);
        }
        invalidate();
    }

    public int getTouchedPoint(float xTouch, float yTouch) {
        for (int i = 0; i < figures.size(); i++) {
            Point p = figures.get(i);
            if ((p.x - xTouch) * (p.x - xTouch) + (p.y - yTouch) * (p.y - yTouch) <= widthPoint * widthPoint) {
                return i;
            }
        }
        return -1;
    }
}
