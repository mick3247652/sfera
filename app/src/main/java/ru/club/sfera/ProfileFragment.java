package ru.club.sfera;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.balysv.materialripple.MaterialRippleLayout;
import com.melnykov.fab.FloatingActionButton;
import com.pkmmte.view.CircularImageView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.club.sfera.adapter.AdvancedItemListAdapter;
import ru.club.sfera.adapter.PreviewGalleryListAdapter;
import ru.club.sfera.app.App;
import ru.club.sfera.common.FragmentBase;
import ru.club.sfera.constants.Constants;
import ru.club.sfera.dialogs.ImageChooseDialog;
import ru.club.sfera.dialogs.ItemDeleteDialog;
import ru.club.sfera.dialogs.ItemReportDialog;
import ru.club.sfera.dialogs.ProfileBlockDialog;
import ru.club.sfera.dialogs.ProfileReportDialog;
import ru.club.sfera.dialogs.SendRepostDialog;
import ru.club.sfera.model.GalleryItem;
import ru.club.sfera.model.Item;
import ru.club.sfera.model.Profile;
import ru.club.sfera.util.Api;
import ru.club.sfera.util.CustomRequest;

import static ru.club.sfera.R.id.profileOnlineIcon;


public class ProfileFragment extends FragmentBase implements Constants, SwipeRefreshLayout.OnRefreshListener {

    public final static int STATUS_START = 100;

    public final static String PARAM_TASK = "task";
    public final static String PARAM_STATUS = "status";

    private static final String STATE_LIST = "State Adapter Data";
    private static final String GALLERY_STATE_LIST = "Gallery State Adapter Data";

    private static final String TAG = ProfileFragment.class.getSimpleName();
    public final static String BROADCAST_ACTION_UPDATE = "ru.ifsoft.mnetwork.update";

    BroadcastReceiver br;

    private static final int SELECT_PHOTO = 1;
    private static final int SELECT_COVER = 2;
    private static final int PROFILE_EDIT = 3;
    private static final int PROFILE_NEW_POST = 4;
    private static final int CREATE_PHOTO = 5;
    private static final int CREATE_COVER = 6;
    private static final int PROFILE_CHAT = 7;

    private int mAccountAction = 0; // 0 = choicePhoto, 1 = choiceCover

    FloatingActionButton mFabButton;

    RelativeLayout mProfileLoadingScreen, mProfileErrorScreen, mProfileDisabledScreen;

    LinearLayout mLocationContainer, mProfileInfoContainer, mProfileCountersContainer;

    LinearLayout mProfileGenderContainer, mProfileActivityContainer, mProfileStatusContainer, mProfilePhoneContainer, mProfileFacebookContainer, mProfileSiteContainer;
    TextView mProfileActivity, mProfileGender, mProfileStatus, mProfilePhone, mProfileFacebookUrl, mProfileSiteUrl;

    SwipeRefreshLayout mProfileRefreshLayout;
    NestedScrollView mNestedScrollView;

    CircularImageView mProfilePhoto, mProfileIcon;
    ImageView mProfileCover, mProfileOnlineIcon;
    TextView mProfileLocation, mProfileFullname, mProfileUsername, mProfileMessage, mGalleryTitle;
    RecyclerView mRecyclerView, mGalleryRecyclerView;
    TextView mProfileItemsCount, mProfileFriendsCount, mProfileLikesCount, mProfileGiftsCount;
    MaterialRippleLayout mProfileItemsBtn, mProfileFriendsBtn, mProfileLikesBtn, mProfileGiftsBtn;

    LinearLayout mGalleryContainer;

    Button mProfileMessageBtn, mProfileActionBtn, mGalleryBtn;

    Toolbar mToolbar;

    Profile profile;

    private ArrayList<Item> itemsList;
    private ArrayList<GalleryItem> galleryList;

    private AdvancedItemListAdapter itemsAdapter;
    private PreviewGalleryListAdapter galleryAdapter;

    private String selectedPhoto, selectedCover;
    private Uri outputFileUri;

    private Boolean loadingComplete = false;
    private Boolean loadingMore = false;
    private Boolean viewMore = false;

    private String profile_mention;
    public long profile_id;
    int itemId = 0, galleryItemId = 0;
    int arrayLength = 0;
    int accessMode = 0;

    private Boolean loading = false;
    private Boolean restore = false;
    private Boolean preload = false;

    private Boolean isMainScreen = false;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setClassName(ProfileFragment.class.getSimpleName());

        setRetainInstance(true);

        setHasOptionsMenu(true);

        initpDialog();

        Intent i = getActivity().getIntent();

        profile_id = i.getLongExtra("profileId", 0);
        profile_mention = i.getStringExtra("profileMention");

        if (App.getInstance().getId() != profile_id) {

            App.getInstance().setCurrentProfileId(profile_id);
        }

        if (profile_id == 0 && (profile_mention == null || profile_mention.length() == 0)) {

            profile_id = App.getInstance().getId();
            isMainScreen = true;
        }

        profile = new Profile();
        profile.setId(profile_id);

        itemsList = new ArrayList<>();
        itemsAdapter = new AdvancedItemListAdapter(getActivity(), itemsList);

        galleryList = new ArrayList<>();
        galleryAdapter = new PreviewGalleryListAdapter(getActivity(), galleryList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        if (savedInstanceState != null) {

            galleryList = savedInstanceState.getParcelableArrayList(GALLERY_STATE_LIST);
            galleryAdapter = new PreviewGalleryListAdapter(getActivity(), galleryList);

            itemsList = savedInstanceState.getParcelableArrayList(STATE_LIST);
            itemsAdapter = new AdvancedItemListAdapter(getActivity(), itemsList);

            itemId = savedInstanceState.getInt("itemId");

            restore = savedInstanceState.getBoolean("restore");
            loading = savedInstanceState.getBoolean("loading");
            preload = savedInstanceState.getBoolean("preload");

            profile = savedInstanceState.getParcelable("profileObj");

        } else {

            galleryList = new ArrayList<>();
            galleryAdapter = new PreviewGalleryListAdapter(getActivity(), galleryList);

            itemsList = new ArrayList<>();
            itemsAdapter = new AdvancedItemListAdapter(getActivity(), itemsList);

            itemId = 0;

            restore = false;
            loading = false;
            preload = false;
        }

        br = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {

                int task = intent.getIntExtra(PARAM_TASK, 0);
                int status = intent.getIntExtra(PARAM_STATUS, 0);

                Log.e("UPDATE", "PROFILE UPDATE");

                getData();
            }
        };

        IntentFilter intFilt4 = new IntentFilter(BROADCAST_ACTION_UPDATE);
        getActivity().registerReceiver(br, intFilt4);

        if (loading) {


            showpDialog();
        }

        mFabButton = (FloatingActionButton) rootView.findViewById(R.id.fabButton);
        mFabButton.setImageResource(R.drawable.ic_action_new);

        mFabButton.setVisibility(View.GONE);

        mProfileRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.profileRefreshLayout);
        mProfileRefreshLayout.setOnRefreshListener(this);

        mProfileLoadingScreen = (RelativeLayout) rootView.findViewById(R.id.profileLoadingScreen);
        mProfileErrorScreen = (RelativeLayout) rootView.findViewById(R.id.profileErrorScreen);
        mProfileDisabledScreen = (RelativeLayout) rootView.findViewById(R.id.profileDisabledScreen);

        mProfileInfoContainer = (LinearLayout) rootView.findViewById(R.id.profileInfoContainer);
        mProfileCountersContainer = (LinearLayout) rootView.findViewById(R.id.profileCountersContainer);

        mProfileGenderContainer = (LinearLayout) rootView.findViewById(R.id.profileGenderContainer);
        mProfileActivityContainer = (LinearLayout) rootView.findViewById(R.id.profileActivityContainer);
        mProfileStatusContainer = (LinearLayout) rootView.findViewById(R.id.profileStatusContainer);
        mProfilePhoneContainer = (LinearLayout) rootView.findViewById(R.id.profilePhoneContainer);
        mProfileFacebookContainer = (LinearLayout) rootView.findViewById(R.id.profileFacebookContainer);
        mProfileSiteContainer = (LinearLayout) rootView.findViewById(R.id.profileSiteContainer);

        mGalleryContainer = (LinearLayout) rootView.findViewById(R.id.galleryContainer);

        mGalleryContainer.setVisibility(View.GONE);

        mProfileGender = (TextView) rootView.findViewById(R.id.profileGender);
        mProfileActivity = (TextView) rootView.findViewById(R.id.profileActivity);
        mProfileStatus = (TextView) rootView.findViewById(R.id.profileStatus);
        mProfilePhone = (TextView) rootView.findViewById(R.id.profilePhone);
        mProfileFacebookUrl = (TextView) rootView.findViewById(R.id.profileFacebookUrl);
        mProfileSiteUrl = (TextView) rootView.findViewById(R.id.profileSiteUrl);

        mNestedScrollView = (NestedScrollView) rootView.findViewById(R.id.nestedScrollView);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        itemsAdapter.setOnMoreButtonClickListener(new AdvancedItemListAdapter.OnItemMenuButtonClickListener() {

            @Override
            public void onItemClick(View v, Item obj, int actionId, int position) {

                switch (actionId){

                    case R.id.action_repost: {

                        if (obj.getFromUserId() != App.getInstance().getId()) {

                            if (obj.getToUserId() != App.getInstance().getId()) {

                                if (obj.getReFromUserId() != App.getInstance().getId()) {

                                    if (obj.getReToUserId() != App.getInstance().getId()) {

                                        android.app.FragmentManager fm = getActivity().getFragmentManager();

                                        SendRepostDialog alert = new SendRepostDialog();

                                        Bundle b  = new Bundle();
                                        b.putInt("position", position);

                                        alert.setArguments(b);
                                        alert.show(fm, "alert_dialog_item_repost");

                                        break;
                                    }
                                }
                            }
                        }

                        Toast.makeText(getActivity(), getActivity().getString(R.string.msg_not_make_repost), Toast.LENGTH_SHORT).show();

                        break;
                    }

                    case R.id.action_share: {

                        Api api = new Api(getActivity());

                        api.itemShare(obj);

                        break;
                    }

                    case R.id.action_report: {

                        android.app.FragmentManager fm = getActivity().getFragmentManager();

                        ItemReportDialog alert = new ItemReportDialog();

                        Bundle b  = new Bundle();
                        b.putLong("itemId", obj.getId());
                        b.putInt("reason", 0);

                        alert.setArguments(b);
                        alert.show(fm, "alert_dialog_photo_report");

                        break;
                    }

                    case R.id.action_remove: {

                        android.app.FragmentManager fm = getActivity().getFragmentManager();

                        ItemDeleteDialog alert = new ItemDeleteDialog();

                        Bundle b = new Bundle();
                        b.putInt("position", position);
                        b.putLong("itemId", obj.getId());

                        alert.setArguments(b);
                        alert.show(fm, "alert_dialog_photo_delete");

                        break;
                    }
                }
            }
        });

        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(itemsAdapter);

        mRecyclerView.setNestedScrollingEnabled(false);

        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {

                    if (!loadingMore && (viewMore) && !(mProfileRefreshLayout.isRefreshing())) {

                        mProfileRefreshLayout.setRefreshing(true);

                        loadingMore = true;

                        getItems();
                    }
                }
            }
        });

        mGalleryTitle = (TextView) rootView.findViewById(R.id.galleryTitle);
        mGalleryBtn = (Button) rootView.findViewById(R.id.galleryBtn);
        mGalleryRecyclerView = (RecyclerView) rootView.findViewById(R.id.galleryRecyclerView);

        mGalleryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mGalleryRecyclerView.setAdapter(galleryAdapter);

        galleryAdapter.setOnItemClickListener(new PreviewGalleryListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, GalleryItem obj, int position) {

                Intent intent = new Intent(getActivity(), ViewGalleryItemActivity.class);
                intent.putExtra("itemId", obj.getId());
                startActivity(intent);
            }
        });

        mGalleryBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), GalleryActivity.class);
                intent.putExtra("profileId", profile_id);
                startActivity(intent);
            }
        });

        mProfileActionBtn = (Button) rootView.findViewById(R.id.profileActionBtn);
        mProfileMessageBtn = (Button) rootView.findViewById(R.id.profileMessageBtn);

        mProfileFullname = (TextView) rootView.findViewById(R.id.profileFullname);
        mProfileUsername = (TextView) rootView.findViewById(R.id.profileUsername);
        mProfileMessage = (TextView) rootView.findViewById(R.id.profileMessage);

        mProfileOnlineIcon = (ImageView) rootView.findViewById(profileOnlineIcon);

        mProfileItemsCount = (TextView) rootView.findViewById(R.id.profileItemsCount);
        mProfileFriendsCount = (TextView) rootView.findViewById(R.id.profileFriendsCount);
        mProfileLikesCount = (TextView) rootView.findViewById(R.id.profileLikesCount);
        mProfileGiftsCount = (TextView) rootView.findViewById(R.id.profileGiftsCount);

        mProfileItemsBtn = (MaterialRippleLayout) rootView.findViewById(R.id.profileItemsBtn);
        mProfileFriendsBtn = (MaterialRippleLayout) rootView.findViewById(R.id.profileFriendsBtn);
        mProfileLikesBtn = (MaterialRippleLayout) rootView.findViewById(R.id.profileLikesBtn);
        mProfileGiftsBtn = (MaterialRippleLayout) rootView.findViewById(R.id.profileGiftsBtn);

        mLocationContainer = (LinearLayout) rootView.findViewById(R.id.profileLocationContainer);
        mProfileLocation = (TextView) rootView.findViewById(R.id.profileLocation);

        mProfileFacebookContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!profile.getFacebookPage().startsWith("https://") && !profile.getFacebookPage().startsWith("http://")){

                    profile.setFacebookPage("http://" + profile.getFacebookPage());
                }

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(profile.getFacebookPage()));
                startActivity(i);
            }
        });

        mProfileSiteContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!profile.getInstagramPage().startsWith("https://") && !profile.getInstagramPage().startsWith("http://")){

                    profile.setInstagramPage("http://" + profile.getInstagramPage());
                }

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(profile.getInstagramPage()));
                startActivity(i);
            }
        });

        mProfileFriendsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), FriendsActivity.class);
                intent.putExtra("profileId", profile.getId());
                startActivity(intent);
            }
        });

        mProfileGiftsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), GiftsActivity.class);
                intent.putExtra("profileId", profile.getId());
                startActivity(intent);
            }
        });

        mProfilePhoto = (CircularImageView) rootView.findViewById(R.id.giftImg);
        mProfileIcon = (CircularImageView) rootView.findViewById(R.id.profileIcon);
        mProfileCover = (ImageView) rootView.findViewById(R.id.profileCover);

        mProfileActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App.getInstance().getId() == profile.getId()) {

                    Intent i = new Intent(getActivity(), AccountSettingsActivity.class);
                    i.putExtra("profileId", App.getInstance().getId());
                    i.putExtra("sex", profile.getSex());
                    i.putExtra("year", profile.getYear());
                    i.putExtra("month", profile.getMonth());
                    i.putExtra("day", profile.getDay());
                    i.putExtra("phone", profile.getPhone());
                    i.putExtra("fullname", profile.getFullname());
                    i.putExtra("location", profile.getLocation());
                    i.putExtra("facebookPage", profile.getFacebookPage());
                    i.putExtra("instagramPage", profile.getInstagramPage());
                    i.putExtra("bio", profile.getBio());
                    startActivityForResult(i, PROFILE_EDIT);

                } else {

                    if (profile.isFriend()) {

                        removeFromFriends();

                    } else {

                        if (profile.isFriendRequest()) {

                            cancelFriendsRequest();

                        } else {

                            newFriendsRequest();
                        }
                    }
                }
            }
        });

        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), NewItemActivity.class);
                intent.putExtra("wall_id", profile.getId());
                startActivityForResult(intent, STREAM_NEW_POST);
            }
        });

        mProfileMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (profile.getId() == App.getInstance().getId()) {

                    Intent intent = new Intent(getActivity(), NewItemActivity.class);
                    startActivityForResult(intent, STREAM_NEW_POST);

                } else {

                    if (!profile.isInBlackList()) {

                        Intent i = new Intent(getActivity(), ChatActivity.class);
                        i.putExtra("chatId", 0);
                        i.putExtra("profileId", profile.getId());
                        i.putExtra("withProfile", profile.getFullname());

                        i.putExtra("with_android_fcm_regId", profile.get_android_fcm_regId());
                        i.putExtra("with_ios_fcm_regId", profile.get_iOS_fcm_regId());

                        i.putExtra("with_user_username", profile.getUsername());
                        i.putExtra("with_user_fullname", profile.getFullname());
                        i.putExtra("with_user_photo_url", profile.getPhotoUrl());

                        i.putExtra("with_user_state", profile.getState());
                        i.putExtra("with_user_verified", profile.getVerified());

                        startActivityForResult(i, PROFILE_CHAT);
                    }
                }
            }
        });


        mProfilePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (profile.getPhotoUrl().length() > 0) {

                    Intent i = new Intent(getActivity(), PhotoViewActivity.class);
                    i.putExtra("imgUrl", profile.getPhotoUrl());
                    startActivity(i);
                }
            }
        });

        if (profile.getFullname() == null || profile.getFullname().length() == 0) {

            if (App.getInstance().isConnected()) {

                showLoadingScreen();
                getData();

                Log.e("Profile", "OnReload");

            } else {

                showErrorScreen();
            }

        } else {

            if (App.getInstance().isConnected()) {

                if (profile.getState() == ACCOUNT_STATE_ENABLED) {

                    showContentScreen();

                    loadingComplete();
                    updateProfile();

                } else {

                    showDisabledScreen();
                }

            } else {

                showErrorScreen();
            }
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    public void onDestroyView() {

        super.onDestroyView();

        getActivity().unregisterReceiver(br);

        hidepDialog();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putInt("itemId", itemId);

        outState.putBoolean("restore", restore);
        outState.putBoolean("loading", loading);
        outState.putBoolean("preload", preload);

        outState.putParcelable("profileObj", profile);
        outState.putParcelableArrayList(STATE_LIST, itemsList);
        outState.putParcelableArrayList(GALLERY_STATE_LIST, galleryList);
    }

    private Bitmap resize(String path){

        int maxWidth = 512;
        int maxHeight = 512;

        // create the options
        BitmapFactory.Options opts = new BitmapFactory.Options();

        //just decode the file
        opts.inJustDecodeBounds = true;
        Bitmap bp = BitmapFactory.decodeFile(path, opts);

        //get the original size
        int orignalHeight = opts.outHeight;
        int orignalWidth = opts.outWidth;

        //initialization of the scale
        int resizeScale = 1;

        //get the good scale
        if (orignalWidth > maxWidth || orignalHeight > maxHeight) {

            final int heightRatio = Math.round((float) orignalHeight / (float) maxHeight);
            final int widthRatio = Math.round((float) orignalWidth / (float) maxWidth);
            resizeScale = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        //put the scale instruction (1 -> scale to (1/1); 8-> scale to 1/8)
        opts.inSampleSize = resizeScale;
        opts.inJustDecodeBounds = false;

        //get the futur size of the bitmap
        int bmSize = (orignalWidth / resizeScale) * (orignalHeight / resizeScale) * 4;

        //check if it's possible to store into the vm java the picture
        if (Runtime.getRuntime().freeMemory() > bmSize) {

            //decode the file
            bp = BitmapFactory.decodeFile(path, opts);

        } else {

            return null;
        }

        return bp;
    }

    public void save(String outFile, String inFile) {

        try {

            Bitmap bmp = resize(outFile);

            File file = new File(Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER, inFile);
            FileOutputStream fOut = new FileOutputStream(file);

            bmp.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
            fOut.flush();
            fOut.close();

        } catch (Exception ex) {

            Log.e("Error", ex.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == STREAM_NEW_POST && resultCode == getActivity().RESULT_OK && null != data) {

            itemId = 0;
            getItems();

            profile.setItemsCount(profile.getItemsCount() + 1);

            updateProfile();

        } else if (requestCode == SELECT_PHOTO && resultCode == getActivity().RESULT_OK && null != data) {

            Uri selectedImage = data.getData();

            selectedPhoto = getImageUrlWithAuthority(getActivity(), selectedImage, "photo.jpg");

            if (selectedPhoto != null) {

                save(selectedPhoto, "photo.jpg");

                File f = new File(Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER, "photo.jpg");

                uploadFile(METHOD_PROFILE_UPLOADPHOTO, f, UPLOAD_TYPE_PHOTO);
            }

        } else if (requestCode == SELECT_COVER && resultCode == getActivity().RESULT_OK && null != data) {

            Uri selectedImage = data.getData();

            selectedCover = getImageUrlWithAuthority(getActivity(), selectedImage, "cover.jpg");

            if (selectedCover != null) {

                save(selectedCover, "cover.jpg");

                File f = new File(Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER, "cover.jpg");

                uploadFile(METHOD_PROFILE_UPLOADCOVER, f, UPLOAD_TYPE_COVER);
            }

        } else if (requestCode == PROFILE_EDIT && resultCode == getActivity().RESULT_OK) {

            profile.setPhone(data.getStringExtra("phone"));
            profile.setFullname(data.getStringExtra("fullname"));
            profile.setLocation(data.getStringExtra("location"));
            profile.setFacebookPage(data.getStringExtra("facebookPage"));
            profile.setInstagramPage(data.getStringExtra("instagramPage"));
            profile.setBio(data.getStringExtra("bio"));

            profile.setSex(data.getIntExtra("sex", 0));

            profile.setYear(data.getIntExtra("year", 0));
            profile.setMonth(data.getIntExtra("month", 0));
            profile.setDay(data.getIntExtra("day", 0));

            updateProfile();

        } else if (requestCode == PROFILE_NEW_POST && resultCode == getActivity().RESULT_OK) {

            getData();

        } else if (requestCode == CREATE_PHOTO && resultCode == getActivity().RESULT_OK) {

            try {

                selectedPhoto = Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER + File.separator + "photo.jpg";

                save(selectedPhoto, "photo.jpg");

                File f = new File(Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER, "photo.jpg");

                uploadFile(METHOD_PROFILE_UPLOADPHOTO, f, UPLOAD_TYPE_PHOTO);

            } catch (Exception ex) {

                Log.v("OnCameraCallBack", ex.getMessage());
            }

        } else if (requestCode == CREATE_COVER && resultCode == getActivity().RESULT_OK) {

            try {

                selectedCover = Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER + File.separator + "cover.jpg";

                save(selectedCover, "cover.jpg");

                File f = new File(Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER, "cover.jpg");

                uploadFile(METHOD_PROFILE_UPLOADCOVER, f, UPLOAD_TYPE_COVER);

            } catch (Exception ex) {

                Log.v("OnCameraCallBack", ex.getMessage());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_PHOTO: {

                // If request is cancelled, the result arrays are empty.

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    choiceImage(mAccountAction);

                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {

                    if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        showNoStoragePermissionSnackbar();
                    }
                }

                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void showNoStoragePermissionSnackbar() {

        Snackbar.make(getView(), getString(R.string.label_no_storage_permission) , Snackbar.LENGTH_LONG).setAction(getString(R.string.action_settings), new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                openApplicationSettings();

                Toast.makeText(getActivity(), getString(R.string.label_grant_storage_permission), Toast.LENGTH_SHORT).show();
            }

        }).show();
    }

    public void openApplicationSettings() {

        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getActivity().getPackageName()));
        startActivityForResult(appSettingsIntent, 10001);
    }

    public void choiceImage(int type) {

        android.app.FragmentManager fm = getActivity().getFragmentManager();

        ImageChooseDialog alert = new ImageChooseDialog();

        alert.show(fm, "alert_dialog_cover_choose");
    }

    public void imageFromGallery() {

        if (mAccountAction == 0) {

            photoFromGallery();

        } else {

            coverFromGallery();
        }
    }

    public void imageFromCamera() {

        if (mAccountAction == 0) {

            photoFromCamera();

        } else {

            coverFromCamera();
        }
    }

    public void photoFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, getText(R.string.label_select_img)), SELECT_PHOTO);
    }

    public void photoFromCamera() {

        try {

            File root = new File(Environment.getExternalStorageDirectory(), APP_TEMP_FOLDER);

            if (!root.exists()) {

                root.mkdirs();
            }

            File sdImageMainDirectory = new File(root, "photo.jpg");
            outputFileUri = Uri.fromFile(sdImageMainDirectory);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(cameraIntent, CREATE_PHOTO);

        } catch (Exception e) {

            Toast.makeText(getActivity(), "Error occured. Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public void coverFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, getText(R.string.label_select_img)), SELECT_COVER);
    }

    public void coverFromCamera() {

        try {

            File root = new File(Environment.getExternalStorageDirectory(), APP_TEMP_FOLDER);

            if (!root.exists()) {

                root.mkdirs();
            }

            File sdImageMainDirectory = new File(root, "cover.jpg");
            outputFileUri = Uri.fromFile(sdImageMainDirectory);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(cameraIntent, CREATE_COVER);

        } catch (Exception e) {

            Toast.makeText(getActivity(), "Error occured. Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getImageUrlWithAuthority(Context context, Uri uri, String fileName) {

        InputStream is = null;

        if (uri.getAuthority() != null) {

            try {

                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);

                return writeToTempImageAndGetPathUri(context, bmp, fileName).toString();

            } catch (FileNotFoundException e) {

                e.printStackTrace();

            } finally {

                try {

                    if (is != null) {

                        is.close();
                    }

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static String writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage, String fileName) {

        String file_path = Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER;
        File dir = new File(file_path);
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, fileName);

        try {

            FileOutputStream fos = new FileOutputStream(file);

            inImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {

            Toast.makeText(inContext, "Error occured. Please try again later.", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER + File.separator + fileName;
    }

    @Override
    public void onRefresh() {

        if (App.getInstance().isConnected()) {

            getData();

        } else {

            mProfileRefreshLayout.setRefreshing(false);
        }
    }

    public void updateProfile() {

        if (App.getInstance().getSettings().getAllowGifts() == ENABLED) {

            mProfileGiftsBtn.setVisibility(View.VISIBLE);

        } else {

            mProfileGiftsBtn.setVisibility(View.GONE);
        }

        if (profile.getAllowShowMyFriends() == DISABLED && App.getInstance().getId() != profile.getId()) {

            mProfileFriendsBtn.setVisibility(View.GONE);

        } else {

            mProfileFriendsBtn.setVisibility(View.VISIBLE);
        }

        if (App.getInstance().getSettings().getAllowGallery() == ENABLED) {

            if (profile.getAllowShowMyGallery() == DISABLED && App.getInstance().getId() != profile.getId()) {

                mGalleryContainer.setVisibility(View.GONE);

            } else {

                if (profile.getGalleryItemsCount() == 0 && (profile.getId() != App.getInstance().getId())) {

                    mGalleryContainer.setVisibility(View.GONE);

                } else {

                    mGalleryContainer.setVisibility(View.VISIBLE);

                    if (profile.getGalleryItemsCount() > 0) {

                        mGalleryRecyclerView.setVisibility(View.VISIBLE);

                    } else {

                        mGalleryRecyclerView.setVisibility(View.GONE);
                    }

                    mGalleryTitle.setText(getString(R.string.label_gallery) + " (" + Integer.toString(profile.getGalleryItemsCount()) + ")");
                }
            }

        } else {

            mGalleryContainer.setVisibility(View.GONE);
        }

        mProfileMessage.setVisibility(View.GONE);

        mProfileUsername.setText("@" + profile.getUsername());
        mProfileLocation.setText(profile.getLocation());

        mProfileItemsCount.setText(Integer.toString(profile.getItemsCount()));
        mProfileFriendsCount.setText(Integer.toString(profile.getFriendsCount()));
        mProfileLikesCount.setText(Integer.toString(profile.getLikesCount()));
        mProfileGiftsCount.setText(Integer.toString(profile.getGiftsCount()));

        // Show settings button is your profile
        if (profile.getId() == App.getInstance().getId()) {

            mFabButton.setVisibility(View.VISIBLE);

            mProfileActionBtn.setText(R.string.action_profile_edit);

            mProfileActionBtn.setVisibility(View.VISIBLE);

            mProfileMessageBtn.setText(R.string.action_new);

            mProfileMessageBtn.setVisibility(View.VISIBLE);

        } else {

            if (profile.isFriendRequest()) {

                mProfileActionBtn.setEnabled(true);
                mProfileActionBtn.setText(getString(R.string.action_cancel_request));

            } else {

                mProfileActionBtn.setEnabled(true);

                if (profile.isFriend()) {

                    mProfileActionBtn.setText(getString(R.string.action_remove_from_friends));

                } else {

                    mProfileActionBtn.setText(getString(R.string.action_add_to_friends));
                }
            }

            // Allow post Items from friend

            if (profile.isFriend() && profile.getAllowItemsFromFriends() == ENABLED && !profile.isInBlackList()) {

                mFabButton.setVisibility(View.VISIBLE);
            }

            mProfileMessageBtn.setText(R.string.action_message);

            mProfileMessageBtn.setVisibility(View.VISIBLE);

            if (profile.getAllowMessagesFromAnyone() == 0 && !profile.isFriend()) {

                mProfileMessageBtn.setEnabled(false);

            } else {

                if (!profile.isInBlackList()) {

                    mProfileMessageBtn.setEnabled(true);

                } else {

                    mProfileMessageBtn.setEnabled(false);
                }
            }
        }

        if (profile.getLocation() != null && profile.getLocation().length() != 0) {

            mLocationContainer.setVisibility(View.VISIBLE);

        } else {

            mLocationContainer.setVisibility(View.GONE);
        }

        if (profile.getFacebookPage() != null && profile.getFacebookPage().length() != 0) {

            mProfileFacebookContainer.setVisibility(View.VISIBLE);
            mProfileFacebookUrl.setText(profile.getFacebookPage());

        } else {

            mProfileFacebookContainer.setVisibility(View.GONE);
        }

        if (profile.getInstagramPage() != null && profile.getInstagramPage().length() != 0) {

            mProfileSiteContainer.setVisibility(View.VISIBLE);
            mProfileSiteUrl.setText(profile.getInstagramPage());

        } else {

            mProfileSiteContainer.setVisibility(View.GONE);
        }

        if (profile.getBio() != null && profile.getBio().length() != 0) {

            mProfileStatusContainer.setVisibility(View.VISIBLE);
            mProfileStatus.setText(profile.getBio());

        } else {

            mProfileStatusContainer.setVisibility(View.GONE);
        }

        if (profile.getPhone() != null && profile.getPhone().length() != 0 && profile.getAllowShowPhoneNumber() == 1) {

            mProfilePhoneContainer.setVisibility(View.VISIBLE);
            mProfilePhone.setText(profile.getPhone());

        } else {

            mProfilePhoneContainer.setVisibility(View.GONE);
        }

        if (profile.getSex() != SEX_UNKNOWN) {

            mProfileGenderContainer.setVisibility(View.VISIBLE);

            if (profile.getSex() == SEX_MALE) {

                mProfileGender.setText(getString(R.string.label_male));

            } else {

                mProfileGender.setText(getString(R.string.label_female));
            }

        } else {

            mProfileGenderContainer.setVisibility(View.GONE);
        }

        updateFullname();

        showPhoto(profile.getPhotoUrl());
        showCover(profile.getCoverUrl());

        showContentScreen();

        if (profile.isOnline() && profile.getAllowShowOnline() == 1) {

            // User Online

            mProfileOnlineIcon.setVisibility(View.VISIBLE);

            if (profile.getLastActive() == 0) {

                mProfileActivity.setText(getString(R.string.label_offline));

            } else {

                mProfileActivity.setText(getString(R.string.label_online));
            }

        } else {

            mProfileOnlineIcon.setVisibility(View.GONE);

            if (profile.getAllowShowOnline() == 1) {

                mProfileActivity.setText(profile.getLastActiveTimeAgo());

            } else {

                mProfileActivity.setVisibility(View.GONE);
                mProfileActivityContainer.setVisibility(View.GONE);
            }
        }

        if (profile.isVerified()) {

            mProfileIcon.setVisibility(View.VISIBLE);

        } else {

            mProfileIcon.setVisibility(View.GONE);
        }

        if (App.getInstance().getId() != profile.getId() && !profile.isFriend() && profile.getAllowShowProfileOnlyToFriends() == 1) {

            // User no friend and allow show profile info only to friends

            mProfileInfoContainer.setVisibility(View.GONE);
            mProfileCountersContainer.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);

            mProfileMessage.setVisibility(View.VISIBLE);

        } else {

            mProfileInfoContainer.setVisibility(View.VISIBLE);
            mProfileCountersContainer.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);

            mProfileMessage.setVisibility(View.GONE);
        }

        if (this.isVisible()) {

            try {

                getActivity().invalidateOptionsMenu();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private void updateFullname() {

        if (profile.getFullname() == null || profile.getFullname().length() == 0) {

            mProfileFullname.setText(profile.getUsername());
            if (!isMainScreen) getActivity().setTitle(profile.getUsername());

        } else {

            mProfileFullname.setText(profile.getFullname());
            if (!isMainScreen) getActivity().setTitle(profile.getFullname());
        }
    }

    public void getData() {

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_PROFILE_GET, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ProfileFragment Not Added to Activity");

                            return;
                        }

                        try {

                            if (!response.getBoolean("error")) {

                                profile = new Profile(response);

                                if (profile.getGalleryItemsCount() > 0 && App.getInstance().getSettings().getAllowGallery() == ENABLED) {

                                    galleryList.clear();
                                    galleryItemId = 0;

                                    if (response.has("gallery")) {

                                        JSONObject obj = response.getJSONObject("gallery");

                                        read_gallery_items(obj);
                                    }
                                }

                                if (profile.getItemsCount() > 0) {

                                    itemsList.clear();
                                    itemId = 0;

                                    if (response.has("items")) {

                                        JSONObject obj = response.getJSONObject("items");

                                        read_items(obj);

                                    }
                                }

                                if (profile.getState() == ACCOUNT_STATE_ENABLED) {

                                    showContentScreen();

                                    updateProfile();

                                } else {

                                    showDisabledScreen();
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loadingComplete();

                            Log.d("Profile GET Success", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ProfileFragment Not Added to Activity");

                    return;
                }

                showErrorScreen();

                Log.e("Profile GET Error", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("profileId", Long.toString(profile_id));

                params.put("ghostMode", Integer.toString(App.getInstance().getGhostUpgrade()));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void read_items(JSONObject obj) {

        try {

            itemId = obj.getInt("itemId");

            Log.e("itemId", Integer.toString(itemId));

            if (obj.has("items")) {

                JSONArray itemsArray = obj.getJSONArray("items");

                arrayLength = itemsArray.length();

                if (arrayLength > 0) {

                    for (int i = 0; i < itemsArray.length(); i++) {

                        JSONObject itemObj = (JSONObject) itemsArray.get(i);

                        Item item = new Item(itemObj);

                        itemsList.add(item);

                        // Ad after first item
                        if (i == MY_AD_AFTER_ITEM_NUMBER && App.getInstance().getAdmobUpgrade() == DISABLED) {

                            if (App.getInstance().getSettings().getAllowAdmobBanner() == ENABLED) {

                                Item ad = new Item(itemObj);

                                ad.setAd(1);

                                itemsList.add(ad);
                            }
                        }
                    }
                }
            }

        } catch (JSONException e) {

            e.printStackTrace();

        } finally {

            loadingComplete();

            Log.e("Items", Integer.toString(itemsAdapter.getItemCount()));
        }
    }

    public void read_gallery_items(JSONObject obj) {

        try {

            galleryItemId = obj.getInt("itemId");

            Log.e("itemId", Integer.toString(itemId));

            if (obj.has("items")) {

                JSONArray itemsArray = obj.getJSONArray("items");

                arrayLength = itemsArray.length();

                if (arrayLength > 0) {

                    for (int i = 0; i < itemsArray.length(); i++) {

                        JSONObject itemObj = (JSONObject) itemsArray.get(i);

                        GalleryItem item = new GalleryItem(itemObj);

                        galleryList.add(item);
                    }
                }
            }

        } catch (JSONException e) {

            e.printStackTrace();

        } finally {

            loadingComplete();

            Log.e("Gallery Items", Integer.toString(galleryAdapter.getItemCount()));
        }
    }

    public void showPhoto(String photoUrl) {

        if (photoUrl != null && photoUrl.length() > 0) {

            ImageLoader imageLoader = App.getInstance().getImageLoader();

            imageLoader.get(photoUrl, ImageLoader.getImageListener(mProfilePhoto, R.drawable.profile_default_photo, R.drawable.profile_default_photo));
        }
    }

    public void showCover(String coverUrl) {

        if (coverUrl != null && coverUrl.length() > 0) {

            ImageLoader imageLoader = App.getInstance().getImageLoader();

            imageLoader.get(coverUrl, ImageLoader.getImageListener(mProfileCover, R.drawable.profile_default_cover, R.drawable.profile_default_cover));

            if (Build.VERSION.SDK_INT > 15) {

                mProfileCover.setImageAlpha(200);
            }
        }
    }

    public void getGalleryItems() {

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_GALLERY_GET, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ProfileFragment Not Added to Activity");

                            return;
                        }

                        try {

                            if (!loadingMore) {

                                galleryList.clear();
                            }

                            arrayLength = 0;

                            if (!response.getBoolean("error")) {

                                read_gallery_items(response);
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loadingComplete();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ProfileFragment Not Added to Activity");

                    return;
                }

                loadingComplete();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("profileId", Long.toString(profile.getId()));
                params.put("itemId", Integer.toString(galleryItemId));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void getItems() {

        if (loadingMore) {

            mProfileRefreshLayout.setRefreshing(true);

        } else{

            itemId = 0;
        }

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ITEMS_GET, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ProfileFragment Not Added to Activity");

                            return;
                        }

                        try {

                            if (!loadingMore) {

                                itemsList.clear();
                            }

                            arrayLength = 0;

                            if (!response.getBoolean("error")) {

                                read_items(response);
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loadingComplete();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ProfileFragment Not Added to Activity");

                    return;
                }

                loadingComplete();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("profileId", Long.toString(profile.getId()));
                params.put("itemId", Integer.toString(itemId));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void loadingComplete() {

        if (arrayLength == LIST_ITEMS) {

            viewMore = true;

        } else {

            viewMore = false;
        }

        itemsAdapter.notifyDataSetChanged();
        galleryAdapter.notifyDataSetChanged();

        mProfileRefreshLayout.setRefreshing(false);

        loadingMore = false;

//        if (this.isVisible()) getActivity().invalidateOptionsMenu();
    }

    public void showLoadingScreen() {

        if (!isMainScreen) getActivity().setTitle(getText(R.string.title_activity_profile));

        mProfileRefreshLayout.setVisibility(View.GONE);
        mProfileErrorScreen.setVisibility(View.GONE);
        mProfileDisabledScreen.setVisibility(View.GONE);
//
        mProfileLoadingScreen.setVisibility(View.VISIBLE);

        loadingComplete = false;
    }

    public void showErrorScreen() {

        if (!isMainScreen) getActivity().setTitle(getText(R.string.title_activity_profile));

        mProfileLoadingScreen.setVisibility(View.GONE);
        mProfileDisabledScreen.setVisibility(View.GONE);
        mProfileRefreshLayout.setVisibility(View.GONE);
//
        mProfileErrorScreen.setVisibility(View.VISIBLE);

        loadingComplete = false;
    }

    public void showDisabledScreen() {

        if (profile.getState() != ACCOUNT_STATE_ENABLED) {

            //mProfileDisabledScreenMsg.setText(getText(R.string.msg_account_blocked));
        }

        getActivity().setTitle(getText(R.string.label_account_disabled));

        mProfileRefreshLayout.setVisibility(View.GONE);
        mProfileLoadingScreen.setVisibility(View.GONE);
        mProfileErrorScreen.setVisibility(View.GONE);
//
        mProfileDisabledScreen.setVisibility(View.VISIBLE);

        loadingComplete = false;
    }

    public void showContentScreen() {

        if (!isMainScreen) {

            getActivity().setTitle(profile.getFullname());
        }

        mProfileDisabledScreen.setVisibility(View.GONE);
        mProfileLoadingScreen.setVisibility(View.GONE);
        mProfileErrorScreen.setVisibility(View.GONE);
//
        mProfileRefreshLayout.setVisibility(View.VISIBLE);
        mProfileRefreshLayout.setRefreshing(false);

        loadingComplete = true;
        restore = true;
    }

    public void action(int position) {

        final Item item = itemsList.get(position);
    }

    public void onCloseRepostDialog(String mMessage, int position) {

        Item item = (Item) itemsList.get(position);

        item.setRepostsCount(item.getRepostsCount() + 1);

        Api api = new Api(getActivity());

        if (item.getRepostId() != 0) {

            api.itemRepost(item.getRepostId(), mMessage, 1);

        } else {

            api.itemRepost(item.getId(), mMessage, 1);
        }

        item.setMyRepost(true);

        itemsAdapter.notifyDataSetChanged();
    }

    public void onItemDelete(long itemId, final int position) {

        Api api = new Api(getActivity());

        api.itemDelete(itemId);

        itemsList.remove(position);

        itemsAdapter.notifyDataSetChanged();

        profile.setItemsCount(profile.getItemsCount() - 1);

        updateProfile();
    }

    public void onItemReport(long itemId, int reasonId) {

        if (App.getInstance().isConnected()) {

            Api api = new Api(getActivity());

            api.report(itemId, ITEM_TYPE_POST, reasonId);

        } else {

            Toast.makeText(getActivity(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();

        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

        if (loadingComplete) {

            if (profile.getState() != ACCOUNT_STATE_ENABLED) {

                //hide all menu items
                hideMenuItems(menu, false);
            }

            if (App.getInstance().getId() != profile.getId()) {

                MenuItem menuItem = menu.findItem(R.id.action_profile_block);

                if (profile.isBlocked()) {

                    menuItem.setTitle(getString(R.string.action_unblock));

                } else {

                    menuItem.setTitle(getString(R.string.action_block));
                }

                menu.removeItem(R.id.action_profile_edit_photo);
                menu.removeItem(R.id.action_profile_edit_cover);

                if (App.getInstance().getSettings().getAllowGifts() == DISABLED) {

                    menu.removeItem(R.id.action_new_gift);
                }

            } else {

                // your profile

                menu.removeItem(R.id.action_profile_report);
                menu.removeItem(R.id.action_profile_block);
                menu.removeItem(R.id.action_new_gift);
            }

            //show all menu items
            hideMenuItems(menu, true);

        } else {

            //hide all menu items
            hideMenuItems(menu, false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_new_gift: {

                Intent intent = new Intent(getActivity(), GiftSelectActivity.class);
                intent.putExtra("profileId", profile.getId());
                startActivity(intent);

                return true;
            }

            case R.id.action_profile_refresh: {

                mProfileRefreshLayout.setRefreshing(true);
                onRefresh();

                return true;
            }

            case R.id.action_profile_report: {

                profileReport();

                return true;
            }

            case R.id.action_profile_block: {

                profileBlock();

                return true;
            }

            case R.id.action_profile_edit_photo: {

                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_PHOTO);

                    } else {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_PHOTO);
                    }

                } else {

                    mAccountAction = 0;

                    choiceImage(mAccountAction);
                }

                return true;
            }

            case R.id.action_profile_edit_cover: {

                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_PHOTO);

                    } else {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_PHOTO);
                    }

                } else {

                    mAccountAction = 1;

                    choiceImage(mAccountAction);
                }

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void hideMenuItems(Menu menu, boolean visible) {

        for (int i = 0; i < menu.size(); i++){

            menu.getItem(i).setVisible(visible);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void profileReport() {

        /** Getting the fragment manager */
        android.app.FragmentManager fm = getActivity().getFragmentManager();

        /** Instantiating the DialogFragment class */
        ProfileReportDialog alert = new ProfileReportDialog();

        /** Creating a bundle object to store the selected item's index */
        Bundle b  = new Bundle();

        /** Storing the selected item's index in the bundle object */
        b.putInt("position", 0);

        /** Setting the bundle object to the dialog fragment object */
        alert.setArguments(b);

        /** Creating the dialog fragment object, which will in turn open the alert dialog window */

        alert.show(fm, "alert_dialog_profile_report");
    }

    public  void onProfileReport(final int position) {

        Api api = new Api(getActivity());

        api.report(profile.getId(), ITEM_TYPE_PROFILE, position);
    }

    public void profileBlock() {

        if (!profile.isBlocked()) {

            /** Getting the fragment manager */
            android.app.FragmentManager fm = getActivity().getFragmentManager();

            /** Instantiating the DialogFragment class */
            ProfileBlockDialog alert = new ProfileBlockDialog();

            /** Creating a bundle object to store the selected item's index */
            Bundle b  = new Bundle();

            /** Storing the selected item's index in the bundle object */
            b.putString("blockUsername", profile.getUsername());

            /** Setting the bundle object to the dialog fragment object */
            alert.setArguments(b);

            /** Creating the dialog fragment object, which will in turn open the alert dialog window */

            alert.show(fm, "alert_dialog_profile_block");

        } else {

            loading = true;

            showpDialog();

            CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_BLACKLIST_REMOVE, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (!isAdded() || getActivity() == null) {

                                Log.e("ERROR", "ProfileFragment Not Added to Activity");

                                return;
                            }

                            try {

                                if (!response.getBoolean("error")) {

                                    profile.setBlocked(false);

                                    Toast.makeText(getActivity(), getString(R.string.msg_profile_removed_from_blacklist), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {

                                e.printStackTrace();

                            } finally {

                                loading = false;

                                hidepDialog();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (!isAdded() || getActivity() == null) {

                        Log.e("ERROR", "ProfileFragment Not Added to Activity");

                        return;
                    }

                    loading = false;

                    hidepDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", Long.toString(App.getInstance().getId()));
                    params.put("accessToken", App.getInstance().getAccessToken());
                    params.put("profileId", Long.toString(profile.getId()));

                    return params;
                }
            };

            App.getInstance().addToRequestQueue(jsonReq);
        }
    }

    public  void onProfileBlock() {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_BLACKLIST_ADD, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ProfileFragment Not Added to Activity");

                            return;
                        }

                        try {

                            if (!response.getBoolean("error")) {

                                profile.setBlocked(true);

                                Toast.makeText(getActivity(), getString(R.string.msg_profile_added_to_blacklist), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                            hidepDialog();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ProfileFragment Not Added to Activity");

                    return;
                }

                loading = false;

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("profileId", Long.toString(profile.getId()));
                params.put("reason", "example");

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public Boolean uploadFile(String serverURL, File file, final int type) {

        loading = true;

        showpDialog();

        final OkHttpClient client = new OkHttpClient();

        try {

            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("uploaded_file", file.getName(), RequestBody.create(MediaType.parse("text/csv"), file))
                    .addFormDataPart("accountId", Long.toString(App.getInstance().getId()))
                    .addFormDataPart("accessToken", App.getInstance().getAccessToken())
                    .build();

            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                    .url(serverURL)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(com.squareup.okhttp.Request request, IOException e) {

                    loading = false;

                    hidepDialog();

                    Log.e("failure", request.toString());
                }

                @Override
                public void onResponse(com.squareup.okhttp.Response response) throws IOException {

                    String jsonData = response.body().string();

                    Log.e("response", jsonData);

                    try {

                        JSONObject result = new JSONObject(jsonData);

                        if (!result.getBoolean("error")) {

                            switch (type) {

                                case 0: {

                                    profile.setPhotoUrl(result.getString("photoUrl"));

                                    App.getInstance().setPhotoUrl(result.getString("photoUrl"));

                                    break;
                                }

                                default: {

                                    profile.setCoverUrl(result.getString("coverUrl"));

                                    App.getInstance().setCoverUrl(result.getString("coverUrl"));

                                    break;
                                }
                            }
                        }

                        Log.d("My App", response.toString());

                    } catch (Throwable t) {

                        Log.e("My App", "Could not parse malformed JSON: \"" + response.body().string() + "\"");

                    } finally {

                        loading = false;

                        hidepDialog();

                        getData();
                    }

                }
            });

            return true;

        } catch (Exception ex) {
            // Handle the error

            loading = false;

            hidepDialog();
        }

        return false;
    }












    public void newFriendsRequest() {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_FRIENDS_REQUEST, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ProfileFragment Not Added to Activity");

                            return;
                        }

                        try {

                            if (!response.getBoolean("error")) {

                                mProfileActionBtn.setEnabled(true);
                                mProfileActionBtn.setText(getString(R.string.action_cancel_request));

                                profile.setFriendRequest(true);

                                updateProfile();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                            hidepDialog();

                            Log.e("", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ProfileFragment Not Added to Activity");

                    return;
                }

                loading = false;

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("toUser", Long.toString(profile.getId()));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void cancelFriendsRequest() {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_FRIENDS_CANCEL_REQUEST, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ProfileFragment Not Added to Activity");

                            return;
                        }

                        try {

                            if (!response.getBoolean("error")) {

                                mProfileActionBtn.setEnabled(true);
                                mProfileActionBtn.setText(getString(R.string.action_add_to_friends));

                                profile.setFriendRequest(false);

                                updateProfile();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                            hidepDialog();

                            Log.e("", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ProfileFragment Not Added to Activity");

                    return;
                }

                loading = false;

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("toUser", Long.toString(profile.getId()));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void removeFromFriends() {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_FRIENDS_REMOVE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ProfileFragment Not Added to Activity");

                            return;
                        }

                        try {

                            if (!response.getBoolean("error")) {

                                mProfileActionBtn.setEnabled(true);
                                mProfileActionBtn.setText(getString(R.string.action_add_to_friends));

                                profile.setFriend(false);

                                updateProfile();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                            hidepDialog();

                            Log.e("", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ProfileFragment Not Added to Activity");

                    return;
                }

                loading = false;

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("profileId", Long.toString(profile.getId()));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }
}