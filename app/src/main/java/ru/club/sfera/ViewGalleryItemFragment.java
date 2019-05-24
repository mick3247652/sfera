package ru.club.sfera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.balysv.materialripple.MaterialRippleLayout;
import com.pkmmte.view.CircularImageView;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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

import github.ankushsachdeva.emojicon.EditTextImeBackListener;
import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconTextView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;
import ru.club.sfera.adapter.AdvancedCommentListAdapter;
import ru.club.sfera.app.App;
import ru.club.sfera.common.FragmentBase;
import ru.club.sfera.constants.Constants;
import ru.club.sfera.dialogs.CommentDeleteDialog;
import ru.club.sfera.dialogs.CommentReportDialog;
import ru.club.sfera.dialogs.ImageChooseDialog;
import ru.club.sfera.dialogs.ItemDeleteDialog;
import ru.club.sfera.dialogs.ItemReportDialog;
import ru.club.sfera.model.Comment;
import ru.club.sfera.model.GalleryItem;
import ru.club.sfera.util.Api;
import ru.club.sfera.util.CustomRequest;
import ru.club.sfera.view.ResizableImageView;


public class ViewGalleryItemFragment extends FragmentBase implements Constants, SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mRefreshLayout;
    RelativeLayout mErrorScreen, mLoadingScreen, mEmptyScreen;
    LinearLayout mContentScreen, mCommentFormContainer, mItemLocationContainer;

    EmojiconEditText mCommentText;

    RecyclerView mRecyclerView;
    NestedScrollView mNestedScrollView;
    Button mRetryBtn;

    ImageView mItemLike, mItemComment, mEmojiBtn, mChoiceImg, mSendComment, mItemAuthorOnlineIcon;
    TextView mItemLocation, mItemAuthor, mItemUsername, mItemTimeAgo, mItemLikesCount, mItemCommentsCount;
    EmojiconTextView mItemText;
    ResizableImageView mItemImg;
    CircularImageView mItemAuthorPhoto, mItemAuthorIcon;

    public MaterialRippleLayout mItemShareButton;

    ImageView mItemPlay;
    ProgressBar mProgressBar;

    ImageLoader imageLoader = App.getInstance().getImageLoader();


    private ArrayList<Comment> itemsList;

    private AdvancedCommentListAdapter itemsAdapter;

    GalleryItem item;

    long itemId = 0, replyToUserId = 0;
    int arrayLength = 0;
    String commentText = "", imgUrl = "";

    private Boolean loading = false;
    private Boolean restore = false;
    private Boolean preload = false;
    private Boolean loadingComplete = false;

    EmojiconsPopup popup;

    private String selectedImg = "";
    private Uri selectedImage;
    private Uri outputFileUri;

    public ViewGalleryItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        setHasOptionsMenu(true);

        Intent i = getActivity().getIntent();

        itemId = i.getLongExtra("itemId", 0);

        itemsList = new ArrayList<>();
        itemsAdapter = new AdvancedCommentListAdapter(getActivity(), itemsList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_view_gallery_item, container, false);

        popup = new EmojiconsPopup(rootView, getActivity());

        popup.setSizeForSoftKeyboard();

        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {

                mCommentText.append(emojicon.getEmoji());
            }
        });

        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {

                KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                mCommentText.dispatchKeyEvent(event);
            }
        });

        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

                setIconEmojiKeyboard();
            }
        });

        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {

            }

            @Override
            public void onKeyboardClose() {

                if (popup.isShowing())

                    popup.dismiss();
            }
        });

        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {

                mCommentText.append(emojicon.getEmoji());
            }
        });

        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {

                KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                mCommentText.dispatchKeyEvent(event);
            }
        });

        if (savedInstanceState != null) {

            restore = savedInstanceState.getBoolean("restore");
            loading = savedInstanceState.getBoolean("loading");
            preload = savedInstanceState.getBoolean("preload");

            replyToUserId = savedInstanceState.getLong("replyToUserId");

        } else {

            restore = false;
            loading = false;
            preload = false;

            replyToUserId = 0;
        }

        if (loading) {

            showpDialog();
        }

        mEmptyScreen = (RelativeLayout) rootView.findViewById(R.id.emptyScreen);
        mErrorScreen = (RelativeLayout) rootView.findViewById(R.id.errorScreen);
        mLoadingScreen = (RelativeLayout) rootView.findViewById(R.id.loadingScreen);
        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);

        mContentScreen = (LinearLayout) rootView.findViewById(R.id.contentScreen);
        mCommentFormContainer = (LinearLayout) rootView.findViewById(R.id.commentFormContainer);

        mItemLocationContainer = (LinearLayout) rootView.findViewById(R.id.itemLocationContainer);
        mItemLocation = (TextView) rootView.findViewById(R.id.itemLocation);

        mCommentText = (EmojiconEditText) rootView.findViewById(R.id.commentText);
        mSendComment = (ImageView) rootView.findViewById(R.id.sendCommentImg);
        mEmojiBtn = (ImageView) rootView.findViewById(R.id.emojiBtn);
        mChoiceImg = (ImageView) rootView.findViewById(R.id.choiceImg);

        mSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commentText = mCommentText.getText().toString();
                commentText = commentText.trim();

                if ((selectedImg != null && selectedImg.length() > 0) || commentText.length() != 0) {

                    loading = true;

                    showpDialog();

                    if (selectedImg != null && selectedImg.length() > 0) {

                        File f = new File(Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER, "photo.jpg");

                        uploadFile(METHOD_COMMENT_UPLOAD_IMAGE, f);

                        send();

                    } else {

                        send();
                    }
                }
            }
        });

        mRetryBtn = (Button) rootView.findViewById(R.id.retryBtn);

        mRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App.getInstance().isConnected()) {

                    showLoadingScreen();

                    getItem();
                }
            }
        });

        mNestedScrollView = (NestedScrollView) rootView.findViewById(R.id.nestedScrollView);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        itemsAdapter.setOnMoreButtonClickListener(new AdvancedCommentListAdapter.OnItemMenuButtonClickListener() {

            @Override
            public void onItemClick(View v, Comment obj, int actionId, int position) {

                switch (actionId){

                    case R.id.action_report: {

                        android.app.FragmentManager fm = getActivity().getFragmentManager();

                        CommentReportDialog alert = new CommentReportDialog();

                        Bundle b  = new Bundle();
                        b.putLong("itemId", obj.getId());
                        b.putInt("reason", 0);

                        alert.setArguments(b);
                        alert.show(fm, "alert_dialog_comment_report");

                        break;
                    }

                    case R.id.action_remove: {

                        android.app.FragmentManager fm = getActivity().getFragmentManager();

                        CommentDeleteDialog alert = new CommentDeleteDialog();

                        Bundle b = new Bundle();
                        b.putInt("position", position);
                        b.putLong("itemId", obj.getId());

                        alert.setArguments(b);
                        alert.show(fm, "alert_dialog_comment_delete");

                        break;
                    }

                    case R.id.action_reply: {

                        replyToUserId = obj.getFromUserId();

                        mCommentText.setText("@" + obj.getFromUserUsername() + ", ");
                        mCommentText.setSelection(mCommentText.getText().length());

                        mCommentText.requestFocus();

                        break;
                    }
                }
            }
        });

        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(itemsAdapter);

        mRecyclerView.setNestedScrollingEnabled(false);

        mItemAuthorPhoto = (CircularImageView) rootView.findViewById(R.id.itemAuthorPhoto);
        mItemAuthorIcon = (CircularImageView) rootView.findViewById(R.id.itemAuthorIcon);
        mItemLike = (ImageView) rootView.findViewById(R.id.itemLikeImg);
        mItemComment = (ImageView) rootView.findViewById(R.id.itemCommentImg);

        mItemAuthor = (TextView) rootView.findViewById(R.id.itemAuthor);
        mItemAuthorOnlineIcon = (ImageView) rootView.findViewById(R.id.itemAuthorOnlineIcon);
        mItemUsername = (TextView) rootView.findViewById(R.id.itemAuthorUsername);
        mItemText = (EmojiconTextView) rootView.findViewById(R.id.itemDescription);
        mItemTimeAgo = (TextView) rootView.findViewById(R.id.itemTimeAgo);
        mItemLikesCount = (TextView) rootView.findViewById(R.id.itemLikesCount);
        mItemCommentsCount = (TextView) rootView.findViewById(R.id.itemCommentsCount);
        mItemImg = (ResizableImageView) rootView.findViewById(R.id.itemImg);

        mItemPlay = (ImageView) rootView.findViewById(R.id.itemPlayVideo);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mItemShareButton = (MaterialRippleLayout) rootView.findViewById(R.id.itemShareButton);

        if (App.getInstance().getSettings().getAllowEmoji() == DISABLED) {

            mEmojiBtn.setVisibility(View.GONE);
        }

        if (App.getInstance().getSettings().getAllowAddImageToComment() == DISABLED) {

            mChoiceImg.setVisibility(View.GONE);
        }

        if (selectedImg != null && selectedImg.length() > 0) {

            mChoiceImg.setImageURI(Uri.fromFile(new File(selectedImg)));
        }

        mEmojiBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!popup.isShowing()) {

                    if (popup.isKeyBoardOpen()){

                        popup.showAtBottom();
                        setIconSoftKeyboard();

                    } else {

                        mCommentText.setFocusableInTouchMode(true);
                        mCommentText.requestFocus();
                        popup.showAtBottomPending();

                        final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(mCommentText, InputMethodManager.SHOW_IMPLICIT);
                        setIconSoftKeyboard();
                    }

                } else {

                    popup.dismiss();
                }
            }
        });

        mChoiceImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (selectedImg.length() == 0) {

                    if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_PHOTO);

                        } else {

                            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_PHOTO);
                        }

                    } else {

                        choiceImage();
                    }

                } else {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle(getText(R.string.action_remove));

                    alertDialog.setMessage(getText(R.string.label_delete_item));
                    alertDialog.setCancelable(true);

                    alertDialog.setNeutralButton(getText(R.string.action_cancel), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });

                    alertDialog.setPositiveButton(getText(R.string.action_remove), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            mChoiceImg.setImageResource(R.drawable.ic_action_camera);
                            selectedImg = "";

                            dialog.cancel();
                        }
                    });

                    alertDialog.show();
                }
            }
        });

        EditTextImeBackListener er = new EditTextImeBackListener() {

            @Override
            public void onImeBack(EmojiconEditText ctrl, String text) {

                hideEmojiKeyboard();
            }
        };

        mCommentText.setOnEditTextImeBackListener(er);

        if (!restore) {

            if (App.getInstance().isConnected()) {

                showLoadingScreen();
                getItem();

            } else {

                showErrorScreen();
            }

        } else {

            if (App.getInstance().isConnected()) {

                if (!preload) {

                    loadingComplete();
                    updateItem();

                } else {

                    showLoadingScreen();
                }

            } else {

                showErrorScreen();
            }
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    public void hideEmojiKeyboard() {

        popup.dismiss();
    }

    public void setIconEmojiKeyboard() {

        mEmojiBtn.setBackgroundResource(R.drawable.ic_emoji);
    }

    public void setIconSoftKeyboard() {

        mEmojiBtn.setBackgroundResource(R.drawable.ic_keyboard);
    }

    public void onDestroyView() {

        super.onDestroyView();

        hidepDialog();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean("restore", true);
        outState.putBoolean("loading", loading);
        outState.putBoolean("preload", preload);

        outState.putLong("replyToUserId", replyToUserId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_PHOTO: {

                // If request is cancelled, the result arrays are empty.

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    choiceImage();

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

        if (requestCode == ITEM_EDIT && resultCode == getActivity().RESULT_OK) {

            item.setDescription(data.getStringExtra("desc"));
            item.setImgUrl(data.getStringExtra("imgUrl"));
            item.setPreviewImgUrl(data.getStringExtra("previewImgUrl"));
            item.setVideoUrl(data.getStringExtra("videoUrl"));
            item.setPreviewVideoImgUrl(data.getStringExtra("previewVideoImgUrl"));
            item.setArea(data.getStringExtra("area"));
            item.setCountry(data.getStringExtra("country"));
            item.setCity(data.getStringExtra("city"));
            item.setLat(data.getDoubleExtra("lat", 0.000000));
            item.setLng(data.getDoubleExtra("lng", 0.000000));

            if (item.getVideoUrl().length() > 0) {

                item.setItemType(GALLERY_ITEM_TYPE_VIDEO);

            } else {

                item.setItemType(GALLERY_ITEM_TYPE_IMAGE);
            }

            updateItem();

        } else if (requestCode == SELECT_POST_IMG && resultCode == getActivity().RESULT_OK && null != data) {

            selectedImage = data.getData();

            selectedImg = getImageUrlWithAuthority(getActivity(), selectedImage, "photo.jpg");

            try {

                save(selectedImg, "photo.jpg");

                selectedImg = Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER + File.separator + "photo.jpg";

                mChoiceImg.setImageURI(Uri.fromFile(new File(selectedImg)));

            } catch (Exception e) {

                Log.e("OnSelectPostImage", e.getMessage());
            }

        } else if (requestCode == CREATE_POST_IMG && resultCode == getActivity().RESULT_OK) {

            try {

                selectedImg = Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER + File.separator + "photo.jpg";

                save(selectedImg, "photo.jpg");

                mChoiceImg.setImageURI(Uri.fromFile(new File(selectedImg)));

            } catch (Exception ex) {

                Log.v("OnCameraCallBack", ex.getMessage());
            }
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

    public void choiceImage() {

        android.app.FragmentManager fm = getActivity().getFragmentManager();

        ImageChooseDialog alert = new ImageChooseDialog();

        alert.show(fm, "alert_dialog_image_choose");
    }

    public void imageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, getText(R.string.label_select_img)), SELECT_POST_IMG);
    }

    public void imageFromCamera() {

        try {

            File root = new File(Environment.getExternalStorageDirectory(), APP_TEMP_FOLDER);

            if (!root.exists()) {

                root.mkdirs();
            }

            File sdImageMainDirectory = new File(root, "photo.jpg");
            outputFileUri = Uri.fromFile(sdImageMainDirectory);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(cameraIntent, CREATE_POST_IMG);

        } catch (Exception e) {

            Toast.makeText(getActivity(), "Error occured. Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {

        if (App.getInstance().isConnected()) {

            mRefreshLayout.setRefreshing(true);
            getItem();

        } else {

            mRefreshLayout.setRefreshing(false);
        }
    }

    public void updateItem() {

        if (imageLoader == null) {

            imageLoader = App.getInstance().getImageLoader();
        }

        mProgressBar.setVisibility(View.GONE);
        mItemPlay.setVisibility(View.GONE);

        if ((item.getCity() != null && item.getCity().length() > 0) || (item.getCountry() != null && item.getCountry().length() > 0)) {

            String location;

            if (item.getCountry() != null && item.getCountry().length() > 0) {

                location = item.getCountry();

            } else {

                location = "";
            }

            if (item.getCity() != null && item.getCity().length() > 0) {

                location = location + ", " + item.getCity();
            }

            mItemLocation.setText(location);

            mItemLocationContainer.setVisibility(View.VISIBLE);

        } else {

            mItemLocationContainer.setVisibility(View.GONE);
        }

        mItemAuthor.setText(item.getFromUserFullname());
        mItemUsername.setText("@" + item.getFromUserUsername());

        if (item.getFromUserOnline() && item.getFromUserAllowShowOnline() == 1) {

            mItemAuthorOnlineIcon.setVisibility(View.VISIBLE);

        } else {

            mItemAuthorOnlineIcon.setVisibility(View.GONE);
        }

        if (item.getFromUserPhotoUrl().length() != 0) {

            mItemAuthorPhoto.setVisibility(View.VISIBLE);

            imageLoader.get(item.getFromUserPhotoUrl(), ImageLoader.getImageListener(mItemAuthorPhoto, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

        } else {

            mItemAuthorPhoto.setVisibility(View.VISIBLE);
            mItemAuthorPhoto.setImageResource(R.drawable.profile_default_photo);
        }

        if (item.getFromUserVerified() == 1) {

            mItemAuthorIcon.setVisibility(View.VISIBLE);

        } else {

            mItemAuthorIcon.setVisibility(View.GONE);
        }

        mItemAuthorPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("profileId", item.getFromUserId());
                startActivity(intent);
            }
        });

        mItemAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("profileId", item.getFromUserId());
                startActivity(intent);
            }
        });

        mItemShareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Api api = new Api(getActivity());

                api.itemShare(item);
            }
        });

        mItemLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (App.getInstance().isConnected()) {

                    CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_LIKE_ADD, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    if (!isAdded() || getActivity() == null) {

                                        Log.e("ERROR", "ViewGalleryItemFragment Not Added to Activity");

                                        return;
                                    }

                                    try {

                                        if (!response.getBoolean("error")) {

                                            item.setLikesCount(response.getInt("likesCount"));
                                            item.setMyLike(response.getBoolean("myLike"));
                                        }

                                    } catch (JSONException e) {

                                        e.printStackTrace();

                                    } finally {

                                        updateItem();

                                        Log.e("", response.toString());
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if (!isAdded() || getActivity() == null) {

                                Log.e("ERROR", "ViewGalleryItemFragment Not Added to Activity");

                                return;
                            }

                            Log.e("Gallery.Like", error.toString());
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("accountId", Long.toString(App.getInstance().getId()));
                            params.put("accessToken", App.getInstance().getAccessToken());
                            params.put("itemId", Long.toString(item.getId()));
                            params.put("itemFromUserId", Long.toString(item.getFromUserId()));
                            params.put("itemType", Integer.toString(ITEM_TYPE_GALLERY_ITEM));

                            return params;
                        }
                    };

                    App.getInstance().addToRequestQueue(jsonReq);

                } else {

                    Toast.makeText(getActivity(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (item.isMyLike()) {

            mItemLike.setImageResource(R.drawable.perk_active);

        } else {

            mItemLike.setImageResource(R.drawable.perk);
        }

        if (item.getCommentsCount() > 0) {

            mItemCommentsCount.setText(Integer.toString(item.getCommentsCount()));
            mItemCommentsCount.setVisibility(View.VISIBLE);

        } else {

            mItemCommentsCount.setText(Integer.toString(item.getCommentsCount()));
            mItemCommentsCount.setVisibility(View.GONE);
        }

        if (item.getLikesCount() > 0) {

            mItemLikesCount.setText(Integer.toString(item.getLikesCount()));
            mItemLikesCount.setVisibility(View.VISIBLE);

        } else {

            mItemLikesCount.setText(Integer.toString(item.getLikesCount()));
            mItemLikesCount.setVisibility(View.GONE);
        }

        mItemLikesCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), LikersActivity.class);
                intent.putExtra("itemId", item.getId());
                intent.putExtra("itemType", ITEM_TYPE_GALLERY_ITEM);
                startActivity(intent);
            }
        });


        mItemTimeAgo.setText(item.getTimeAgo());
        mItemTimeAgo.setVisibility(View.VISIBLE);

        if (item.getDescription().length() > 0) {

            mItemText.setText(item.getDescription().replaceAll("<br>", "\n"));

            mItemText.setVisibility(View.VISIBLE);

        } else {

            mItemText.setVisibility(View.GONE);
        }

        if (item.getItemType() == Constants.GALLERY_ITEM_TYPE_VIDEO) {

            mItemImg.setVisibility(View.VISIBLE);

            mProgressBar.setVisibility(View.VISIBLE);

            final ProgressBar progressView = mProgressBar;
            final ImageView playButtonView = mItemPlay;
            final ImageView imageView = mItemImg;

            Picasso.with(getActivity())
                    .load(item.getPreviewVideoImgUrl())
                    .into(mItemImg, new Callback() {

                        @Override
                        public void onSuccess() {

                            progressView.setVisibility(View.GONE);
                            playButtonView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                            progressView.setVisibility(View.GONE);
                            playButtonView.setVisibility(View.VISIBLE);
                            imageView.setImageResource(R.drawable.img_loading_error);
                        }
                    });

        } else {

            mItemImg.setVisibility(View.VISIBLE);

            mProgressBar.setVisibility(View.VISIBLE);

            final ProgressBar progressView = mProgressBar;
            final ImageView imageView = mItemImg;

            Picasso.with(getActivity())
                    .load(item.getImgUrl())
                    .into(mItemImg, new Callback() {

                        @Override
                        public void onSuccess() {

                            progressView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                            progressView.setVisibility(View.GONE);
                            imageView.setImageResource(R.drawable.img_loading_error);
                        }
                    });
        }

        mItemImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (item.getItemType() == Constants.GALLERY_ITEM_TYPE_VIDEO) {

                    playVideo(item.getVideoUrl());

                } else {

                    Intent i = new Intent(getActivity(), PhotoViewActivity.class);
                    i.putExtra("imgUrl", item.getImgUrl());
                    startActivity(i);
                }
            }
        });

        mItemPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                playVideo(item.getVideoUrl());
            }
        });
    }

    public void playVideo(String videoUrl) {

        Intent i = new Intent(getActivity(), VideoPlayActivity.class);
        i.putExtra("videoUrl", videoUrl);
        startActivity(i);
    }

    public void getItem() {

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_GALLERY_GET_ITEM, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ViewGalleryItemFragment Not Added to Activity");

                            return;
                        }

                        try {

                            arrayLength = 0;

                            if (!response.getBoolean("error")) {

                                itemsList.clear();

                                itemId = response.getInt("itemId");

                                if (response.has("items")) {

                                    JSONArray itemsArray = response.getJSONArray("items");

                                    arrayLength = itemsArray.length();

                                    if (arrayLength > 0) {

                                        for (int i = 0; i < itemsArray.length(); i++) {

                                            JSONObject itemObj = (JSONObject) itemsArray.get(i);

                                            item = new GalleryItem(itemObj);

                                            updateItem();
                                        }
                                    }
                                }

                                if (response.has("comments") && item.getFromUserAllowGalleryComments() == 1) {

                                    JSONObject commentsObj = response.getJSONObject("comments");

                                    if (commentsObj.has("comments")) {

                                        JSONArray commentsArray = commentsObj.getJSONArray("comments");

                                        arrayLength = commentsArray.length();

                                        if (arrayLength > 0) {

                                            for (int i = commentsArray.length() - 1; i > -1 ; i--) {

                                                JSONObject itemObj = (JSONObject) commentsArray.get(i);

                                                Comment comment = new Comment(itemObj);

                                                comment.setItemFromUserId(item.getFromUserId());

                                                itemsList.add(comment);
                                            }
                                        }
                                    }
                                }

                                loadingComplete();

                            } else {

                                showErrorScreen();
                            }

                        } catch (JSONException e) {

                            showErrorScreen();

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ViewGalleryItemFragment Not Added to Activity");

                    return;
                }

                showErrorScreen();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("itemId", Long.toString(itemId));
                params.put("language", "en");

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void send() {

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_COMMENT_ADD, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "ViewGalleryItemFragment Not Added to Activity");

                            return;
                        }

                        try {

                            if (!response.getBoolean("error")) {

                                if (response.has("comment")) {

                                    JSONObject commentObj = (JSONObject) response.getJSONObject("comment");

                                    Comment comment = new Comment(commentObj);

                                    itemsList.add(comment);

                                    itemsAdapter.notifyDataSetChanged();

                                    item.setCommentsCount(item.getCommentsCount() + 1);

                                    mNestedScrollView.fullScroll(View.FOCUS_DOWN);

                                    mCommentText.setText("");

                                    replyToUserId = 0;

                                    imgUrl = "";
                                }

                                Toast.makeText(getActivity(), getString(R.string.msg_comment_has_been_added), Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                            hidepDialog();

                            mChoiceImg.setImageResource(R.drawable.ic_action_camera);
                            selectedImg = "";

                            updateItem();

                            hideKeyboard();

                            Log.d("Comment.add (Response)", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "ViewGalleryItemFragment Not Added to Activity");

                    return;
                }

                loading = false;

                hidepDialog();

                Log.e("Comment.add", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());

                params.put("itemId", Long.toString(item.getId()));
                params.put("itemFromUserId", Long.toString(item.getFromUserId()));
                params.put("content", commentText);
                params.put("imgUrl", imgUrl);
                params.put("itemType", Integer.toString(ITEM_TYPE_GALLERY_ITEM));

                params.put("replyToUserId", Long.toString(replyToUserId));

                return params;
            }
        };

        int socketTimeout = 0;//0 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonReq.setRetryPolicy(policy);

        App.getInstance().addToRequestQueue(jsonReq);
    }

    private void hideKeyboard() {

        View view = getActivity().getCurrentFocus();

        if (view != null) {

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onItemDelete(long itemId, final int position) {

        Api api = new Api(getActivity());

        api.galleryItemDelete(itemId);

        getActivity().finish();
    }

    public void onCommentDelete(final int position) {

        final Comment comment = itemsList.get(position);

        itemsList.remove(position);
        itemsAdapter.notifyDataSetChanged();

        item.setCommentsCount(item.getCommentsCount() - 1);

        Api api = new Api(getActivity());

        api.commentDelete(comment.getId(), ITEM_TYPE_GALLERY_ITEM);

        updateItem();
    }

    public void onItemReport(long itemId, int reasonId) {

        if (App.getInstance().isConnected()) {

            Api api = new Api(getActivity());

            api.report(itemId, ITEM_TYPE_GALLERY_ITEM, reasonId);

        } else {

            Toast.makeText(getActivity(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void onCommentReport(long itemId, int reasonId) {

        if (App.getInstance().isConnected()) {

            Api api = new Api(getActivity());

            api.report(itemId, ITEM_TYPE_COMMENT, reasonId);

        } else {

            Toast.makeText(getActivity(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void remove(long itemId, int position) {

        android.app.FragmentManager fm = getActivity().getFragmentManager();

        ItemDeleteDialog alert = new ItemDeleteDialog();

        Bundle b = new Bundle();
        b.putInt("position", 0);
        b.putLong("itemId", itemId);

        alert.setArguments(b);
        alert.show(fm, "alert_dialog_item_delete");
    }

    public void report(int position) {

        android.app.FragmentManager fm = getActivity().getFragmentManager();

        ItemReportDialog alert = new ItemReportDialog();

        Bundle b  = new Bundle();
        b.putLong("itemId", item.getId());
        b.putInt("reason", 0);

        alert.setArguments(b);
        alert.show(fm, "alert_dialog_item_report");
    }

    public void loadingComplete() {

        itemsAdapter.notifyDataSetChanged();

        showContentScreen();

        if (mRefreshLayout.isRefreshing()) {

            mRefreshLayout.setRefreshing(false);
        }
    }

    public void showLoadingScreen() {

        preload = true;

        mContentScreen.setVisibility(View.GONE);
        mErrorScreen.setVisibility(View.GONE);
        mEmptyScreen.setVisibility(View.GONE);

        mLoadingScreen.setVisibility(View.VISIBLE);

        loadingComplete = false;
    }

    public void showEmptyScreen() {

        mContentScreen.setVisibility(View.GONE);
        mLoadingScreen.setVisibility(View.GONE);
        mErrorScreen.setVisibility(View.GONE);

        mEmptyScreen.setVisibility(View.VISIBLE);

        loadingComplete = false;
    }

    public void showErrorScreen() {

        mContentScreen.setVisibility(View.GONE);
        mLoadingScreen.setVisibility(View.GONE);
        mEmptyScreen.setVisibility(View.GONE);

        mErrorScreen.setVisibility(View.VISIBLE);

        loadingComplete = false;
    }

    public void showContentScreen() {

        preload = false;

        mLoadingScreen.setVisibility(View.GONE);
        mErrorScreen.setVisibility(View.GONE);
        mEmptyScreen.setVisibility(View.GONE);

        mContentScreen.setVisibility(View.VISIBLE);

        if (item.getFromUserAllowGalleryComments() == COMMENTS_DISABLED) {

            mCommentFormContainer.setVisibility(View.GONE);
        }

        loadingComplete = true;

        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void hideMenuItems(Menu menu, boolean visible) {

        for (int i = 0; i < menu.size(); i++){

            menu.getItem(i).setVisible(visible);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();

        inflater.inflate(R.menu.menu_gallery_item, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

        if (loadingComplete) {

            if (App.getInstance().getId() != item.getFromUserId()) {

                menu.removeItem(R.id.action_remove);
                menu.removeItem(R.id.action_edit);

            } else {

                menu.removeItem(R.id.action_report);
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

            case R.id.action_remove: {

                // remove item

                remove(this.item.getId(), 0);

                return true;
            }

            case R.id.action_edit: {

                // edit item

                Intent i = new Intent(getActivity(), EditGalleryItemActivity.class);
                i.putExtra("position", 0);
                i.putExtra("itemId", this.item.getId());
                i.putExtra("desc", this.item.getDescription());
                i.putExtra("imgUrl", this.item.getImgUrl());
                i.putExtra("previewImgUrl", this.item.getPreviewImgUrl());
                i.putExtra("videoUrl", this.item.getVideoUrl());
                i.putExtra("previewVideoImgUrl", this.item.getPreviewVideoImgUrl());
                i.putExtra("area", this.item.getArea());
                i.putExtra("country", this.item.getCountry());
                i.putExtra("city", this.item.getCity());
                i.putExtra("lat", this.item.getLat());
                i.putExtra("lng", this.item.getLng());
                startActivityForResult(i, ITEM_EDIT);

                return true;
            }

            case R.id.action_report: {

                // report item

                report(0);

                return true;
            }

            case R.id.action_share: {

                // share item

                Api api = new Api(getActivity());

                api.itemShare(this.item);

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    public Boolean uploadFile(String serverURL, File file) {

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
                    .addHeader("Accept", "application/json;")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new com.squareup.okhttp.Callback() {

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

                            imgUrl = result.getString("imgUrl");
                        }

                        Log.d("My App", response.toString());

                    } catch (Throwable t) {

                        Log.e("My App", "Could not parse malformed JSON: \"" + t.getMessage() + "\"");

                    } finally {

                        Log.e("response", jsonData);

                        send();
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
}