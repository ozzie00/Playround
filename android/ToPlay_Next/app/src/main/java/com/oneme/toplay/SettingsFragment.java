package com.oneme.toplay;

import android.preference.PreferenceActivity;

//import im.tox.jtoxcore.ToxException;
//import im.tox.jtoxcore.ToxUserStatus;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsFragment { //extends com.github.machinarius.preferencefragment.PreferenceFragment
        //implements SharedPreferences.OnSharedPreferenceChangeListener {

    /*

    // user last location
    private ParseGeoPoint userLastLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceCategory header = new PreferenceCategory(getActivity());

        // Add Profile header and preferences
        addPreferencesFromResource(R.xml.pref_profile);

        // Add Notification header and preferences
        header = new PreferenceCategory(getActivity());
        header.setTitle(R.string.pref_header_notifications);
        getPreferenceScreen().addPreference(header);
        addPreferencesFromResource(R.xml.pref_notification);

        // Add Other header and preferences
        header = new PreferenceCategory(getActivity());
        header.setTitle(R.string.pref_header_other);
        getPreferenceScreen().addPreference(header);
        addPreferencesFromResource(R.xml.pref_other);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to
        // their values. When their values change, their summaries are updated
        // to reflect the new value, per the Android Design guidelines.

        bindPreferenceSummaryToValue(findPreference(AppConstant.OMEPARSEUSERNICKNAMEKEY));
        bindPreferenceSummaryToValue(findPreference(AppConstant.OMEPARSEUSERSTATUSKEY));
        bindPreferenceSummaryToValue(findPreference(AppConstant.OMEPARSEUSERSTATUALERTSKEY));
        bindPreferenceSummaryToValue(findPreference(AppConstant.OMEPARSELANGUAGEKEY));
        bindPreferenceSummaryToValue(findPreference(AppConstant.OMEPARSEUSERIDKEY));
        bindPreferenceSummaryToValue(findPreference(AppConstant.OMEPARSEUSERACTIVEACCOUNTSKEY));

        // Override the ID click functionality to display a dialog with the qr image
        // and copy to clipboard button
        //
        Preference IDPreference = (Preference) findPreference(AppConstant.OMEPARSEUSERIDKEY);
        IDPreference.setOnPreferenceClickListener(new EditTextPreference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DialogFragment dialog = new DialogID(getActivity());
                Bundle bundle         = new Bundle();
                bundle.putString("Enter Friend's Pin", "Enter Friend's Pin");
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "NoticeDialogFragment");
                return true;
            }
        });

        Preference logoutPreference = (Preference) findPreference(AppConstant.OMEPARSELOGOUTKEY);
        logoutPreference.setOnPreferenceClickListener(new EditTextPreference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences preferences   = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(AppConstant.OMEPARSEUSERLOGGEDINSKEY, false);
                editor.apply();

                //get point according to  current latitude and longitude
                userLastLocation = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));

                if (ParseUser.getCurrentUser() != null) {
                    ParseUser.getCurrentUser().put(AppConstant.OMEPARSEUSERLASTTIMEKEY, Time.currentTime());
                    ParseUser.getCurrentUser().put(AppConstant.OMEPARSEUSERLASTLOCATIONKEY, userLastLocation);

                    ParseUser.getCurrentUser().saveInBackground();

                    // Call the Parse log out method
                    ParseUser.logOut();
                }

                // Stop the Core Service
                Intent stopCoreServiceIntent = new Intent(getActivity().getApplicationContext(), CoreService.class);
                getActivity().getApplicationContext().stopService(stopCoreServiceIntent);

                // Finish this activity
                getActivity().finish();

                return true;
            }
        });

        Preference nospamPreference = (Preference) findPreference(AppConstant.OMEPARSENOSPAMKEY);
        nospamPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Singleton mSingleton = Singleton.getInstance();
                try {
                    Random random = new Random();
                    int nospam    = random.nextInt(1234567890);
                    mSingleton.jTox.setNospam(nospam);
                    SharedPreferences preferences   = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString(AppConstant.OMEPARSEUSERIDKEY, mSingleton.jTox.getAddress());



                    editor.commit();
                    bindPreferenceSummaryToValue(findPreference(AppConstant.OMEPARSEUSERIDKEY));
                } catch (ToxException e) {
                    if (Application.APPDEBUG) {
                        e.printStackTrace();
                    }
                }

                return true;
            }
        });
    }

    //
    // Binds a preference's summary to its value. More specifically, when the
    // preference's value is changed, its summary (line of text below the
    // preference title) is updated to reflect the value. The summary is also
    // immediately updated upon calling this method. The exact display format is
    // dependent on the type of preference.
    //
    // @see #sBindPreferenceSummaryToValueListener
    //
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index                     = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    // Callback will handle updating the new settings on the tox network
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        IdentityDatabase mIdentityDatabase = new IdentityDatabase(getActivity());

        if (key.equals(AppConstant.OMEPARSEUSERNICKNAMEKEY)) {

            Singleton mSingleton = Singleton.getInstance();
            try {
                mSingleton.jTox.setName(sharedPreferences.getString(key, ""));
            } catch (ToxException e) {
                if (Application.APPDEBUG) {
                    e.printStackTrace();
                }
            }

            // Update user DB
            mIdentityDatabase.updateUserDetail(sharedPreferences.getString(AppConstant.OMEPARSEUSERACTIVEACCOUNTSKEY, ""),
                    AppConstant.OMEPARSEUSERNICKNAMEKEY, sharedPreferences.getString(key, ""));
        }

        if (key.equals(AppConstant.OMEPARSEUSERSTATUSKEY)) {

            ToxUserStatus newStatus = ToxUserStatus.TOX_USERSTATUS_NONE;
            String newStatusString  = sharedPreferences.getString(key, "");
            newStatus               = UserStatus.getToxUserStatusFromString(newStatusString);

            Singleton mSingleton    = Singleton.getInstance();
            try {
                mSingleton.jTox.setUserStatus(newStatus);
            } catch (ToxException e) {
                if (Application.APPDEBUG) {
                    e.printStackTrace();
                }
            }

            // Update user DB
            mIdentityDatabase.updateUserDetail(sharedPreferences.getString(AppConstant.OMEPARSEUSERACTIVEACCOUNTSKEY, ""),
                    AppConstant.OMEPARSEUSERSTATUSKEY, sharedPreferences.getString(key, ""));
        }

        if (key.equals(AppConstant.OMEPARSEUSERSTATUALERTSKEY)) {

            Singleton mSingleton = Singleton.getInstance();
            try {
                mSingleton.jTox.setStatusMessage(sharedPreferences.getString(key, ""));
            } catch (ToxException e) {
                if (Application.APPDEBUG) {
                    e.printStackTrace();
                }
            }

            // Update user DB
            mIdentityDatabase.updateUserDetail(sharedPreferences.getString(AppConstant.OMEPARSEUSERACTIVEACCOUNTSKEY, ""),
                    AppConstant.OMEPARSEUSERSTATUALERTSKEY, sharedPreferences.getString(key, ""));
        }

        if (key.equals("enable_udp")) {
            Singleton mSingleton = Singleton.getInstance();

            Options.udpEnabled = sharedPreferences.getBoolean("enable_udp", false);

            // Stop service
            Intent service = new Intent(getActivity(), CoreService.class);
            getActivity().stopService(service);
            // Start service
            getActivity().startService(service);
        }

        if(key.equals(AppConstant.OMEPARSEWIFIONLYKEY)) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            final ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            boolean wifiOnly = sharedPreferences.getBoolean(AppConstant.OMEPARSEWIFIONLYKEY, true);

            // Set all offline as we wont receive callbacks for them by not doing doTox()
            if(wifiOnly && !mWifi.isConnected()) {
                Database mDatabase = new Database(getActivity());
                mDatabase.setAllOffline();
                mDatabase.close();

            }
        }

        if(key.equals(AppConstant.OMEPARSELANGUAGEKEY)) {
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
        }
    }

    */
}
