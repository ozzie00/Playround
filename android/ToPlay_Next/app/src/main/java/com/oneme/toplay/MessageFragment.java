package com.oneme.toplay;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.adapter.MessageAdapter;
import com.oneme.toplay.database.Database;

import com.oneme.toplay.service.Singleton;

import com.oneme.toplay.base.FriendInfo;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class MessageFragment { //extends Fragment {

    /*
    private static final String TAG           = "MessageFragment";

    Singleton mSingleton = Singleton.getInstance();

    private ListView conversationListView;
    private ArrayAdapter<FriendInfo> conversationAdapter;
    private Subscription mSubscription;
    private Database mDatabase;
    private MessageAdapter adapter;
    private View rootView;
    private boolean paused;
    LinearLayout noConversations;

    public MessageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView             = inflater.inflate(R.layout.fragment_message, container, false);
            conversationListView = (ListView) rootView.findViewById(R.id.conversations_list);
            conversationListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            noConversations      = (LinearLayout) rootView.findViewById(R.id.message_no_conversations);
        } else {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        Observable.create(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> observer) {
                try {
                    Cursor cursor = getCursor();
                    observer.onNext(cursor);
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Cursor>() {
            @Override
            public void call(Cursor cursor) {
                if (adapter == null) {
                    adapter = new MessageAdapter(getActivity(), cursor);
                    conversationListView.setAdapter(adapter);
                } else {
                    adapter.changeCursor(cursor);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        mSubscription = mSingleton.friendInfoListSubject.observeOn(AndroidSchedulers.mainThread()).distinctUntilChanged()
                .subscribe(new Action1<ArrayList<FriendInfo>>() {
                    @Override
                    public void call(ArrayList<FriendInfo> friends_list) {
                        if (!paused) {
                            updateRecentConversations(friends_list);
                        }
                        paused = false;
                    }
                });
    }

    @Override
    public void onPause(){
        super.onPause();
        paused = true;
        mSubscription.unsubscribe();
    }

    private Cursor getCursor() {
        if (this.mDatabase == null) {
            this.mDatabase = new Database(getActivity());
        }
        Cursor cursor = this.mDatabase.getRecentCursor();
        return cursor;
    }
    public void updateRecentConversations(ArrayList<FriendInfo> friendsList) {

        //If you have no recent conversations, display  message
        if (friendsList.size() == 0) {
            noConversations.setVisibility(View.VISIBLE);
        } else {
            noConversations.setVisibility(View.GONE);
        }

        Observable.create(new Observable.OnSubscribeFunc<Cursor>() {
            @Override
            public Subscription onSubscribe(Observer<? super Cursor> observer) {
                try {
                    Cursor cursor = getCursor();
                    observer.onNext(cursor);
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }

                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Cursor>() {
                    @Override
                    public void call(Cursor cursor) {
                        adapter.changeCursor(cursor);
                    }
                });
        if (Application.APPDEBUG) {
            Log.d(TAG, "updated Message fragment");
        }
    }

    */
}
