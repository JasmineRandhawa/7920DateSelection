package com.example.DateSelection;

public interface WheelListListener {
        void onWheelScrollFinished(WheelList wheelList,
                                   int firstVisibleItem,
                                   int visibleItemCount,
                                   int totalItemCount);

        enum ItemAllignment {
            None,
            Left,
            Right
        }
}
