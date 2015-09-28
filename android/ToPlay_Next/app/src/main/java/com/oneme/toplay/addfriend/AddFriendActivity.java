package com.oneme.toplay.addfriend;

//import im.tox.jtoxcore.FriendExistsException;
//import im.tox.jtoxcore.ToxException;

//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;


public class AddFriendActivity { //extends ActionBarActivity implements PinDialogFragment.PinDialogListener {

    /*
    private static String TAG = "AddFriendActivity";

    String _friendID         = "";
    String _friendCHECK      = "";
    String _originalUsername = "";

    boolean isV2             = false;

    static Context mcontext;
    CharSequence text;
    int duration = Toast.LENGTH_SHORT;
    Toast toast;

    EditText friendID;
    EditText friendMessage;
    EditText friendAlias;

    TextView addmeusernameText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fix for an android 4.1.x bug
        if(Build.VERSION.SDK_INT != Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            );
        }

        setContentView(R.layout.activity_add_friend);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            getSupportActionBar().setIcon(R.drawable.ic_actionbar);
        }

        mcontext = getApplicationContext();
        text    = getString(R.string.addfriend_friend_added);

        friendID      = (EditText) findViewById(R.id.addfriend_key);
        friendMessage = (EditText) findViewById(R.id.addfriend_message);
        friendAlias   = (EditText) findViewById(R.id.addfriend_friendAlias);

        Intent intent = getIntent();
        //If coming from tox uri link
        if (Intent.ACTION_VIEW.equals(intent.getAction())
                && intent != null) {
            EditText friendID = (EditText) findViewById(R.id.addfriend_key);
            Uri uri;
            uri               = intent.getData();
            if (uri != null)
                friendID.setText(uri.getHost());
            //TODO: ACCEPT DNS LOOKUPS FROM URI

        } else if (intent.getAction() == "toxv2") {
            //else if it came from toxv2 restart

            friendID.setText(intent.getStringExtra("originalUsername"));
            friendAlias.setText(intent.getStringExtra("alias"));
            friendMessage.setText(intent.getStringExtra("message"));

            if(checkAndSend(intent.getStringExtra("key"), intent.getStringExtra("originalUsername")) == 0) {
                toast = Toast.makeText(mcontext, text, duration);
                toast.show();
            } else if (checkAndSend(intent.getStringExtra("key"), intent.getStringExtra("originalUsername")) == AppConstant.OMETOPLAYNOVALIDPUBLICKEY) {
                toast = Toast.makeText(mcontext, getResources().getString(R.string.OMEPARSEADDCONTACTSENDREQUESTINVALIDFRIENDID), Toast.LENGTH_SHORT);
                toast.show();
                return;
            } else if (checkAndSend(intent.getStringExtra("key"), intent.getStringExtra("originalUsername")) == AppConstant.OMETOPLAYFRIENDEXIST) {
                toast = Toast.makeText(mcontext, getString(R.string.OMEPARSEADDCONTACTSENDREQUESTFRIENDEXIST), Toast.LENGTH_SHORT);
                toast.show();
            }

            Intent update = new Intent(Constants.BROADCAST_ACTION);
            update.putExtra("action", Constants.UPDATE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(update);
            Intent i      = new Intent();
            setResult(RESULT_OK, i);

            // Close activity
            finish();
        }



        // search contact
        RelativeLayout searchcontact = (RelativeLayout) findViewById(R.id.search_contact_block);
        searchcontact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent invokeSearchContactActivityIntent = new Intent(AddFriendActivity.this, SearchContactActivity.class);
                startActivity(invokeSearchContactActivityIntent);

            }
        });

        addmeusernameText = (TextView) findViewById(R.id.addme_username_view);

        if (ParseUser.getCurrentUser()  != null) {
            addmeusernameText.setText(ParseUser.getCurrentUser().getUsername());
        }

        // add me
        RelativeLayout addme = (RelativeLayout) findViewById(R.id.addme_block);
        addme.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent invokeShowQRCodeActivityIntent = new Intent(AddFriendActivity.this, ShowQRcodeActivity.class);
                startActivity(invokeShowQRCodeActivityIntent);
            }
        });

        // search friend radar
        RelativeLayout friendradar = (RelativeLayout) findViewById(R.id.friend_radar_block);
        friendradar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent invokeFriendRadarActivityIntent = new Intent(AddFriendActivity.this, FriendRadarActivity.class);
                startActivity(invokeFriendRadarActivityIntent);

            }
        });

        // add friend by scan qrcode
        RelativeLayout scanqrcode = (RelativeLayout) findViewById(R.id.scan_qrcode_block);
        scanqrcode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               scanIntent();
            }
        });


    }

    private static boolean isKeyOwn(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mcontext);
        String tmp = preferences.getString(AppConstant.OMEPARSEUSERIDKEY, "");
        if(tmp.toLowerCase().startsWith(AppConstant.OMEPARSEUSERIDHEAER))
            tmp = tmp.substring(AppConstant.OMEPARSEUSERIDHEAER.length());
        if(tmp.equals(key))
            return true;
        else
            return false;
    }

    private int checkAndSend(String key, String originalUsername) {

        if(!isKeyOwn(key)) {
            if (validateFriendKey(key)) {
                String ID      = key;
                String message = friendMessage.getText().toString();
                String alias   = friendAlias.getText().toString();

                // Check to see if message was blank, if so set a default
                if(message.equals("")) {
                    if (ParseUser.getCurrentUser() != null) {
                        message = getString(R.string.OMEPARSEADDCONTACTREQUESTNOTEIAM)
                                + " " + ParseUser.getCurrentUser().getUsername()
                                + " " + getString(R.string.addfriend_default_message);
                    }
                }

                String[] friendData = {ID, message, alias};

                Database mDatabase = new Database(getApplicationContext());
                if (!mDatabase.doesFriendExist(ID)) {
                    try {
                        Singleton mSingleton = Singleton.getInstance();
                        if (Application.APPDEBUG) {
                            Log.d(TAG + " Addfriend " + "key " + friendData[0] + "message  " + friendData[1], "Adding friend to database");
                        }
                        mSingleton.jTox.addFriend(friendData[0], friendData[1]);
                    } catch (ToxException e) {
                        e.printStackTrace();
                    } catch (FriendExistsException e) {
                        e.printStackTrace();
                    }

                    if (Application.APPDEBUG) {
                        Log.d("AddFriendActivity", "Adding friend to database");
                    }
                    mDatabase.addFriend(ID, "Friend Request Sent",alias, originalUsername);
                } else {
                    return AppConstant.OMETOPLAYFRIENDEXIST;
                }
                mDatabase.close();
                return 0;
            } else {
                return  AppConstant.OMETOPLAYNOVALIDPUBLICKEY;
            }
        } else {
            return  AppConstant.OMETOPLAYNOKEYOWN;
        }
    }



    //
    // method is outside so that the intent can be passed this object
    //
    private void scanIntent() {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    public void addFriend(View view) {

        if(friendID.getText().toString().contains("@") || friendID.getText().length() != AppConstant.OMETOPLAYUSERPUBLICKEYLENGTH) {
            _originalUsername = friendID.getText().toString();
            // Get the first TXT record
            try {
                //.get() is a possible ui lag on very slow internet connections where dns lookup takes a long time

                // Ozzie Zhang 2014-12-21 disable this code
              //  new DNSLookup().execute(friendID.getText().toString()).get();
            } catch (Exception e) {
                if (Application.APPDEBUG) {
                    e.printStackTrace();
                }
            }
        }

        if(isV2) {
            DialogFragment dialog = new PinDialogFragment();
            Bundle bundle         = new Bundle();
            bundle.putString(getResources().getString(R.string.addfriend_friend_pin_title), getResources().getString(R.string.addfriend_friend_pin_text));
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
        }

        String finalFriendKey = friendID.getText().toString();

        if(!_friendID.equals(""))
            finalFriendKey = _friendID;

        if(!isV2) {

            int result = checkAndSend(finalFriendKey, _originalUsername);

            if(result == 0) {
                toast = Toast.makeText(mcontext, text, duration);
                toast.show();
            } else if(result == AppConstant.OMETOPLAYNOVALIDPUBLICKEY) {
                toast = Toast.makeText(mcontext, getResources().getString(R.string.OMEPARSEADDCONTACTSENDREQUESTINVALIDFRIENDID), Toast.LENGTH_SHORT);
                toast.show();
                return;
            } else if(result == AppConstant.OMETOPLAYFRIENDEXIST) {
                toast = Toast.makeText(mcontext, getResources().getString(R.string.OMEPARSEADDCONTACTSENDREQUESTFRIENDEXIST), Toast.LENGTH_SHORT);
                toast.show();
            } else if(result == AppConstant.OMETOPLAYNOKEYOWN) {
                toast = Toast.makeText(mcontext, getResources().getString(R.string.OMEPARSEADDCONTACTSENDREQUESTADDYOURSELF), Toast.LENGTH_SHORT);
                toast.show();
            }

            Intent update = new Intent(Constants.BROADCAST_ACTION);
            update.putExtra("action", Constants.UPDATE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(update);
            Intent i      = new Intent();
            setResult(RESULT_OK, i);

            finish();
        }
    }


    public void addFriendForQRcode(String FriendPublicKey) {

        if(isV2) {
            DialogFragment dialog = new PinDialogFragment();
            Bundle bundle         = new Bundle();
            bundle.putString(getResources().getString(R.string.addfriend_friend_pin_title), getResources().getString(R.string.addfriend_friend_pin_text));
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
        }

        String finalFriendKey = FriendPublicKey; //friendID.getText().toString();

        if(!isV2) {



            int result = checkAndSend(finalFriendKey, _originalUsername);

            if(result == 0) {
                toast = Toast.makeText(mcontext, text, duration);
                toast.show();
            } else if(result == AppConstant.OMETOPLAYNOVALIDPUBLICKEY) {
                toast = Toast.makeText(mcontext, getResources().getString(R.string.OMEPARSEADDCONTACTSENDREQUESTINVALIDFRIENDID), Toast.LENGTH_SHORT);
                toast.show();
                return;
            } else if(result == AppConstant.OMETOPLAYFRIENDEXIST) {
                toast = Toast.makeText(mcontext, getResources().getString(R.string.OMEPARSEADDCONTACTSENDREQUESTFRIENDEXIST), Toast.LENGTH_SHORT);
                toast.show();
            } else if(result == AppConstant.OMETOPLAYNOKEYOWN) {
                toast = Toast.makeText(mcontext, getResources().getString(R.string.OMEPARSEADDCONTACTSENDREQUESTADDYOURSELF), Toast.LENGTH_SHORT);
                toast.show();
            }

            Intent update = new Intent(Constants.BROADCAST_ACTION);
            update.putExtra(AppConstant.OMEPARSEADDFRIENDACTION, Constants.UPDATE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(update);
            Intent i      = new Intent();
            setResult(RESULT_OK, i);

            finish();
        }
    }

    public static int addFriendUsingID(Context context, String FriendPublicKey, String FriendUsername, String RequestMessage) {
        if(!isKeyOwn(FriendPublicKey)) {
            if (validateFriendKey(FriendPublicKey)) {
                String ID      = FriendPublicKey;
                String message = RequestMessage;//friendMessage.getText().toString();
                String alias   = "";//friendAlias.getText().toString();

                if (message != null) {
                    message = "he want to be your friend !"; // getResources().getString(R.string.OMEPARSEADDCONTACTDEFAULTREQUESTMESSAGE);;
                }

                String[] friendData = {ID, message, alias};

                Database mDatabase = new Database(context);
              //  Database mDatabase = new Database(getApplicationContext());
                if (!mDatabase.doesFriendExist(ID)) {
                    try {
                        Singleton mSingleton = Singleton.getInstance();
                        if (Application.APPDEBUG) {
                            Log.d(TAG, " ID " + friendData[0] + " Message " + friendData[1]);
                        }
                        mSingleton.jTox.addFriend(friendData[0], friendData[1]);
                    } catch (ToxException e) {
                        e.printStackTrace();
                    } catch (FriendExistsException e) {
                        e.printStackTrace();
                    }

                    if (Application.APPDEBUG) {
                        Log.d("AddFriendActivity", "Adding friend to database");
                    }
                    mDatabase.addFriend(ID, "Friend Request Sent",alias, FriendUsername);
                } else {
                    return AppConstant.OMETOPLAYFRIENDEXIST;
                }
                mDatabase.close();
                return 0;
            } else {
                return  AppConstant.OMETOPLAYNOVALIDPUBLICKEY;
            }
        } else {
            return  AppConstant.OMETOPLAYNOKEYOWN;
        }
    }



    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String pin) {
        pin = pin + "==";
        //Base64 to Bytes
        try {
            byte[] decoded = Base64.decode(pin, Base64.DEFAULT);

            //Bytes to Hex
            StringBuilder sb = new StringBuilder();
            for(byte b: decoded)
                sb.append(String.format("%02x", b&0xff));
            String encodedString = sb.toString();

            //Finally set the correct ID to add
            _friendID = _friendID + encodedString + _friendCHECK;

            //Restart activity with info needed
            Intent restart = new Intent(this, AddFriendActivity.class);
            restart.putExtra("key", _friendID);
            restart.putExtra("alias", friendAlias.getText().toString());
            restart.putExtra("message", friendMessage.getText().toString());
            restart.putExtra("originalUsername", _originalUsername);
            restart.setAction("toxv2");
            startActivity(restart);

            finish();

        } catch (IllegalArgumentException e) {
            Context context   = getApplicationContext();
            CharSequence text = getString(R.string.addfriend_invalid_pin);
            int duration      = Toast.LENGTH_SHORT;
            Toast toast       = Toast.makeText(context, text, duration);
            toast.show();
            e.printStackTrace();
        }

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {}

    //
    // handle intent to read a friend QR code
    //
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            if (scanResult.getContents() != null) {
                EditText addFriendKey = (EditText) findViewById(R.id.addfriend_key);
                String friendKey      = (scanResult.getContents().toLowerCase().contains(AppConstant.OMEPARSEUSERIDHEAER) ? scanResult.getContents().substring(AppConstant.OMEPARSEUSERIDHEAER.length()) : scanResult.getContents());
                if (validateFriendKey(friendKey)) {
                    //Ozzie Zhang 2014-12-18 change this code
                   // addFriendKey.setText(friendKey);
                    addFriendForQRcode(friendKey);
                } else {
                    Context context = getApplicationContext();
                    Toast toast     = Toast.makeText(context, getResources().getString(R.string.OMEPARSEADDCONTACTSENDREQUESTINVALIDFRIENDID), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }

    private static boolean validateFriendKey(String friendKey) {

        if (friendKey == null) {
            if (Application.APPDEBUG) {
                Log.d(TAG, "friendKey is null");
            }
            return false;
        }

        if (friendKey.length() != AppConstant.OMETOPLAYUSERPUBLICKEYLENGTH || friendKey.matches("[[:xdigit:]]")) {
            return false;
        }
        int x = 0;
        try {
            for (int i = 0; i < friendKey.length(); i += 4) {
                x = x ^ Integer.valueOf(friendKey.substring(i, i + 4), 16);
            }
        }
        catch (NumberFormatException e) {
            return false;
        }
        return x == 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.add_friend, menu);
        return true;
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
                NavUtils.navigateUpFromSameTask(this);
                return true;
            //scanQR button to call the barcode reader app
            //case R.id.scanFriend:
            //    scanIntent();
            //    break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DNSLookup extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... params) {

            // If just a username was passed and not a full domain
            String user, domain, lookup;
            if(!params[0].contains("@")) {
                user   = params[0];
                domain = "toxme.se";
                lookup = user + "._tox." + domain;
            } else {
                user   = params[0].substring(0, params[0].indexOf("@"));
                domain = params[0].substring(params[0].indexOf("@") + 1);
                lookup = user + "._tox." + domain;
            }

            TXTRecord txt = null;
            try {
                Record[] records = new Lookup(lookup, Type.TXT).run();
                txt              = (TXTRecord) records[0];
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(txt != null) {
                String txtString = txt.toString().substring(txt.toString().indexOf('"'));

                if(txtString.contains("tox1")) {
                    String key = txtString.substring(11, 11+76);
                    _friendID  = key;

                } else if (txtString.contains("tox2")) {
                    isV2         = true;
                    String key   = txtString.substring(12, 12+64);
                    String check = txtString.substring(12+64+7,12+64+7+4);
                    _friendID    = key;
                    _friendCHECK = check;
                }
            }

            return null;
        }
    }
    */

}
