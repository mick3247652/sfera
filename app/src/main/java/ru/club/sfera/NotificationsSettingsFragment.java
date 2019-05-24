package ru.club.sfera;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.club.sfera.app.App;
import ru.club.sfera.constants.Constants;
import ru.club.sfera.util.CustomRequest;

public class NotificationsSettingsFragment extends PreferenceFragment implements Constants {

    private CheckBoxPreference mAllowFriendsRequestsGCM, mAllowCommentsGCM, mAllowMessagesGCM, mAllowCommentReplyGCM, mAllowLikesGCM, mAllowGiftsGCM, mAllowItemsGCM, mAllowRepostsGCM;

    private ProgressDialog pDialog;

    int mAllowFriendsRequests, mAllowComments, mAllowMessages, mAllowLikes, mAllowCommentReply, mAllowGifts, mAllowItems, mAllowReposts;

    private Boolean loading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        initpDialog();

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.notifications_settings);

        mAllowFriendsRequestsGCM = (CheckBoxPreference) getPreferenceManager().findPreference("allowFriendsRequestsGCM");

        mAllowFriendsRequestsGCM.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        mAllowFriendsRequests = 1;

                    } else {

                        mAllowFriendsRequests = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        saveGCM_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        mAllowCommentsGCM = (CheckBoxPreference) getPreferenceManager().findPreference("allowCommentsGCM");

        mAllowCommentsGCM.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        mAllowComments = 1;

                    } else {

                        mAllowComments = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        saveGCM_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        mAllowMessagesGCM = (CheckBoxPreference) getPreferenceManager().findPreference("allowMessagesGCM");

        mAllowMessagesGCM.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        mAllowMessages = 1;

                    } else {

                        mAllowMessages = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        saveGCM_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        mAllowCommentReplyGCM = (CheckBoxPreference) getPreferenceManager().findPreference("allowCommentReplyGCM");

        mAllowCommentReplyGCM.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        mAllowCommentReply = 1;

                    } else {

                        mAllowCommentReply = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        saveGCM_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        mAllowLikesGCM = (CheckBoxPreference) getPreferenceManager().findPreference("allowLikesGCM");

        mAllowLikesGCM.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        mAllowLikes = 1;

                    } else {

                        mAllowLikes = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        saveGCM_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        mAllowGiftsGCM = (CheckBoxPreference) getPreferenceManager().findPreference("allowGiftsGCM");

        mAllowGiftsGCM.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        mAllowGifts = 1;

                    } else {

                        mAllowGifts = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        saveGCM_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        mAllowItemsGCM = (CheckBoxPreference) getPreferenceManager().findPreference("allowItemsGCM");

        mAllowItemsGCM.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        mAllowItems = 1;

                    } else {

                        mAllowItems = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        saveGCM_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        mAllowRepostsGCM = (CheckBoxPreference) getPreferenceManager().findPreference("allowRepostsGCM");

        mAllowRepostsGCM.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        mAllowReposts = 1;

                    } else {

                        mAllowReposts = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        saveGCM_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        checkAllowFriendsRequestsGCM(App.getInstance().getAllowFriendsRequestsGCM());
        checkAllowComments(App.getInstance().getAllowCommentsGCM());
        checkAllowMessages(App.getInstance().getAllowMessagesGCM());
        checkAllowLikes(App.getInstance().getAllowLikesGCM());
        checkAllowGifts(App.getInstance().getAllowGiftsGCM());
        checkAllowCommentReply(App.getInstance().getAllowCommentReplyGCM());
        checkAllowItems(App.getInstance().getAllowItemsGCM());
        checkAllowReposts(App.getInstance().getAllowRepostsGCM());
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            loading = savedInstanceState.getBoolean("loading");

        } else {

            loading = false;
        }

        if (loading) {

            showpDialog();
        }
    }

    public void onDestroyView() {

        super.onDestroyView();

        hidepDialog();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean("loading", loading);
    }

    public void checkAllowMessages(int value) {

        if (value == 1) {

            mAllowMessagesGCM.setChecked(true);
            mAllowMessages = 1;

        } else {

            mAllowMessagesGCM.setChecked(false);
            mAllowMessages = 0;
        }
    }

    public void checkAllowLikes(int value) {

        if (value == 1) {

            mAllowLikesGCM.setChecked(true);
            mAllowLikes = 1;

        } else {

            mAllowLikesGCM.setChecked(false);
            mAllowLikes = 0;
        }
    }

    public void checkAllowGifts(int value) {

        if (value == 1) {

            mAllowGiftsGCM.setChecked(true);
            mAllowGifts = 1;

        } else {

            mAllowGiftsGCM.setChecked(false);
            mAllowGifts = 0;
        }
    }

    public void checkAllowComments(int value) {

        if (value == 1) {

            mAllowCommentsGCM.setChecked(true);
            mAllowComments = 1;

        } else {

            mAllowCommentsGCM.setChecked(false);
            mAllowComments = 0;
        }
    }

    public void checkAllowCommentReply(int value) {

        if (value == 1) {

            mAllowCommentReplyGCM.setChecked(true);
            mAllowCommentReply = 1;

        } else {

            mAllowCommentReplyGCM.setChecked(false);
            mAllowCommentReply = 0;
        }
    }

    public void checkAllowFriendsRequestsGCM(int value) {

        if (value == 1) {

            mAllowFriendsRequestsGCM.setChecked(true);
            mAllowFriendsRequests = 1;

        } else {

            mAllowFriendsRequestsGCM.setChecked(false);
            mAllowFriendsRequests = 0;
        }
    }

    public void checkAllowItems(int value) {

        if (value == 1) {

            mAllowItemsGCM.setChecked(true);
            mAllowItems = 1;

        } else {

            mAllowItemsGCM.setChecked(false);
            mAllowItems = 0;
        }
    }

    public void checkAllowReposts(int value) {

        if (value == 1) {

            mAllowRepostsGCM.setChecked(true);
            mAllowReposts = 1;

        } else {

            mAllowRepostsGCM.setChecked(false);
            mAllowReposts = 0;
        }
    }


    public void saveGCM_settings() {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ACCOUNT_SET_GCM_SETTINGS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (!response.getBoolean("error")) {

                                App.getInstance().setAllowMessagesGCM(response.getInt("allowMessagesGCM"));
                                App.getInstance().setAllowLikesGCM(response.getInt("allowLikesGCM"));
                                App.getInstance().setAllowGiftsGCM(response.getInt("allowGiftsGCM"));
                                App.getInstance().setAllowCommentsGCM(response.getInt("allowCommentsGCM"));
                                App.getInstance().setAllowCommentReplyGCM(response.getInt("allowCommentReplyGCM"));
                                App.getInstance().setAllowFriendsRequestsGCM(response.getInt("allowFriendsRequestsGCM"));
                                App.getInstance().setAllowItemsGCM(response.getInt("allowItemsGCM"));
                                App.getInstance().setAllowRepostsGCM(response.getInt("allowRepostsGCM"));

                                checkAllowMessages(App.getInstance().getAllowMessagesGCM());
                                checkAllowLikes(App.getInstance().getAllowLikesGCM());
                                checkAllowGifts(App.getInstance().getAllowGiftsGCM());
                                checkAllowComments(App.getInstance().getAllowCommentsGCM());
                                checkAllowCommentReply(App.getInstance().getAllowCommentReplyGCM());
                                checkAllowFriendsRequestsGCM(App.getInstance().getAllowFriendsRequestsGCM());
                                checkAllowItems(App.getInstance().getAllowItemsGCM());
                                checkAllowReposts(App.getInstance().getAllowRepostsGCM());
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

                loading = false;

                hidepDialog();

                Log.e("GCM Settings", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("clientId", CLIENT_ID);
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("allowMessagesGCM", Integer.toString(mAllowMessages));
                params.put("allowLikesGCM", Integer.toString(mAllowLikes));
                params.put("allowGiftsGCM", Integer.toString(mAllowGifts));
                params.put("allowItemsGCM", Integer.toString(mAllowItems));
                params.put("allowRepostsGCM", Integer.toString(mAllowReposts));
                params.put("allowCommentsGCM", Integer.toString(mAllowComments));
                params.put("allowCommentReplyGCM", Integer.toString(mAllowCommentReply));
                params.put("allowFriendsRequestsGCM", Integer.toString(mAllowFriendsRequests));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    protected void initpDialog() {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.msg_loading));
        pDialog.setCancelable(false);
    }

    protected void showpDialog() {

        if (!pDialog.isShowing())
            pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}