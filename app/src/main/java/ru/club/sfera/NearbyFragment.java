package ru.club.sfera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.club.sfera.adapter.AdvancedItemListAdapter;
import ru.club.sfera.adapter.SearchListAdapter;
import ru.club.sfera.app.App;
import ru.club.sfera.constants.Constants;
import ru.club.sfera.dialogs.ItemDeleteDialog;
import ru.club.sfera.dialogs.ItemReportDialog;
import ru.club.sfera.dialogs.NearbySettingsDialog;
import ru.club.sfera.dialogs.SendRepostDialog;
import ru.club.sfera.model.Item;
import ru.club.sfera.model.Profile;
import ru.club.sfera.util.Api;
import ru.club.sfera.util.CustomRequest;
import ru.club.sfera.util.Helper;

import static com.facebook.FacebookSdk.getApplicationContext;
import static ru.club.sfera.R.id.spotlight;

public class NearbyFragment extends Fragment implements Constants, SwipeRefreshLayout.OnRefreshListener {

    private static final String STATE_LIST = "State Adapter Data";
    private static final String STATE_LIST_2 = "State Adapter Data 2";

    Menu MainMenu;

    RecyclerView mRecyclerView;
    TextView mMessage, mDetails;
    ImageView mSplash;

    SwipeRefreshLayout mItemsContainer;

    LinearLayout mSpotLight, mPermissionSpotlight;

    Button mGrantPermission;

    private ArrayList<Item> itemsList;
    private AdvancedItemListAdapter itemsAdapter;

    private ArrayList<Profile> peopleList;
    private SearchListAdapter peopleAdapter;

    private int itemId = 0;
    private int arrayLength = 0;
    private Boolean loadingMore = false;
    private Boolean viewMore = false;
    private Boolean restore = false;

    int pastVisiblesItems = 0, visibleItemCount = 0, totalItemCount = 0;

    private int distance = NEARBY_DIALOG_OPTION_1;      // im miles
    private int mode = 0;                               // view mode

    public NearbyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//        setRetainInstance(true);

        setHasOptionsMenu(true);

        if (savedInstanceState != null) {

            itemsList = savedInstanceState.getParcelableArrayList(STATE_LIST);
            itemsAdapter = new AdvancedItemListAdapter(getActivity(), itemsList);

            peopleList = savedInstanceState.getParcelableArrayList(STATE_LIST_2);
            peopleAdapter = new SearchListAdapter(getActivity(), peopleList);

            restore = savedInstanceState.getBoolean("restore");
            itemId = savedInstanceState.getInt("itemId");
            mode = savedInstanceState.getInt("mode");
            distance = savedInstanceState.getInt("distance");
            viewMore = savedInstanceState.getBoolean("viewMore");

        } else {

            itemsList = new ArrayList<>();
            itemsAdapter = new AdvancedItemListAdapter(getActivity(), itemsList);

            peopleList = new ArrayList<>();
            peopleAdapter = new SearchListAdapter(getActivity(), peopleList);

            restore = false;
            itemId = 0;
            mode = 0;
            distance = NEARBY_DIALOG_OPTION_1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);

        mItemsContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.container_items);
        mItemsContainer.setOnRefreshListener(this);

        mMessage = (TextView) rootView.findViewById(R.id.message);
        mSplash = (ImageView) rootView.findViewById(R.id.splash);

        mSpotLight = (LinearLayout) rootView.findViewById(spotlight);
        mDetails = (TextView) rootView.findViewById(R.id.openLocationSettings);

        mPermissionSpotlight = (LinearLayout) rootView.findViewById(R.id.permission_spotlight);
        mGrantPermission = (Button) rootView.findViewById(R.id.grantPermissionBtn);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        switch (mode) {

            case 0: {

                setPeopleMode();

                break;
            }

            default: {

                setItemsMode();

                break;
            }
        }

        mDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), LocationActivity.class);
                startActivityForResult(i, 101);
            }
        });

        mGrantPermission.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)){

                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);

                    } else {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
                    }
                }
            }
        });

        updateSpotLight();

        if (!restore && App.getInstance().getLat() != 0.000000 && App.getInstance().getLng() != 0.000000) {

            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                updateSpotLight();

            } else {

                showMessage(getText(R.string.msg_loading_2).toString());

                setM(mode);
            }
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    public void choiceMode(int view) {

        switch (view) {

            case 0: {

                peopleMode();

                break;
            }

            default: {

                itemsMode();

                break;
            }
        }
    }

    public void setItemsMode() {

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

                            getItems();
                        }
                    }
                }
            }
        });

        if (itemsAdapter.getItemCount() == 0) {

            showMessage(getText(R.string.label_empty_list).toString());

        } else {

            hideMessage();
        }
    }

    public void itemsMode() {

        mode = 1;

        setItemsMode();

        setM(mode);

        getActivity().invalidateOptionsMenu();
    }

    public void setPeopleMode() {

        peopleAdapter.setOnItemClickListener(new SearchListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, Profile obj, int position) {

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("profileId", obj.getId());
                startActivity(intent);
            }
        });

        final LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), Helper.getGridSpanCount(getActivity()));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(peopleAdapter);

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

                            getPeople();
                        }
                    }
                }
            }
        });

        if (peopleAdapter.getItemCount() == 0) {

            showMessage(getText(R.string.label_empty_list).toString());

        } else {

            hideMessage();
        }
    }

    public void peopleMode() {

        mode = 0;

        setPeopleMode();

        setM(mode);

        getActivity().invalidateOptionsMenu();
    }

    public void updateSpotLight() {

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)){

                showPermissionSpotlight();
                hideNoLocationSpotlight();
                hideItemsContainer();
                hideMessage();

            } else {

                showPermissionSpotlight();
                hideNoLocationSpotlight();
                hideItemsContainer();
                hideMessage();
            }

        } else {

            hidePermissionSpotlight();

            if (App.getInstance().getLat() != 0.000000 && App.getInstance().getLng() != 0.000000) {

                hidePermissionSpotlight();
                hideNoLocationSpotlight();
                showItemsContainer();

            } else {

                showNoLocationSpotlight();
                hideItemsContainer();
                hideMessage();
            }
        }

        getActivity().invalidateOptionsMenu();
    }

    public void showItemsContainer() {

        mItemsContainer.setVisibility(View.VISIBLE);
    }

    public void hideItemsContainer() {

        mItemsContainer.setVisibility(View.GONE);
    }

    public void showPermissionSpotlight() {

        mPermissionSpotlight.setVisibility(View.VISIBLE);
    }

    public void showNoLocationSpotlight() {

        mSpotLight.setVisibility(View.VISIBLE);
    }

    public void hidePermissionSpotlight() {

        mPermissionSpotlight.setVisibility(View.GONE);
    }

    public void hideNoLocationSpotlight() {

        mSpotLight.setVisibility(View.GONE);
    }

    public void updateItems() {

        if (App.getInstance().getLat() != 0.000000 && App.getInstance().getLng() != 0.000000) {

            showMessage(getText(R.string.msg_loading_2).toString());

            setM(mode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == getActivity().RESULT_OK) {

            updateSpotLight();

            updateItems();

        } else if (requestCode == 10001 && resultCode == getActivity().RESULT_OK) {

            updateSpotLight();

            updateItems();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_ACCESS_LOCATION: {

                // If request is cancelled, the result arrays are empty.

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    updateSpotLight();

                    updateItems();

                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {

                    if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) || !ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                        showNoLocationPermissionSnackbar();
                    }
                }

                return;
            }
        }
    }

    public void showNoLocationPermissionSnackbar() {

        Snackbar.make(getView(), getString(R.string.label_no_location_permission) , Snackbar.LENGTH_LONG).setAction(getString(R.string.action_settings), new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                openApplicationSettings();

                Toast.makeText(getApplicationContext(), getString(R.string.label_grant_location_permission), Toast.LENGTH_SHORT).show();

            }

        }).show();
    }

    public void openApplicationSettings() {

        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getActivity().getPackageName()));
        startActivityForResult(appSettingsIntent, 10001);
    }

    @Override
    public void onStart() {

        super.onStart();

        updateSpotLight();
    }

    @Override
    public void onRefresh() {

        if (App.getInstance().isConnected()) {

            setM(mode);

        } else {

            mItemsContainer.setRefreshing(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean("viewMore", viewMore);
        outState.putBoolean("restore", true);
        outState.putInt("itemId", itemId);
        outState.putInt("mode", mode);
        outState.putInt("distance", distance);
        outState.putParcelableArrayList(STATE_LIST, itemsList);
        outState.putParcelableArrayList(STATE_LIST_2, peopleList);
    }

    public void setM(int m) {

        switch (m) {

            case 0: {

                itemId = 0;

                getPeople();

                break;
            }

            default: {

                itemId = 0;

                getItems();

                break;
            }
        }
    }

    public void onChangeDistance(int position) {

        switch (position) {

            case 0: {

                distance = NEARBY_DIALOG_OPTION_1;

                setM(mode);

                break;
            }

            case 1: {

                distance = NEARBY_DIALOG_OPTION_2;

                setM(mode);

                break;
            }

            case 2: {

                distance = NEARBY_DIALOG_OPTION_3;

                setM(mode);

                break;
            }

            case 3: {

                distance = NEARBY_DIALOG_OPTION_4;

                setM(mode);

                break;
            }

            case 4: {

                distance = NEARBY_DIALOG_OPTION_5;

                setM(mode);

                break;
            }

            default: {

                distance = NEARBY_DIALOG_OPTION_1;

                setM(mode);

                break;
            }
        }
    }

    public void getItems() {

        mItemsContainer.setRefreshing(true);

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ITEMS_NEARBY, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "NearbyFragment Not Added to Activity");

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

                                            Item c = new Item(itemObj);

                                            itemsList.add(c);
                                        }
                                    }
                                }

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loadingComplete();

                            Log.d("Items Nearby", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "NearbyFragment Not Added to Activity");

                    return;
                }

                loadingComplete();

                Log.e("Items Nearby Error", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("distance", Integer.toString(distance));
                params.put("itemId", Long.toString(itemId));
                params.put("lat", Double.toString(App.getInstance().getLat()));
                params.put("lng", Double.toString(App.getInstance().getLng()));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void getPeople() {

        mItemsContainer.setRefreshing(true);

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_PROFILE_NEARBY, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "NearbyFragment Not Added to Activity");

                            return;
                        }

                        if (!loadingMore) {

                            peopleList.clear();
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

                                            Profile c = new Profile(itemObj);

                                            peopleList.add(c);
                                        }
                                    }
                                }

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loadingComplete();

                            Log.d("People Nearby", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "NearbyFragment Not Added to Activity");

                    return;
                }

                loadingComplete();

                Log.e("People Nearby Error", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("distance", Integer.toString(distance));
                params.put("itemId", Long.toString(itemId));
                params.put("lat", Double.toString(App.getInstance().getLat()));
                params.put("lng", Double.toString(App.getInstance().getLng()));

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

        switch (mode) {

            case 0: {

                peopleAdapter.notifyDataSetChanged();

                if (peopleAdapter.getItemCount() == 0) {

                    showMessage(getText(R.string.label_empty_list).toString());

                } else {

                    hideMessage();
                }

                break;
            }

            default: {

                itemsAdapter.notifyDataSetChanged();

                if (itemsAdapter.getItemCount() == 0) {

                    showMessage(getText(R.string.label_empty_list).toString());

                } else {

                    hideMessage();
                }

                break;
            }
        }

        loadingMore = false;
        mItemsContainer.setRefreshing(false);

        getActivity().invalidateOptionsMenu();
    }

    public void showMessage(String message) {

        mMessage.setText(message);
        mMessage.setVisibility(View.VISIBLE);

        mSplash.setVisibility(View.VISIBLE);
    }

    public void hideMessage() {

        mMessage.setVisibility(View.GONE);

        mSplash.setVisibility(View.GONE);
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

    private void hideMenuItems(Menu menu, boolean visible) {

        for (int i = 0; i < menu.size(); i++){

            menu.getItem(i).setVisible(visible);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

        if (App.getInstance().getLat() != 0.000000 && App.getInstance().getLng() != 0.000000) {

            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //hide all menu items
                hideMenuItems(menu, false);

            } else {

                //show all menu items
                hideMenuItems(menu, true);

                switch (mode) {

                    case 0: {

                        menu.findItem(R.id.action_mode).setTitle(getString(R.string.action_nearby_mode_people));

                        break;
                    }

                    default: {

                        menu.findItem(R.id.action_mode).setTitle(getString(R.string.action_nearby_mode_items));

                        break;
                    }
                }
            }

        } else {

            //hide all menu items
            hideMenuItems(menu, false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();

        inflater.inflate(R.menu.menu_nearby, menu);

        MainMenu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_nearby_settings: {

                /** Getting the fragment manager */
                android.app.FragmentManager fm = getActivity().getFragmentManager();

                /** Instantiating the DialogFragment class */
                NearbySettingsDialog alert = new NearbySettingsDialog();

                /** Creating a bundle object to store the selected item's index */
                Bundle b  = new Bundle();

                /** Storing the selected item's index in the bundle object */
                b.putInt("distance", distance);

                /** Setting the bundle object to the dialog fragment object */
                alert.setArguments(b);

                /** Creating the dialog fragment object, which will in turn open the alert dialog window */

                alert.show(fm, "alert_dialog_nearby_settings");

                return true;
            }

            case R.id.action_mode: {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle(getText(R.string.title_dialog_nearby_mode));

                alertDialog.setCancelable(true);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
                arrayAdapter.add(getString(R.string.action_nearby_mode_people));
                arrayAdapter.add(getString(R.string.action_nearby_mode_items));

                alertDialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {

                            case 0: {

                                choiceMode(0);

                                break;
                            }

                            default: {

                                choiceMode(1);

                                break;
                            }
                        }

                    }
                });

                alertDialog.create();

                alertDialog.show();

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
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
}