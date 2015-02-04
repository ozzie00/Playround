package com.oneme.toplay.base;

public final class Constants {

    public static final int TYPE_HEADER           = 0;
    public static final int TYPE_FRIEND_REQUEST   = 1;
    public static final int TYPE_CONTACT          = 2;
    public static final int TYPE_MAX_COUNT        = 3;
    public static final String START_TOX          = "com.oneme.toplay.START_TOX";
    public static final String STOP_TOX           = "com.oneme.toplay.STOP_TOX";
    public static final String BROADCAST_ACTION   = "com.oneme.toplay.BROADCAST";
    public static final String SWITCH_TO_FRIEND   = "com.oneme.toplay.SWITCH_TO_FRIEND";
    public static final String UPDATE             = "com.oneme.toplay.UPDATE";
    public static final String DOWNLOAD_DIRECTORY = "playround received files";

    //All DB Constants
    public static final int DATABASE_VERSION                 = 1;
    public static final String TABLE_FRIENDS                 = "friends";
    public static final String TABLE_CHAT_LOGS               = "messages";
    public static final String TABLE_FRIEND_REQUEST          = "friend_requests";
    public static final String COLUMN_NAME_KEY               = "ome_key";//"tox_key";
    public static final String COLUMN_NAME_MESSAGE           = "message";
    public static final String COLUMN_NAME_USERNAME          = "username";
    public static final String COLUMN_NAME_TIMESTAMP         = "timestamp";
    public static final String COLUMN_NAME_NOTE              = "note";
    public static final String COLUMN_NAME_STATUS            = "status";
    public static final String COLUMN_NAME_MESSAGE_ID        = "message_id";
    public static final String COLUMN_NAME_HAS_BEEN_RECEIVED = "has_been_received";
    public static final String COLUMN_NAME_HAS_BEEN_READ     = "has_been_read";
    public static final String COLUMN_NAME_SUCCESSFULLY_SENT = "successfully_sent";
    public static final String COLUMN_NAME_ISONLINE          = "isonline";
    public static final String COLUMN_NAME_ALIAS             = "alias";
    public static final String COLUMN_NAME_ISBLOCKED         = "isblocked";

    //Activity request code for onActivityResult methods
    public static final int ADD_FRIEND_REQUEST_CODE       = 0;
    public static final int WELCOME_ACTIVITY_REQUEST_CODE = 3;

    //file sending
    public static final int IMAGE_RESULT = 0;
    public static final int PHOTO_RESULT = 1;
    public static final int FILE_RESULT  = 3;

    // Message Types
    public static final int MESSAGE_TYPE_OWN                  = 1;
    public static final int MESSAGE_TYPE_FRIEND               = 2;
    public static final int MESSAGE_TYPE_FILE_TRANSFER        = 3;
    public static final int MESSAGE_TYPE_FILE_TRANSFER_FRIEND = 4;
    public static final int MESSAGE_TYPE_ACTION               = 5;

    // Epoch time
    public static long epoch;
}
