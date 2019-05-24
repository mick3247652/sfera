package ru.club.sfera;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.club.sfera.app.App;
import ru.club.sfera.common.ActivityBase;
import ru.club.sfera.util.CustomRequest;

public class AppActivity extends ActivityBase {

    CallbackManager callbackManager;

    LoginButton fbLoginButton;

    Button loginBtn, signupBtn, facebookBtn;
    TextView label_or;

    String facebookId = "", facebookName = "", facebookEmail = "";

    RelativeLayout loadingScreen, connectionScreen;
    RelativeLayout contentScreen;

    Boolean restore = false;
    Boolean loading = false;
    Boolean auth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_app);

        if (AccessToken.getCurrentAccessToken()!= null) LoginManager.getInstance().logOut();

        callbackManager = CallbackManager.Factory.create();

//        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        App.getInstance().setGcmToken(refreshedToken);

        if (savedInstanceState != null) {

            restore = savedInstanceState.getBoolean("restore");
            loading = savedInstanceState.getBoolean("loading");

        } else {

            restore = false;
            loading = false;
        }

        contentScreen = (RelativeLayout) findViewById(R.id.contentScreen);
        loadingScreen = (RelativeLayout) findViewById(R.id.loadingScreen);
        connectionScreen = (RelativeLayout) findViewById(R.id.connectionScreen);

        label_or = (TextView) findViewById(R.id.label_or);

        facebookBtn = (Button) findViewById(R.id.facebookBtn);

        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);

        if (App.getInstance().getSettings().getAllowFacebookAuthorization() == DISABLED) {

            fbLoginButton.setVisibility(View.GONE);
            facebookBtn.setVisibility(View.GONE);
            label_or.setVisibility(View.GONE);
        }

        loginBtn = (Button) findViewById(R.id.loginBtn);
        signupBtn = (Button) findViewById(R.id.signupBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(AppActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(AppActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });

        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fbLoginButton.performClick();
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code

                        if (App.getInstance().isConnected()) {

                            loading = true;

                            showpDialog();

                            GraphRequest request = GraphRequest.newMeRequest(
                                    AccessToken.getCurrentAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {

                                            // Application code

                                            try {

                                                facebookId = object.getString("id");
                                                facebookName = object.getString("name");

                                                if (object.has("email")) {

                                                    facebookEmail = object.getString("email");
                                                }

                                            } catch (Throwable t) {

                                                Log.e("Profile", "Could not parse malformed JSON: \"" + object.toString() + "\"");

                                            } finally {

                                                if (AccessToken.getCurrentAccessToken() != null)
                                                    LoginManager.getInstance().logOut();

                                                Log.d("Profile", object.toString());

                                                if (App.getInstance().isConnected()) {

                                                    if (!facebookId.equals("")) {

                                                        signinByFacebookId();

                                                    } else {

                                                        loading = false;

                                                        hidepDialog();
                                                    }

                                                } else {

                                                    loading = false;

                                                    hidepDialog();
                                                }
                                            }
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,link,email");
                            request.setParameters(parameters);
                            request.executeAsync();
                        }
                    }

                    @Override
                    public void onCancel() {

                        // App code
                        // Cancel
                    }

                    @Override
                    public void onError(FacebookException exception) {

                        // App code
                        // error
                    }
                });

        if (loading) showLoadingScreen();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {

        super.onActivityResult(requestCode, responseCode, data);

        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean("restore", true);
        outState.putBoolean("loading", loading);
    }

    @Override
    protected void onStart() {

        super.onStart();

        if (!loading && !auth && App.getInstance().getId() != 0) {

            if (App.getInstance().isConnected()) {

                showLoadingScreen();

                loading = true;

                CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ACCOUNT_AUTHORIZE, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                if (App.getInstance().authorize(response)) {

                                    if (App.getInstance().getState() == ACCOUNT_STATE_ENABLED) {

                                        App.getInstance().updateGeoLocation();

                                        Intent intent = new Intent(AppActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    } else {

                                        loading = false;

                                        showContentScreen();

                                        App.getInstance().logout();
                                    }

                                } else {

                                    loading = false;

                                    showContentScreen();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showContentScreen();

                        loading = false;
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("clientId", CLIENT_ID);
                        params.put("accountId", Long.toString(App.getInstance().getId()));
                        params.put("accessToken", App.getInstance().getAccessToken());
                        params.put("fcm_regId", App.getInstance().getGcmToken());

                        return params;
                    }
                };

                App.getInstance().addToRequestQueue(jsonReq);

            } else {

                showContentScreen();
            }

        } else {

            showContentScreen();
        }
    }

    public void signinByFacebookId() {

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ACCOUNT_LOGINBYFACEBOOK, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (App.getInstance().authorize(response)) {

                            if (App.getInstance().getState() == ACCOUNT_STATE_ENABLED) {

                                App.getInstance().updateGeoLocation();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {

                                if (App.getInstance().getState() == ACCOUNT_STATE_BLOCKED) {

                                    App.getInstance().logout();
                                    Toast.makeText(getApplicationContext(), getText(R.string.msg_account_blocked), Toast.LENGTH_SHORT).show();

                                } else {

                                    App.getInstance().updateGeoLocation();

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }

                        } else {

                            if (facebookId != "") {

                                Intent i = new Intent(getApplicationContext(), SignupActivity.class);
                                i.putExtra("facebookId", facebookId);
                                i.putExtra("facebookName", facebookName);
                                i.putExtra("facebookEmail", facebookEmail);
                                startActivity(i);

                            } else {

                                Toast.makeText(getApplicationContext(), getString(R.string.error_signin), Toast.LENGTH_SHORT).show();
                            }
                        }

                        loading = false;

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("LOGINBYFACEBOOK | Error", error.toString());

                loading = false;

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("facebookId", facebookId);
                params.put("clientId", CLIENT_ID);
                params.put("fcm_regId", App.getInstance().getGcmToken());

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void showContentScreen() {

        auth = true;

        contentScreen.setVisibility(View.VISIBLE);

        loadingScreen.setVisibility(View.GONE);
        connectionScreen.setVisibility(View.GONE);
    }

    public void showLoadingScreen() {

        loadingScreen.setVisibility(View.VISIBLE);

        contentScreen.setVisibility(View.GONE);
        connectionScreen.setVisibility(View.GONE);
    }

    public void showConnectionScreen() {

        connectionScreen.setVisibility(View.VISIBLE);

        loadingScreen.setVisibility(View.GONE);
        contentScreen.setVisibility(View.GONE);
    }
}
