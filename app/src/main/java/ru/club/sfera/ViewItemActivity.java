package ru.club.sfera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ru.club.sfera.common.ActivityBase;
import ru.club.sfera.dialogs.CommentDeleteDialog;
import ru.club.sfera.dialogs.CommentReportDialog;
import ru.club.sfera.dialogs.ImageChooseDialog;
import ru.club.sfera.dialogs.ItemDeleteDialog;
import ru.club.sfera.dialogs.ItemReportDialog;
import ru.club.sfera.dialogs.SendRepostDialog;


public class ViewItemActivity extends ActivityBase implements CommentReportDialog.AlertPositiveListener, CommentDeleteDialog.AlertPositiveListener, ItemDeleteDialog.AlertPositiveListener, ItemReportDialog.AlertPositiveListener, ImageChooseDialog.AlertPositiveListener, SendRepostDialog.AlertPositiveListener {

    Toolbar mToolbar;

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState != null) {

            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");

        } else {

            fragment = new ViewItemFragment();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_body, fragment).commit();
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {

        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "currentFragment", fragment);
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
    protected void onPause() {

        ViewItemFragment p = (ViewItemFragment) fragment;
        p.hideEmojiKeyboard();

        super.onPause();
    }

    @Override
    public void onCloseRepostDialog(String mMessage, int position) {

        ViewItemFragment p = (ViewItemFragment) fragment;
        p.onCloseRepostDialog(mMessage, position);
    }

    @Override
    public void onItemDelete(long itemId, int position) {

        ViewItemFragment p = (ViewItemFragment) fragment;
        p.onItemDelete(itemId, position);
    }

    @Override
    public void onItemReport(long itemId, int reasonId) {

        ViewItemFragment p = (ViewItemFragment) fragment;
        p.onItemReport(itemId, reasonId);
    }

    @Override
    public void onCommentReport(long itemId, int reasonId) {

        ViewItemFragment p = (ViewItemFragment) fragment;
        p.onCommentReport(itemId, reasonId);
    }

    @Override
    public void onCommentDelete(int position) {

        ViewItemFragment p = (ViewItemFragment) fragment;
        p.onCommentDelete(position);
    }

    @Override
    public void onImageFromCamera() {

        ViewItemFragment p = (ViewItemFragment) fragment;
        p.imageFromCamera();
    }

    @Override
    public void onImageFromGallery() {

        ViewItemFragment p = (ViewItemFragment) fragment;
        p.imageFromGallery();
    }

    @Override
    public void onBackPressed(){

        finish();
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
}
