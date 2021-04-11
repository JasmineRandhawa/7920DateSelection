package com.example.DateSelection;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


/* Wheel List View  for days and years lists*/
public class WheelList extends ListView implements AbsListView.OnScrollListener {

    //class fields
    private int wheelListItemHeight = 0;
    private WheelListListener wheelListener;
    private WheelListAdaptor wheelAdaptor;
    private boolean isInfiniteScrollingEnabled = true;
    private double wheelRadius = -1;
    private final int wheelScrollDuration = 50;
    private WheelListListener.ItemAllignment wheelListAlignment = WheelListListener.ItemAllignment.Left;

    //constructor
    public WheelList(Context context) {
        this(context, null);
    }

    public WheelList(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.listViewStyle);
    }

    public WheelList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setOnScrollListener(this);
        setClipChildren(false);
        setEnableInfiniteScrolling(true);
    }

    public static ArrayAdapter<String> GetDaysAdaptor(Context context) {
        ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(context, R.layout.wheel_list_item);
        for (int i = 0; i < 31; i++) {
            daysAdapter.add(String.format((i <= 8 ? "0" : "") + (1 + i)));
        }
        return daysAdapter;
    }

    public static ArrayAdapter<String> GetYearsAdaptor(Context context) {
        ArrayAdapter<String> yearAdaptor = new ArrayAdapter<String>(context, R.layout.wheel_list_item);
        for (int i = 0; i < 146; i++) {
            yearAdaptor.add(String.format(""+ (1900+i)));
        }
        return yearAdaptor;
    }

    //get set methods
    public void setWheelListAdaptor(ListAdapter adapter) {
        wheelAdaptor = new WheelListAdaptor(adapter);
        wheelAdaptor.setEnableInfiniteScrolling(isInfiniteScrollingEnabled);
        super.setAdapter(wheelAdaptor);
    }

    public void setWheelListListener(WheelListListener circularListViewListener) {
        this.wheelListener = circularListViewListener;
    }

    public void setEnableInfiniteScrolling(boolean enableInfiniteScrolling) {
        isInfiniteScrollingEnabled = enableInfiniteScrolling;
        if (wheelAdaptor != null) {
            wheelAdaptor.setEnableInfiniteScrolling(enableInfiniteScrolling);
        }
        if (isInfiniteScrollingEnabled) {
            setHorizontalScrollBarEnabled(false);
            setVerticalScrollBarEnabled(false);
        }
    }

    public void setWheelListAlignment(
            WheelListListener.ItemAllignment listAlignment) {
        if (wheelListAlignment != listAlignment) {
            wheelListAlignment = listAlignment;
            requestLayout();
        }
    }

    public void setWheelListRadius(double radius) {
        if (this.wheelRadius != radius) {
            this.wheelRadius = radius;
            requestLayout();
        }
    }

    public int getWheelListItemHeight() {
        if (wheelListItemHeight == 0) {
            View child = getChildAt(0);
            if (child != null) {
                wheelListItemHeight = child.getHeight();
            }
        }
        return wheelListItemHeight;
    }


    @TargetApi(Build.VERSION_CODES.FROYO)
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (isInfiniteScrollingEnabled) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                            smoothScrollBy(wheelListItemHeight, wheelScrollDuration);
                            return true;
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                            smoothScrollBy(-wheelListItemHeight, wheelScrollDuration);
                            return true;
                        }
                        break;
                    default:
                        break;

                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            if (!isInTouchMode()) {
                moveSelectedItemToCenter(getCentralPosition());
            }
        }
    }

    @Override
    public void onScroll(AbsListView wheelList, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        if (!isInfiniteScrollingEnabled) {
            return;
        }

        View itemView = this.getChildAt(0);
        if (itemView == null) {
            return;
        }
        int realTotalItemCount = wheelAdaptor.getRealCount();
        if (realTotalItemCount == 0) {
            return;
        }


        if (wheelListItemHeight == 0) {
            wheelListItemHeight = itemView.getHeight();
        }

        if (firstVisibleItem == 0) {
            // scroll one unit
            this.setSelectionFromTop(realTotalItemCount, itemView.getTop());
        }

        if (totalItemCount == firstVisibleItem + visibleItemCount) {
            // back one unit
            this.setSelectionFromTop(firstVisibleItem - realTotalItemCount,
                    itemView.getTop());
        }


        if (wheelListAlignment != WheelListListener.ItemAllignment.None) {

            double viewHalfHeight = wheelList.getHeight() / 2.0f;

            double vRadius = wheelList.getHeight();
            double hRadius = wheelList.getWidth();

            double yRadius = (wheelList.getHeight() + wheelListItemHeight) / 2.0f;
            double xRadius = (vRadius < hRadius) ? vRadius : hRadius;
            if (wheelRadius > 0) {
                xRadius = wheelRadius;
            }

            for (int i = 0; i < visibleItemCount; i++) {
                itemView = this.getChildAt(i);
                if (itemView != null) {
                    double y = Math.abs(viewHalfHeight - (itemView.getTop() + (itemView.getHeight() / 2.0f)));
                    y = Math.min(y, yRadius);
                    double angle = Math.asin(y / yRadius);
                    double x = xRadius * Math.cos(angle);

                    if (wheelListAlignment == WheelListListener.ItemAllignment.Left) {
                        x -= xRadius;
                        //itemView.setLayoutParams(new LayoutParams(90, itemView.getHeight()));
                        View temp = itemView;
                        itemView.post(new Runnable() {
                            @Override public void run() {
                                temp.setLayoutParams(new LayoutParams(100, temp.getHeight()));
                                ((TextView)temp).setTextSize(TypedValue.COMPLEX_UNIT_PX,40);
                            }
                        });
                        int itemWidth =itemView.getWidth();
                        int xPos=(int)(itemWidth-x);
                        itemView.setX(xPos);
                        itemView.scrollTo((int) xPos/70, -xPos/70);
                        itemView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                        itemView.setBackgroundResource(R.drawable.wheel_list_item_design);

                    } else {
                        x = xRadius / 2 - x;
                        View temp = itemView;
                        itemView.post(new Runnable() {
                            @Override public void run() {
                                temp.setLayoutParams(new LayoutParams(100, temp.getHeight()));
                                ((TextView)temp).setTextSize(TypedValue.COMPLEX_UNIT_PX,40);
                            }
                        });
                        int itemWidth =itemView.getWidth();
                        int xPos=(int)(itemWidth-x);
                        itemView.setX(xPos);
                        itemView.scrollTo((int)xPos/90, -xPos/90);
                        itemView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                        itemView.setBackgroundResource(R.drawable.wheel_list_item_design);
                    }


                }
            }
        }
        else {
            for (int i = 0; i < visibleItemCount; i++) {
                itemView = this.getChildAt(i);
                if (itemView != null) {
                    int xPos=(int)(90);
                    itemView.scrollTo(0, 0);
                    itemView.setX(xPos/60);
                    itemView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                    itemView.setBackgroundResource(R.drawable.selected_item_design);
                    ((TextView)itemView).setTextColor(getResources().getColor(R.color.Red));
                }
            }
        }


        if (wheelListener != null) {
            wheelListener.onWheelScrollFinished(this, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public int getCentralPosition() {
        double vCenterPos = getHeight() / 2.0f;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child != null) {
                if (child.getTop() <= vCenterPos
                        && child.getTop() + child.getHeight() >= vCenterPos) {
                    return getFirstVisiblePosition() + i;
                }
            }
        }
        return -1;
    }

    public View getListItemAtCenter() {
        int pos = getCentralPosition();
        if (pos != -1) {
            return getChildAt(pos - getFirstVisiblePosition() - 2);
        }
        return null;
    }

    public void scrollFirstListItemToCenter() {
        if (!isInfiniteScrollingEnabled) {
            return;
        }
        int realTotalItemCount = wheelAdaptor.getRealCount();
        if (realTotalItemCount > 0) {
            setSelectionFromTop(realTotalItemCount, getTopAreaOfCentralWheelItem());
        }
    }

    public int getTopAreaOfCentralWheelItem() {
        int selectedWheelItemHeight = getWheelListItemHeight();
        if (selectedWheelItemHeight > 0) {
            return getHeight() / 2 - selectedWheelItemHeight / 2;
        }
        return 0;
    }

    public void moveSelectedItemToCenter(int position) {
        if (!isInfiniteScrollingEnabled) {
            return;
        }

        int realTotalItemCount = wheelAdaptor.getRealCount();
        if (realTotalItemCount == 0) {
            return;
        }

        position = position % realTotalItemCount;
        int centralPosition = getCentralPosition() % realTotalItemCount;

        int y = getTopAreaOfCentralWheelItem();
        if (centralPosition == position) {
            View centralView = getListItemAtCenter();
            y = centralView.getTop();
        }
        setSelectionFromTop(position + realTotalItemCount, y);
    }

    class WheelListAdaptor implements ListAdapter {
        private int REPEAT_COUNT = 10;

        private boolean mEnableInfiniteScrolling = true;

        private ListAdapter listAdapter;

        //constructor
        public WheelListAdaptor(ListAdapter wheelListAdapter) {
            listAdapter = wheelListAdapter;
        }


        private void setEnableInfiniteScrolling(boolean enableInfiniteScrolling) {
            mEnableInfiniteScrolling = enableInfiniteScrolling;
        }

        public int getRealCount() {
            return listAdapter.getCount();
        }

        public int positionToIndex(int position) {
            int count = listAdapter.getCount();
            return (count == 0) ? 0 : position % count;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            listAdapter.registerDataSetObserver(observer);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            listAdapter.unregisterDataSetObserver(observer);
        }

        @Override
        public int getCount() {
            int count = listAdapter.getCount();
            return (mEnableInfiniteScrolling) ? count * REPEAT_COUNT : count;
        }

        @Override
        public Object getItem(int position) {
            return listAdapter.getItem(this.positionToIndex(position));
        }

        @Override
        public long getItemId(int position) {
            return listAdapter.getItemId(this.positionToIndex(position));
        }

        @Override
        public boolean hasStableIds() {
            return listAdapter.hasStableIds();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return listAdapter.getView(this.positionToIndex(position), convertView, parent);
        }

        @Override
        public int getItemViewType(int position) {
            return listAdapter.getItemViewType(this.positionToIndex(position));
        }

        @Override
        public int getViewTypeCount() {
            return listAdapter.getViewTypeCount();
        }


        @Override
        public boolean isEmpty() {
            return listAdapter.isEmpty();
        }

        @Override
        public boolean areAllItemsEnabled() {
            return listAdapter.areAllItemsEnabled();
        }

        @Override
        public boolean isEnabled(int position) {
            return listAdapter.isEnabled(this.positionToIndex(position));
        }
    }
}
