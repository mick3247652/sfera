package ru.club.sfera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.club.sfera.app.App;
import ru.club.sfera.common.FragmentBase;
import ru.club.sfera.constants.Constants;
import ru.club.sfera.util.CustomRequest;

public class UpgradesFragment extends FragmentBase implements Constants {

    Button mGetCreditsButton, mGhostModeButton, mVerifiedBadgeButton, mDisableAdsButton, mEnableGuestsButton;
    TextView mLabelCredits, mLabelGhostModeStatus, mLabelVerifiedBadgeStatus, mLabelDisableAdsStatus, mLabelEnableGuestsStatus;

    LinearLayout mAdContainer, mAdSeparaor;

    private Boolean loading = false;

    public UpgradesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        initpDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_upgrades, container, false);

        if (loading) {

            showpDialog();
        }

        mLabelCredits = (TextView) rootView.findViewById(R.id.labelCredits);

        mAdContainer = (LinearLayout) rootView.findViewById(R.id.adContainer);
        mAdSeparaor = (LinearLayout) rootView.findViewById(R.id.adSeparator);

        mLabelGhostModeStatus = (TextView) rootView.findViewById(R.id.labelGhostModeStatus);
        mLabelVerifiedBadgeStatus = (TextView) rootView.findViewById(R.id.labelVerifiedBadgeStatus);
        mLabelDisableAdsStatus = (TextView) rootView.findViewById(R.id.labelDisableAdsStatus);
        mLabelEnableGuestsStatus = (TextView) rootView.findViewById(R.id.labelEnableGuestsStatus);

        mGhostModeButton = (Button) rootView.findViewById(R.id.ghostModeBtn);
        mVerifiedBadgeButton = (Button) rootView.findViewById(R.id.verifiedBadgeBtn);
        mDisableAdsButton = (Button) rootView.findViewById(R.id.disableAdsBtn);
        mEnableGuestsButton = (Button) rootView.findViewById(R.id.enableGuestsBtn);
        mGetCreditsButton = (Button) rootView.findViewById(R.id.getCreditsBtn);

        mGetCreditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), BalanceActivity.class);
                startActivityForResult(i, 1945);
            }
        });

        mGhostModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App.getInstance().getBalance() >= GHOST_MODE_COST) {

                    setGhostMode();

                } else {

                    Toast.makeText(getActivity(), getString(R.string.error_credits), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mVerifiedBadgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App.getInstance().getBalance() >= VERIFIED_BADGE_COST) {

                    setVerifiedBadge();

                } else {

                    Toast.makeText(getActivity(), getString(R.string.error_credits), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDisableAdsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App.getInstance().getBalance() >= DISABLE_ADS_COST) {

                    setDisableAds();

                } else {

                    Toast.makeText(getActivity(), getString(R.string.error_credits), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mEnableGuestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (App.getInstance().getBalance() >= ENABLE_GUESTS_COST) {

                    setEnableGuests();

                } else {

                    Toast.makeText(getActivity(), getString(R.string.error_credits), Toast.LENGTH_SHORT).show();
                }
            }
        });

        update();

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1945 && resultCode == getActivity().RESULT_OK && null != data) {

            update();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onDestroyView() {

        super.onDestroyView();

        hidepDialog();
    }

    @Override
    public void onStart() {

        super.onStart();

        update();
    }

    public void update() {

        if (App.getInstance().getSettings().getAllowAdmobBanner() == DISABLED) {

            mAdContainer.setVisibility(View.GONE);
            mAdSeparaor.setVisibility(View.GONE);

        } else {

            mAdContainer.setVisibility(View.VISIBLE);
            mAdSeparaor.setVisibility(View.VISIBLE);
        }

        mLabelCredits.setText(getString(R.string.label_credits) + " (" + Integer.toString(App.getInstance().getBalance()) + ")");

        mGhostModeButton.setText(getString(R.string.action_enable) + " (" + Integer.toString(GHOST_MODE_COST) + ")");
        mVerifiedBadgeButton.setText(getString(R.string.action_enable) + " (" + Integer.toString(VERIFIED_BADGE_COST) + ")");
        mDisableAdsButton.setText(getString(R.string.action_enable) + " (" + Integer.toString(DISABLE_ADS_COST) + ")");
        mEnableGuestsButton.setText(getString(R.string.action_enable) + " (" + Integer.toString(ENABLE_GUESTS_COST) + ")");

        if (App.getInstance().getGhostUpgrade() == 0) {

            mLabelGhostModeStatus.setVisibility(View.GONE);
            mGhostModeButton.setEnabled(true);
            mGhostModeButton.setVisibility(View.VISIBLE);

        } else {

            mLabelGhostModeStatus.setVisibility(View.VISIBLE);
            mGhostModeButton.setEnabled(false);
            mGhostModeButton.setVisibility(View.GONE);
        }

        if (App.getInstance().getVerified() == 0) {

            mLabelVerifiedBadgeStatus.setVisibility(View.GONE);
            mVerifiedBadgeButton.setEnabled(true);
            mVerifiedBadgeButton.setVisibility(View.VISIBLE);

        } else {

            mLabelVerifiedBadgeStatus.setVisibility(View.VISIBLE);
            mVerifiedBadgeButton.setEnabled(false);
            mVerifiedBadgeButton.setVisibility(View.GONE);
        }

        if (App.getInstance().getAdmobUpgrade() == 0) {

            mLabelDisableAdsStatus.setVisibility(View.GONE);
            mDisableAdsButton.setEnabled(true);
            mDisableAdsButton.setVisibility(View.VISIBLE);

        } else {

            mLabelDisableAdsStatus.setVisibility(View.VISIBLE);
            mDisableAdsButton.setEnabled(false);
            mDisableAdsButton.setVisibility(View.GONE);
        }

        if (App.getInstance().getGuestsUpgrade() == 0) {

            mLabelEnableGuestsStatus.setVisibility(View.GONE);
            mEnableGuestsButton.setEnabled(true);
            mEnableGuestsButton.setVisibility(View.VISIBLE);

        } else {

            mLabelEnableGuestsStatus.setVisibility(View.VISIBLE);
            mEnableGuestsButton.setEnabled(false);
            mEnableGuestsButton.setVisibility(View.GONE);
        }
    }

    public void setGhostMode() {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_UPGRADES_GHOST, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (!response.getBoolean("error")) {

                                App.getInstance().setBalance(App.getInstance().getBalance() - GHOST_MODE_COST);
                                App.getInstance().setGhostUpgrade(1);

                                Toast.makeText(getActivity(), getString(R.string.msg_success_ghost_mode), Toast.LENGTH_SHORT).show();

                                update();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                            hidepDialog();

                            update();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loading = false;

                update();

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("cost", Integer.toString(GHOST_MODE_COST));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void setVerifiedBadge() {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_UPGRADES_VERIFIED_BADGE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (!response.getBoolean("error")) {

                                App.getInstance().setBalance(App.getInstance().getBalance() - VERIFIED_BADGE_COST);
                                App.getInstance().setVerified(1);

                                Toast.makeText(getActivity(), getString(R.string.msg_success_verified_badge), Toast.LENGTH_SHORT).show();

                                update();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                            hidepDialog();

                            update();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loading = false;

                update();

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("cost", Integer.toString(VERIFIED_BADGE_COST));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void setDisableAds() {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_UPGRADES_ADMOB, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (!response.getBoolean("error")) {

                                App.getInstance().setBalance(App.getInstance().getBalance() - DISABLE_ADS_COST);
                                App.getInstance().setAdmobUpgrade(1);

                                Toast.makeText(getActivity(), getString(R.string.msg_success_disable_ads), Toast.LENGTH_SHORT).show();

                                update();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                            hidepDialog();

                            update();

                            Log.d("Upgrade Success", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loading = false;

                update();

                hidepDialog();

                Log.e("Upgrade Error", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("cost", Integer.toString(DISABLE_ADS_COST));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void setEnableGuests() {

        loading = true;

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_UPGRADES_GUESTS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (!response.getBoolean("error")) {

                                App.getInstance().setBalance(App.getInstance().getBalance() - ENABLE_GUESTS_COST);
                                App.getInstance().setGuestsUpgrade(1);

                                Toast.makeText(getActivity(), getString(R.string.msg_success_enable_guests), Toast.LENGTH_SHORT).show();

                                update();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                            hidepDialog();

                            update();

                            Log.d("Upgrade Success", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loading = false;

                update();

                hidepDialog();

                Log.e("Upgrade Error", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("cost", Integer.toString(ENABLE_GUESTS_COST));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
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