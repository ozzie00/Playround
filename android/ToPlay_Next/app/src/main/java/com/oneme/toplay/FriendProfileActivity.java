package com.oneme.toplay;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oneme.toplay.database.Database;

public class FriendProfileActivity extends AppCompatActivity {

    String friendName = null;
    String friendKey = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        friendKey              = getIntent().getStringExtra("key");
        Database mDatabase     = new Database(this);
        String[] friendDetails = mDatabase.getFriendDetails(friendKey);
        friendName             = friendDetails[0];
        String friendAlias     = friendDetails[1];
        String friendNote      = friendDetails[2];

        if(friendAlias.equals(""))
            setTitle(getResources().getString(R.string.friend_profile_title, friendName));
        else
            setTitle(getResources().getString(R.string.friend_profile_title, friendAlias));

        EditText editFriendAlias = (EditText) findViewById(R.id.friendAliasText);
        editFriendAlias.setText(friendAlias);

        TextView editFriendNote = (TextView) findViewById(R.id.friendNoteText);
        editFriendNote.setText("\""+friendNote+"\"");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
          //  getSupportActionBar().setIcon(R.drawable.ic_actionbar);
        }
    }

    public void updateAlias(View view) {
        Database mDatabase   = new Database(this);
        EditText friendAlias = (EditText) findViewById(R.id.friendAliasText);
        mDatabase.updateAlias(friendAlias.getText().toString(), friendKey);
        mDatabase.close();

        Context context   = getApplicationContext();
        CharSequence text = getString(R.string.friend_profile_updated);
        int duration      = Toast.LENGTH_SHORT;
        Toast toast       = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FriendProfileActivity.this,MainActivity.class);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        FriendProfileActivity.this.startActivity(intent);
        finish();

    }
}
