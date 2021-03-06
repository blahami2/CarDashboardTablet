package com.eclubprague.cardashboard.tablet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Michael on 23.09.2015.
 */
public class GridLayout extends ViewGroup {
    private static final String TAG = GridLayout.class.getSimpleName();

    private int width;
    private int height;
    private int childWidth;
    private int childHeight;
    private int columns;
    private int rows;
    private int space;

    public GridLayout( Context context ) {
        super( context );
    }

    public GridLayout( Context context, AttributeSet attrs ) {
        super( context, attrs );
    }

    public GridLayout( Context context, AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );
    }

    public GridLayout( Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes ) {
        super( context, attrs, defStyleAttr, defStyleRes );
    }

    public GridLayout init( int height, int width, int childHeight, int childWidth ) {
        return init( height, width, childHeight, childWidth, 0 );
    }

    public GridLayout init( int height, int width, int childHeight, int childWidth, int space ) {
        this.height = height;
        this.width = width;
        this.space = space;
        this.columns = ( width - space - getPaddingLeft() - getPaddingRight() ) / ( childWidth + space );
        this.rows = ( height - space - getPaddingTop() - getPaddingBottom() ) / ( childHeight + space );
        int horizontalSpacePerChild = ( width - space - getPaddingLeft() - getPaddingRight() ) / columns;
        int verticalSpacePerChild = ( height - space - getPaddingTop() - getPaddingBottom() ) / rows;
        this.childHeight = verticalSpacePerChild - space;
        this.childWidth = horizontalSpacePerChild - space;
//        Log.d( TAG, "initializing gridview: " +  this);

        measure( width, height );
        return this;
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }


    public GridLayout setChildAt( int column, int row, View child ) {
        int position = row * columns + column;
        return setChildAt( position, child );
    }

    public GridLayout setChildAt( int position, View child ) {
        addView( child, position );
        return this;
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        super.onMeasure( widthMeasureSpec, heightMeasureSpec );
        measureChildren( widthMeasureSpec, heightMeasureSpec );
        final int count = getChildCount();
        for ( int i = 0; i < count; i++ ) {
            final View child = getChildAt( i );
            if ( child.getVisibility() != GONE ) {
                ViewGroup.LayoutParams params = child.getLayoutParams();
                params.width = childWidth;
                params.height = childHeight;
                measureChild( child, childWidth, childHeight );
            }
        }
        setMeasuredDimension( resolveSizeAndState( width, widthMeasureSpec, 0 ),
                resolveSizeAndState( height, heightMeasureSpec, 0 ) );
    }

    @Override
    protected void onLayout( boolean changed, int left, int top, int right, int bottom ) {
        final int count = getChildCount();

        final int currentHeight = height - getPaddingTop() - getPaddingBottom();
        final int currentWidth = width - getPaddingLeft() - getPaddingRight();
//        Log.d(TAG, "creating grid layout with size: " + height + " / " + width);

        // These are the far left and right edges in which we are performing layout.
        final int leftPos = getPaddingLeft();
        final int rightPos = right - left - getPaddingRight();

        // These are the top and bottom edges in which we are performing layout.
        final int topPos = getPaddingTop();
        final int bottomPos = bottom - top - getPaddingBottom();

        final int availableHeight = ( currentHeight - ( rows - 1 ) * space ) / rows;
        final int availableWidth = ( currentWidth - ( columns - 1 ) * space ) / columns;
        for ( int i = 0; i < count; i++ ) {
            final View child = getChildAt( i );
            if ( child.getVisibility() != GONE ) {
                int column = getColumn( i );
                int row = getRow( i );
//                Log.d(TAG, "positioning child [" + i + ", " + column + ", " + row + "]");

                int childLeft = leftPos + column * ( availableWidth + space );
                int childTop = topPos + row * ( availableHeight + space );
                int childRight = childLeft + childWidth;
                int childBottom = childTop + childHeight;
                child.layout( childLeft, childTop, childRight, childBottom );
//                Log.d( TAG, "positioning child at[" + childLeft + ", " + childTop + ", " + childRight + ", " + childBottom + "]: " + child);
//                Log.d(TAG, "positioning child with size: " + child.getHeight() + " / " + child.getWidth());
            }
        }

    }

/*    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean ret =  super.onInterceptTouchEvent(ev);
        Log.d(TAG, "onInterceptTouchEvent = " + ret);
        return ret;
    }*/

    @Override
    public String toString() {
        return "GridLayout{" +
                "childHeight=" + childHeight +
                ", width=" + width +
                ", height=" + height +
                ", childWidth=" + childWidth +
                ", columns=" + columns +
                ", rows=" + rows +
                ", space=" + space +
                '}';
    }

    private int getRow( int position ) {
        return position / columns;
    }

    private int getColumn( int position ) {
        //Log.d(TAG, "getColumn for " + position + " while columns = " + columns);
        if ( columns == 1 ) {
            return 0;
        }
        //Log.d(TAG, "getColumn returning some shit: " + position + " % " + columns + " = " + (position % columns));
        return position % columns;
    }

    /**
     * Per-child layout information associated with GridLayout.
     */
    public static class LayoutParams extends ViewGroup.LayoutParams {

        public final int x;
        public final int y;

        /**
         * Creates a new set of layout parameters with the specified width,
         * height and location.
         *
         * @param width  the width, either {@link #MATCH_PARENT},
         *               {@link #WRAP_CONTENT} or a fixed size in pixels
         * @param height the height, either {@link #MATCH_PARENT},
         *               {@link #WRAP_CONTENT} or a fixed size in pixels
         * @param x      the X location of the child
         * @param y      the Y location of the child
         */
        public LayoutParams( int width, int height, int x, int y ) {
            super( width, height );
            this.x = x;
            this.y = y;
        }
    }
}
