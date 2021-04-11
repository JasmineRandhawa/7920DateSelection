package com.example.DateSelection;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

public class MonthList extends ViewGroup {
    private OnClickListener onClickListener = null;
    private OnSelectListener onSelectListener = null;
    private OnScrollListener onScrollListener = null;

    public enum ItemDirection {
        EAST(0), SOUTH(90), WEST(180), NORTH(270);
        private int angle;
        ItemDirection(int angle) {
            this.angle = angle;
        }
        public int getAngle() {
            return angle;
        }
    }
    private int circleWidth, circleHeight;
    private float circleRadius = -1;
    private int itemWidth = 0,itemHeight = 0;
    private GestureDetector detector;
    private boolean[] quad;
    private int circleSpeed = 25;
    private float circleAngle = 90;
    private ItemDirection itemDirection = ItemDirection.NORTH;
    private boolean isScrolling = true;
    private double touchDegree;
    private boolean isMoving = false;
    private View selectedMonth = null;
    private ObjectAnimator anim;
    private String monthSelected ="";
    private MonthList monthList;

    public MonthList(Context context) {
        this(context, null);
    }

    public MonthList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        detector = new GestureDetector(getContext(),
                new GestureListener());
        quad = new boolean[]{false, false, false, false, false};

        if (attrs != null) { ;
            for (ItemDirection pos : ItemDirection.values()) {
                if (pos.getAngle() == circleAngle) {
                    itemDirection = pos;
                    break;
                }
            }
        }
    }


    //get set methods
    public void setAngle(float angle) {
        this.circleAngle = angle % 360;
        setItemPositions();
    }

    public void setCircleRadius(float radius) {
        this.circleRadius = radius;
        setItemPositions();
    }

    public void setFirstItemDirection(ItemDirection itemDir) {
        this.itemDirection = itemDir;
        if (selectedMonth != null) {
            if (isScrolling)
                scrollViewToCenter(selectedMonth);
            else
                setAngle(itemDirection.getAngle());
        }
    }

    public View getSelectedItem() {
        if (selectedMonth == null)  selectedMonth = getChildAt(0);
        return selectedMonth;
    }

    private double getAngle(double xTouch, double yTouch) {
        double x = xTouch - (circleWidth / 2d);
        double y = circleHeight - yTouch - (circleHeight / 2d);
        switch (getPositionQuadrant(x, y)) {
            case 1:
                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 2:
            case 3:
                return 180 - (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
            case 4:
                return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            default:
                return 0;
        }
    }

    private static int getPositionQuadrant(double x, double y) {
        if (x >= 0)  return y >= 0 ? 1 : 4;
        else  return y >= 0 ? 2 : 3;
    }

    public void scrollViewToCenter(View view) {
        if (isScrolling) {
            float initialAngle = view.getTag() != null ? (Float) view.getTag() : 0;
            float finalAngle = itemDirection.getAngle() - initialAngle;
            if (finalAngle < 0) finalAngle += 360;
            if (finalAngle > 180)  finalAngle = -1 * (360 - finalAngle);
            startAnimation(circleAngle + finalAngle, 7500L / circleSpeed);
        }
    }

    private void scrollItems(float degrees) {
        circleAngle += degrees;
        setItemPositions();
    }

    private void setItemPositions() {
        int left, top, childWidth, childHeight, childCount = getChildCount();
        float angleDelay = 360.0f / childCount;
        float halfAngle = angleDelay / 2;
        float localAngle = circleAngle;
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;
            if (localAngle > 360)  localAngle -= 360;
            else if (localAngle < 0) localAngle += 360;

            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            left = Math.round((float) (((circleWidth / 2.0) - childWidth / 2.0) + circleRadius
                    * Math.cos(Math.toRadians(localAngle))));
            top = Math.round((float) (((circleHeight / 2.0) - childHeight / 2.0) + circleRadius
                    * Math.sin(Math.toRadians(localAngle))));
            child.setTag(localAngle);
            float distance = Math.abs(localAngle - itemDirection.getAngle());
            boolean isFirstItem = distance <= halfAngle || distance >= (360 - halfAngle);
            if (isFirstItem && selectedMonth != child) {
                selectedMonth = child;
                if (onSelectListener != null && isScrolling)
                    onSelectListener.onMonthSelect(child);
            }
            child.layout(left, top, left + childWidth, top + childHeight);
            localAngle += angleDelay;
        }
    }

    private void startAnimation(float toAngle, long time) {
        if (anim != null && anim.isRunning() || Math.abs(circleAngle - toAngle) < 1) return;
        anim = ObjectAnimator.ofFloat(MonthList.this, "angle", circleAngle, toAngle);
        anim.setDuration(time);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.addListener(new Animator.AnimatorListener() {
            private boolean wasCanceled = false;
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationEnd(Animator animation) {
                if (wasCanceled) return;
                View view = getSelectedItem();
                if(onScrollListener != null )
                onScrollListener.onScrollEnd(view);
            }
            @Override
            public void onAnimationCancel(Animator animation) { wasCanceled = true;}
        });
        anim.start();
    }

    private void haltAnimation() {
        if (anim != null && anim.isRunning()) {
            anim.cancel();
            anim = null;
        }
    }

    private float getCenterDegree(float angle) {
        if (getChildCount() == 0) return angle;
        float localAngle = angle % 360;
        if (localAngle < 0)
            localAngle = 360 + localAngle;
        for (float i = itemDirection.getAngle(); i < itemDirection.getAngle() + 360; i += 360 / getChildCount()) {
            if (Math.abs(localAngle - i % 360) < 360 / getChildCount())
                angle -= localAngle - i % 360;
            break;
        }
        return angle;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void updateMonthListItemBackgroundWhileRotating() {
        for (int i = 0; i < monthList.getChildCount(); i++) {
            TextView monthListItem = (TextView) monthList.getChildAt(i);
            if (monthListItem != null) {
                monthListItem.setBackgroundResource(R.drawable.month);
                monthListItem.setTextColor(getResources().getColor(R.color.White));
                monthListItem.setElevation(2.0f);
                ((TextView) monthListItem).setHeight(90);
                ((TextView) monthListItem).setPadding(0,10,0,0);
                ((TextView) monthListItem).setWidth(130);
                ((TextView) monthListItem).setTextSize(25);
            }
        }
    }

    //events
    @Override
    protected void onMeasure(int w, int h) {
        itemWidth = 0;
        itemHeight = 0;
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(w), MeasureSpec.AT_MOST);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(w), MeasureSpec.AT_MOST);

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            measureChild(child, childWidthMeasureSpec, childHeightMeasureSpec);
            itemWidth = Math.max(itemHeight, child.getMeasuredWidth());
            itemWidth = Math.max(itemHeight, child.getMeasuredHeight());
        }
        int width;
        int height;
        if (MeasureSpec.getMode(w) == MeasureSpec.EXACTLY)
            width = MeasureSpec.getSize(w);
        else if (MeasureSpec.getMode(w) == MeasureSpec.AT_MOST)
            width = Math.min(MeasureSpec.getSize(w), MeasureSpec.getSize(h));
        else
            width = itemWidth * 3;

        if ( MeasureSpec.getMode(h) == MeasureSpec.EXACTLY)
            height = MeasureSpec.getSize(h);
        else if ( MeasureSpec.getMode(h) == MeasureSpec.AT_MOST)
            height = Math.min(MeasureSpec.getSize(h), MeasureSpec.getSize(w));
        else
            height = itemHeight * 3;
        setMeasuredDimension(resolveSize(width, w), resolveSize(height, h));
    }

    @Override
    protected void onLayout(boolean isLayoutChanged, int left, int top, int right, int bottom) {
        if(circleRadius < 0)
            circleRadius = ((right - left) <= (bottom - top)) ? (right - left) / 3
                    : (bottom - top) / 3;
        circleHeight = getHeight()*2;
        circleWidth = getWidth();
        setItemPositions();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            detector.onTouchEvent(event);
            if (isScrolling) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        for (int i = 0; i < quad.length; i++)
                            quad[i] = false;
                        haltAnimation();
                        touchDegree = getAngle(event.getX(), event.getY());
                        isMoving = false;
                        updateMonthListItemBackgroundWhileRotating();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        double angle = getAngle(event.getX(),event.getY());
                        scrollItems((float) (touchDegree - angle));
                        touchDegree = angle;
                        isMoving = true;
                        updateMonthListItemBackgroundWhileRotating();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isMoving) scrollViewToCenter(selectedMonth);
                        break;
                    default:
                        break;
                }
            }
            quad[getPositionQuadrant(event.getX()
                    - (circleWidth / 2), circleHeight - event.getY()
                    - (circleHeight / 2))] = true;
            return true;
        }
        return false;
    }

    //Listeners
    private class GestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent me1, MotionEvent me2, float speedX, float speedY) {
            if (!isScrolling) {
                return false;
            }
            int quadFirst = getPositionQuadrant(me1.getX() - (circleWidth / 2),
                    circleHeight - me1.getY() - (circleHeight / 2));
            int quadSec = getPositionQuadrant(me2.getX() - (circleWidth / 2),
                    circleHeight - me2.getY() - (circleHeight / 2));

            if ((quadFirst == 2 && quadSec== 2 && Math.abs(speedX) < Math
                    .abs(speedY))
                    || (quadFirst == 3 && quadSec == 3)
                    || (quadFirst == 1 && quadSec == 3)
                    || (quadFirst == 4 && quadSec== 4 && Math.abs(speedX) > Math
                    .abs(speedY))
                    || ((quadFirst == 2 && quadSec == 3) || (quadSec == 3 && quadSec == 2))
                    || ((quadFirst == 3 && quadSec == 4) || (quadSec == 4 && quadSec == 3))
                    || (quadFirst == 2 && quadSec == 4 && quad[3])
                    || (quadFirst == 4 && quadSec== 2 && quad[3])) {
                startAnimation(
                        getCenterDegree(circleAngle - (speedX + speedY) / 25),
                        25000L / circleSpeed);
            } else {
                startAnimation(
                        getCenterDegree(circleAngle + (speedX + speedY) / 25),
                        25000L / circleSpeed);
            }
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            View view = null;
            int pos = -1 ;
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i).getLeft() < event.getX() && getChildAt(i).getRight() > event.getX()
                        & getChildAt(i).getTop() < event.getY() && getChildAt(i).getBottom() > event.getY()) {
                    pos = i;
                    break;
                }
            }
            if (pos >= 0) {
                view = getChildAt(pos);
                view.setPressed(true);
            }
            if (view != null) {
                if (onScrollListener != null && selectedMonth == view)
                     onClickListener.onMonthClick(view);
                else {
                    scrollViewToCenter(view);
                    if (!isScrolling) {
                        if (!isScrolling && onSelectListener != null) {
                            onSelectListener.onMonthSelect(view);
                            onClickListener.onMonthClick(view);
                        }
                    }
                }
                return true;
            }
            return super.onSingleTapUp(event);
        }
    }

    public interface OnClickListener {
        void onMonthClick(View view);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnSelectListener {
        void onMonthSelect(View view);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener, MonthList monthListView) {
        this.onSelectListener = onSelectListener;
        this.monthList = monthListView;
    }


    public interface OnScrollListener {
        void onScrollEnd(View view);
    }

    public void setOnScrollListener(
            OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }
}