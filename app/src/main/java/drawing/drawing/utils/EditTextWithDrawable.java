package drawing.drawing.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatEditText;
import android.view.MotionEvent;
import android.view.View;

import drawing.drawing.R;

import static drawing.drawing.utils.EditTextWithDrawable.Position.DRAWABLE_BOTTOM;
import static drawing.drawing.utils.EditTextWithDrawable.Position.DRAWABLE_LEFT;
import static drawing.drawing.utils.EditTextWithDrawable.Position.DRAWABLE_RIGHT;
import static drawing.drawing.utils.EditTextWithDrawable.Position.DRAWABLE_TOP;

/**
 * Created by leo on 19/01/18.
 */

public class EditTextWithDrawable extends AppCompatEditText {
    private boolean leftDrawableToggleable;
    private boolean rightDrawableToggleable;
    private boolean topDrawableToggleable;
    private boolean bottomDrawableToggleable;
    private boolean onLeftBis;
    private boolean onRightBis;
    private boolean onTopBis;
    private boolean onBottomBis;
    private Drawable drawableLeft;
    private Drawable drawableRight;
    private Drawable drawableTop;
    private Drawable drawableBottom;
    private Drawable drawableLeftBis;
    private Drawable drawableRightBis;
    private Drawable drawableTopBis;
    private Drawable drawableBottomBis;
    private OnDrawableClickListener leftListener;
    private OnDrawableClickListener rightListener;
    private OnDrawableClickListener topListener;
    private OnDrawableClickListener bottomListener;
    public enum Position{ DRAWABLE_LEFT, DRAWABLE_TOP, DRAWABLE_RIGHT, DRAWABLE_BOTTOM}

    public interface OnDrawableClickListener {
        void onClick(View v, boolean bis);
    }

    public EditTextWithDrawable(Context context) {
        super(context);
        retrieveAttributes(context, null);
    }

    public EditTextWithDrawable(Context context, AttributeSet attrs) {
        super(context, attrs);
        retrieveAttributes(context, attrs);
    }

    public EditTextWithDrawable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        retrieveAttributes(context, attrs);
    }

    private void retrieveAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.EditTextWithDrawable,
                0, 0);

        try {
            leftDrawableToggleable = a.getBoolean(R.styleable.EditTextWithDrawable_leftDrawableToggleable, false);
            rightDrawableToggleable = a.getBoolean(R.styleable.EditTextWithDrawable_rightDrawableToggleable, false);
            topDrawableToggleable = a.getBoolean(R.styleable.EditTextWithDrawable_topDrawableToggleable, false);
            bottomDrawableToggleable = a.getBoolean(R.styleable.EditTextWithDrawable_bottomDrawableToggleable, false);

            //int resId;
            drawableLeftBis = a.getDrawable(R.styleable.EditTextWithDrawable_drawableLeftBis);
            drawableRightBis = a.getDrawable(R.styleable.EditTextWithDrawable_drawableRightBis);
            drawableTopBis = a.getDrawable(R.styleable.EditTextWithDrawable_drawableTopBis);
            drawableBottomBis = a.getDrawable(R.styleable.EditTextWithDrawable_drawableBottomBis);
            final Drawable[] drawables = getCompoundDrawables();
            drawableLeft = drawables[DRAWABLE_LEFT.ordinal()];
            drawableRight = drawables[DRAWABLE_RIGHT.ordinal()];
            drawableTop = drawables[DRAWABLE_TOP.ordinal()];
            drawableBottom = drawables[DRAWABLE_BOTTOM.ordinal()];
        } finally {
            a.recycle();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_UP) {
            final Drawable[] drawables = getCompoundDrawables();

            if(drawables[DRAWABLE_LEFT.ordinal()] != null
                    && event.getRawX() <= (getLeft() + drawables[DRAWABLE_LEFT.ordinal()].getBounds().width())) {
                onLeftBis = !onLeftBis;
                if (leftDrawableToggleable && drawableLeftBis != null) {
                    updateDrawable();
                }

                if (leftListener != null) {
                    leftListener.onClick(this, onLeftBis);
                }
                return false;
            } else if(drawables[DRAWABLE_RIGHT.ordinal()] != null
                    && event.getRawX() >= (getRight() - drawables[DRAWABLE_RIGHT.ordinal()].getBounds().width())) {
                onRightBis = !onRightBis;
                if (rightDrawableToggleable && drawableRightBis != null) {
                    updateDrawable();
                }

                if (rightListener != null) {
                    rightListener.onClick(this, onRightBis);
                }
                return false;
            } else if(drawables[DRAWABLE_TOP.ordinal()] != null
                    && event.getRawY() <= (getTop() + drawables[DRAWABLE_TOP.ordinal()].getBounds().height())) {
                onTopBis = !onTopBis;
                if (topDrawableToggleable && drawableTopBis != null) {
                    updateDrawable();
                }

                if (topListener != null) {
                    topListener.onClick(this, onTopBis);
                }
                return false;
            } else if(drawables[DRAWABLE_BOTTOM.ordinal()] != null
                    && event.getRawY() >= (getBottom() - drawables[DRAWABLE_BOTTOM.ordinal()].getBounds().width())) {
                onBottomBis = !onBottomBis;
                if (bottomDrawableToggleable && drawableBottomBis != null) {
                    updateDrawable();
                }

                if (bottomListener != null) {
                    bottomListener.onClick(this, onBottomBis);
                }
                return false;
            } else {
                return super.onTouchEvent(event);
            }
        } else {
            return super.onTouchEvent(event);
        }
    }

    public void setLeftDrawableListener(OnDrawableClickListener listener) {
        leftListener = listener;
    }

    public void setRightDrawableListener(OnDrawableClickListener listener) {
        rightListener = listener;
    }

    public void setTopDrawableListener(OnDrawableClickListener listener) {
        topListener = listener;
    }

    public void setBottomDrawableListener(OnDrawableClickListener listener) {
        bottomListener = listener;
    }

    private void updateDrawable() {
        final Drawable left = onLeftBis ? drawableLeftBis : drawableLeft;
        final Drawable right = onRightBis ? drawableRightBis : drawableRight;
        final Drawable top = onTopBis ? drawableTopBis : drawableTop;
        final Drawable bottom = onBottomBis ? drawableBottomBis : drawableBottom;
        setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        invalidate();
    }

}
