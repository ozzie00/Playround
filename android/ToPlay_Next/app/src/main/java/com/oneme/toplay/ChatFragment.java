package com.oneme.toplay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.WindowManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.adapter.ChatMessagesAdapter;
import com.oneme.toplay.database.Database;

import com.oneme.toplay.service.Singleton;

import com.oneme.toplay.base.ClientFriend;
import com.oneme.toplay.base.ChatMessages;
import com.oneme.toplay.base.Constants;
import com.oneme.toplay.base.FileDialog;
import com.oneme.toplay.base.FriendInfo;
import com.oneme.toplay.base.IconColor;
import com.oneme.toplay.base.Tuple;

//import im.tox.jtoxcore.ToxException;
//import im.tox.jtoxcore.ToxUserStatus;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;


public class ChatFragment { //extends Fragment {
    /*
    private static String TAG = "com.oneme.toplay.ChatFragment";
    public static String ARG_CONTACT_NUMBER = "contact_number";
    private ListView chatListView;

    private ChatMessagesAdapter adapter;
    private EditText messageBox;
    private TextView isTypingBox;
    private TextView statusTextBox;
    Singleton mSingleton = Singleton.getInstance();

    Subscription messagesSubscribe;
    Subscription progressSubscribe;
    Subscription activeKeySubscribe;
    Subscription titleSubscribe;
    Subscription typingSubscribe;

    private ArrayList<ChatMessages> chatMessages;
    private String activeKey;
    private boolean scrolling = false;
    private Database mDatabase;
    public String photoPath;

    public ChatFragment(String key) {
        this.activeKey = key;
    }

    public ChatFragment() {
        super();
    }

    @Override
    public void onResume(){
        super.onResume();
        if (Application.APPDEBUG) {
            Log.d("ChatFragment", "onResume");
        }

        progressSubscribe = Observable.interval(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                if (!scrolling && mSingleton.fileIds.size() > 0) {
                    updateProgress();
                }
            }
        });

        messagesSubscribe = mSingleton.updatedMessagesSubject.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (Application.APPDEBUG) {
                    Log.d(" ChatFragment ", "updatedMessageSubject subscription");
                }
                updateChat();
            }
        });

        activeKeySubscribe = mSingleton.activeKeyAndIsFriendSubject
                .subscribe(new Action1<Tuple<String, Boolean>>() {
                    @Override
                    public void call(Tuple<String, Boolean> activeKeyAndIfFriend) {
                        String key = activeKeyAndIfFriend.x;
                        boolean isFriend = activeKeyAndIfFriend.y;
                        if (Application.APPDEBUG) {
                            Log.d("ChatFragment", "activekey sub: key: " + key + " mSingleton active key: " + mSingleton.activeKey);
                        }
                        if (!key.equals("") && !key.equals(activeKey)) {
                            mSingleton.doClosePaneSubject.onNext(true);
                            if (isFriend) {
                                if (Application.APPDEBUG) {
                                    Log.d("ChatFragment", "chat fragment enabled, isFriend: " + isFriend + ", key: " + activeKey);
                                }
                                changeKey(key);
                            }
                        }
                    }
                });

        titleSubscribe = mSingleton.friendInfoListAndActiveSubject.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Tuple<ArrayList<FriendInfo>, Tuple<String,Boolean>>>() {
            @Override
            public void call(Tuple<ArrayList<FriendInfo>, Tuple<String,Boolean>> tup) {
                ArrayList<FriendInfo> fi = tup.x;
                String key               = tup.y.x;
                Boolean isFriend         = tup.y.y;

                if (isFriend) {
                    String friendName          = "";
                    String friendAlias         = "";
                    String friendNote          = "";
                    ToxUserStatus friendStatus = ToxUserStatus.TOX_USERSTATUS_NONE;
                    boolean friendIsOnline     = false;
                    for (FriendInfo f : fi) {
                        if (f.friendKey.equals(key)) {
                            friendName     = f.friendName;
                            friendNote     = f.personalNote;
                            friendAlias    = f.alias;
                            friendStatus   = f.getFriendStatusAsToxUserStatus();
                            friendIsOnline = f.isOnline;
                            break;
                        }
                    }

                    TextView chatName = (TextView) getActivity().findViewById(R.id.chatActiveName);
                    if (!friendAlias.equals(""))
                        chatName.setText(friendAlias);
                    else
                        chatName.setText(friendName);

                    TextView statusText = (TextView) getActivity().findViewById(R.id.chatActiveStatus);
                    statusText.setText(friendNote);

                    TextView statusIcon = (TextView) getActivity().findViewById(R.id.chat_friend_status_icon);
                    statusIcon.setBackgroundColor(IconColor.iconColorAsColor(friendIsOnline,friendStatus));
                }
            }
        });

        typingSubscribe = mSingleton.typingSubject.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean x) {
                if (mSingleton.typingMap.containsKey(activeKey)) {
                    boolean isTyping = mSingleton.typingMap.get(activeKey);
                    if (isTyping) {
                        isTypingBox.setVisibility(View.VISIBLE);
                        statusTextBox.setVisibility(View.GONE);
                    } else {
                        isTypingBox.setVisibility(View.GONE);
                        statusTextBox.setVisibility(View.VISIBLE);
                    }
                } else {
                    isTypingBox.setVisibility(View.GONE);
                    statusTextBox.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        if (Application.APPDEBUG) {
            Log.d("ChatFragment", "onPause");
        }
        messagesSubscribe.unsubscribe();
        titleSubscribe.unsubscribe();
        typingSubscribe.unsubscribe();
        activeKeySubscribe.unsubscribe();
        progressSubscribe.unsubscribe();
        activeKey = "";
        mSingleton.chatActive = false;
    }

    private void updateProgress() {
        int start = chatListView.getFirstVisiblePosition();
        for (int i = start, j = chatListView.getLastVisiblePosition(); i <= j; i++) {
            View view = chatListView.getChildAt(i - start);
            chatListView.getAdapter().getView(i, view, chatListView);
        }
    }

    private void sendMessage() {
        if (Application.APPDEBUG) {
            Log.d("ChatFragment", "sendMessage");
        }
        if (messageBox.getText() != null && messageBox.getText().toString().length() == 0) {
            return;
        }
        final String mMessage;
        if (messageBox.getText() != null ) {
            mMessage = messageBox.getText().toString();
        } else {
            mMessage = "";
        }
        final String key = this.activeKey;
        messageBox.setText("");
        Observable<Boolean> send = Observable.create(
                new Observable.OnSubscribe<Boolean>() {
                     @Override
                         public void call(Subscriber<? super Boolean> subscriber) {
                            try {
                                // Send message
                                ClientFriend friend = null;
                                Random generator    = new Random();
                                int id = generator.nextInt();
                                try {
                                    friend = mSingleton.getFriend(key);
                                } catch (Exception e) {
                                    if (Application.APPDEBUG) {
                                        Log.d(TAG, e.toString());
                                    }
                                }
                                if (friend != null) {
                                    boolean sendingSucceeded = true;
                                    try {
                                        // NB: substring includes from start up to but not including the end position
                                        // Max message length in tox is 1368 bytes
                                        // jToxCore seems to append a null byte so split around 1367
                                        final byte[] utf8Bytes = mMessage.getBytes("UTF-8");
                                        int numOfMessages      = (utf8Bytes.length/1367) + 1;

                                        if(numOfMessages > 1) {

                                            final int OneByte   = 0xFFFFFF80;
                                            final int TwoByte   = 0xFFFFF800;
                                            final int ThreeByte = 0xFFFF0000;

                                            int total                = 0;
                                            int previous             = 0;
                                            int numberOfMessagesSent = 0;

                                            for(int i = 0; i < mMessage.length(); i++) {
                                                if((mMessage.charAt(i) & OneByte) == 0)
                                                    total += 1;
                                                else if((mMessage.charAt(i) & TwoByte) == 0)
                                                    total += 2;
                                                else if((mMessage.charAt(i) & ThreeByte) == 0)
                                                    total += 3;
                                                else
                                                    total += 4;

                                                if(numberOfMessagesSent == numOfMessages-1) {
                                                    // TODO: fix id's since withid was removed
                                                    mSingleton.jTox.sendMessage(friend, mMessage.substring(previous));
                                                    break;
                                                } else if(total >= 1366) {
                                                    mSingleton.jTox.sendMessage(friend, mMessage.substring(previous, i));
                                                    numberOfMessagesSent++;
                                                    previous = i;
                                                    total    = 0;
                                                }
                                            }

                                        } else {

                                            mSingleton.jTox.sendMessage(friend, mMessage);

                                        }

                                    } catch (ToxException e) {
                                        if (Application.APPDEBUG) {
                                            Log.d(TAG, e.toString());
                                            e.printStackTrace();
                                        }
                                        sendingSucceeded = false;
                                    }
                                    Database mDatabase = new Database(getActivity());
                                    // Add message to chatlog
                                    mDatabase.addMessage(id, key, mMessage, false, false, sendingSucceeded, 1);
                                    mDatabase.close();
                                    // update UI
                                    mSingleton.updateMessages(getActivity());
                                }
                                subscriber.onCompleted();
                            } catch (Exception e) {
                                if (Application.APPDEBUG) {
                                    Log.e("ChatFragment", "Subscriber error: " + e.getMessage());
                                }
                                subscriber.onError(e);

                            }
                         }
                     });
        send.subscribeOn(Schedulers.io()).subscribe();
    }

    private Cursor getCursor() {
        if (this.mDatabase == null) {
            this.mDatabase = new Database(getActivity());
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Cursor cursor                 = this.mDatabase.getMessageCursor(activeKey, preferences.getBoolean("action_messages", true));
        return cursor;
    }

    private void updateChat() {
        Observable.create(new Observable.OnSubscribeFunc<Cursor>() {
            @Override
            public Subscription onSubscribe(Observer<? super Cursor> observer) {
                Cursor cursor = getCursor();
                try {

                    observer.onNext(cursor);
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
                return Subscriptions.empty();
                }
            }

            ).

            subscribeOn(Schedulers.io()

            ).

            observeOn(AndroidSchedulers.mainThread()

            )
                    .

            subscribe(new Action1<Cursor>() {
                          @Override
                          public void call(Cursor cursor) {
                              adapter.changeCursor(cursor);
                          }
                      }

            );
        if (Application.APPDEBUG) {
            Log.d("ChatFragment", "new key: " + activeKey);
        }
    }

    public void changeKey(String key) {
        activeKey = key;
        updateChat();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Application.APPDEBUG) {
            Log.d("ChatFragment ImageResult resultCode", Integer.toString(resultCode));
            Log.d("ChatFragment ImageResult requestCode", Integer.toString(requestCode));
        }
        if (requestCode == Constants.IMAGE_RESULT  && resultCode == Activity.RESULT_OK) {
            Uri uri                 = data.getData();
            String path             = null;
            String[] filePathColumn = {MediaStore.Images.Media.DATA,MediaStore.Images.Media.DISPLAY_NAME};
            String filePath         = null;
            String fileName         = null;
            CursorLoader loader     = new CursorLoader(getActivity(), uri, filePathColumn, null, null, null);
            Cursor cursor           = loader.loadInBackground();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex   = cursor.getColumnIndexOrThrow(filePathColumn[0]);
                    filePath          = cursor.getString(columnIndex);
                    int fileNameIndex = cursor.getColumnIndexOrThrow(filePathColumn[1]);
                    fileName          = cursor.getString(fileNameIndex);
                }
            }
            try {
                path = filePath;
            } catch (Exception e) {
                if (Application.APPDEBUG) {
                    Log.d("onActivityResult", e.toString());
                }
            }
            if (path != null) {
                mSingleton.sendFileSendRequest(path, this.activeKey, getActivity());
            }
        }

        if(requestCode==Constants.PHOTO_RESULT && resultCode==Activity.RESULT_OK){

            if(photoPath!=null) {
                mSingleton.sendFileSendRequest(photoPath, this.activeKey, getActivity());
                photoPath=null;
            }

        }
    }




    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView                 = inflater.inflate(R.layout.fragment_chat, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Cursor cursor                 = getCursor();
        adapter                       = new ChatMessagesAdapter(getActivity(), cursor, mDatabase.getMessageIds(this.activeKey, preferences.getBoolean("action_messages", true)));

        chatListView                  = (ListView) rootView.findViewById(R.id.chatMessages);
        chatListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        chatListView.setStackFromBottom(true);
        chatListView.setAdapter(adapter);
        chatListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    scrolling = false;
                } else {
                    scrolling = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }

        });
        isTypingBox   = (TextView) rootView.findViewById(R.id.isTyping);
        statusTextBox = (TextView) rootView.findViewById(R.id.chatActiveStatus);

        messageBox = (EditText) rootView.findViewById(R.id.yourMessage);
        messageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                boolean isTyping;
                if(i3 > 0) {
                    isTyping = true;
                } else {
                    isTyping = false;
                }
                if (Application.APPDEBUG) {
                   // Log.d(TAG + " activekey ", activeKey);
                }

                //
                //ClientFriend friend = mSingleton.getFriend(activeKey);
                //if (friend != null && friend.isOnline()) {
                //    try {
                //        mSingleton.jTox.sendIsTyping(friend.getFriendnumber(), isTyping);
                //    } catch (ToxException ex) {

                //    }
               // }

               //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        messageBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //chatListView.setSelection(adapter.getCount() - 1);
            }
        });

        View b = (View) rootView.findViewById(R.id.sendMessageButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
                ClientFriend friend = mSingleton.getFriend(activeKey);
                if (friend != null) {
                    try {
                        mSingleton.jTox.sendIsTyping(friend.getFriendnumber(), false);
                    } catch (ToxException ex) {

                    }
                }
            }
        });


        //View backButton = (View) rootView.findViewById(R.id.backButton);

        //backButton.setOnClickListener(new View.OnClickListener() {
        //                                  @Override
        //                                  public void onClick(View v) {
        //                                      mSingleton.doClosePaneSubject.onNext(false);
        //                                  }
        //                              });

        View attachmentButton = (View) rootView.findViewById(R.id.attachmentButton);

        attachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final CharSequence items[];
                items = new CharSequence[] {
                        getResources().getString(R.string.attachment_photo),
                        getResources().getString(R.string.attachment_takephoto),
                        getResources().getString(R.string.attachment_file)
                };
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, Constants.IMAGE_RESULT);
                                break;

                            case 1:
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                String image_name   = AppConstant.OMETOPLAYATTACHMENTFILENAME + new Date().toString();
                                File storageDir     = Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES);
                                File file           = null;
                                try {
                                    file = File.createTempFile(
                                            image_name,  // prefix
                                            ".jpg",      // suffix
                                            storageDir   // directory
                                    );
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (file != null) {
                                    Uri imageUri = Uri.fromFile(file);
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                    photoPath    = file.getAbsolutePath();
                                }
                                startActivityForResult(cameraIntent, Constants.PHOTO_RESULT);
                                break;

                            case 2:
                                File mPath            = new File(Environment.getExternalStorageDirectory() + "//DIR//");
                                FileDialog fileDialog = new FileDialog(getActivity(), mPath);
                                fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
                                    public void fileSelected(File file) {
                                        mSingleton.sendFileSendRequest(file.getPath(), activeKey, getActivity());
                                    }
                                });
                                fileDialog.showDialog();
                        }
                    }
                });
                builder.create().show();
            }
        });

        return rootView;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            //scanQR button to call the barcode reader app
            //case R.id.scanFriend:
               // scanIntent();
            //    break;
        }
        return super.onOptionsItemSelected(item);
    }

        */

}
