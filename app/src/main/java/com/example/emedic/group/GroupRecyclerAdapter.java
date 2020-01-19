package com.example.emedic.group;

import android.content.Context;


import com.example.emedic.base.adapter.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;


@SuppressWarnings("unused")
public abstract class GroupRecyclerAdapter<Parent, Child> extends BaseRecyclerAdapter<Child> {
    private LinkedHashMap<Parent, List<Child>> mGroups;
    private List<Parent> mGroupTitles;

    public GroupRecyclerAdapter(Context context) {
        super(context);
        mGroups = new LinkedHashMap<>();
        mGroupTitles = new ArrayList<>();
    }


     Parent getGroup(int groupPosition) {
        return mGroupTitles.get(groupPosition);
    }



     int getGroupCount() {
        return mGroupTitles.size();
    }


     int getChildCount(int groupPosition) {
        if (mGroupTitles == null || mGroups.size() == 0)
            return 0;
        if (mGroups.get(mGroupTitles.get(groupPosition)) == null)
            return 0;
        return Objects.requireNonNull(mGroups.get(mGroupTitles.get(groupPosition))).size();
    }

    /**
     * @param groups groups
     * @param titles titles
     */
    protected void resetGroups(LinkedHashMap<Parent, List<Child>> groups, List<Parent> titles) {
        if (groups == null || titles == null) {
            return;
        }
        mGroups.clear();
        mGroupTitles.clear();
        mGroups.putAll(groups);
        mGroupTitles.addAll(titles);
        mItems.clear();
        for (Parent key : mGroups.keySet()) {
            mItems.addAll(Objects.requireNonNull(mGroups.get(key)));
        }
        notifyDataSetChanged();
    }


    public final void clearGroup() {
        mGroupTitles.clear();
        mGroups.clear();
        clear();
    }


    public boolean removeGroupItem(int position) {
        int group = getGroupIndex(position);
        removeGroupChildren(group);
        int count = getChildCount(group);
        removeItem(position);
        if (count <= 0) {
            mGroupTitles.remove(group);
            return true;
        }
        return false;
    }


    private int getGroupIndex(int position) {
        int count = 0;
        if (position <= count)
            return 0;
        int i = 0;
        for (Parent parent : mGroups.keySet()) {
            count += Objects.requireNonNull(mGroups.get(parent)).size();
            if (position < count) {
                return i;
            }
            i++;
        }
        return 0;
    }

    private void removeGroupChildren(int groupPosition) {
        if (groupPosition >= mGroupTitles.size())
            return;
        List<Child> childList = mGroups.get(mGroupTitles.get(groupPosition));
        if (childList != null && childList.size() != 0) {
            childList.remove(childList.size() - 1);
        }
    }
}
