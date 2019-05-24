package ru.club.sfera;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
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

public class PrivacySettingsFragment extends PreferenceFragment implements Constants {

    private CheckBoxPreference mAllowItemsFromFriends, mMessagesFromAnyone, mAllowItemsComments, mAllowGalleryComments, mAllowShowProfileOnlyToFriends, mAllowShowOnline, mAllowShowPhoneNumber, mAllowShowMyGallery, mAllowShowMyFriends;

    private ProgressDialog pDialog;

    int allowItemsFromFriends, allowMessagesFromAnyone, allowItemsComments, allowGalleryComments, allowShowProfileOnlyToFriends, allowShowOnline, allowShowPhoneNumber, allowShowMyGallery, allowShowMyFriends;

    private Boolean loading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        initpDialog();

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.privacy_settings);

        mAllowItemsFromFriends = (CheckBoxPreference) getPreferenceManager().findPreference("allowItemsFromFriends");

        mAllowItemsFromFriends.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        allowItemsFromFriends = 1;

                    } else {

                        allowItemsFromFriends = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        savePrivacy_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        mAllowShowMyGallery = (CheckBoxPreference) getPreferenceManager().findPreference("allowShowMyGallery");

        mAllowShowMyGallery.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        allowShowMyGallery = 1;

                    } else {

                        allowShowMyGallery = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        savePrivacy_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        if (App.getInstance().getSettings().getAllowGallery() == DISABLED) {

            PreferenceCategory headerGeneral = (PreferenceCategory) findPreference("header_general");

            headerGeneral.removePreference(mAllowShowMyGallery);
        }

        mAllowShowMyFriends = (CheckBoxPreference) getPreferenceManager().findPreference("allowShowMyFriends");

        mAllowShowMyFriends.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        allowShowMyFriends = 1;

                    } else {

                        allowShowMyFriends = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        savePrivacy_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        mMessagesFromAnyone = (CheckBoxPreference) getPreferenceManager().findPreference("allowMessagesFromAnyone");

        mMessagesFromAnyone.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        allowMessagesFromAnyone = 1;

                    } else {

                        allowMessagesFromAnyone = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        savePrivacy_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        mAllowItemsComments = (CheckBoxPreference) getPreferenceManager().findPreference("allowItemsComments");

        mAllowItemsComments.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        allowItemsComments = 1;

                    } else {

                        allowItemsComments = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        savePrivacy_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        mAllowGalleryComments = (CheckBoxPreference) getPreferenceManager().findPreference("allowGalleryComments");

        mAllowGalleryComments.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        allowGalleryComments = 1;

                    } else {

                        allowGalleryComments = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        savePrivacy_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        mAllowShowProfileOnlyToFriends = (CheckBoxPreference) getPreferenceManager().findPreference("allowShowProfileOnlyToFriends");

        mAllowShowProfileOnlyToFriends.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        allowShowProfileOnlyToFriends = 1;

                    } else {

                        allowShowProfileOnlyToFriends = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        savePrivacy_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        mAllowShowOnline = (CheckBoxPreference) getPreferenceManager().findPreference("allowShowOnline");

        mAllowShowOnline.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        allowShowOnline = 1;

                    } else {

                        allowShowOnline = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        savePrivacy_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        mAllowShowPhoneNumber = (CheckBoxPreference) getPreferenceManager().findPreference("allowShowPhoneNumber");

        mAllowShowPhoneNumber.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue instanceof Boolean) {

                    Boolean value = (Boolean) newValue;

                    if (value) {

                        allowShowPhoneNumber = 1;

                    } else {

                        allowShowPhoneNumber = 0;
                    }

                    if (App.getInstance().isConnected()) {

                        savePrivacy_settings();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        checkAllowItemsFromFriends(App.getInstance().getAllowItemsFromFriends());
        checkAllowMessagesFromAnyone(App.getInstance().getAllowMessagesFromAnyone());
        checkAllowItemsComments(App.getInstance().getAllowItemsComments());
        checkAllowGalleryComments(App.getInstance().getAllowGalleryComments());
        checkAllowShowProfileOnlyToFriends(App.getInstance().getAllowShowProfileOnlyToFriends());
        checkAllowShowOnline(App.getInstance().getAllowShowOnline());
        checkAllowShowPhoneNumber(App.getInstance().getAllowShowPhoneNumber());
        checkAllowShowMyGallery(App.getInstance().getAllowShowMyGallery());
        checkAllowShowMyFriends(App.getInstance().getAllowShowMyFriends());
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

    public void checkAllowItemsFromFriends(int value) {

        if (value == 1) {

            mAllowItemsFromFriends.setChecked(true);
            allowItemsFromFriends = 1;

        } else {

            mAllowItemsFromFriends.setChecked(false);
            allowItemsFromFriends = 0;
        }
    }

    public void checkAllowMessagesFromAnyone(int value) {

        if (value == 1) {

            mMessagesFromAnyone.setChecked(true);
            allowMessagesFromAnyone = 1;

        } else {

            mMessagesFromAnyone.setChecked(false);
            allowMessagesFromAnyone = 0;
        }
    }

    public void checkAllowItemsComments(int value) {

        if (value == 1) {

            mAllowItemsComments.setChecked(true);
            allowItemsComments = 1;

        } else {

            mAllowItemsComments.setChecked(false);
            allowItemsComments = 0;
        }
    }

    public void checkAllowGalleryComments(int value) {

        if (value == 1) {

            mAllowGalleryComments.setChecked(true);
            allowGalleryComments = 1;

        } else {

            mAllowGalleryComments.setChecked(false);
            allowGalleryComments = 0;
        }
    }

    public void checkAllowShowProfileOnlyToFriends(int value) {

        if (value == 1) {

            mAllowShowProfileOnlyToFriends.setChecked(true);
            allowShowProfileOnlyToFriends = 1;

        } else {

            mAllowShowProfileOnlyToFriends.setChecked(false);
            allowShowProfileOnlyToFriends = 0;
        }
    }

    public void checkAllowShowOnline(int value) {

        if (value == 1) {

            mAllowShowOnline.setChecked(true);
            allowShowOnline = 1;

        } else {

            mAllowShowOnline.setChecked(false);
            allowShowOnline = 0;
        }
    }

    public void checkAllowShowPhoneNumber(int value) {

        if (value == 1) {

            mAllowShowPhoneNumber.setChecked(true);
            allowShowPhoneNumber = 1;

        } else {

            mAllowShowPhoneNumber.setChecked(false);
            allowShowPhoneNumber = 0;
        }
    }

    public void checkAllowShowMyGallery(int value) {

        if (value == 1) {

            mAllowShowMyGallery.setChecked(true);
            allowShowMyGallery = 1;

        } else {

            mAllowShowMyGallery.setChecked(false);
            allowShowMyGallery = 0;
        }
    }

    public void checkAllowShowMyFriends(int value) {

        if (value == 1) {

            mAllowShowMyFriends.setChecked(true);
            allowShowMyFriends = 1;

        } else {

            mAllowShowMyFriends.setChecked(false);
            allowShowMyFriends = 0;
        }
    }

    public void savePrivacy_settings() {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ACCOUNT_SET_PRIVACY_SETTINGS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (!response.getBoolean("error")) {

                                App.getInstance().setAllowItemsFromFriends(response.getInt("allowItemsFromFriends"));
                                App.getInstance().setAllowMessagesFromAnyone(response.getInt("allowMessagesFromAnyone"));
                                App.getInstance().setAllowItemsComments(response.getInt("allowItemsComments"));
                                App.getInstance().setAllowGalleryComments(response.getInt("allowGalleryComments"));
                                App.getInstance().setAllowShowProfileOnlyToFriends(response.getInt("allowShowProfileOnlyToFriends"));
                                App.getInstance().setAllowShowOnline(response.getInt("allowShowOnline"));
                                App.getInstance().setAllowShowPhoneNumber(response.getInt("allowShowPhoneNumber"));
                                App.getInstance().setAllowShowMyGallery(response.getInt("allowShowMyGallery"));
                                App.getInstance().setAllowShowMyFriends(response.getInt("allowShowMyFriends"));

                                checkAllowItemsFromFriends(App.getInstance().getAllowItemsFromFriends());
                                checkAllowMessagesFromAnyone(App.getInstance().getAllowMessagesFromAnyone());
                                checkAllowItemsComments(App.getInstance().getAllowItemsComments());
                                checkAllowGalleryComments(App.getInstance().getAllowGalleryComments());
                                checkAllowShowProfileOnlyToFriends(App.getInstance().getAllowShowProfileOnlyToFriends());
                                checkAllowShowOnline(App.getInstance().getAllowShowOnline());
                                checkAllowShowPhoneNumber(App.getInstance().getAllowShowPhoneNumber());
                                checkAllowShowMyGallery(App.getInstance().getAllowShowMyGallery());
                                checkAllowShowMyFriends(App.getInstance().getAllowShowMyFriends());
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                            hidepDialog();

                            Log.d("Privacy Success", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loading = false;

                hidepDialog();

                Log.e("Privacy Error", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("clientId", CLIENT_ID);
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("allowItemsFromFriends", Integer.toString(allowItemsFromFriends));
                params.put("allowMessagesFromAnyone", Integer.toString(allowMessagesFromAnyone));
                params.put("allowItemsComments", Integer.toString(allowItemsComments));
                params.put("allowGalleryComments", Integer.toString(allowGalleryComments));
                params.put("allowShowProfileOnlyToFriends", Integer.toString(allowShowProfileOnlyToFriends));
                params.put("allowShowOnline", Integer.toString(allowShowOnline));
                params.put("allowShowPhoneNumber", Integer.toString(allowShowPhoneNumber));
                params.put("allowShowMyGallery", Integer.toString(allowShowMyGallery));
                params.put("allowShowMyFriends", Integer.toString(allowShowMyFriends));

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