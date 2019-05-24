package ru.club.sfera;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ru.club.sfera.common.ActivityBase;
import ru.club.sfera.dialogs.ItemDeleteDialog;
import ru.club.sfera.dialogs.ItemReportDialog;
import ru.club.sfera.dialogs.SendRepostDialog;

public class HashtagActivity extends ActivityBase implements ItemDeleteDialog.AlertPositiveListener, ItemReportDialog.AlertPositiveListener, SendRepostDialog.AlertPositiveListener {

    Toolbar mToolbar;

    Fragment fragment;
    Boolean restore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hashtag);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState != null) {

            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");

            restore = savedInstanceState.getBoolean("restore");

        } else {

            fragment = new HashtagFragment();

            Intent i = getIntent();

            String hashtag = i.getStringExtra("hashtag");

            if (hashtag != null) {

                getSupportActionBar().setTitle(hashtag);

            } else {

                getSupportActionBar().setTitle(R.string.title_activity_hashtag);
            }

            restore = false;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_body, fragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean("restore", true);
        getSupportFragmentManager().putFragment(outState, "currentFragment", fragment);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        fragment.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case android.R.id.home: {

                finish();

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public void onCloseRepostDialog(String mMessage, int position) {

        HashtagFragment p = (HashtagFragment) fragment;
        p.onCloseRepostDialog(mMessage, position);
    }

    @Override
    public void onItemDelete(long itemId, int position) {

        HashtagFragment p = (HashtagFragment) fragment;
        p.onItemDelete(itemId, position);
    }

    @Override
    public void onItemReport(long itemId, int reasonId) {

        HashtagFragment p = (HashtagFragment) fragment;
        p.onItemReport(itemId, reasonId);
    }
}