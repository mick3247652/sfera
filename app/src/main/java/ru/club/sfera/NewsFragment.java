package ru.club.sfera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.club.sfera.adapter.AdvancedItemListAdapter;
import ru.club.sfera.app.App;
import ru.club.sfera.common.FragmentBase;
import ru.club.sfera.constants.Constants;
import ru.club.sfera.dialogs.ItemDeleteDialog;
import ru.club.sfera.dialogs.ItemReportDialog;
import ru.club.sfera.dialogs.SendRepostDialog;
import ru.club.sfera.model.Item;
import ru.club.sfera.util.Api;
import ru.club.sfera.util.CustomRequest;

public class NewsFragment extends FragmentBase implements Constants, SwipeRefreshLayout.OnRefreshListener, IMenuItemSelect {
    private boolean isShowPhoto = true;
    private boolean isShowText = true;
    private boolean isShowVideo = true;

    private static final String STATE_LIST = "State Adapter Data";
    private static final String STATE_LIST_2 = "State Adapter Data";

    RecyclerView mRecyclerView;
    TextView mMessage;

    SwipeRefreshLayout mItemsContainer;

    FloatingActionButton mFabButton;

    private ArrayList<Item> itemsList;
    private AdvancedItemListAdapter itemsAdapter;

    private Parcelable mListState;

    private int itemId = 0;
    private int arrayLength = 0;
    private Boolean loadingMore = false;
    private Boolean viewMore = false;
    private Boolean restore = false;

    int pastVisiblesItems = 0, visibleItemCount = 0, totalItemCount = 0;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

            itemsList = savedInstanceState.getParcelableArrayList(STATE_LIST_2);
            itemsAdapter = new AdvancedItemListAdapter(getActivity(), itemsList);

            restore = savedInstanceState.getBoolean("restore");
            itemId = savedInstanceState.getInt("itemId");

            viewMore = savedInstanceState.getBoolean("viewMore");

        } else {

            itemsList = new ArrayList<>();
            itemsAdapter = new AdvancedItemListAdapter(getActivity(), itemsList);

            restore = false;
            itemId = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        mItemsContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.container_items);
        mItemsContainer.setOnRefreshListener(this);

        mMessage = (TextView) rootView.findViewById(R.id.message);

        mFabButton = (FloatingActionButton) rootView.findViewById(R.id.fabButton);
        mFabButton.setImageResource(R.drawable.ic_action_new);

        if (App.getInstance().getId() == 0) {

            mFabButton.setVisibility(View.GONE);
        }

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

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
                        alert.show(fm, "alert_dialog_item_report");

                        break;
                    }

                    case R.id.action_remove: {

                        android.app.FragmentManager fm = getActivity().getFragmentManager();

                        ItemDeleteDialog alert = new ItemDeleteDialog();

                        Bundle b = new Bundle();
                        b.putInt("position", position);
                        b.putLong("itemId", obj.getId());

                        alert.setArguments(b);
                        alert.show(fm, "alert_dialog_item_delete");

                        break;
                    }
                }
            }
        });

        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(itemsAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) {

                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (!loadingMore) {

                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && (viewMore) && !(mItemsContainer.isRefreshing())) {

                            loadingMore = true;
                            Log.e("...", "Last Item Wow !");

                            getItems();
                        }
                    }
                }
            }
        });

        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), NewItemActivity.class);
                startActivityForResult(intent, STREAM_NEW_POST);
            }
        });

        if (itemsAdapter.getItemCount() == 0) {

            showMessage(getText(R.string.label_empty_list).toString());

        } else {

            hideMessage();
        }

        if (!restore) {

            showMessage(getText(R.string.msg_loading_2).toString());

            getItems();
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean("viewMore", viewMore);

        outState.putBoolean("restore", true);
        outState.putInt("itemId", itemId);

        mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(STATE_LIST, mListState);

        outState.putParcelableArrayList(STATE_LIST_2, itemsList);
    }

    @Override
    public void onRefresh() {

        if (App.getInstance().isConnected()) {

            itemId = 0;
            getItems();

        } else {

            mItemsContainer.setRefreshing(false);
        }
    }

    public void updateAdapter() {

        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == STREAM_NEW_POST && resultCode == getActivity().RESULT_OK && null != data) {

            itemId = 0;
            getItems();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void getItems() {

        mItemsContainer.setRefreshing(true);

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ITEMS_FEED, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "NewsFragment Not Added to Activity");

                            return;
                        }

                        if (!loadingMore) {

                            itemsList.clear();
                        }

                        try {

                            arrayLength = 0;

                            if (!response.getBoolean("error")) {

                                itemId = response.getInt("itemId");

                                if (response.has("items")) {

                                    JSONArray itemsArray = response.getJSONArray("items");

                                    arrayLength = itemsArray.length();

                                    if (arrayLength > 0) {

                                        for (int i = 0; i < itemsArray.length(); i++) {

                                            JSONObject itemObj = (JSONObject) itemsArray.get(i);

                                            Item item = new Item(itemObj);

                                            if (item.getImgUrl().length() != 0 && isShowPhoto) {
                                                itemsList.add(item);
                                            } else
                                            if(item.getVideoUrl() != null && item.getVideoUrl().length() != 0 && isShowVideo){
                                                itemsList.add(item);
                                            } else
                                            if (item.getYouTubeVideoUrl() != null && item.getYouTubeVideoUrl().length() != 0 && isShowVideo) {
                                                itemsList.add(item);
                                            } else if(isShowText) {
                                                itemsList.add(item);
                                            }

                                            // Spotlight after first item
                                            /*if (i == SPOTLIGHT_AFTER_ITEM_NUMBER && App.getInstance().getSettings().getAllowSpotlight() == ENABLED && itemsAdapter.getItemCount() <= LIST_ITEMS) {

                                                Item spotlight = new Item(itemObj);

                                                spotlight.setSpotlight(1);

                                                itemsList.add(spotlight);
                                            }

                                            // Ad after first item
                                            if (i == MY_AD_AFTER_ITEM_NUMBER && App.getInstance().getAdmobUpgrade() == DISABLED) {

                                                if (App.getInstance().getSettings().getAllowAdmobBanner() == ENABLED) {

                                                    Item ad = new Item(itemObj);

                                                    ad.setAd(1);

                                                    itemsList.add(ad);
                                                }
                                            }*/
                                        }
                                    }
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loadingComplete();

                            Log.d("Feed.response", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "NewsFragment Not Added to Activity");

                    return;
                }

                loadingComplete();

                Log.e("Feed.error", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("itemId", Integer.toString(itemId));
                params.put("language", "en");

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

        if (itemsAdapter.getItemCount() == 0) {

            if (NewsFragment.this.isVisible()) {

                showMessage(getText(R.string.label_empty_list).toString());
            }

        } else {

            hideMessage();
        }

        loadingMore = false;
        mItemsContainer.setRefreshing(false);
    }

    public void showMessage(String message) {

        mMessage.setText(message);
        mMessage.setVisibility(View.VISIBLE);
    }

    public void hideMessage() {

        mMessage.setVisibility(View.GONE);
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setTitle(int titleId){
        MainActivity activity = (MainActivity)getActivity();
        if(activity != null) {
            if(activity.getSupportActionBar() != null)
                activity.getSupportActionBar().setTitle(titleId);
        }

    }

    private void showPhoto(){
        isShowPhoto = true;
        isShowText = false;
        isShowVideo = false;
        setTitle(R.string.photo_title);
        onRefresh();
    }
    private void showVideo(){
        isShowPhoto = false;
        isShowText = false;
        isShowVideo = true;
        setTitle(R.string.video_title);
        onRefresh();
    }
    private void showAll(){
        isShowPhoto = true;
        isShowText = true;
        isShowVideo = true;
        setTitle(R.string.nav_feed);
        onRefresh();
    }
    private void loadPhoto(){
        Intent i = getActivity().getIntent();

        Long profile_id = i.getLongExtra("profileId", 0);
        String profile_mention = i.getStringExtra("profileMention");

        if (App.getInstance().getId() != profile_id) {

            App.getInstance().setCurrentProfileId(profile_id);
        }

        if (profile_id == 0 && (profile_mention == null || profile_mention.length() == 0)) {

            profile_id = App.getInstance().getId();

        }

        Intent intent = new Intent(getActivity(), GalleryActivity.class);
        intent.putExtra("profileId", profile_id);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.stream:
                showAll();
                break;
            case R.id.photo:
                showPhoto();
                break;
            case R.id.video:
                showVideo();
                break;
            case R.id.load_photo:
                loadPhoto();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickMenuItem(MenuItem item) {
        onOptionsItemSelected(item);
    }
}