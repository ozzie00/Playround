/*
* Copyright 2015 OneME
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

package com.oneme.toplay.base;

import android.content.Intent;

import com.oneme.toplay.Application;
import com.oneme.toplay.database.Invite;
import com.parse.ParseUser;

public class InviteToIntentExtra {

    public static void putExtra(Intent intent, Invite invite) {

        ParseUser minviteuser = invite.getUser();

        if (minviteuser != null) {
            intent.putExtra(Application.INTENT_EXTRA_USEROBJECTID, minviteuser.getObjectId());
            intent.putExtra(Application.INTENT_EXTRA_USERNAME, minviteuser.getUsername());
            // check user omeID
            if (minviteuser.getString(AppConstant.OMEPARSEUSEROMEIDKEY) == null) {
                intent.putExtra(Application.INTENT_EXTRA_USEROMEID, AppConstant.OMEPARSEUSEROMEIDNULL);
            } else {
                intent.putExtra(Application.INTENT_EXTRA_USEROMEID, minviteuser.getString(AppConstant.OMEPARSEUSEROMEIDKEY));
            }
        }

        intent.putExtra(Application.INTENT_EXTRA_WORKOUTNAME, invite.getWorkoutName());
        intent.putExtra(Application.INTENT_EXTRA_SPORTTYPEVALUE, invite.getSportTypeValue());
        intent.putExtra(Application.INTENT_EXTRA_SPORTTYPE, invite.getSportType());
        intent.putExtra(Application.INTENT_EXTRA_PLAYERNUMBER, invite.getPlayerNumber());
        intent.putExtra(Application.INTENT_EXTRA_PLAYERLEVEL, invite.getPlayerLevel());
        intent.putExtra(Application.INTENT_EXTRA_TIME, invite.getPlayTime());
        intent.putExtra(Application.INTENT_EXTRA_COURT, invite.getCourt());
        intent.putExtra(Application.INTENT_EXTRA_FEE, invite.getFee());
        intent.putExtra(Application.INTENT_EXTRA_OTHER, invite.getOther());
        intent.putExtra(Application.INTENT_EXTRA_SUBMITTIME, invite.getSubmitTime());
        intent.putExtra(Application.INTENT_EXTRA_INVITEOBJECTID, invite.getObjectId());
    }
}
