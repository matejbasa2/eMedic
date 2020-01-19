package com.example.emedic.group;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("all")
public class GroupItemDecoration<Group, Child> extends RecyclerView.ItemDecoration {
    protected int mGroupHeight;
    protected int mGroutBackground;
    protected Paint mBackgroundPaint;
    protected Paint mTextPaint;
    protected float mTextBaseLine;
    protected int mPaddingLeft, mPaddingRight;
    protected boolean isCenter;
    protected boolean isHasHeader;
    protected int mChildItemOffset;
    @SuppressLint("UseSparseArrays")
    protected Map<Integer, Group> mGroup = new HashMap<>();

    public GroupItemDecoration() {
        super();
        init();
    }

    private void init() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xFFf5f7f8);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(0xFF353535);
        mTextPaint.setAntiAlias(true);
    }

    /**
     * RecyclerViewItem onDraw
     *
     * @param c      RecyclerView canvas
     * @param parent RecyclerView
     * @param state  stare
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        onDrawGroup(c, parent);
    }

    /**
     * Group
     *
     * @param c      Canvas
     * @param parent RecyclerView
     */
    protected void onDrawGroup(Canvas c, RecyclerView parent) {
        int paddingLeft = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int top, bottom;
        int count = parent.getChildCount();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int key = params.getViewLayoutPosition();
            if (mGroup.containsKey(key)) {
                top = child.getTop() - params.topMargin - mGroupHeight;
                bottom = top + mGroupHeight;
                c.drawRect(paddingLeft, top, right, bottom, mBackgroundPaint);
                String group = mGroup.get(params.getViewLayoutPosition()).toString();
                float x;
                float y = top + mTextBaseLine;
                if (isCenter) {
                    x = parent.getMeasuredWidth() / 2 - getTextX(group);
                } else {
                    x = mPaddingLeft;
                }
                c.drawText(group, x, y, mTextPaint);
            }
        }
    }

    /**
     * RecyclerViewItem onDraw
     *
     * @param c      RecyclerView canvas
     * @param parent RecyclerView
     * @param state  stare
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        onDrawOverGroup(c, parent);
    }

    /**
     *
     * @param c      Canvas
     * @param parent RecyclerView
     */
    protected void onDrawOverGroup(Canvas c, RecyclerView parent) {
        int firstVisiblePosition = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        if (firstVisiblePosition == RecyclerView.NO_POSITION) {
            return;
        }
        Group group = getCroup(firstVisiblePosition);
        if (group == null)
            return;
        String groupTitle = group.toString();
        if (TextUtils.isEmpty(groupTitle)) {
            return;
        }
        boolean isRestore = false;
        Group nextGroup = getCroup(firstVisiblePosition + 1);
        if (nextGroup != null && !group.equals(nextGroup)) {

            View child = parent.findViewHolderForAdapterPosition(firstVisiblePosition).itemView;
            if (child.getTop() + child.getMeasuredHeight() < mGroupHeight) {

                c.save();
                isRestore = true;
                c.translate(0, child.getTop() + child.getMeasuredHeight() - mGroupHeight);
            }
        }
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int top = parent.getPaddingTop();
        int bottom = top + mGroupHeight;
        c.drawRect(left, top, right, bottom, mBackgroundPaint);
        float x;
        float y = top + mTextBaseLine;
        if (isCenter) {
            x = parent.getMeasuredWidth() / 2 - getTextX(groupTitle);
        } else {
            x = mPaddingLeft;
        }
        c.drawText(groupTitle, x, y, mTextPaint);
        if (isRestore) {
            c.restore();
        }
    }

    /**
     * item
     *
     * @param outRect rect
     * @param view    item
     * @param parent  RecyclerView
     * @param state   stare
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        getItemOffsets(outRect, view, parent, parent.getChildViewHolder(view).getAdapterPosition());
    }

    /**
     * item
     *
     * @param outRect         outRect
     * @param view            view
     * @param parent          RecyclerView
     * @param adapterPosition position
     */
    protected void getItemOffsets(Rect outRect, View view, RecyclerView parent, int adapterPosition) {
        if (mGroup.containsKey(adapterPosition)) {
            outRect.set(0, mGroupHeight, 0, mGroup.containsKey(adapterPosition + 1) ? 0 : mChildItemOffset);
        } else {
            outRect.set(0, 0, 0, mGroup.containsKey(adapterPosition + 1) ? 0 : mChildItemOffset);
        }
    }

    /**
     * ViewPosition
     *
     * @param position Viewposition
     * @return ViewPosition
     */
    protected Group getCroup(int position) {
        while (position >= 0) {
            if (mGroup.containsKey(position)) {
                return mGroup.get(position);
            }
            position--;
        }
        return null;
    }

    /**
     *
     * @param adapter GroupRecyclerAdapter
     */
    public void notifyDataSetChanged(GroupRecyclerAdapter<Group, Child> adapter) {
        mGroup.clear();
        if (adapter == null) return;
        int key = 0;
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            if (i == 0) {
                mGroup.put(isHasHeader ? 1 : 0, adapter.getGroup(i));
                key += adapter.getChildCount(i) + (isHasHeader ? 1 : 0);
                ;
            } else {
                mGroup.put(key, adapter.getGroup(i));
                key += adapter.getChildCount(i);
            }
        }
    }

    public void setChildItemOffset(int childItemOffset){
        this.mChildItemOffset = childItemOffset;
    }

    public void setBackground(int groupBackground) {
        mBackgroundPaint.setColor(groupBackground);
    }

    public void setTextColor(int textColor) {
        mTextPaint.setColor(textColor);
    }

    public void setTextSize(float textSize) {
        mTextPaint.setTextSize(textSize);
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        mTextBaseLine = mGroupHeight / 2 - metrics.descent + (metrics.bottom - metrics.top) / 2;
    }

    public void setGroupHeight(int groupHeight) {
        mGroupHeight = groupHeight;
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        mTextBaseLine = mGroupHeight / 2 - metrics.descent + (metrics.bottom - metrics.top) / 2;
    }

    public void setPadding(int mPaddingLeft, int mPaddingRight) {
        this.mPaddingLeft = mPaddingLeft;
        this.mPaddingRight = mPaddingRight;
    }

    public void setCenter(boolean isCenter) {
        this.isCenter = isCenter;
    }

    public void setHasHeader(boolean hasHeader) {
        isHasHeader = hasHeader;
    }

    /**
     *
     * @param str
     * @return x
     */
    protected float getTextX(String str) {
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(str, 0, str.length(), bounds);
        return bounds.width() / 2;
    }

    /**
     * @param str
     * @return px
     */
    protected float getTextLenghtPx(String str) {
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(str, 0, str.length(), bounds);
        return bounds.width();
    }
}
