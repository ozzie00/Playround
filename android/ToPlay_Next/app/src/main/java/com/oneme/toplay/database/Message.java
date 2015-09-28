/*
* Copyright 2014 OneME
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.oneme.toplay.database;

import com.oneme.toplay.base.AppConstant;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


/**
 * Data model for a message.
 */
@ParseClassName(AppConstant.OMETOPLAYMESSAGECLASSKEY)
public final class Message extends ParseObject {

    public ParseUser getUser() {
        return getParseUser(AppConstant.OMEPARSEUSERKEY);
    }

    public void setUser(ParseUser value) {
        put(AppConstant.OMEPARSEUSERKEY, value);
    }

    public ParseUser getMessageFromUser() {
        return getParseUser(AppConstant.OMEPARSEMESSAGEFROMUSERKEY);
    }

    public void setMessageFromUser(ParseUser value) {
        put(AppConstant.OMEPARSEMESSAGEFROMUSERKEY, value);
    }

    public String getUsername() {
        return getString(AppConstant.OMEPARSEUSERNAMEKEY);
    }

    public void setUsername(String value) {
        put(AppConstant.OMEPARSEUSERNAMEKEY, value);
    }

    public void setToUser(ParseUser value) {
        put(AppConstant.OMEPARSEMESSAGETOUSERKEY, value);
    }

    public ParseUser getToUser() {
        return getParseUser(AppConstant.OMEPARSEMESSAGETOUSERKEY);
    }

    public String getContent() {return getString(AppConstant.OMEPARSEMESSAGECONTENTKEY);}

    public final void setContent(String value) {put(AppConstant.OMEPARSEMESSAGECONTENTKEY, value);}

    public String  getFromUsername() {return getString(AppConstant.OMEPARSEMESSAGEFROMUSERNAMEKEY);}

    public final void setFromUsername(String value) {put(AppConstant.OMEPARSEMESSAGEFROMUSERNAMEKEY, value);}

    public String  getFromOmeID() {return getString(AppConstant.OMEPARSEMESSAGEFROMOMEIDKEY);}

    public final void setFromOmeID(String value) {put(AppConstant.OMEPARSEMESSAGEFROMOMEIDKEY, value);}

    public String  getReplyForObjectID() {return getString(AppConstant.OMEPARSEMESSAGEFOROBJECTIDKEY);}

    public final void setReplyForObjectID(String value) {put(AppConstant.OMEPARSEMESSAGEFOROBJECTIDKEY, value);}

    // public String getToUsername() {return getString(AppConstant.OMEPARSEMESSAGETOUSERNAMEKEY);}

    public final void setToUsername(String value) {put(AppConstant.OMEPARSEMESSAGETOUSERNAMEKEY, value);}

    public String getSendTime() {return getString(AppConstant.OMEPARSEMESSAGESENDTIMEKEY);}

    public final void setSendTime(String value) {put(AppConstant.OMEPARSEMESSAGESENDTIMEKEY, value);}

    public static ParseQuery<Message> getQuery() {
        return ParseQuery.getQuery(Message.class);
    }
}
