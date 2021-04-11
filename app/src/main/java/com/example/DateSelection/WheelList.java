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
    private final int wheelListScrollDuration = 50;
    int wheelRadius = 0;
    private WheelListAdapter wheelListAdapter;
    private boolean isInfiniteScrollingEnabled = true;
    private int wheelListItemHeight = 0;
    private WheelListListener.ItemAllignment wheelListAlignment = WheelListListener.ItemAllignment.Left;
    private WheelListListener wheelListListener;

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
        setInfiniteScroll(true);
    }

    //Adapters
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
            yearAdaptor.add(String.format("" + (1900 + i)));
        }
        return yearAdaptor;
    }

    //get set methods
    public int getWheelListItemHeight() {
        return (wheelListItemHeight == 0 ? ((getChildAt(0) != null) ? (getChildAt(0)).getHeight() : wheelListItemHeight)
                : wheelListItemHeight);
    }

    public int getCenter() {
        for (int i = 0; i < getChildCount(); i++)
            if (getChildAt(i) != null && getChildAt(i).getTop() <= getHeight() / 2
                    && getChildAt(i).getTop() + getChildAt(i).getHeight() >= getHeight() / 2)
                return getFirstVisiblePosition() + i;
        return -1;
    }

    public View getCenterItem() {
        if (getCenter() != -1) {
            return getChildAt(getCenter() - getFirstVisiblePosition() - 2);
        }
        return null;
    }

    public void setWheelListAdaptor(ListAdapter listAdapter) {
        wheelListAdapter = new WheelListAdapter(listAdapter);
        wheelListAdapter.enableInfiniteScroll(isInfiniteScrollingEnabled);
        super.setAdapter(wheelListAdapter);
    }

    public void setWheelListListener(WheelListListener listViewListener) {
        this.wheelListListener = listViewListener;
    }

    public void setWheelListAlignment(WheelListListener.ItemAllignment listAlignment) {
        if (wheelListAlignment != listAlignment) {
            wheelListAlignment = listAlignment;
            requestLayout();
        }
    }

    public void setWheelListRadius(double radius) {
        if (this.wheelRadius != radius) {
            this.wheelRadius = (int) radius;
            requestLayout();
        }
    }

    public void setInfiniteScroll(boolean enableInfiniteScroll) {
        isInfiniteScrollingEnabled = enableInfiniteScroll;
        if (wheelListAdapter != null)
            wheelListAdapter.enableInfiniteScroll(enableInfiniteScroll);
        if (isInfiniteScrollingEnabled) {
            setVerticalScrollBarEnabled(false);
            setHorizontalScrollBarEnabled(false);
        }
    }

    public void scrollFirstItemToCenter() {
        if (!isInfiniteScrollingEnabled)
            return;
        int topHeight = 0;
        if (getWheelListItemHeight() > 0)
            topHeight = getHeight() / 2 - getWheelListItemHeight() / 2;
        if (wheelListAdapter.getItemCount() > 0)
            setSelectionFromTop(wheelListAdapter.getItemCount(), topHeight);
    }

    public void scrollSelectedItemToCenter(int index) {
        if (!isInfiniteScrollingEnabled || wheelListAdapter.getItemCount() == 0)
            return;
        index = index % wheelListAdapter.getItemCount();
        int topHeight = 0;
        if (getCenter() % wheelListAdapter.getItemCount() == index && getCenterItem() != null)
            topHeight = getCenterItem().getTop();
        if (getWheelListItemHeight() > 0)
            topHeight = getHeight() / 2 - getWheelListItemHeight() / 2;
        setSelectionFromTop(index + wheelListAdapter.getItemCount(), topHeight);
    }

    //events
    @TargetApi(Build.VERSION_CODES.FROYO)
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (isInfiniteScrollingEnabled) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        smoothScrollBy(-wheelListItemHeight, wheelListScrollDuration);
                        return true;
                    case KeyEvent.KEYCODE_DPAD_UP:
                        smoothScrollBy(wheelListItemHeight, wheelListScrollDuration);
                        return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (!isInTouchMode() && scrollState == SCROLL_STATE_IDLE)
            scrollSelectedItemToCenter(getCenter());
    }

    @Override
    public void onScroll(AbsListView innerList, int firstItem, int itemDisplayed, int totalItems) {
        if (!isInfiniteScrollingEnabled || this.getChildAt(0) == null || wheelListAdapter.getItemCount() == 0)
            return;

        if (wheelListItemHeight == 0)
            wheelListItemHeight = this.getChildAt(0).getHeight();

        if (firstItem == 0)
            this.setSelectionFromTop(wheelListAdapter.getItemCount(), this.getChildAt(0).getTop());


        if (totalItems == firstItem + itemDisplayed)
            this.setSelectionFromTop(firstItem - wheelListAdapter.getItemCount(),
                    this.getChildAt(0).getTop());


        if (wheelListAlignment != WheelListListener.ItemAllignment.None) {
            int vRad = (innerList.getHeight() + wheelListItemHeight) / 2;
            int hRad = (innerList.getHeight() < innerList.getWidth()) ? innerList.getHeight() : innerList.getWidth();
            if (wheelRadius > 0)
                hRad = wheelRadius;
            for (int i = 0; i < itemDisplayed; i++) {
                if (this.getChildAt(i) != null) {
                    double yVal = Math.min(Math.abs(innerList.getHeight() / 2 - (this.getChildAt(i).getTop() + (this.getChildAt(i).getHeight() / 2))), vRad);
                    double xVal = hRad * Math.cos(Math.asin(yVal / vRad));
                    int textViewScrollOffset = 70;
                    if (wheelListAlignment == WheelListListener.ItemAllignment.Left) {
                        xVal = xVal - hRad;
                    } else {
                        textViewScrollOffset = 90;
                        xVal = hRad / 2 - xVal;
                    }
                    View temp = this.getChildAt(i);
                    this.getChildAt(i).post(() -> {
                        temp.setLayoutParams(new LayoutParams(100, temp.getHeight()));
                        ((TextView) temp).setTextSize(TypedValue.COMPLEX_UNIT_PX, 35);
                    });
                    this.getChildAt(i).setX((int) (this.getChildAt(i).getWidth() - xVal));
                    this.getChildAt(i).scrollTo((int) (int) (this.getChildAt(i).getWidth() - xVal) / textViewScrollOffset, -(int) (this.getChildAt(i).getWidth() - xVal) / textViewScrollOffset);
                    this.getChildAt(i).setTextAlignment(TEXT_ALIGNMENT_CENTER);
                    this.getChildAt(i).setBackgroundResource(R.drawable.wheel_list_item_design);
                }
            }
        }
        if (wheelListListener != null)
            wheelListListener.onScrollEnd(this, firstItem, itemDisplayed, totalItems);

    }

    //Listener
    interface WheelListListener {
        void onScrollEnd(WheelList wheelList,
                         int firstItem,
                         int itemDisplayed,
                         int totalItems);

        enum ItemAllignment {
            None,
            Left,
            Right
        }
    }

    class WheelListAdapter implements ListAdapter {

        private final ListAdapter wheelListAdapter;
        private boolean infiniteScrolling = true;

        public WheelListAdapter(ListAdapter listAdapter) {
            wheelListAdapter = listAdapter;
        }

        private void enableInfiniteScroll(boolean infiniteScroll) {
            infiniteScrolling = infiniteScroll;
        }

        public int getItemCount() {
            return wheelListAdapter.getCount();
        }

        public int goToIndex(int position) {
            return (wheelListAdapter.getCount() == 0) ? 0 : position % wheelListAdapter.getCount();
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            wheelListAdapter.registerDataSetObserver(observer);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            wheelListAdapter.unregisterDataSetObserver(observer);
        }

        @Override
        public int getCount() {
            return (infiniteScrolling) ? wheelListAdapter.getCount() * 10 : wheelListAdapter.getCount();
        }

        @Override
        public Object getItem(int index) {
            return wheelListAdapter.getItem(this.goToIndex(index));
        }

        @Override
        public long getItemId(int index) {
            return wheelListAdapter.getItemId(this.goToIndex(index));
        }

        @Override
        public boolean hasStableIds() {
            return wheelListAdapter.hasStableIds();
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            return wheelListAdapter.getView(this.goToIndex(index), convertView, parent);
        }

        @Override
        public int getItemViewType(int index) {
            return wheelListAdapter.getItemViewType(this.goToIndex(index));
        }

        @Override
        public int getViewTypeCount() {
            return wheelListAdapter.getViewTypeCount();
        }

        @Override
        public boolean isEmpty() {
            return wheelListAdapter.isEmpty();
        }

        @Override
        public boolean areAllItemsEnabled() {
            return wheelListAdapter.areAllItemsEnabled();
        }

        @Override
        public boolean isEnabled(int index) {
            return wheelListAdapter.isEnabled(this.goToIndex(index));
        }
    }
}

