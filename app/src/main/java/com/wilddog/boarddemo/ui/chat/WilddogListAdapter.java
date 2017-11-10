package com.wilddog.boarddemo.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.wilddog.boarddemo.R;
import com.wilddog.boarddemo.util.SharedpereferenceTool;
import com.wilddog.client.ChildEventListener;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.Query;
import com.wilddog.client.SyncError;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeen
 * @since 9/17/15
 *
 * This class is a generic way of backing an Android ListView with a Wilddog location.
 * It handles all of the child events at the given Wilddog location. It marshals received data into the given
 * class type. Extend this class and provide an implementation of <code>populateView</code>, which will be given an
 * instance of your list item mLayout and an instance your class that holds your data. Simply populate the view however
 * you like and this class will handle updating the list as the data changes.
 *
 * @param <T> The class type to use as a model for the data contained in the children of the given Wilddog location
 */
public abstract class WilddogListAdapter<T> extends BaseAdapter {

    private Query mRef;
    private Class<T> mModelClass;
//    private int mLayout;
    private LayoutInflater mInflater;
    private List<T> mModels;
    private List<String> mKeys;
    private ChildEventListener mListener;
    private final String mUsername;


    /**
     * @param mRef        The Wilddog location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     * @param mModelClass Wilddog will marshall the data at a location into an instance of a class that you provide
     */
    public WilddogListAdapter(Query mRef, Class<T> mModelClass,  Context context) {
        this.mRef = mRef;
        this.mModelClass = mModelClass;
        mUsername = SharedpereferenceTool.getUserId(context);
//        this.mLayout = mLayout;
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<T>();
        mKeys = new ArrayList<String>();
        // Look for all child events. We will then map them to our own internal ArrayList, which backs ListView
        mListener = this.mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                T model = (T) dataSnapshot.getValue(WilddogListAdapter.this.mModelClass);
                String key = dataSnapshot.getKey();

                // Insert into the correct location, based on previousChildName
                if (previousChildName == null) {
                    mModels.add(0, model);
                    mKeys.add(0, key);
                } else {
                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == mModels.size()) {
                        mModels.add(model);
                        mKeys.add(key);
                    } else {
                        mModels.add(nextIndex, model);
                        mKeys.add(nextIndex, key);
                    }
                }

                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // One of the mModels changed. Replace it in our list and name mapping
                String key = dataSnapshot.getKey();
                T newModel = (T) dataSnapshot.getValue(WilddogListAdapter.this.mModelClass);
                int index = mKeys.indexOf(key);

                mModels.set(index, newModel);

                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                // A model was removed from the list. Remove it from our list and the name mapping
                String key = dataSnapshot.getKey();
                int index = mKeys.indexOf(key);

                mKeys.remove(index);
                mModels.remove(index);

                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                // A model changed position in the list. Update our list accordingly
                String key = dataSnapshot.getKey();
                T newModel = (T) dataSnapshot.getValue(WilddogListAdapter.this.mModelClass);
                int index = mKeys.indexOf(key);
                mModels.remove(index);
                mKeys.remove(index);
                if (previousChildName == null) {
                    mModels.add(0, newModel);
                    mKeys.add(0, key);
                } else {
                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == mModels.size()) {
                        mModels.add(newModel);
                        mKeys.add(key);
                    } else {
                        mModels.add(nextIndex, newModel);
                        mKeys.add(nextIndex, key);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(SyncError syncError) {
                Log.e("WilddogListAdapter", "Listen was cancelled, no more updates will occur");
            }

        });
    }

    public void cleanup() {
        // We're being destroyed, let go of our mListener and forget about all of the mModels
        mRef.removeEventListener(mListener);
        mModels.clear();
        mKeys.clear();
    }

    @Override
    public int getCount() {
        return mModels.size();
    }

    @Override
    public Object getItem(int i) {
        return mModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        Chat msg = (Chat) mModels.get(position);
        if(msg.getUid().equals(mUsername))
            return 1;
        else
            return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
//        if (view == null) {
//            view = mInflater.inflate(mLayout, viewGroup, false);
//        }
        ViewHolder viewHolder;
        if(convertView == null){
            if(getItemViewType(position) == 0){
                convertView = mInflater.inflate(R.layout.chat_left,null);
                viewHolder = new ViewHolder();
                viewHolder.author = (TextView)convertView.findViewById(R.id.author);
                viewHolder.message = (TextView)convertView.findViewById(R.id.message);
            }else {
                convertView = mInflater.inflate(R.layout.chat_right,null);
                viewHolder = new ViewHolder();
                viewHolder.author = (TextView)convertView.findViewById(R.id.author);
                viewHolder.message = (TextView)convertView.findViewById(R.id.message);
            }
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        T model = mModels.get(position);
        // Call out to subclass to marshall this model into the provided view
        populateView(viewHolder, model);
        return convertView;
    }
    static class ViewHolder{
        TextView author;
        TextView message;
    }
    /**
     * Each time the data at the given Wilddog location changes, this method will be called for each item that needs
     * to be displayed. The arguments correspond to the mLayout and mModelClass given to the constructor of this class.
     * <p/>
     * Your implementation should populate the view using the data contained in the model.
     *
     * @param holder     The view to populate
     * @param model The object containing the data used to populate the view
     */
    protected abstract void populateView(ViewHolder holder, T model);
}
