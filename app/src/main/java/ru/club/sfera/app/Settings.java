package ru.club.sfera.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import ru.club.sfera.R;
import ru.club.sfera.constants.Constants;

public class Settings extends Application implements Constants {

	public static final String TAG = Settings.class.getSimpleName();

    private SharedPreferences sharedPref;

    private int allowUpgradesSection = 1, allowGifts = 1, allowSpotlight = 1, allowAddImageToComment = 1, allowGallery = 1, allowAddVideoToItems = 1, allowTypingFunction = 1, allowSeenFunction = 1, allowAddImageToMessage = 1, allowEmoji = 1, allowAdmobBanner = 1, allowAddVideoToGallery = 1, navMessagesMenuItem = 1, navNotificationsMenuItem = 1, allowFacebookAuthorization = 1, allowLogIn = 1, allowSignUp = 1, allowPasswordRecovery = 1;

	@Override
	public void onCreate() {

		super.onCreate();

        sharedPref = App.getInstance().getSharedPreferences(getString(R.string.settings_file), Context.MODE_PRIVATE);
	}

    public void read_from_json(JSONObject jsonData) {

        try {

            if (jsonData.has("navMessagesMenuItem")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("navMessagesMenuItem");

                this.setNavMessagesMenuItem(obj.getInt("intValue"));
            }

            if (jsonData.has("navNotificationsMenuItem")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("navNotificationsMenuItem");

                this.setNavNotificationsMenuItem(obj.getInt("intValue"));
            }

            if (jsonData.has("allowFacebookAuthorization")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("allowFacebookAuthorization");

                this.setAllowFacebookAuthorization(obj.getInt("intValue"));
            }

            if (jsonData.has("allowLogIn")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("allowLogIn");

                this.setAllowLogin(obj.getInt("intValue"));
            }

            if (jsonData.has("allowSignUp")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("allowSignUp");

                this.setAllowSignUp(obj.getInt("intValue"));
            }

            if (jsonData.has("allowAdmobBanner")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("allowAdmobBanner");

                this.setAllowAdmobBanner(obj.getInt("intValue"));
            }

            if (jsonData.has("allowAddVideoToGallery")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("allowAddVideoToGallery");

                this.setAllowAddVideoToGallery(obj.getInt("intValue"));
            }

            if (jsonData.has("allowEmoji")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("allowEmoji");

                this.setAllowEmoji(obj.getInt("intValue"));
            }

            if (jsonData.has("allowPasswordRecovery")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("allowPasswordRecovery");

                this.setAllowPasswordRecovery(obj.getInt("intValue"));
            }

            if (jsonData.has("allowAddImageToMessage")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("allowAddImageToMessage");

                this.setAllowAddImageToMessage(obj.getInt("intValue"));
            }

            if (jsonData.has("allowSeenFunction")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("allowSeenFunction");

                this.setAllowSeenFunction(obj.getInt("intValue"));
            }

            if (jsonData.has("allowTypingFunction")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("allowTypingFunction");

                this.setAllowTypingFunction(obj.getInt("intValue"));
            }

            if (jsonData.has("allowAddVideoToItems")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("allowAddVideoToItems");

                this.setAllowAddVideoToItems(obj.getInt("intValue"));
            }

            if (jsonData.has("allowGallery")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("allowGallery");

                this.setAllowGallery(obj.getInt("intValue"));
            }

            if (jsonData.has("allowAddImageToComment")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("allowAddImageToComment");

                this.setAllowAddImageToComment(obj.getInt("intValue"));
            }

            if (jsonData.has("allowUpgradesSection")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("allowUpgradesSection");

                this.setAllowUpgradesSection(obj.getInt("intValue"));
            }

            if (jsonData.has("allowSpotlight")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("allowSpotlight");

                this.setAllowSpotlight(obj.getInt("intValue"));
            }

            if (jsonData.has("allowGifts")) {

                JSONObject obj = (JSONObject) jsonData.getJSONObject("allowGifts");

                this.setAllowGifts(obj.getInt("intValue"));
            }

        } catch (Throwable t) {

            Log.e("Settings", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

//            this.save_settings();
        }
    }

    public void read_settings() {

        this.setNavMessagesMenuItem(sharedPref.getInt(getString(R.string.settings_nav_messages_menu_item), 1));
        this.setNavNotificationsMenuItem(sharedPref.getInt(getString(R.string.settings_nav_notifications_menu_item), 1));

        this.setAllowFacebookAuthorization(sharedPref.getInt(getString(R.string.settings_allow_facebook_authorization), 1));
        this.setAllowLogin(sharedPref.getInt(getString(R.string.settings_allow_login), 1));
        this.setAllowSignUp(sharedPref.getInt(getString(R.string.settings_allow_signup), 1));
        this.setAllowAdmobBanner(sharedPref.getInt(getString(R.string.settings_allow_admob), 1));
        this.setAllowPasswordRecovery(sharedPref.getInt(getString(R.string.settings_allow_password_recovery), 1));
        this.setAllowAddVideoToGallery(sharedPref.getInt(getString(R.string.settings_allow_add_video_to_gallery), 1));
        this.setAllowEmoji(sharedPref.getInt(getString(R.string.settings_allow_emoji), 1));
        this.setAllowAddImageToMessage(sharedPref.getInt(getString(R.string.settings_allow_add_image_to_message), 1));

        this.setAllowSeenFunction(sharedPref.getInt(getString(R.string.settings_allow_seen_function), 1));
        this.setAllowTypingFunction(sharedPref.getInt(getString(R.string.settings_allow_typing_function), 1));

        this.setAllowAddVideoToItems(sharedPref.getInt(getString(R.string.settings_allow_add_video_to_items), 1));
        this.setAllowGallery(sharedPref.getInt(getString(R.string.settings_allow_gallery), 1));
        this.setAllowAddImageToComment(sharedPref.getInt(getString(R.string.settings_allow_add_image_to_comment), 1));
        this.setAllowUpgradesSection(sharedPref.getInt(getString(R.string.settings_allow_upgrades_section), 1));
        this.setAllowSpotlight(sharedPref.getInt(getString(R.string.settings_allow_spotlight), 1));
        this.setAllowGifts(sharedPref.getInt(getString(R.string.settings_allow_gifts), 1));

        Log.e("SETTINGS READ", "qq");
    }

    public void save_settings() {

        sharedPref = getSharedPreferences(getString(R.string.settings_file), Context.MODE_PRIVATE);

        sharedPref.edit().putInt(getString(R.string.settings_nav_messages_menu_item), this.getNavMessagesMenuItem()).apply();
        sharedPref.edit().putInt(getString(R.string.settings_nav_notifications_menu_item), this.getNavNotificationsMenuItem()).apply();

        sharedPref.edit().putInt(getString(R.string.settings_allow_facebook_authorization), this.getAllowFacebookAuthorization()).apply();
        sharedPref.edit().putInt(getString(R.string.settings_allow_login), this.getAllowLogIn()).apply();
        sharedPref.edit().putInt(getString(R.string.settings_allow_signup), this.getAllowSignUp()).apply();
        sharedPref.edit().putInt(getString(R.string.settings_allow_admob), this.getAllowAdmobBanner()).apply();
        sharedPref.edit().putInt(getString(R.string.settings_allow_password_recovery), this.getAllowPasswordRecovery()).apply();
        sharedPref.edit().putInt(getString(R.string.settings_allow_add_video_to_gallery), this.getAllowAddVideoToGallery()).apply();
        sharedPref.edit().putInt(getString(R.string.settings_allow_emoji), this.getAllowEmoji()).apply();
        sharedPref.edit().putInt(getString(R.string.settings_allow_add_image_to_message), this.getAllowAddImageToMessage()).apply();

        sharedPref.edit().putInt(getString(R.string.settings_allow_seen_function), this.getAllowSeenFunction()).apply();
        sharedPref.edit().putInt(getString(R.string.settings_allow_typing_function), this.getAllowTypingFunction()).apply();

        sharedPref.edit().putInt(getString(R.string.settings_allow_add_video_to_items), this.getAllowAddVideoToItems()).apply();
        sharedPref.edit().putInt(getString(R.string.settings_allow_gallery), this.getAllowGallery()).apply();
        sharedPref.edit().putInt(getString(R.string.settings_allow_add_image_to_comment), this.getAllowAddImageToComment()).apply();
        sharedPref.edit().putInt(getString(R.string.settings_allow_upgrades_section), this.getAllowUpgradesSection()).apply();
        sharedPref.edit().putInt(getString(R.string.settings_allow_spotlight), this.getAllowSpotlight()).apply();
        sharedPref.edit().putInt(getString(R.string.settings_allow_gifts), this.getAllowGifts()).apply();

        Log.e("SETTINGS SAVE", "qq");
    }

    public void setNavNotificationsMenuItem(int navNotificationsMenuItem) {

        this.navNotificationsMenuItem = navNotificationsMenuItem;
    }

    public int getNavNotificationsMenuItem() {

        return this.navNotificationsMenuItem;
    }

    public void setNavMessagesMenuItem(int navMessagesMenuItem) {

        this.navMessagesMenuItem = navMessagesMenuItem;
    }

    public int getAllowFacebookAuthorization() {

        return this.allowFacebookAuthorization;
    }

    public void setAllowFacebookAuthorization(int allowFacebookAuthorization) {

        this.allowFacebookAuthorization = allowFacebookAuthorization;
    }

    public int getAllowLogIn() {

        return this.allowLogIn;
    }

    public void setAllowLogin(int allowLogIn) {

        this.allowLogIn = allowLogIn;
    }

    public int getAllowSignUp() {

        return this.allowSignUp;
    }

    public void setAllowSignUp(int allowSignUp) {

        this.allowSignUp = allowSignUp;
    }

    public int getAllowAdmobBanner() {

        return this.allowAdmobBanner;
    }

    public void setAllowAdmobBanner(int allowAdmobBanner) {

        this.allowAdmobBanner = allowAdmobBanner;
    }

    public int getAllowAddVideoToGallery() {

        return this.allowAddVideoToGallery;
    }

    public void setAllowAddVideoToGallery(int allowAddVideoToGallery) {

        this.allowAddVideoToGallery = allowAddVideoToGallery;
    }

    public int getAllowPasswordRecovery() {

        return this.allowPasswordRecovery;
    }

    public void setAllowPasswordRecovery(int allowPasswordRecovery) {

        this.allowPasswordRecovery = allowPasswordRecovery;
    }

    public int getAllowEmoji() {

        return this.allowEmoji;
    }

    public void setAllowEmoji(int allowEmoji) {

        this.allowEmoji = allowEmoji;
    }

    public int getNavMessagesMenuItem() {

        return this.navMessagesMenuItem;
    }

    public int getAllowAddImageToMessage() {

        return this.allowAddImageToMessage;
    }

    public void setAllowAddImageToMessage(int allowAddImageToMessage) {

        this.allowAddImageToMessage = allowAddImageToMessage;
    }

    public int getAllowAddImageToComment() {

        return this.allowAddImageToComment;
    }

    public void setAllowAddImageToComment(int allowAddImageToComment) {

        this.allowAddImageToComment = allowAddImageToComment;
    }

    public int getAllowSeenFunction() {

        return this.allowSeenFunction;
    }

    public void setAllowSeenFunction(int allowSeenFunction) {

        this.allowSeenFunction = allowSeenFunction;
    }

    public int getAllowTypingFunction() {

        return this.allowTypingFunction;
    }

    public void setAllowTypingFunction(int allowTypingFunction) {

        this.allowTypingFunction = allowTypingFunction;
    }

    public int getAllowAddVideoToItems() {

        return this.allowAddVideoToItems;
    }

    public void setAllowAddVideoToItems(int allowAddVideoToItems) {

        this.allowAddVideoToItems = allowAddVideoToItems;
    }

    public int getAllowGallery() {

        return this.allowGallery;
    }

    public void setAllowGallery(int allowGallery) {

        this.allowGallery = allowGallery;
    }

    public int getAllowUpgradesSection() {

        return this.allowUpgradesSection;
    }

    public void setAllowUpgradesSection(int allowUpgradesSection) {

        this.allowUpgradesSection = allowUpgradesSection;
    }

    public int getAllowSpotlight() {

        return this.allowSpotlight;
    }

    public void setAllowSpotlight(int allowSpotlight) {

        this.allowSpotlight = allowSpotlight;
    }

    public int getAllowGifts() {

        return this.allowGifts;
    }

    public void setAllowGifts(int allowGifts) {

        this.allowGifts = allowGifts;
    }
}