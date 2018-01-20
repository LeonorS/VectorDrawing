package drawing.drawing.model;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by leo on 06/12/17.
 */

public class Iso extends PointFigure {

    private final ArrayList<Integer> ids_linked;

    public Iso(int x, int y, int margin, ArrayList<Integer> figures) {
        super(x, y, margin);
        ids_linked = new ArrayList<>();
        ids_linked.addAll(figures);
    }

    @Override
    public Point move(int x, int y, Point anchor){

        super.move(x, y, anchor);
        return anchor;
    }

    public ArrayList<Integer> getIdsLinked(){
        return ids_linked;
    }
}
