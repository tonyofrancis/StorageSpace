package com.tonyodev.storagegrapher;

import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

/**
 * Created by tonyofrancis on 4/23/17.
 *
 * This class is used by the StorageGraphView class to plot
 * storage volume matrix on a bar graph
 */

public final class StorageGraphBar {

    @ColorInt
    private final int color;
    private final float percentage;
    private final String legendTitle;
    private final String legendSubtitle;

    public StorageGraphBar(float percentage, @ColorInt int color) {
        this(percentage,color,null,null);
    }

    public StorageGraphBar(float percentage, @ColorInt int color, @Nullable String legendTitle, @Nullable String legendSubtitle) {
        this.percentage = percentage;
        this.color = color;
        this.legendTitle = legendTitle;
        this.legendSubtitle = legendSubtitle;
    }

    /**
     * @return  the color of the bar and legend id color
     * */
    @ColorInt
    public int getColor() {
        return color;
    }

    /**
     * @return  a percentage of space
     * */
    public float getPercentage() {
        return percentage;
    }

    /**
     * @return  the legend title
     * */
    @Nullable
    public String getLegendTitle() {
        return legendTitle;
    }

    /**
     * @return  the legend subtitle
     * */
    @Nullable
    public String getLegendSubtitle() {
        return legendSubtitle;
    }
}
