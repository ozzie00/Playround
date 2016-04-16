/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oneme.toplay.ui;

import android.accounts.Account;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oneme.toplay.R;
import com.oneme.toplay.base.CheckGoogleService;
import com.oneme.toplay.base.LoadImageFromParseCloud;
import com.oneme.toplay.track.TrackHistoryActivity;
import com.oneme.toplay.ui.widget.BezelImageView;
import com.oneme.toplay.ui.widget.MultiSwipeRefreshLayout;
import com.oneme.toplay.ui.widget.ScrimInsetsScrollView;
import com.oneme.toplay.track.TrackListActivity;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A base activity that handles common functionality in the app. This includes the
 * navigation drawer, login and authentication, Action Bar tweaks, amongst others.
 */
public abstract class BaseActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        MultiSwipeRefreshLayout.CanChildScrollUpCallback {
//    private static final String TAG = makeLogTag(BaseActivity.class);

    // the LoginAndAuthHelper handles signing in to Google Play Services and OAuth
//    private LoginAndAuthHelper mLoginAndAuthHelper;

    // Navigation drawer:
    private DrawerLayout mDrawerLayout;

    // Helper methods for L APIs
//    private LUtils mLUtils;

    private ObjectAnimator mStatusBarColorAnimator;
    private LinearLayout mAccountListContainer;
    private ViewGroup mDrawerItemsListContainer;
    private Handler mHandler;

    private ImageView mExpandAccountBoxIndicator;
    private boolean mAccountBoxExpanded = false;

    // When set, these components will be shown/hidden in sync with the action bar
    // to implement the "quick recall" effect (the Action Bar and the header views disappear
    // when you scroll down a list, and reappear quickly when you scroll up).
    private ArrayList<View> mHideableHeaderViews = new ArrayList<View>();

    // Durations for certain animations we use:
    private static final int HEADER_HIDE_ANIM_DURATION = 300;
    private static final int ACCOUNT_BOX_EXPAND_ANIM_DURATION = 200;

    // symbols for navdrawer items (indices must correspond to array below). This is
    // not a list of items that are necessarily *present* in the Nav Drawer; rather,
    // it's a list of all possible items.
    protected static final int NAVDRAWER_ITEM_EXPLORE = 0;
    protected static final int NAVDRAWER_ITEM_WORKOUT = 1;
   // protected static final int NAVDRAWER_ITEM_HISTORY = 2;
    protected static final int NAVDRAWER_ITEM_VENUE = 2;
    protected static final int NAVDRAWER_ITEM_MAP = 3;
    protected static final int NAVDRAWER_ITEM_MESSAGE = 4;
    protected static final int NAVDRAWER_ITEM_SIGN_IN = 5;
    protected static final int NAVDRAWER_ITEM_SETTINGS = 6;
   // protected static final int NAVDRAWER_ITEM_EXPERTS_DIRECTORY = 7;
   // protected static final int NAVDRAWER_ITEM_PEOPLE_IVE_MET = 8;
    protected static final int NAVDRAWER_ITEM_INVALID = -1;
    protected static final int NAVDRAWER_ITEM_SEPARATOR = -2;
    protected static final int NAVDRAWER_ITEM_SEPARATOR_SPECIAL = -3;

    // titles for navdrawer items (indices must correspond to the above)
    private static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
            R.string.navdrawer_item_explore,
            R.string.navdrawer_item_workout,
      //      R.string.navdrawer_item_history,
            R.string.navdrawer_item_venue,
            R.string.navdrawer_item_map,
            R.string.navdrawer_item_message,
            R.string.navdrawer_item_explore,
            R.string.navdrawer_item_setting,
    };

    // icons for navdrawer items (indices must correspond to above array)
    private static final int[] NAVDRAWER_ICON_RES_ID = new int[] {
            R.drawable.ic_drawer_explore,  // My Schedule
            R.drawable.ic_drawer_my_schedule, // Map
     //       R.drawable.ic_drawer_history, // History
            R.drawable.ic_drawer_experts,  // Explore
            R.drawable.ic_drawer_map, // Social
            R.drawable.ic_drawer_social, // Video Library
            0, // Sign in
            R.drawable.ic_drawer_settings,
    };

    // delay to launch nav drawer item, to allow close animation to play
    private static final int NAVDRAWER_LAUNCH_DELAY = 250;

    // fade in and fade out durations for the main content when switching between
    // different Activities of the app through the Nav Drawer
    private static final int MAIN_CONTENT_FADEOUT_DURATION = 150;
    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;

    // list of navdrawer items that were actually added to the navdrawer, in order
    private ArrayList<Integer> mNavDrawerItems = new ArrayList<Integer>();

    // views that correspond to each navdrawer item, null if not yet created
    private View[] mNavDrawerItemViews = null;

    // SwipeRefreshLayout allows the user to swipe the screen down to trigger a manual refresh
    private SwipeRefreshLayout mSwipeRefreshLayout;

    // Primary toolbar and drawer toggle
    private Toolbar mActionBarToolbar;

    // asynctask that performs GCM registration in the backgorund
    private AsyncTask<Void, Void, Void> mGCMRegisterTask;

    // handle to our sync observer (that notifies us about changes in our sync state)
    private Object mSyncObserverHandle;

    // data bootstrap thread. Data bootstrap is the process of initializing the database
    // with the data cache that ships with the app.
    Thread mDataBootstrapThread = null;

    // variables that control the Action Bar auto hide behavior (aka "quick recall")
    private boolean mActionBarAutoHideEnabled = false;
    private int mActionBarAutoHideSensivity = 0;
    private int mActionBarAutoHideMinY = 0;
    private int mActionBarAutoHideSignal = 0;
    private boolean mActionBarShown = true;

    // A Runnable that we should execute when the navigation drawer finishes its closing animation
    private Runnable mDeferredOnDrawerClosedRunnable;

    private boolean mManualSyncRequest;

    private int mThemedStatusBarColor;
    private int mNormalStatusBarColor;
    private int mProgressBarTopWhenActionBarShown;
    private static final TypeEvaluator ARGB_EVALUATOR = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();

        // Enable or disable each Activity depending on the form factor. This is necessary
        // because this app uses many implicit intents where we don't name the exact Activity
        // in the Intent, so there should only be one enabled Activity that handles each
        // Intent in the app.
   //     UIUtils.enableDisableActivitiesByFormFactor(this);

        if (savedInstanceState == null) {
            registerGCMClient();
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mThemedStatusBarColor = getResources().getColor(R.color.playround_default);
        mNormalStatusBarColor = mThemedStatusBarColor;
    }

    private void trySetupSwipeRefresh() {
     /*
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(
                    R.color.refresh_progress_1,
                    R.color.refresh_progress_2,
                    R.color.refresh_progress_3);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    requestDataRefresh();
                }
            });

            if (mSwipeRefreshLayout instanceof MultiSwipeRefreshLayout) {
                MultiSwipeRefreshLayout mswrl = (MultiSwipeRefreshLayout) mSwipeRefreshLayout;
                mswrl.setCanChildScrollUpCallback(this);
            }
        }

      */
    }

    protected void setProgressBarTopWhenActionBarShown(int progressBarTopWhenActionBarShown) {
        mProgressBarTopWhenActionBarShown = progressBarTopWhenActionBarShown;
        updateSwipeRefreshProgressBarTop();
    }

    private void updateSwipeRefreshProgressBarTop() {
        if (mSwipeRefreshLayout == null) {
            return;
        }

        int progressBarStartMargin = getResources().getDimensionPixelSize(
                R.dimen.swipe_refresh_progress_bar_start_margin);
        int progressBarEndMargin = getResources().getDimensionPixelSize(
                R.dimen.swipe_refresh_progress_bar_end_margin);
        int top = mActionBarShown ? mProgressBarTopWhenActionBarShown : 0;
        mSwipeRefreshLayout.setProgressViewOffset(false,
                top + progressBarStartMargin, top + progressBarEndMargin);
    }

    /**
     * Returns the navigation drawer item that corresponds to this Activity. Subclasses
     * of BaseActivity override this to indicate what nav drawer item corresponds to them
     * Return NAVDRAWER_ITEM_INVALID to mean that this Activity should not have a Nav Drawer.
     */
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_INVALID;
    }

    /**
     * Sets up the navigation drawer as appropriate. Note that the nav drawer will be
     * different depending on whether the attendee indicated that they are attending the
     * event on-site vs. attending remotely.
     */
    private void setupNavDrawer() {
        // What nav drawer item should be selected?
        int selfItem = getSelfNavDrawerItem();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null) {
            return;
        }
        mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.theme_primary_dark));
        ScrimInsetsScrollView navDrawer = (ScrimInsetsScrollView)
                mDrawerLayout.findViewById(R.id.navdrawer);
        if (selfItem == NAVDRAWER_ITEM_INVALID) {
            // do not show a nav drawer
            if (navDrawer != null) {
                ((ViewGroup) navDrawer.getParent()).removeView(navDrawer);
            }
            mDrawerLayout = null;
            return;
        }

        if (navDrawer != null) {
            final View chosenAccountContentView = findViewById(R.id.chosen_account_content_view);
            final View chosenAccountView = findViewById(R.id.chosen_account_view);
            final int navDrawerChosenAccountHeight = getResources().getDimensionPixelSize(
                    R.dimen.navdrawer_chosen_account_height);
            navDrawer.setOnInsetsCallback(new ScrimInsetsScrollView.OnInsetsCallback() {
                @Override
                public void onInsetsChanged(Rect insets) {
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)
                            chosenAccountContentView.getLayoutParams();
                    lp.topMargin = insets.top;
                    chosenAccountContentView.setLayoutParams(lp);

                    ViewGroup.LayoutParams lp2 = chosenAccountView.getLayoutParams();
                    lp2.height = navDrawerChosenAccountHeight + insets.top;
                    chosenAccountView.setLayoutParams(lp2);
                }
            });
        }

        if (mActionBarToolbar != null) {
            mActionBarToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                // run deferred action, if we have one
                if (mDeferredOnDrawerClosedRunnable != null) {
                    mDeferredOnDrawerClosedRunnable.run();
                    mDeferredOnDrawerClosedRunnable = null;
                }
                if (mAccountBoxExpanded) {
                    mAccountBoxExpanded = false;
                   // setupAccountBoxToggle();
                }
                onNavDrawerStateChanged(false, false);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                onNavDrawerStateChanged(true, false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                onNavDrawerStateChanged(isNavDrawerOpen(), newState != DrawerLayout.STATE_IDLE);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                onNavDrawerSlide(slideOffset);
            }
        });

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // populate the nav drawer with the correct items
        populateNavDrawer();

        // When the user runs the app for the first time, we want to land them with the
        // navigation drawer open. But just the first time.
      //  if (!PrefUtils.isWelcomeDone(this)) {
            // first run of the app starts with the nav drawer open
      //      PrefUtils.markWelcomeDone(this);
      //      mDrawerLayout.openDrawer(Gravity.START);
     //   }

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    // Subclasses can override this for custom behavior
    protected void onNavDrawerStateChanged(boolean isOpen, boolean isAnimating) {
        if (mActionBarAutoHideEnabled && isOpen) {
            autoShowOrHideActionBar(true);
        }
    }

    protected void onNavDrawerSlide(float offset) {}

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    /** Populates the navigation drawer with the appropriate items. */
    private void populateNavDrawer() {
      //  boolean attendeeAtVenue = PrefUtils.isAttendeeAtVenue(this);
        mNavDrawerItems.clear();

        // decide which items will appear in the nav drawer
     //   if (AccountUtils.hasActiveAccount(this)) {
            // Only logged-in users can save sessions, so if there is no active account,
            // there is no My Schedule
            mNavDrawerItems.add(NAVDRAWER_ITEM_EXPLORE);
      //  } else {
            // If no active account, show Sign In
      //      mNavDrawerItems.add(NAVDRAWER_ITEM_SIGN_IN);
      //  }

        mNavDrawerItems.add(NAVDRAWER_ITEM_WORKOUT);

       // mNavDrawerItems.add(NAVDRAWER_ITEM_HISTORY);


        // Explore is always shown
        mNavDrawerItems.add(NAVDRAWER_ITEM_VENUE);

        // If the attendee is on-site, show Map on the nav drawer
        //if (attendeeAtVenue) {
       // }
        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR);

        // If attendee is on-site, show the People I've Met item
      //  if (attendeeAtVenue) {
        //    mNavDrawerItems.add(NAVDRAWER_ITEM_PEOPLE_IVE_MET);
      //  }

        // If the experts directory hasn't expired, show it
     //    if (!Config.hasExpertsDirectoryExpired()) {
       //     mNavDrawerItems.add(NAVDRAWER_ITEM_EXPERTS_DIRECTORY);
     //   }


        // Other items that are always in the nav drawer irrespective of whether the
        // attendee is on-site or remote:
        mNavDrawerItems.add(NAVDRAWER_ITEM_MAP);
        mNavDrawerItems.add(NAVDRAWER_ITEM_MESSAGE);
        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR_SPECIAL);
        mNavDrawerItems.add(NAVDRAWER_ITEM_SETTINGS);

        createNavDrawerItems();
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void createNavDrawerItems() {
        mDrawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_list);
        if (mDrawerItemsListContainer == null) {
            return;
        }

        mNavDrawerItemViews = new View[mNavDrawerItems.size()];
        mDrawerItemsListContainer.removeAllViews();
        int i = 0;
        for (int itemId : mNavDrawerItems) {
            mNavDrawerItemViews[i] = makeNavDrawerItem(itemId, mDrawerItemsListContainer);
            mDrawerItemsListContainer.addView(mNavDrawerItemViews[i]);
            ++i;
        }
    }

    /**
     * Sets up the given navdrawer item's appearance to the selected state. Note: this could
     * also be accomplished (perhaps more cleanly) with state-based layouts.
     */
    private void setSelectedNavDrawerItem(int itemId) {
        if (mNavDrawerItemViews != null) {
            for (int i = 0; i < mNavDrawerItemViews.length; i++) {
                if (i < mNavDrawerItems.size()) {
                    int thisItemId = mNavDrawerItems.get(i);
                    formatNavDrawerItem(mNavDrawerItemViews[i], thisItemId, itemId == thisItemId);
                }
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
      //  if (key.equals(PrefUtils.PREF_ATTENDEE_AT_VENUE)) {
          //  LOGD(TAG, "Attendee at venue preference changed, repopulating nav drawer and menu.");
            populateNavDrawer();
            invalidateOptionsMenu();
      //  }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();
        //setupAccountBox();

       // trySetupSwipeRefresh();
       // updateSwipeRefreshProgressBarTop();

        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(MAIN_CONTENT_FADEIN_DURATION);
        } else {
            //LOGW(TAG, "No view with ID main_content to fade in.");
        }
    }

    /**
     * Sets up the account box. The account box is the area at the top of the nav drawer that
     * shows which account the user is logged in as, and lets them switch accounts. It also
     * shows the user's Google+ cover photo as background.
     */
    private void setupAccountBox() {

        final View chosenAccountView = findViewById(R.id.chosen_account_view);
//        ImageView coverImageView     = (ImageView) chosenAccountView.findViewById(R.id.profile_cover_image);
//        BezelImageView profileImageView   = (BezelImageView) chosenAccountView.findViewById(R.id.profile_image);
        TextView nameTextView        = (TextView) chosenAccountView.findViewById(R.id.profile_name_text);

        ParseUser muser = ParseUser.getCurrentUser();
        String name     = null;

        if (muser != null) {
            name = muser.getUsername();
        }

        if (name == null) {
            nameTextView.setVisibility(View.GONE);
        } else {
            nameTextView.setVisibility(View.VISIBLE);
            nameTextView.setText(name);
        }

        if (muser == null) {
        //    profileImageView.setVisibility(View.GONE);
            nameTextView.setVisibility(View.GONE);
        } else {
        //    profileImageView.setVisibility(View.VISIBLE);
        //    LoadImageFromParseCloud.getAvatar(BaseActivity.this, muser, profileImageView);
        }

        if (name == null || name.isEmpty()) {
            // There's only one account on the device, so no need for a switcher.
            //mExpandAccountBoxIndicator.setVisibility(View.GONE);
            //mAccountListContainer.setVisibility(View.GONE);
            chosenAccountView.setEnabled(false);
            return;
        }

        chosenAccountView.setEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            /*
            case R.id.menu_about:
                //HelpUtils.showAbout(this);
                return true;

            case R.id.menu_wifi:
                //WiFiUtils.showWiFiDialog(this);
                return true;

            case R.id.menu_i_o_hunt:
                //launchIoHunt();
                return true;

            case R.id.menu_debug:
                //if (BuildConfig.DEBUG) {
                //    startActivity(new Intent(this, DebugActionRunnerActivity.class));
               // }
                return true;

            case R.id.menu_refresh:
                //requestDataRefresh();
                break;

            case R.id.menu_io_extended:
                //startActivity(new Intent(Intent.ACTION_VIEW,
                //        Uri.parse(Config.IO_EXTENDED_LINK)));
                break;
            */
            //case R.id.action_invite:
                //startActivity(new Intent(this, UIUtils.getMapActivityClass(this)));
                //finish();
            //    break;
        }
        // Handle home menu item, up navigation
        if (item.getItemId() == android.R.id.home) {
            onHomeSelected();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateAccountList(List<Account> accounts) {
        mAccountListContainer.removeAllViews();

    }

    protected void onAccountChangeRequested() {
        // override if you want to be notified when another account has been selected account has changed
    }

    private void setupAccountBoxToggle() {

    }


    private void launchIoHunt() {

    }

    protected void requestDataRefresh() {

    }

    private void goToNavDrawerItem(int item) {
        Intent intent;
        switch (item) {
            case NAVDRAWER_ITEM_EXPLORE:
                if (CheckGoogleService.access(BaseActivity.this)) {
                    intent = new Intent(this, LocalNextActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                } else {
                    intent = new Intent(this, CnLocalNextActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
            case NAVDRAWER_ITEM_VENUE:
                intent = new Intent(this, VenueTypeAndSearchActivity.class);
                startActivity(intent);
                finish();
                break;
            case NAVDRAWER_ITEM_WORKOUT:
                intent = new Intent(this, TrackListActivity.class);
                startActivity(intent);
                finish();
                break;
          //  case NAVDRAWER_ITEM_HISTORY:
          //      intent = new Intent(this, TrackHistoryActivity.class);
          //      startActivity(intent);
          //      finish();
          //      break;
            case NAVDRAWER_ITEM_MAP:
                if (CheckGoogleService.access(BaseActivity.this)) {
                    intent = new Intent(this, MapNextActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                } else {
                    intent = new Intent(this, CnMapNextActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }

            case NAVDRAWER_ITEM_MESSAGE:
                intent = new Intent(this, MessageListActivity.class);
                startActivity(intent);
                finish();
                break;
           // case NAVDRAWER_ITEM_PEOPLE_IVE_MET:
               // intent = new Intent(this, PeopleIveMetActivity.class);
               // startActivity(intent);
              //  finish();
           //     break;
            case NAVDRAWER_ITEM_SIGN_IN:
                //signInOrCreateAnAccount();
                break;
            case NAVDRAWER_ITEM_SETTINGS:
                intent = new Intent(this, SettingNextActivity.class);
                startActivity(intent);
                finish();
                break;
           // case NAVDRAWER_ITEM_VIDEO_LIBRARY:
              //  intent = new Intent(this, VideoLibraryActivity.class);
              //  startActivity(intent);
              ///  finish();
           //     break;
        }
    }


    private void onNavDrawerItemClicked(final int itemId) {
        if (itemId == getSelfNavDrawerItem()) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        if (isSpecialItem(itemId)) {
            goToNavDrawerItem(itemId);
        } else {
            // launch the target Activity after a short delay, to allow the close animation to play
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToNavDrawerItem(itemId);
                }
            }, NAVDRAWER_LAUNCH_DELAY);

            // change the active item on the list so the user can see the item changed
            setSelectedNavDrawerItem(itemId);
            // fade out the main content
            View mainContent = findViewById(R.id.main_content);
            if (mainContent != null) {
                mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
            }

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    protected void configureStandardMenuItems(Menu menu) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Verifies the proper version of Google Play Services exists on the device.
      //  PlayServicesUtils.checkGooglePlaySevices(this);

        // Watch for sync state changes
        mSyncStatusObserver.onStatusChanged(0);
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
    }

    /**
     * Converts an intent into a {@link Bundle} suitable for use as fragment arguments.
     */
    public static Bundle intentToFragmentArguments(Intent intent) {
        Bundle arguments = new Bundle();
        if (intent == null) {
            return arguments;
        }

        final Uri data = intent.getData();
        if (data != null) {
            arguments.putParcelable("_uri", data);
        }

        final Bundle extras = intent.getExtras();
        if (extras != null) {
            arguments.putAll(intent.getExtras());
        }

        return arguments;
    }

    /**
     * Converts a fragment arguments bundle into an intent.
     */
    public static Intent fragmentArgumentsToIntent(Bundle arguments) {
        Intent intent = new Intent();
        if (arguments == null) {
            return intent;
        }

        final Uri data = arguments.getParcelable("_uri");
        if (data != null) {
            intent.setData(data);
        }

        intent.putExtras(arguments);
        intent.removeExtra("_uri");
        return intent;
    }

   // @Override
   // public void onStart() {
   //     super.onStart();

        // Perform one-time bootstrap setup, if needed
     //   if (!PrefUtils.isDataBootstrapDone(this) && mDataBootstrapThread == null) {
     //       LOGD(TAG, "One-time data bootstrap not done yet. Doing now.");
//            performDataBootstrap();
     //   }

 //       startLoginProcess();
   // }

    /**
     * Performs the one-time data bootstrap. This means taking our prepackaged conference data
     * from the R.raw.bootstrap_data resource, and parsing it to populate the database. This
     * data contains the sessions, speakers, etc.
     */
    private void performDataBootstrap() {

        mDataBootstrapThread.start();
    }

    /**
     * Returns the default account on the device. We use the rule that the first account
     * should be the default. It's arbitrary, but the alternative would be showing an account
     * chooser popup which wouldn't be a smooth first experience with the app. Since the user
     * can easily switch the account with the nav drawer, we opted for this implementation.
     */
    private String getDefaultAccount() {
        return null;//accounts[0].name;
    }


    private void complainMustHaveGoogleAccount() {

    }

    private void promptAddAccount() {
        Intent intent = new Intent(Settings.ACTION_ADD_ACCOUNT);
        intent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, new String[]{"com.google"});
        startActivity(intent);
        finish();
    }

    private void startLoginProcess() {

      }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

   // @Override
   // public void onStop() {

   // }

    //@Override
    public void onPlusInfoLoaded(String accountName) {
        //setupAccountBox();
        populateNavDrawer();
    }

    /**
     * Called when authentication succeeds. This may either happen because the user just
     * authenticated for the first time (and went through the sign in flow), or because it's
     * a returning user.
     * @param accountName name of the account that just authenticated successfully.
     * @param newlyAuthenticated If true, this user just authenticated for the first time.
     * If false, it's a returning user.
     */
   // @Override
    public void onAuthSuccess(String accountName, boolean newlyAuthenticated) {

        //setupAccountBox();
        populateNavDrawer();
        //registerGCMClient();
    }

   // @Override
    public void onAuthFailure(String accountName) {
        refreshAccountDependantData();
    }

    protected void refreshAccountDependantData() {
        // Force local data refresh for data that depends on the logged user:
    }


    /**
     * Initializes the Action Bar auto-hide (aka Quick Recall) effect.
     */
    private void initActionBarAutoHide() {

    }

    /**
     * Indicates that the main content has scrolled (for the purposes of showing/hiding
     * the action bar for the "action bar auto hide" effect). currentY and deltaY may be exact
     * (if the underlying view supports it) or may be approximate indications:
     * deltaY may be INT_MAX to mean "scrolled forward indeterminately" and INT_MIN to mean
     * "scrolled backward indeterminately".  currentY may be 0 to mean "somewhere close to the
     * start of the list" and INT_MAX to mean "we don't know, but not at the start of the list"
     */
    private void onMainContentScrolled(int currentY, int deltaY) {
        if (deltaY > mActionBarAutoHideSensivity) {
            deltaY = mActionBarAutoHideSensivity;
        } else if (deltaY < -mActionBarAutoHideSensivity) {
            deltaY = -mActionBarAutoHideSensivity;
        }

        if (Math.signum(deltaY) * Math.signum(mActionBarAutoHideSignal) < 0) {
            // deltaY is a motion opposite to the accumulated signal, so reset signal
            mActionBarAutoHideSignal = deltaY;
        } else {
            // add to accumulated signal
            mActionBarAutoHideSignal += deltaY;
        }

        boolean shouldShow = currentY < mActionBarAutoHideMinY ||
                (mActionBarAutoHideSignal <= -mActionBarAutoHideSensivity);
        autoShowOrHideActionBar(shouldShow);
    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    protected void autoShowOrHideActionBar(boolean show) {
        if (show == mActionBarShown) {
            return;
        }

        mActionBarShown = show;
        onActionBarAutoShowOrHide(show);
    }

    protected void enableActionBarAutoHide(final ListView listView) {
        initActionBarAutoHide();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            final static int ITEMS_THRESHOLD = 3;
            int lastFvi = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                onMainContentScrolled(firstVisibleItem <= ITEMS_THRESHOLD ? 0 : Integer.MAX_VALUE,
                        lastFvi - firstVisibleItem > 0 ? Integer.MIN_VALUE :
                                lastFvi == firstVisibleItem ? 0 : Integer.MAX_VALUE
                );
                lastFvi = firstVisibleItem;
            }
        });
    }

    private View makeNavDrawerItem(final int itemId, ViewGroup container) {
        boolean selected = getSelfNavDrawerItem() == itemId;
        int layoutToInflate = 0;
        if (itemId == NAVDRAWER_ITEM_SEPARATOR) {
              layoutToInflate = R.layout.navdrawer_separator;
        } else if (itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL) {
              layoutToInflate = R.layout.navdrawer_separator;
        } else {
              layoutToInflate = R.layout.navdrawer_item;
        }
        View view = getLayoutInflater().inflate(layoutToInflate, container, false);

        if (isSeparator(itemId)) {
            // we are done
          //  UIUtils.setAccessibilityIgnore(view);
            return view;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        int iconId = itemId >= 0 && itemId < NAVDRAWER_ICON_RES_ID.length ?
                NAVDRAWER_ICON_RES_ID[itemId] : 0;
        int titleId = itemId >= 0 && itemId < NAVDRAWER_TITLE_RES_ID.length ?
                NAVDRAWER_TITLE_RES_ID[itemId] : 0;

        // set icon and text
        iconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
        if (iconId > 0) {
            iconView.setImageResource(iconId);
        }
        titleView.setText(getString(titleId));

        formatNavDrawerItem(view, itemId, selected);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavDrawerItemClicked(itemId);
            }
        });

        return view;
    }

    private boolean isSpecialItem(int itemId) {
        return itemId == NAVDRAWER_ITEM_SETTINGS;
    }

    private boolean isSeparator(int itemId) {
        return itemId == NAVDRAWER_ITEM_SEPARATOR || itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL;
    }

    private void formatNavDrawerItem(View view, int itemId, boolean selected) {
        if (isSeparator(itemId)) {
            // not applicable
            return;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        TextView titleView = (TextView) view.findViewById(R.id.title);

        if (selected) {
            view.setBackgroundResource(R.drawable.selected_navdrawer_item_background);
        }

        // configure its appearance according to whether or not it's selected
        titleView.setTextColor(selected ?
                getResources().getColor(R.color.navdrawer_text_color_selected) :
                getResources().getColor(R.color.navdrawer_text_color));
        iconView.setColorFilter(selected ?
                getResources().getColor(R.color.navdrawer_icon_tint_selected) :
                getResources().getColor(R.color.navdrawer_icon_tint));
    }

    /** Registers device on the GCM server, if necessary. */
    private void registerGCMClient() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

      //  if (mGCMRegisterTask != null) {
      //      mGCMRegisterTask.cancel(true);
      //  }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.unregisterOnSharedPreferenceChangeListener(this);
    }

    private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
        @Override
        public void onStatusChanged(int which) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    };

    protected void onRefreshingStateChanged(boolean refreshing) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(refreshing);
        }
    }

    protected void enableDisableSwipeRefresh(boolean enable) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(enable);
        }
    }

    protected void registerHideableHeaderView(View hideableHeaderView) {
        if (!mHideableHeaderViews.contains(hideableHeaderView)) {
            mHideableHeaderViews.add(hideableHeaderView);
        }
    }

    protected void deregisterHideableHeaderView(View hideableHeaderView) {
        if (mHideableHeaderViews.contains(hideableHeaderView)) {
            mHideableHeaderViews.remove(hideableHeaderView);
        }
    }

    public int getThemedStatusBarColor() {
        return mThemedStatusBarColor;
    }

    public void setNormalStatusBarColor(int color) {
        mNormalStatusBarColor = color;
        if (mDrawerLayout != null) {
            mDrawerLayout.setStatusBarBackgroundColor(mNormalStatusBarColor);
        }
    }

    protected void onActionBarAutoShowOrHide(boolean shown) {
        /*
        if (mStatusBarColorAnimator != null) {
            mStatusBarColorAnimator.cancel();
        }
        mStatusBarColorAnimator = ObjectAnimator.ofInt(
                (mDrawerLayout != null) ? mDrawerLayout : mLUtils,
                (mDrawerLayout != null) ? "statusBarBackgroundColor" : "statusBarColor",
                shown ? Color.BLACK : mNormalStatusBarColor,
                shown ? mNormalStatusBarColor : Color.BLACK)
                .setDuration(250);
        if (mDrawerLayout != null) {
            mStatusBarColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ViewCompat.postInvalidateOnAnimation(mDrawerLayout);
                }
            });
        }
        mStatusBarColorAnimator.setEvaluator(ARGB_EVALUATOR);
        mStatusBarColorAnimator.start();

        */

      //  updateSwipeRefreshProgressBarTop();

        for (View view : mHideableHeaderViews) {
            if (shown) {
                view.animate()
                        .translationY(0)
                        .alpha(1)
                        .setDuration(HEADER_HIDE_ANIM_DURATION)
                        .setInterpolator(new DecelerateInterpolator());
            } else {
                view.animate()
                        .translationY(-view.getBottom())
                        .alpha(0)
                        .setDuration(HEADER_HIDE_ANIM_DURATION)
                        .setInterpolator(new DecelerateInterpolator());
            }
        }
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return false;
    }


    /**
     * Callback when the home menu item is selected. E.g., setup the back stack
     * when home is selected.
     */
    protected void onHomeSelected() {
        finish();
    }
}
