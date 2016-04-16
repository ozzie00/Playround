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

package com.oneme.toplay.addfriend;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.third.geocoder.PlaceProvider;

public class SearchContactActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>   {

    // Places Listview
    ListView contactlist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Fix for an android 4.1.x bug */
        if(Build.VERSION.SDK_INT != Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            );
        }

        setContentView(R.layout.activity_search_contact);

        onSearchRequested();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
           //getSupportActionBar().setIcon(R.drawable.ic_actionbar);
        }


        // Get the intent, verify the action and get the query
      //  Intent intent = getIntent();
      //  if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
         //   String query = intent.getStringExtra(SearchManager.QUERY);
         //   doSearch(query);
     //   }

      //  Intent intent = getIntent();
      //  String query="";
      //  if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      //      query = intent.getStringExtra(SearchManager.QUERY);
      //  }

        handleIntent(getIntent());

        // Getting listview
        contactlist = (ListView) findViewById(R.id.list);

       // contactlist.setAdapter();

    }


    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {

    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            if (query.length() >= 1) {
                doSearch(query);
            } else {

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.OMEPARSEADDCONTACTSEARCHUSERNAMEERROR), Toast.LENGTH_LONG).show();

            }
        }
    }

    private void doSearch(String username) {

        if (username.length() >= 1 ) {
            Intent invokeSearchOtherActivityIntent = new Intent(SearchContactActivity.this, SearchOtherActivity.class);
            invokeSearchOtherActivityIntent.putExtra(Application.INTENT_EXTRA_USERNAME, username);
            invokeSearchOtherActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(invokeSearchOtherActivityIntent);
            //finish();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.OMEPARSEADDCONTACTSEARCHUSERNAMEERROR), Toast.LENGTH_LONG).show();
        }

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


    /*
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_search:
                onSearchRequested();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    */

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle query) {
        CursorLoader cLoader = null;
        if (arg0 == 0)
            cLoader = new CursorLoader(getBaseContext(), PlaceProvider.SEARCH_URI, null, null, new String[]{query.getString("query")}, null);
        else if (arg0 == 1)
            cLoader = new CursorLoader(getBaseContext(), PlaceProvider.DETAILS_URI, null, null, new String[]{query.getString("query")}, null);
        return cLoader;

    }



    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
        // showLocations(c);
    }

}
