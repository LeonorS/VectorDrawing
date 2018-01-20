package drawing.drawing.model;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by leo on 05/12/17.
 */

public class Selector {

    protected Rect rectangle;

    public Selector(Point anchor, int x, int y){
        rectangle = makeRectangle(anchor, x, y);
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

    public Rect getRectangle(){
        return rectangle;
    }
}
