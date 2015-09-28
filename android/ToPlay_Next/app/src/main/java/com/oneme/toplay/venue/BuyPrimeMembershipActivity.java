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

package com.oneme.toplay.venue;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Time;
import com.oneme.toplay.database.PayPrime;
import com.oneme.toplay.database.ThirdRequest;
import com.oneme.toplay.pay.PayBuyPrimeMembershipActivity;
import com.oneme.toplay.pay.SignUtils;
import com.oneme.toplay.ui.BaseActivity;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class BuyPrimeMembershipActivity extends BaseActivity {

    private ParseUser muser       = ParseUser.getCurrentUser();
    private String musername      = null;

    private final Context context = BuyPrimeMembershipActivity.this;

    private TextView mphoneText;
    private EditText mphoneedit;

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String SIGN            = "&sign=";
    private static final String SIGN_TYPE       = "&sign_type=RSA";

    private String m3rd;
    private String mname;
    private String mnameid;
    private String mcardname;
    private String mcardid;
    private String mcardprice;
    private String mcurrency = AppConstant.OMEPARSENULLSTRING;
    private String mcurrencysymbol = AppConstant.OMEPARSENULLSTRING;
    private String mphone;
    private String mpayno;
    private String mnumber = "1";
    private String mhttprequest;
    private StringBuilder mhttpjsonResult = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_buy_prime_membership);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 finish();
                //navigateUpTo(IntentCompat.makeMainActivity(new ComponentName(BookingActivity.this,
                //        DetailInfoActivity.class)));
            }
        });

        if (muser != null) {
            musername = muser.getUsername();
        }

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            m3rd       = extras.getString(Application.INTENT_EXTRA_VENUEJSON3RD);
            mname      = extras.getString(Application.INTENT_EXTRA_VENUEJSONNAME);
            mnameid    = extras.getString(Application.INTENT_EXTRA_VENUEJSONNAMEID);
            mcurrency  = extras.getString(Application.INTENT_EXTRA_VENUEJSONCURRENCY);
            mcardname  = extras.getString(Application.INTENT_EXTRA_VENUEJSONCARDNAME);
            mcardid    = extras.getString(Application.INTENT_EXTRA_VENUEJSONCARDID);
            mcardprice = extras.getString(Application.INTENT_EXTRA_VENUEJSONCARDPRICE);
        }

        // set prime venue textview
        TextView mnametext = (TextView)findViewById(R.id.prime_membership_venue_view);
        mnametext.setText(mname + " " + mcardname);

        // set phone block
        RelativeLayout mphonerelative = (RelativeLayout)findViewById(R.id.prime_membership_phone_block);
        mphoneText                    = (TextView)findViewById(R.id.prime_membership_phone_text_view);

        if (muser != null) {
            if (muser.getString(AppConstant.OMEPARSEUSERPHONEKEY) != null) {
                String mphonenumber = muser.getString(AppConstant.OMEPARSEUSERPHONEKEY);
                mphoneText.setText(mphonenumber);
                mphone = mphonenumber;
            }
        }

        mphonerelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom phone number dialog
                final Dialog phonedialog = new Dialog(context);
                phonedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                phonedialog.setContentView(R.layout.ome_activity_add_phone_number_dialog);

                mphoneedit = (EditText)phonedialog.findViewById(R.id.phone_dialog_edittext);

                // set the custom dialog components - text, image and button
                TextView phonetitle = (TextView) phonedialog.findViewById(R.id.phone_dialog_title);
                phonetitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phonedialog.dismiss();
                    }
                });

                TextView phonedone  = (TextView) phonedialog.findViewById(R.id.phone_dialog_OK);
                // if TextView is clicked, close the custom dialog
                phonedone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mphone = mphoneedit.getText().toString();
                        phonedialog.dismiss();
                        mphoneText.setText(mphone);
                    }
                });

                phonedialog.show();
            }
        });

        // set fee
        for (Locale ll: Locale.getAvailableLocales()){
            try {
                Currency currency = Currency.getInstance(ll);

                if (mcurrency.equals(currency.getCurrencyCode()) ) {
                    mcurrencysymbol = currency.getSymbol();
                }

            }catch (Exception e){
                // when the locale is not supported
            }
        }

        TextView currency  = (TextView) findViewById(R.id.prime_membership_currency);
        currency.setText(mcurrencysymbol);
        currency.setTextColor(getResources().getColor(R.color.playround_default));

        final TextView mfee = (TextView)findViewById(R.id.prime_membership_fee_text);
        mfee.setText(mcardprice);
        if (muser != null) {
            mfee.setVisibility(View.VISIBLE);
            mfee.setTextColor(getResources().getColor(R.color.playround_default));
        }

        // set confirm button
        Button confirm = (Button) findViewById(R.id.prime_membership_confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invokePayBuyPrimeMembershipActivityIntent = new Intent(BuyPrimeMembershipActivity.this, PayBuyPrimeMembershipActivity.class);

                if (m3rd != null) {
                    invokePayBuyPrimeMembershipActivityIntent.putExtra(Application.INTENT_EXTRA_VENUEJSON3RD, m3rd);
                }

                if (mname != null) {
                    invokePayBuyPrimeMembershipActivityIntent.putExtra(Application.INTENT_EXTRA_VENUEJSONNAME, mname);
                }

                if (mnameid != null) {
                    invokePayBuyPrimeMembershipActivityIntent.putExtra(Application.INTENT_EXTRA_VENUEJSONNAMEID, mnameid);
                }

                if (mcurrencysymbol != null) {
                    invokePayBuyPrimeMembershipActivityIntent.putExtra(Application.INTENT_EXTRA_VENUEJSONCURRENCY, mcurrencysymbol);
                }

                if (mcardname != null) {
                    invokePayBuyPrimeMembershipActivityIntent.putExtra(Application.INTENT_EXTRA_VENUEJSONCARDNAME, mcardname);
                }

                if (mcardid != null) {
                    invokePayBuyPrimeMembershipActivityIntent.putExtra(Application.INTENT_EXTRA_VENUEJSONCARDID, mcardid);
                }

                if (mcardprice != null) {
                    invokePayBuyPrimeMembershipActivityIntent.putExtra(Application.INTENT_EXTRA_VENUEJSONCARDPRICE, mcardprice);
                }

                if (mphone != null) {
                    invokePayBuyPrimeMembershipActivityIntent.putExtra(Application.INTENT_EXTRA_USERPHONE, mphone);
                }

                if (muser != null) {
                    startActivityForResult(invokePayBuyPrimeMembershipActivityIntent, AppConstant.OMEPARSEBUYPRIMEMEMBERSHIPPAYRESULT);
                } else {
                    Toast.makeText(BuyPrimeMembershipActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.OMEPARSEBUYPRIMEMEMBERSHIPPAYRESULT && resultCode == ActionBarActivity.RESULT_OK) {
            Toast.makeText(BuyPrimeMembershipActivity.this, getResources().getString(R.string.OMEPARSEPAYBUYPRIMEMEMBERSHIPNOTIFICATION),
                    Toast.LENGTH_LONG).show();
            mpayno = data.getStringExtra(Application.INTENT_EXTRA_PRIMEPAYNO);

            ParseQuery<ThirdRequest> query = ThirdRequest.getQuery();
            query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
            query.include(AppConstant.OMEPARSEINVITELIKEAUTHORKEY);
            query.whereEqualTo(AppConstant.OMEPARSETHIRDNAMEKEY, m3rd);
            query.whereEqualTo(AppConstant.OMEPARSETHIRDSERVICEKEY, AppConstant.OMEPARSETHIRDSERVICEPRIME);
            query.orderByDescending(AppConstant.OMEPARSECREATEDATKEY);
            query.setLimit(1);
            query.findInBackground(new FindCallback<ThirdRequest>() {
                @Override
                public void done(List<ThirdRequest> list, ParseException e) {
                    if (e == null) {
                        mhttpjsonResult.append(list.get(0).getHttp());
                        if (mpayno != null) {
                            new getPrimeMembership().execute(mpayno);
                        }
                    }
                }
            });

        }

    }

    class getPrimeMembership extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... key) {

            String paynumber = key[0];

            HttpURLConnection mconnection = null;
            StringBuilder jsonResults     = new StringBuilder();
            StringBuilder mstringBuilder  = null;

            PayPrime mpayprime = new PayPrime();
            mpayprime.setUsername(musername);
            mpayprime.set3rd(m3rd);
            mpayprime.setVenuename(mname);
            mpayprime.setVenueId(mnameid);
            mpayprime.setCardname(mcardname);
            mpayprime.setCardId(mcardid);
            mpayprime.setCardPrice(mcardprice);
            mpayprime.setPayCurrency(mcurrency);
            mpayprime.setPayNumber(paynumber);
            mpayprime.setUserphone(mphone);

            try {
                // Create a JSON object hierarchy from the http json results
                JSONObject httpjsonObj = new JSONObject(mhttpjsonResult.toString());
                mstringBuilder         = new StringBuilder(httpjsonObj.getString(AppConstant.OMEPARSETHIRDHTTPJSONAPIBASE));
                mstringBuilder.append(httpjsonObj.getString(AppConstant.OMEPARSETHIRDHTTPJSONAPIMOBILE) + mphone);
                mstringBuilder.append(httpjsonObj.getString(AppConstant.OMEPARSETHIRDHTTPJSONAPICARDID) + mcardid);
                mstringBuilder.append(httpjsonObj.getString(AppConstant.OMEPARSETHIRDHTTPJSONAPICARDNUMBER) + mnumber);
                mstringBuilder.append(httpjsonObj.getString(AppConstant.OMEPARSETHIRDHTTPJSONAPIPAYNUMBER) + paynumber);

                // signature with RSA
                String signature = SignUtils.sign(mstringBuilder.toString(), PayBuyPrimeMembershipActivity.RSA_PRIVATE);
                try {
                    // URL encode sign
                    signature = URLEncoder.encode(signature, DEFAULT_CHARSET);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // signature information
                final String signinfo = SIGN + signature + SIGN_TYPE;

                mstringBuilder.append(signinfo);

                URL murl             = new URL(mstringBuilder.toString());
                mconnection          = (HttpURLConnection) murl.openConnection();
                InputStreamReader in = new InputStreamReader(mconnection.getInputStream());

                // Load the results into a StringBuilder
                int read;
                char[] buff  = new char[AppConstant.OMEPARSEBUFFERLENGTH];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
            } catch (MalformedURLException me) {

            } catch (IOException ie) {

            } catch (JSONException je) {

            } finally {
                if (mconnection != null) {
                    mconnection.disconnect();
                }
            }

            try {
                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(jsonResults.toString());

                String third     = jsonObj.getString(AppConstant.SPORT_JSON_3RD);
                String payno     = jsonObj.getString(AppConstant.SPORT_JSON_PAYNO);
                String sms       = jsonObj.getString(AppConstant.SPORT_JSON_SMSCODE);
                String errorcode = jsonObj.getString(AppConstant.SPORT_JSON_ERRCODE);
                String other     = AppConstant.OMEPARSENULLSTRING;

                if (!errorcode.equals(AppConstant.SPORT_JSON_SUCCESS)) {
                    other = errorcode;
                }

                if (m3rd.equals(third) && mpayno.equals(payno)) {

                    mpayprime.set3rd(m3rd);
                    mpayprime.setPayNumber(paynumber);
                    mpayprime.setSMSCode(sms);
                    mpayprime.setPayTime(Time.currentTime());
                    mpayprime.setOther(other);

                    ParseACL acl = new ParseACL();

                    // No give public read access
                    acl.setPublicReadAccess(false);
                    acl.setWriteAccess(muser, false);
                    mpayprime.setACL(acl);

                    mpayprime.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                finish();
                            }
                        }
                    });

                }

            } catch (JSONException je) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {

            }
        }

    }

}
