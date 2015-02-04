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

package com.oneme.toplay.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.oneme.toplay.R;


/**
 * Activity that displays the settings screen.
 */
public class SettingActivity extends FragmentActivity {

    private TextView mAboutText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ome_activity_setting);

        addAboutText();
        addFeedback();



    }


    public void onClickFeedback(View v) {

        Intent invokeFeedbackActivityIntent = new Intent(SettingActivity.this, FeedbackActivity.class);
        startActivity(invokeFeedbackActivityIntent);
    }

    // define feedback text
    private void addFeedback() {
        mAboutText = (TextView) findViewById(R.id.setting_Feedback);
        mAboutText.setText(getResources().getString(R.string.OMEPARSEFEEDBACK));
    }

    // define about text
    private void addAboutText() {
        mAboutText = (TextView) findViewById(R.id.setting_About);
        mAboutText.setText(getResources().getString(R.string.app_version));
    }

}



