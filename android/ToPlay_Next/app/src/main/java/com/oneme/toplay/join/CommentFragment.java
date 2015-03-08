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

package com.oneme.toplay.join;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.database.InviteComment;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ParseQuery;

public class CommentFragment extends Fragment {
    private String minviteObjectID = null;
    private String mcontent        = null;
    private ParseUser muser        = ParseUser.getCurrentUser();
   // private ListView  mcommentlist = null;
   // private View rootView;

    private ParseQueryAdapter<InviteComment> commentQueryAdapter;


    // Store instance variables
    private String title;
    private int page;


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle   = this.getArguments();
        minviteObjectID = bundle.getString(Application.INTENT_EXTRA_INVITEOBJECTID);

       // requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
       // getActivity().supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
       // getActivity().setSupportProgressBarIndeterminate(true);
       // getActivity().setSupportProgressBarIndeterminateVisibility(true);


        View rootView         = inflater.inflate(R.layout.ome_activity_join_comment_fragment, container, false);
        ListView mcommentlist = (ListView)rootView.findViewById(R.id.join_comment_list);

        //View view = inflater.inflate(R.layout.fragment_feed, container, false);
        //mListView = (AbsListView) view.findViewById(android.R.id.list);
        //((AdapterView<ListAdapter>) mListView).setAdapter(mainAdapter);

        //   View rootView = inflater.inflate(R.layout.places_tab, container, false);

      //  mainAdapter = new ParseQueryAdapter<ParseObject>(getActivity(), “Places”);
      //  mainAdapter.setTextKey("name");
      //  mainAdapter.setImageKey(“placeImage");

      //          listView = (ListView) rootView.findViewById(android.R.id.list);
      //  listView.setAdapter(mainAdapter);
      //  mainAdapter.loadObjects();




        // Set up a customized query
        ParseQueryAdapter.QueryFactory<InviteComment> factory =
                new ParseQueryAdapter.QueryFactory<InviteComment>() {
                    public ParseQuery<InviteComment> create() {
                        ParseQuery<InviteComment> query = InviteComment.getQuery();
                        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                        query.orderByDescending(AppConstant.OMEPARSECREATEDATKEY);
                        query.whereMatches(AppConstant.OMEPARSEINVITECOMMENTPARENTIDKEY, minviteObjectID);
                        return query;
                    }
                };

        // Set up a progress dialog
        final ProgressDialog listLoadDialog = new ProgressDialog(getActivity());
        listLoadDialog.show();

        // Set up the query adapter
        commentQueryAdapter = new ParseQueryAdapter<InviteComment>(getActivity().getApplicationContext(), factory) {

            @Override
            public View getItemView(InviteComment comment, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.ome_activity_join_comment_item, null);
                }

                super.getItemView(comment, view, parent);

                ImageView avatarView    = (ImageView) view.findViewById(R.id.join_comment_avatar_icon_view);
                TextView usernameView   = (TextView) view.findViewById(R.id.join_comment_username_view);
                TextView contentView    = (TextView) view.findViewById(R.id.join_comment_content_view);
                TextView submittimeView = (TextView) view.findViewById(R.id.join_comment_submit_time_view);


                // Ozzie Zhang 2014-11-04 need add query for avatar icon for this user
                // show username and invite content
                avatarView.setImageDrawable(getResources().getDrawable(R.drawable.ome_map_avataricon));
                contentView.setText(comment.getContent());
                usernameView.setText(comment.getAuthorUsername());
                submittimeView.setText(comment.getSubmitTime());

                android.util.Log.d(" comment ", " " + comment.getAuthorUsername()
                        + " "  + comment.getContent());

                listLoadDialog.dismiss();

                return view;
            }
        };

        // Disable automatic loading when the adapter is attached to a view.
        commentQueryAdapter.setAutoload(true);

        // Enable pagination, we'll not manage the query limit ourselves
        commentQueryAdapter.setPaginationEnabled(true);

        // Attach the query adapter to the view
        mcommentlist.setAdapter(commentQueryAdapter);


        // set comment input
        final EditText mcomment = (EditText)rootView.findViewById(R.id.join_comment_input_text);
        if (muser != null) {
            mcomment.setVisibility(View.VISIBLE);
        }
        mcomment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //mcomment.setBackground(getResources().getDrawable(R.drawable.ome_textfield_rectangle_background));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mcontent = s.toString();
              //  mcomment.setBackgroundColor(getResources().getColor(R.color.white_absolute));
              //  mcomment.setBackground(getResources().getDrawable(R.drawable.ome_textfield_input_rectangle_background));
            }

            @Override
            public void afterTextChanged(Editable s) {
                mcontent = s.toString();
              //  mcomment.setBackground(getResources().getDrawable(R.drawable.ome_textfield_rectangle_background));
            }
        });

        final Button maddbutton = (Button)rootView.findViewById(R.id.join_comment_add_text);
        if (muser != null) {
            maddbutton.setVisibility(View.VISIBLE);


            maddbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    android.widget.Toast.makeText(v.getContext(), "Click here",
                            android.widget.Toast.LENGTH_SHORT).show();


                    submitcomment();
                }
            });
        }



        return rootView;
    }

    private void submitcomment() {
        if (muser != null) {
            InviteComment comment = new InviteComment();
            String username       = muser.getUsername();
            String time           = Time.currentTime();
            comment.setAuthor(muser);
            comment.setAuthorUsername(username);
            comment.setParentObjectId(minviteObjectID);
            comment.setSubmitTime(time);
            if (mcontent != null && mcontent.length() >= 1) {
                comment.setContent(mcontent);
                ParseACL acl = new ParseACL();

                // Give public read access
                acl.setPublicReadAccess(true);
                comment.setACL(acl);
                comment.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        getActivity().finish();
                    }
                });


            }
        }
    }
}