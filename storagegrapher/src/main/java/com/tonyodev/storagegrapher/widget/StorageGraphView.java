package com.tonyodev.storagegrapher.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tonyodev.storagegrapher.R;
import com.tonyodev.storagegrapher.StorageGraphBar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by tonyofrancis on 4/21/17.
 *
 * This View is used to plot and display information
 * about a Storage Volume.
 */
public class StorageGraphView extends FrameLayout {

    private static final int VIEW_RECYCLE_LIMIT = 4;
    private static final Queue<View> mBarViewPool = new LinkedList<>();
    private static final Queue<View> mLegendViewPool = new LinkedList<>();

    private final TextView mTitle;
    private final LinearLayout mGraph;
    private final LinearLayout mLegend;

    @DrawableRes
    private int mLegendDrawable = -1;
    @DimenRes
    private int mLegendPadding = -1;
    @ColorInt
    private int mLegendTextColor = -1;
    @DimenRes
    private int mLegendTextSizeRes = -1;
    private float mLegendTextSize = -1;
    private Typeface mLegendTypeFace = null;
    private int mLegendTypeFaceStyle = Typeface.NORMAL;

    public StorageGraphView(Context context) {
        this(context,null);
    }

    public StorageGraphView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public StorageGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context,attrs,defStyleAttr,0);
    }

    public StorageGraphView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context,attrs,defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.storage_graph_view,this,true);

        this.mTitle = (TextView) findViewById(R.id.graph_title);
        this.mGraph = (LinearLayout) findViewById(R.id.graph);
        this.mLegend = (LinearLayout) findViewById(R.id.legend);
        setStyle(attrs);
    }

    private void setStyle(AttributeSet attributeSet) {

        TypedArray styledAttributes = null;

        try {

            styledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.StorageGraphView);

            if(styledAttributes != null) {

                float titleTextSize = styledAttributes.getDimension(R.styleable.StorageGraphView_titleTextSize,-1);

                if(titleTextSize != -1) {
                    mTitle.setTextSize(titleTextSize);
                }

                int titleColor = styledAttributes.getColor(R.styleable.StorageGraphView_titleColor,-1);

                if(titleColor != -1) {
                    mTitle.setTextColor(titleColor);
                }

                String titleText = styledAttributes.getString(R.styleable.StorageGraphView_titleText);
                mTitle.setText(titleText);


                mLegendDrawable = styledAttributes.getResourceId(R.styleable.StorageGraphView_legendDrawable,-1);

                if(mLegendDrawable == -1) {
                    mLegendDrawable = R.drawable.small_circle;
                }

                mLegendTextSize = styledAttributes.getDimension(R.styleable.StorageGraphView_legendTextSize,-1);
                mLegendTextColor = styledAttributes.getColor(R.styleable.StorageGraphView_legendTextColor,-1);
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {

            if(styledAttributes != null) {
                styledAttributes.recycle();
            }
        }
    }

    /**
     * Sets the title for the StorageGraphView
     *
     * @param title title
     * */
    public void setTitle(@Nullable String title) {
        this.mTitle.setText(title);
    }

    /**
     * Sets the title text size for the StorageGraphView
     *
     * @param size dimension resource
     * */
    public void setTitleTextSize(@DimenRes int size) {
        this.mTitle.setTextSize(getContext().getResources().getDimension(size));
    }

    /**
     * Sets the typeface for the StorageGraphView title
     *
     * @param typeFace typeface
     * */
    public void setTitleTypeFace(Typeface typeFace) {
        this.mTitle.setTypeface(typeFace,Typeface.NORMAL);
    }

    /**
     * Sets the typeface for the StorageGraphView title
     *
     * @param typeFace typeface
     * @param style style
     * */
    public void setTitleTypeFace(Typeface typeFace,int style) {
        this.mTitle.setTypeface(typeFace,style);
    }

    /**
     * Sets the title color for the StorageGraphView
     *
     * @param color color resource
     * */
    public void setTitleTextColor(@ColorRes int color) {
        this.mTitle.setTextColor(ContextCompat.getColor(getContext(),color));
    }

    /**
     * hides the title for the StorageGraphView
     * */
    public void hideTitle() {
        this.mTitle.setVisibility(GONE);
    }

    /**
     * shows the title for the StorageGraphView
     * */
    public void showTitle() {
        this.mTitle.setVisibility(VISIBLE);
    }

    /**
     * Sets the bar height for the bar graph
     *
     * @param height resource dimension
     * */
    public void setBarHeight(@DimenRes int height) {

        int pixelHeight = getContext().getResources().getDimensionPixelSize(height);
        this.mGraph.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,pixelHeight));
    }

    /**
     * Sets the drawable used in the legend
     *
     * @param drawable drawable resource
     * */
    public void setLegendDrawable(@DrawableRes int drawable) {
        this.mLegendDrawable = drawable;
    }

    /**
     * Sets the legend drawable padding
     *
     * @param padding resource dimension
     * */
    public void setLegendDrawablePadding(@DimenRes int padding) {
        this.mLegendPadding = padding;
    }

    /**
     * Sets the legend text color
     *
     * @param color resource color
     * */
    public void setLegendTextColor(@ColorRes int color) {
        this.mLegendTextColor = ContextCompat.getColor(getContext(),color);
    }

    /**
     * Sets the legend text size
     *
     * @param size resource dimension
     * */
    public void setLegendTextSize(@DimenRes int size) {
        this.mLegendTextSizeRes = size;
    }

    /**
     * Sets the typeface for legend text
     *
     * @param typeFace typeface
     * */
    public void setLegendTypeFace(@Nullable Typeface typeFace) {
        this.mLegendTypeFace = typeFace;
        this.mLegendTypeFaceStyle = Typeface.NORMAL;
    }

    /**
     * Sets the typeface and style for legend text
     *
     * @param typeFace typeface
     * @param style  style
     * */
    public void setLegendTypeFace(@Nullable Typeface typeFace,int style) {
        this.mLegendTypeFace = typeFace;
        this.mLegendTypeFaceStyle = style;
    }

    /**
     * Adds StorageGraphBar items to the graph.
     * Bars a plotted based on the order they were entered
     *
     * @param bars StorageGraphBar items
     * */
    public void addBars(StorageGraphBar... bars) {

        if(bars == null) {
            return;
        }

        List<View> legendViewList = new ArrayList<>(getLegendViews());
        mLegend.removeAllViews();

        for (StorageGraphBar bar : bars) {

            if(bar == null) {
                continue;
            }

            View barView = getBarView();
            View legendView = getLegendView();

            formatBarView(barView, bar);
            formatLegendView(legendView, bar);

            mGraph.addView(barView);
            legendViewList.add(legendView);
        }
        addViewsToLegend(legendViewList);
    }

    private List<View> getLegendViews() {

        int count = mLegend.getChildCount();
        List<View> keysViewList = new ArrayList<>(count);

        for (int x = 0; x < count; x++) {
            keysViewList.add(mLegend.getChildAt(x));
        }

        return keysViewList;
    }

    private void addViewsToLegend(List<View> viewList) {

        if(viewList.size() == 0) {
            return;
        }

        int weight = 100 / viewList.size();

        for (View keyView : viewList) {

            mLegend.addView(keyView,new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    weight));
        }
    }

    private void formatBarView(View barView,StorageGraphBar bar) {

        barView.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT, bar.getPercentage()));

        barView.setBackgroundColor(bar.getColor());
    }

    private void formatLegendView(View legendView,StorageGraphBar bar) {

        TextView legendKeyView = (TextView) legendView.findViewById(R.id.legend_key);
        TextView legendSubKeyView = (TextView) legendView.findViewById(R.id.legend_subkey);

        legendKeyView.setText(bar.getLegendTitle());
        legendSubKeyView.setText(bar.getLegendSubtitle());

        Drawable drawable = ContextCompat.getDrawable(getContext(),mLegendDrawable);
        drawable = tintDrawable(drawable,bar.getColor());
        legendKeyView.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);

        if(mLegendTypeFace != null) {
            legendKeyView.setTypeface(mLegendTypeFace,mLegendTypeFaceStyle);
            legendSubKeyView.setTypeface(mLegendTypeFace,mLegendTypeFaceStyle);
        }

        if(mLegendTextSizeRes != -1) {
            float size = getContext().getResources().getDimension(mLegendTextSizeRes);
            legendKeyView.setTextSize(size);
            legendSubKeyView.setTextSize(size);
        }else if(mLegendTextSize != -1) {
            legendKeyView.setTextSize(mLegendTextSize);
            legendSubKeyView.setTextSize(mLegendTextSize);
        }

        if(mLegendTextColor != -1) {
            legendKeyView.setTextColor(mLegendTextColor);
            legendSubKeyView.setTextColor(mLegendTextColor);
        }

        if(mLegendPadding != -1) {
            int padding = (int) getContext().getResources().getDimension(mLegendPadding);
            legendKeyView.setCompoundDrawablePadding(padding);
        }
    }

    private View getBarView() {

        if(!mBarViewPool.isEmpty()) {
            return mBarViewPool.remove();
        }

        return new View(getContext());
    }

    private View getLegendView() {

        if(!mLegendViewPool.isEmpty()) {
            return mLegendViewPool.remove();
        }

        return LayoutInflater.from(getContext()).inflate(R.layout.storage_graph_legend, mLegend,false);
    }

    /**
     * Clears all plotted data from the graph
     * */
    public void clear() {

        recyclerViews();
        mGraph.removeAllViews();
        mLegend.removeAllViews();
    }

    private void recyclerViews() {

        int index = mGraph.getChildCount();
        int counter = 0;

        while (mBarViewPool.size() < VIEW_RECYCLE_LIMIT && counter < index) {
            mBarViewPool.add(mGraph.getChildAt(counter++));
        }

        index = mLegend.getChildCount();
        counter = 0;

        while (mLegendViewPool.size() < VIEW_RECYCLE_LIMIT && counter < index) {
            mLegendViewPool.add(mLegend.getChildAt(counter++));
        }
    }

    private Drawable tintDrawable(Drawable drawable,@ColorInt int color) {

        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable,color);
        return wrappedDrawable;
    }
}
