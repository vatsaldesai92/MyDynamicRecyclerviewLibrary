package com.desai.vatsal.myrecylerviewlibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by HCL on 14-11-2016.
 */
public class MyDynamicRecyclerView extends LinearLayout {

    private Context context;
    private AttributeSet attrs;
    private View rootView;
    private RecyclerView recyclerView;
    private ImageView iv_info_image;
    private TextView tv_info_title, tv_info_msg;
    private LinearLayout ll_info_layout, parentLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;

    private SwipeRefreshListener swipeRefreshListener;
    private FABOnClickListener fabOnClickListener;
    private LoadMoreListener loadMoreListener;

    private LinearLayoutManager linearLayoutManager;

    private boolean isFABVisible = false;
    private boolean isLoadMore = false;
    private int flag = 0;
    private int cnt = 0;


    public MyDynamicRecyclerView(Context context) {
        super(context);

        this.context = context;

        if (!isInEditMode()) {
            init();
        }
    }

    public MyDynamicRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.attrs = attrs;

        if (!isInEditMode()) {
            init();
        }
    }

    private void init() {

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyDynamicRecyclerView, 0, 0);

        try {
//            strHeaderBackgroundColor = a.getString(R.styleable.MyCalendar_headerBackgroundColor);
//            strHeaderTextColor = a.getString(R.styleable.MyCalendar_headerTextColor);
        } finally {
            a.recycle();
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.my_dynamic_recyclerview, this, true);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        iv_info_image = (ImageView) rootView.findViewById(R.id.iv_info_image);
        tv_info_title = (TextView) rootView.findViewById(R.id.tv_info_title);
        tv_info_msg = (TextView) rootView.findViewById(R.id.tv_info_msg);
        ll_info_layout = (LinearLayout) rootView.findViewById(R.id.ll_info_layout);
        parentLayout = (LinearLayout) rootView.findViewById(R.id.parentLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);


        swipeRefreshLayout.setEnabled(false);
        fab.setVisibility(GONE);


        actionListeners();
    }

    private void actionListeners() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipeRefreshListener != null) {
                    swipeRefreshListener.OnRefresh();
                }
            }
        });

        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swipeRefreshListener != null) {
                    fabOnClickListener.OnClick();
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy < 0) {
                    // Recycle view scrolling up...
                    cnt = 0;
                } else if (dy > 0) {
                    // Recycle view scrolling down...

                    int pastVisiblesItems, visibleItemCount, totalItemCount;

                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if (cnt == 0) {
                            cnt = 1;

//                        // Check load more is true or false And call listener
                            if (isLoadMore) {
                                if (loadMoreListener != null) {
                                    loadMoreListener.OnLoadMore();
                                }
                            }

                        }
                    }

                }

            }
        });

//        myScrollView.setOnScrollChangedListener(new MyScrollView.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
//
//                if (t > oldt) {
//
//                    fab.setVisibility(GONE);
//
//                    flag = 0;
//
//                    if (t <= 0) {
//                        fab.setVisibility(VISIBLE);
//                    }
//
//                    if ((t + myScrollView.getHeight()) == who.getChildAt(0).getHeight()) {
//                        fab.setVisibility(GONE);
//
//                        // Check load more is true or false And call listener
//                        if (isLoadMore) {
//                            if (loadMoreListener != null) {
//                                loadMoreListener.OnLoadMore();
//                            }
//                        }
//
//                    }
//
//                } else if (t <= 0) {
//                    fab.setVisibility(VISIBLE);
//                } else {
//
//                    if (flag <= 1) {
//                        fab.setVisibility(VISIBLE);
//                    }
//
//                    if ((t + myScrollView.getHeight()) == who.getChildAt(0).getHeight()) {
//                        fab.setVisibility(GONE);
//
//                        // Check load more is true or false And call listener
//                        if (isLoadMore) {
//                            if (loadMoreListener != null) {
//                                loadMoreListener.OnLoadMore();
//                            }
//                        }
//
//                    }
//
//                    flag++;
//                }
//
//            }
//        });


    }

    public interface SwipeRefreshListener {
        void OnRefresh();
    }

    public interface FABOnClickListener {
        void OnClick();
    }

    public interface LoadMoreListener {
        void OnLoadMore();
    }

    public void setBackgroundColor(int backgroundColor) {
        parentLayout.setBackgroundColor(backgroundColor);
    }

    public void setBackgroundColor(String backgroundColor) {
        parentLayout.setBackgroundColor(Color.parseColor(backgroundColor));
    }

    /* ------------------- Below All Are RecyclerView Methods ------------------------ */

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
        recyclerView.setItemAnimator(itemAnimator);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor, int index) {
        recyclerView.addItemDecoration(decor, index);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        recyclerView.addItemDecoration(decor);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration decor) {
        recyclerView.removeItemDecoration(decor);
    }

    public void invalidateItemDecorations() {
        recyclerView.invalidateItemDecorations();
    }

    public void setSimpleDivider(boolean isDivider) {
        if (isDivider) {
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));
        }
    }

    public void setScrollContainer(boolean isScrollContainer) {
        recyclerView.setScrollContainer(isScrollContainer);
    }

    /*  -----------------------------------------------------------------------------  */

    /* ------------------- Below All Are Information Layout Methods ------------------- */

    public void setInfoIcon(int infoIcon, int infoIconColor) {
        iv_info_image.setImageResource(infoIcon);
        iv_info_image.setColorFilter(infoIconColor, PorterDuff.Mode.SRC_IN);
    }

    public void setInfoIcon(int infoIcon, String infoIconColor) {
        iv_info_image.setImageResource(infoIcon);
        if (!TextUtils.isEmpty(infoIconColor)) {
            iv_info_image.setColorFilter(Color.parseColor(infoIconColor), PorterDuff.Mode.SRC_IN);
        }
    }

    public void setInfoTitle(String infoTitleText, float size, int infoTitleTextColor, Typeface tf) {
        tv_info_title.setText(infoTitleText);
        tv_info_title.setTextSize(size);
        tv_info_title.setTextColor(infoTitleTextColor);
        tv_info_title.setTypeface(tf);
    }

    public void setInfoTitle(String infoTitleText, float size, String infoTitleTextColor, Typeface tf) {
        tv_info_title.setText(infoTitleText);
        tv_info_title.setTextSize(size);
        tv_info_title.setTextColor(Color.parseColor(infoTitleTextColor));
        tv_info_title.setTypeface(tf);
    }

    public void setInfoMessage(String infoMessageText, float size, int infoMessageTextColor, Typeface tf) {
        tv_info_msg.setText(infoMessageText);
        tv_info_msg.setTextSize(size);
        tv_info_msg.setTextColor(infoMessageTextColor);
        tv_info_msg.setTypeface(tf);
    }

    public void setInfoMessage(String infoMessageText, float size, String infoMessageTextColor, Typeface tf) {
        tv_info_msg.setText(infoMessageText);
        tv_info_msg.setTextSize(size);
        tv_info_msg.setTextColor(Color.parseColor(infoMessageTextColor));
        tv_info_msg.setTypeface(tf);
    }

    public void showInfoLayout() {
        ll_info_layout.setVisibility(VISIBLE);
        recyclerView.setVisibility(GONE);
    }

    public void hideInfoLayout() {
        ll_info_layout.setVisibility(GONE);
        recyclerView.setVisibility(VISIBLE);
    }

    /*  -----------------------------------------------------------------------------  */


    /* ----------------- Below All Are Empty View Methods ------------------ */



    /*  -----------------------------------------------------------------------------  */


    /* ----------------- Below All Are Swipe Refresh Layout Methods ------------------ */

    public void setSwipeRefresh(boolean isSwipeRefresh) {
        if (isSwipeRefresh) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }
    }

    public void setOnRefreshListener(SwipeRefreshListener swipeRefreshListener) {
        this.swipeRefreshListener = swipeRefreshListener;
    }

    public void stopSwipeRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public void setColorSchemeColors(int... colors) {
        swipeRefreshLayout.setColorSchemeColors(colors);
    }

    /*  -----------------------------------------------------------------------------  */

    /* ----------------- Below All Are Floating Action Button Methods ------------------ */

    public void setFAB(boolean isFAB) {

        isFABVisible = isFAB;

        if (isFAB) {
            fab.setVisibility(VISIBLE);
        } else {
            fab.setVisibility(GONE);
        }
    }

    public void setFABMargins(int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) fab.getLayoutParams();
        layoutParams.setMargins(left, top, right, bottom);
        fab.setLayoutParams(layoutParams);
    }

    public void setFABImage(int fabImage) {
        fab.setImageResource(fabImage);
    }

    public void setFABBackgroundColor(int color) {
        fab.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void setFABBackgroundTintMode(PorterDuff.Mode tintMode) {
        fab.setBackgroundTintMode(tintMode);
    }

    public void setFABRippleColor(int color) {
        fab.setRippleColor(color);
    }

    public void setFABImageColor(int fabImageColor) {
        fab.setColorFilter(fabImageColor);
    }

    public void setFABImageColor(String fabImageColor) {
        fab.setColorFilter(Color.parseColor(fabImageColor));
    }

    public void setFABOnClickListener(FABOnClickListener fabOnClickListener) {
        this.fabOnClickListener = fabOnClickListener;
    }

    /*  -----------------------------------------------------------------------------  */

    /* ------------------------ Below All Are LoadMore Methods ----------------------- */

    public void setLoadMore(boolean isLoadMore, LinearLayoutManager linearLayoutManager) {
        this.isLoadMore = isLoadMore;
        this.linearLayoutManager = linearLayoutManager;
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    /*  -----------------------------------------------------------------------------  */


}
