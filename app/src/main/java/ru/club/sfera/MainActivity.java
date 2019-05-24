package ru.club.sfera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.pkmmte.view.CircularImageView;

import ru.club.sfera.app.App;
import ru.club.sfera.common.ActivityBase;
import ru.club.sfera.dialogs.FriendRequestActionDialog;
import ru.club.sfera.dialogs.ImageChooseDialog;
import ru.club.sfera.dialogs.ItemDeleteDialog;
import ru.club.sfera.dialogs.ItemReportDialog;
import ru.club.sfera.dialogs.NearbySettingsDialog;
import ru.club.sfera.dialogs.ProfileBlockDialog;
import ru.club.sfera.dialogs.ProfileReportDialog;
import ru.club.sfera.dialogs.SearchSettingsDialog;
import ru.club.sfera.dialogs.SendRepostDialog;

public class MainActivity extends ActivityBase implements ImageChooseDialog.AlertPositiveListener, ProfileReportDialog.AlertPositiveListener, ProfileBlockDialog.AlertPositiveListener, NearbySettingsDialog.AlertPositiveListener, SearchSettingsDialog.AlertPositiveListener, FriendRequestActionDialog.AlertPositiveListener, ItemDeleteDialog.AlertPositiveListener, ItemReportDialog.AlertPositiveListener, SendRepostDialog.AlertPositiveListener {

    Toolbar mToolbar;

    private NavigationView mNavView;
    private DrawerLayout mDrawerLayout;
    private Menu mNavMenu;

    private View mNavHeaderLayout;

    private TextView mNavHeaderFullname, mNavHeaderUsername;
    private CircularImageView mNavHeaderPhoto, mNavHeaderIcon;
    private ImageView mNavHeaderCover;

    // used to store app title
    private CharSequence mTitle;

    Fragment fragment;
    Boolean action = false;

    private Boolean restore = false;

    private int page = PAGE_UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {

            //Restore the fragment's instance
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");

            restore = savedInstanceState.getBoolean("restore");
            mTitle = savedInstanceState.getString("mTitle");
            page = savedInstanceState.getInt("page");

        } else {

            fragment = new StreamFragment();

            restore = false;
            mTitle = getString(R.string.app_name);
            page = PAGE_UNKNOWN;
        }

        if (fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_body, fragment).commit();
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(mTitle);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.nav_drawer_open, R.string.nav_drawer_close) {

            public void onDrawerOpened(View drawerView) {

                refreshMenu();

                hideKeyboard();

                updateNavItemCounter(mNavView, R.id.nav_notifications, App.getInstance().getNotificationsCount());
                updateNavItemCounter(mNavView, R.id.nav_messages, App.getInstance().getMessagesCount());
                updateNavItemCounter(mNavView, R.id.nav_friends, App.getInstance().getFriendsCount());
                updateNavItemCounter(mNavView, R.id.nav_guests, App.getInstance().getGuestsCount());

                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavView = (NavigationView) findViewById(R.id.nav_view);

        mNavHeaderLayout = mNavView.getHeaderView(0);
        mNavHeaderFullname = (TextView) mNavHeaderLayout.findViewById(R.id.fullname);
        mNavHeaderUsername = (TextView) mNavHeaderLayout.findViewById(R.id.username);

        mNavHeaderPhoto = (CircularImageView) mNavHeaderLayout.findViewById(R.id.giftImg);
        mNavHeaderIcon = (CircularImageView) mNavHeaderLayout.findViewById(R.id.profileIcon);
        mNavHeaderCover = (ImageView) mNavHeaderLayout.findViewById(R.id.profileCover);

        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                displayFragment(menuItem.getItemId(), menuItem.getTitle().toString());
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        mNavMenu = mNavView.getMenu();

        refreshMenu();

        if (!restore) {

            // Show default section "Explore"

            displayFragment(mNavMenu.findItem(R.id.nav_stream).getItemId(), mNavMenu.findItem(R.id.nav_stream).getTitle().toString());
        }
    }

    private void refreshMenu() {

        if (App.getInstance().getSettings().getNavMessagesMenuItem() == DISABLED) {

            mNavMenu.findItem(R.id.nav_messages).setVisible(false);

        } else {

            mNavMenu.findItem(R.id.nav_messages).setVisible(true);
        }

        if (App.getInstance().getSettings().getNavNotificationsMenuItem() == DISABLED) {

            mNavMenu.findItem(R.id.nav_notifications).setVisible(false);

        } else {

            mNavMenu.findItem(R.id.nav_notifications).setVisible(true);
        }

        if (App.getInstance().getGuestsUpgrade() == DISABLED) {

            mNavMenu.findItem(R.id.nav_guests).setVisible(false);

        } else {

            mNavMenu.findItem(R.id.nav_guests).setVisible(true);
        }

        mNavHeaderFullname.setText(App.getInstance().getFullname());
        mNavHeaderUsername.setText("@" + App.getInstance().getUsername());

        if (App.getInstance().getVerified() == 1) {

            mNavHeaderIcon.setVisibility(View.VISIBLE);

        } else {

            mNavHeaderIcon.setVisibility(View.GONE);
        }

        if (App.getInstance().getPhotoUrl() != null && App.getInstance().getPhotoUrl().length() > 0) {

            ImageLoader imageLoader = App.getInstance().getImageLoader();

            imageLoader.get(App.getInstance().getPhotoUrl(), ImageLoader.getImageListener(mNavHeaderPhoto, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

        } else {

            mNavHeaderPhoto.setImageResource(R.drawable.profile_default_photo);
        }

        if (App.getInstance().getCoverUrl() != null && App.getInstance().getCoverUrl().length() > 0) {

            ImageLoader imageLoader = App.getInstance().getImageLoader();

            imageLoader.get(App.getInstance().getCoverUrl(), ImageLoader.getImageListener(mNavHeaderCover, R.drawable.profile_default_cover, R.drawable.profile_default_cover));

        } else {

            mNavHeaderCover.setImageResource(R.drawable.profile_default_cover);
        }
    }

    private void displayFragment(int id, String title) {

        action = false;

        switch (id) {

            case R.id.nav_feed: {

                page = PAGE_NEWS;

                mNavView.setCheckedItem(R.id.nav_feed);

                fragment = new NewsFragment();

                action = true;

                break;
            }

            case R.id.nav_stream: {

                page = PAGE_STREAM;

                mNavView.setCheckedItem(R.id.nav_stream);

                fragment = new StreamFragment();

                action = true;

                break;
            }

            case R.id.nav_search: {

                page = PAGE_SEARCH;

                mNavView.setCheckedItem(R.id.nav_search);

                fragment = new SearchFragment();

                action = true;

                break;
            }

            case R.id.nav_nearby: {

                page = PAGE_NEARBY;

                mNavView.setCheckedItem(R.id.nav_nearby);

                fragment = new NearbyFragment();

                action = true;

                break;
            }

            case R.id.nav_friends: {

                page = PAGE_FRIENDS;

                mNavView.setCheckedItem(R.id.nav_friends);

                fragment = new FriendsFragment();

                action = true;

                break;
            }

            case R.id.nav_guests: {

                page = PAGE_GUESTS;

                mNavView.setCheckedItem(R.id.nav_guests);

                fragment = new GuestsFragment();

                action = true;

                break;
            }

            case R.id.nav_favorites: {

                page = PAGE_FAVORITES;

                mNavView.setCheckedItem(R.id.nav_favorites);

                fragment = new FavoritesFragment();

                action = true;

                break;
            }

            case R.id.nav_notifications: {

                page = PAGE_NOTIFICATIONS;

                mNavView.setCheckedItem(R.id.nav_notifications);

                fragment = new NotificationsFragment();

                action = true;

                break;
            }

            case R.id.nav_messages: {

                page = PAGE_MESSAGES;

                mNavView.setCheckedItem(R.id.nav_messages);

                fragment = new DialogsFragment();

                action = true;

                break;
            }

            case R.id.nav_profile: {

                page = PAGE_PROFILE;

                mNavView.setCheckedItem(R.id.nav_profile);

                fragment = new ProfileFragment();

                action = true;

                break;
            }

            case R.id.nav_settings: {

                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);

                break;
            }
        }

        if (action && fragment != null) {

            getSupportActionBar().setDisplayShowCustomEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(title);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_body, fragment).commit();
        }
    }

    private void hideKeyboard() {

        View view = this.getCurrentFocus();

        if (view != null) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void updateNavItemCounter(NavigationView nav, @IdRes int itemId, int count) {

        TextView view = (TextView) nav.getMenu().findItem(itemId).getActionView().findViewById(R.id.counter);
        view.setText(String.valueOf(count));

        if (count <= 0) {

            view.setVisibility(View.GONE);

        } else {

            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putInt("page", page);
        outState.putBoolean("restore", true);
        outState.putString("mTitle", getSupportActionBar().getTitle().toString());
        getSupportFragmentManager().putFragment(outState, "currentFragment", fragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_SPOTLIGHT && resultCode == RESULT_OK && page == PAGE_STREAM) {

            Log.e("Spotlight", "Spotlight");

            StreamFragment p = (StreamFragment) fragment;
            p.updateAdapter();

        } else if (requestCode == ACTION_SPOTLIGHT && resultCode == RESULT_OK && page == PAGE_NEWS) {

            Log.e("Spotlight", "Spotlight");

            NewsFragment p = (NewsFragment) fragment;
            p.updateAdapter();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onChangeDistance(int position) {

        NearbyFragment p = (NearbyFragment) fragment;
        p.onChangeDistance(position);
    }

    @Override
    public void onImageFromGallery() {

        ProfileFragment p = (ProfileFragment) fragment;
        p.imageFromGallery();
    }

    @Override
    public void onImageFromCamera() {

        ProfileFragment p = (ProfileFragment) fragment;
        p.imageFromCamera();
    }

    @Override
    public void onProfileReport(int position) {

        ProfileFragment p = (ProfileFragment) fragment;
        p.onProfileReport(position);
    }

    @Override
    public void onProfileBlock() {

        ProfileFragment p = (ProfileFragment) fragment;
        p.onProfileBlock();
    }

    @Override
    public void onCloseSettingsDialog(int searchGender, int searchOnline, int searchVerified, int searchPhoto) {

        SearchFragment p = (SearchFragment) fragment;
        p.onCloseSettingsDialog(searchGender, searchOnline, searchVerified, searchPhoto);
    }

    @Override
    public void onAcceptRequest(int position) {

        NotificationsFragment p = (NotificationsFragment) fragment;
        p.onAcceptRequest(position);
    }

    @Override
    public void onRejectRequest(int position) {

        NotificationsFragment p = (NotificationsFragment) fragment;
        p.onRejectRequest(position);
    }

    @Override
    public void onCloseRepostDialog(String mMessage, int position) {

        switch (page) {

            case PAGE_STREAM: {

                StreamFragment p = (StreamFragment) fragment;
                p.onCloseRepostDialog(mMessage, position);

                break;
            }

            case PAGE_FEED: {

                NewsFragment p = (NewsFragment) fragment;
                p.onCloseRepostDialog(mMessage, position);

                break;
            }

            case PAGE_PROFILE: {

                ProfileFragment p = (ProfileFragment) fragment;
                p.onCloseRepostDialog(mMessage, position);

                break;
            }

            case PAGE_NEARBY: {

                NearbyFragment p = (NearbyFragment) fragment;
                p.onCloseRepostDialog(mMessage, position);

                break;
            }

            case PAGE_FAVORITES: {

                FavoritesFragment p = (FavoritesFragment) fragment;
                p.onCloseRepostDialog(mMessage, position);

                break;
            }

            default: {

                break;
            }
        }
    }

    @Override
    public void onItemDelete(long itemId, int position) {

        switch (page) {

            case PAGE_FAVORITES: {

                FavoritesFragment p = (FavoritesFragment) fragment;
                p.onItemDelete(itemId, position);

                break;
            }

            case PAGE_NEARBY: {

                NearbyFragment p = (NearbyFragment) fragment;
                p.onItemDelete(itemId, position);

                break;
            }

            case PAGE_NEWS: {

                NewsFragment p = (NewsFragment) fragment;
                p.onItemDelete(itemId, position);

                break;
            }

            case PAGE_STREAM: {

                StreamFragment p = (StreamFragment) fragment;
                p.onItemDelete(itemId, position);

                break;
            }

            case PAGE_PROFILE: {

                ProfileFragment p = (ProfileFragment) fragment;
                p.onItemDelete(itemId, position);

                break;
            }

            default: {

                break;
            }
        }
    }

    @Override
    public void onItemReport(long itemId, int reasonId) {

        switch (page) {

            case PAGE_FAVORITES: {

                FavoritesFragment p = (FavoritesFragment) fragment;
                p.onItemReport(itemId, reasonId);

                break;
            }

            case PAGE_NEARBY: {

                NearbyFragment p = (NearbyFragment) fragment;
                p.onItemReport(itemId, reasonId);

                break;
            }

            case PAGE_NEWS: {

                NewsFragment p = (NewsFragment) fragment;
                p.onItemReport(itemId, reasonId);

                break;
            }

            case PAGE_STREAM: {

                StreamFragment p = (StreamFragment) fragment;
                p.onItemReport(itemId, reasonId);

                break;
            }

            case PAGE_PROFILE: {

                ProfileFragment p = (ProfileFragment) fragment;
                p.onItemReport(itemId, reasonId);

                break;
            }

            default: {

                break;
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home: {

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {

        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
}
