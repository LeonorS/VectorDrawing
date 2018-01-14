package drawing.drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Created by leo on 05/12/17.
 */

public class Selector {

    protected static    Paint   myPaint;
    protected           Rect    rectangle;

    public Selector(Point anchor, int x, int y){
        myPaint     = new Paint();
        rectangle = makeRectangle(anchor, x, y);
    }

    public void onDraw(Canvas canvas) {
        myPaint.setColor(Color.RED);
        myPaint.setStrokeWidth(3);
        myPaint.setAlpha(20);
        myPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rectangle, myPaint);
        myPaint.setColor(Color.RED);
        myPaint.setAlpha(100);
        myPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectangle, myPaint);
    }

    public boolean contains(float x, float y) {
        return rectangle.contains((int) x, (int) y);
    }

    private Rect makeRectangle(Point anchor, int x, int y){
        Rect r;
        int x1 = Math.min(anchor.x, x);
        int y1 = Math.min(anchor.y, y);
        int x2 = Math.max(anchor.x, x);
        int y2 = Math.max(anchor.y, y);
        r =  new Rect(x1, y1, x2, y2);
        return r;
    }
}
