package com.oneme.toplay.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.Constants;
import com.oneme.toplay.base.IconColor;
import com.oneme.toplay.base.LeftPaneItem;
import com.oneme.toplay.base.PrettyTimestamp;
import com.oneme.toplay.database.Database;
import com.oneme.toplay.service.Singleton;

import java.util.ArrayList;

public class LeftPaneAdapter { // extends BaseAdapter implements Filterable {

    /*
    private ArrayList<LeftPaneItem> mDataOriginal = new ArrayList<LeftPaneItem>();
    private ArrayList<LeftPaneItem> mData = new ArrayList<LeftPaneItem>();
    private LayoutInflater mInflater;
    private Context context;

    Filter mFilter;

    public LeftPaneAdapter(Context context) {
        mInflater    = ((Activity) context).getLayoutInflater();
        this.context = context;
    }

    public void addItem(final LeftPaneItem item) {
        mData.add(item);
        mDataOriginal.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int type = getItem(position).viewType;
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return Constants.TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public LeftPaneItem getItem(int position) {
        return mData.get(position);
    }

    public String getKey(int position) {
        return getItem(position).key;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case Constants.TYPE_FRIEND_REQUEST:
                    convertView                     = mInflater.inflate(R.layout.friendrequest_list_item, null);
                    //Ozzie Zhang 2015-01-05 disable this code and do not diplay key to user
                    holder.friendKeyText            = (TextView)convertView.findViewById(R.id.request_key);
                    holder.friendRequestMessageText = (TextView)convertView.findViewById(R.id.request_message);
                    break;
                case Constants.TYPE_CONTACT:
                    convertView                     = mInflater.inflate(R.layout.contact_list_item, null);
                    holder.friendKeyText            = (TextView)convertView.findViewById(R.id.friend_name);
                    holder.friendRequestMessageText = (TextView)convertView.findViewById(R.id.friend_status);
                    holder.icon                     = (ImageView)convertView.findViewById(R.id.icon);
                    holder.countText                = (TextView)convertView.findViewById(R.id.unread_messages_count);
                    holder.timeText                 = (TextView)convertView.findViewById(R.id.last_message_timestamp);
                    break;
                case Constants.TYPE_HEADER:
                    convertView          = mInflater.inflate(R.layout.header_list_item, null);
                    holder.friendKeyText = (TextView)convertView.findViewById(R.id.left_pane_header);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        LeftPaneItem item = getItem(position);

        //Ozzie Zhang 2015-01-05 display request message not key
        holder.friendKeyText.setText(item.second);

        if (type != Constants.TYPE_HEADER) {
            if(!item.second.equals(""))
                holder.friendRequestMessageText.setText(item.second);
            else
                holder.friendKeyText.setGravity(Gravity.CENTER_VERTICAL);
        }
        if (type == Constants.TYPE_CONTACT) {
            if (item.count > 0) {
                holder.countText.setVisibility(View.VISIBLE);
                holder.countText.setText(Integer.toString(item.count));
            } else {
                holder.countText.setVisibility(View.GONE);
            }
            holder.timeText.setText(PrettyTimestamp.prettyTimestamp(item.timestamp, false));
            holder.icon.setBackgroundColor(IconColor.iconColorAsColor(item.isOnline,item.status));
        }

        if(holder.timeText != null) {
            holder.timeText.setTextColor(context.getResources().getColor(R.color.gray_darker));
        }

        if(type == Constants.TYPE_FRIEND_REQUEST) {
            ImageView acceptButton = (ImageView) convertView.findViewById(R.id.accept);
            ImageView rejectButton = (ImageView) convertView.findViewById(R.id.reject);

            final String key = item.first;
            acceptButton.setOnClickListener(new View.OnClickListener() {
                Singleton mSingleton = Singleton.getInstance();

                @Override
                public void onClick(View view) {
                    if (Application.APPDEBUG) {
                        Log.d("OnClick", "Accepting Friend: " + key);
                    }

                    Database mDatabase = new Database(context);
                    mDatabase.addFriend(key, "Friend Accepted", "", "");
                    mDatabase.deleteFriendRequest(key);
                    mDatabase.close();
                    try {
                        mSingleton.jTox.confirmRequest(key);
                        mSingleton.jTox.save();
                    } catch (Exception e) {

                    }

                    mSingleton.updateFriendRequests(context);
                    mSingleton.updateFriendsList(context);
                }
            });

            rejectButton.setOnClickListener(new View.OnClickListener() {
                Singleton mSingleton = Singleton.getInstance();

                @Override
                public void onClick(View view) {
                    if (Application.APPDEBUG) {
                        Log.d("OnClick", "Rejecting Friend: " + key);
                    }

                    Database mDatabase = new Database(context);
                    mDatabase.deleteFriendRequest(key);
                    mDatabase.close();

                    mSingleton.updateFriendsList(context);
                    mSingleton.updateFriendRequests(context);
                }
            });
        }
        return convertView;
    }


    private static class ViewHolder {
        public TextView friendKeyText;
        public TextView friendRequestMessageText;
        public ImageView icon;
        public TextView countText;
        public TextView timeText;
    }

    @Override
    public Filter getFilter() {
        if(mFilter == null) {
            mFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();

                    if (mDataOriginal != null) {

                        if (constraint.equals("") || constraint == null) {

                            filterResults.values = mDataOriginal;
                            filterResults.count = mDataOriginal.size();

                        } else {
                            mData = mDataOriginal;
                            ArrayList<LeftPaneItem> tempList1 = new ArrayList<LeftPaneItem>();
                            ArrayList<LeftPaneItem> tempList2 = new ArrayList<LeftPaneItem>();
                            int length = mData.size();
                            int i = 0;
                            while (i < length) {
                                LeftPaneItem item = mData.get(i);
                                if (item.first.toUpperCase().startsWith(constraint.toString().toUpperCase()))
                                    tempList1.add(item);
                                else if (item.first.toLowerCase().contains(constraint.toString().toLowerCase()))
                                    tempList2.add(item);
                                i++;
                            }
                            tempList1.addAll(tempList2);
                            filterResults.values = tempList1;
                            filterResults.count = tempList1.size();
                        }

                    }

                    return filterResults;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence contraint, FilterResults results) {
                    mData = (ArrayList<LeftPaneItem>) results.values;
                    if (results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
        }

        return mFilter;
    }

    */
}
