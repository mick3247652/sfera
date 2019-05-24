package ru.club.sfera.constants;

public interface Constants {

    public static final int VIDEO_FILE_MAX_SIZE = 7340035; //Max size for video file in bytes | For example 5mb = 5*1024*1024
    public static final int ITEM_CHARACTERS_LIMIT = 1000;  //Max text size for Item

    public static final int MY_AD_AFTER_ITEM_NUMBER = 0;  //After first item
    public static final int SPOTLIGHT_AFTER_ITEM_NUMBER = 0;  //After first item

    public static final Boolean GOOGLE_PAY_TEST_BUTTON = false; // false = Do not show google pay test button in section upgrades

    public static final String YOUTUBE_API_KEY = "AIzaSyB9yLCYr9G96pTM12PoZrrH9x6Vq6Ql770";

    //public static final String BILLING_KEY = "";
    // Attention!
    // CLIENT_ID must be identical with CLIENT_ID from db.inc.php

    public static final String CLIENT_ID = "1";  //Client ID | For identify the application | Example: 12567

    public static final String API_DOMAIN = "http://auth.sfera.press/";  //url address to which the application sends requests

    public static final String API_FILE_EXTENSION = ".inc.php";
    public static final String API_VERSION = "v1";

    public static final String METHOD_APP_GET_SETTINGS = API_DOMAIN + "api/" + API_VERSION + "/method/app.getSettings" + API_FILE_EXTENSION;
    public static final String METHOD_ACCOUNT_GET_SETTINGS = API_DOMAIN + "api/" + API_VERSION + "/method/account.getSettings" + API_FILE_EXTENSION;
    public static final String METHOD_DIALOGS_GET = API_DOMAIN + "api/" + API_VERSION + "/method/dialogs.get" + API_FILE_EXTENSION;
    public static final String METHOD_CHAT_UPDATE = API_DOMAIN + "api/" + API_VERSION + "/method/chat.update" + API_FILE_EXTENSION;
    public static final String METHOD_CHAT_NOTIFY = API_DOMAIN + "api/" + API_VERSION + "/method/chat.notify" + API_FILE_EXTENSION;

    public static final String METHOD_ACCOUNT_LOGIN = API_DOMAIN + "api/" + API_VERSION + "/method/account.signIn" + API_FILE_EXTENSION;
    public static final String METHOD_ACCOUNT_SIGNUP = API_DOMAIN + "api/" + API_VERSION + "/method/account.signUp" + API_FILE_EXTENSION;
    public static final String METHOD_ACCOUNT_AUTHORIZE = API_DOMAIN + "api/" + API_VERSION + "/method/account.authorize" + API_FILE_EXTENSION;
    public static final String METHOD_ACCOUNT_SET_GCM_TOKEN = API_DOMAIN + "api/" + API_VERSION + "/method/account.setGcmToken" + API_FILE_EXTENSION;
    public static final String METHOD_ACCOUNT_LOGINBYFACEBOOK = API_DOMAIN + "api/" + API_VERSION + "/method/account.signInByFacebook" + API_FILE_EXTENSION;
    public static final String METHOD_ACCOUNT_RECOVERY = API_DOMAIN + "api/" + API_VERSION + "/method/account.recovery" + API_FILE_EXTENSION;
    public static final String METHOD_ACCOUNT_SETPASSWORD = API_DOMAIN + "api/" + API_VERSION + "/method/account.setPassword" + API_FILE_EXTENSION;
    public static final String METHOD_ACCOUNT_DEACTIVATE = API_DOMAIN + "api/" + API_VERSION + "/method/account.deactivate" + API_FILE_EXTENSION;
    public static final String METHOD_ACCOUNT_SAVE_SETTINGS = API_DOMAIN + "api/" + API_VERSION + "/method/account.saveSettings" + API_FILE_EXTENSION;
    public static final String METHOD_ACCOUNT_CONNECT_TO_FACEBOOK = API_DOMAIN + "api/" + API_VERSION + "/method/account.connectToFacebook" + API_FILE_EXTENSION;
    public static final String METHOD_ACCOUNT_DISCONNECT_FROM_FACEBOOK = API_DOMAIN + "api/" + API_VERSION + "/method/account.disconnectFromFacebook" + API_FILE_EXTENSION;
    public static final String METHOD_ACCOUNT_LOGOUT = API_DOMAIN + "api/" + API_VERSION + "/method/account.logOut" + API_FILE_EXTENSION;

    public static final String METHOD_ACCOUNT_SET_GCM_SETTINGS = API_DOMAIN + "api/" + API_VERSION + "/method/account.setGCM_settings" + API_FILE_EXTENSION;
    public static final String METHOD_ACCOUNT_SET_PRIVACY_SETTINGS = API_DOMAIN + "api/" + API_VERSION + "/method/account.setPrivacy_settings" + API_FILE_EXTENSION;

    public static final String METHOD_ACCOUNT_SET_GEO_LOCATION = API_DOMAIN + "api/" + API_VERSION + "/method/account.setGeoLocation" + API_FILE_EXTENSION;

    public static final String METHOD_SUPPORT_SEND_TICKET = API_DOMAIN + "api/" + API_VERSION + "/method/support.sendTicket" + API_FILE_EXTENSION;

    public static final String METHOD_PROFILE_GET = API_DOMAIN + "api/" + API_VERSION + "/method/profile.get" + API_FILE_EXTENSION;
    public static final String METHOD_PROFILE_UPLOADPHOTO = API_DOMAIN + "api/" + API_VERSION + "/method/profile.uploadPhoto" + API_FILE_EXTENSION;
    public static final String METHOD_PROFILE_UPLOADCOVER = API_DOMAIN + "api/" + API_VERSION + "/method/profile.uploadCover" + API_FILE_EXTENSION;
    public static final String METHOD_PROFILE_NEARBY = API_DOMAIN + "api/" + API_VERSION + "/method/profile.nearby" + API_FILE_EXTENSION;

    public static final String METHOD_BLACKLIST_GET = API_DOMAIN + "api/" + API_VERSION + "/method/blacklist.get" + API_FILE_EXTENSION;
    public static final String METHOD_BLACKLIST_ADD = API_DOMAIN + "api/" + API_VERSION + "/method/blacklist.add" + API_FILE_EXTENSION;
    public static final String METHOD_BLACKLIST_REMOVE = API_DOMAIN + "api/" + API_VERSION + "/method/blacklist.remove" + API_FILE_EXTENSION;

    public static final String METHOD_NOTIFICATIONS_GET = API_DOMAIN + "api/" + API_VERSION + "/method/notifications.get" + API_FILE_EXTENSION;
    public static final String METHOD_NOTIFICATIONS_CLEAR = API_DOMAIN + "api/" + API_VERSION + "/method/notifications.clear" + API_FILE_EXTENSION;

    public static final String METHOD_APP_CHECKUSERNAME = API_DOMAIN + "api/" + API_VERSION + "/method/app.checkUsername" + API_FILE_EXTENSION;
    public static final String METHOD_APP_TERMS = API_DOMAIN + "api/" + API_VERSION + "/method/app.terms" + API_FILE_EXTENSION;
    public static final String METHOD_APP_THANKS = API_DOMAIN + "api/" + API_VERSION + "/method/app.thanks" + API_FILE_EXTENSION;

    public static final String METHOD_CHAT_GET = API_DOMAIN + "api/" + API_VERSION + "/method/chat.get" + API_FILE_EXTENSION;
    public static final String METHOD_CHAT_REMOVE = API_DOMAIN + "api/" + API_VERSION + "/method/chat.remove" + API_FILE_EXTENSION;
    public static final String METHOD_CHAT_GET_PREVIOUS = API_DOMAIN + "api/" + API_VERSION + "/method/chat.getPrevious" + API_FILE_EXTENSION;

    public static final String METHOD_MSG_NEW = API_DOMAIN + "api/" + API_VERSION + "/method/msg.new" + API_FILE_EXTENSION;
    public static final String METHOD_MSG_UPLOAD_IMG = API_DOMAIN + "api/" + API_VERSION + "/method/msg.uploadImg" + API_FILE_EXTENSION;


    public static final String METHOD_REPORT_ADD = API_DOMAIN + "api/" + API_VERSION + "/method/report.add" + API_FILE_EXTENSION;

    public static final String METHOD_BALANCE_MODIFY = API_DOMAIN + "api/" + API_VERSION + "/method/balance.modify" + API_FILE_EXTENSION;


    public static final String METHOD_SEARCH_PROFILE = API_DOMAIN + "api/" + API_VERSION + "/method/search.profile" + API_FILE_EXTENSION;
    public static final String METHOD_SEARCH_PROFILE_PRELOAD = API_DOMAIN + "api/" + API_VERSION + "/method/search.profilePreload" + API_FILE_EXTENSION;


    public static final String METHOD_ITEMS_ADD = API_DOMAIN + "api/" + API_VERSION + "/method/items.add" + API_FILE_EXTENSION;
    public static final String METHOD_ITEMS_REPOST = API_DOMAIN + "api/" + API_VERSION + "/method/items.repost" + API_FILE_EXTENSION;
    public static final String METHOD_ITEMS_EDIT = API_DOMAIN + "api/" + API_VERSION + "/method/items.edit" + API_FILE_EXTENSION;
    public static final String METHOD_ITEMS_STREAM = API_DOMAIN + "api/" + API_VERSION + "/method/items.stream" + API_FILE_EXTENSION;
    public static final String METHOD_ITEMS_FEED = API_DOMAIN + "api/" + API_VERSION + "/method/items.feed" + API_FILE_EXTENSION;
    public static final String METHOD_ITEMS_HASHTAG = API_DOMAIN + "api/" + API_VERSION + "/method/items.hashtag" + API_FILE_EXTENSION;
    public static final String METHOD_ITEMS_REMOVE_ITEM = API_DOMAIN + "api/" + API_VERSION + "/method/items.removeItem" + API_FILE_EXTENSION;
    public static final String METHOD_ITEMS_GET_ITEM = API_DOMAIN + "api/" + API_VERSION + "/method/items.getItem" + API_FILE_EXTENSION;
    public static final String METHOD_ITEMS_GET = API_DOMAIN + "api/" + API_VERSION + "/method/items.get" + API_FILE_EXTENSION;
    public static final String METHOD_ITEMS_NEARBY = API_DOMAIN + "api/" + API_VERSION + "/method/items.nearby" + API_FILE_EXTENSION;
    public static final String METHOD_ITEMS_FAVORITES = API_DOMAIN + "api/" + API_VERSION + "/method/items.favorites" + API_FILE_EXTENSION;


    public static final String METHOD_GALLERY_ADD = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.add" + API_FILE_EXTENSION;
    public static final String METHOD_GALLERY_EDIT = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.edit" + API_FILE_EXTENSION;
    public static final String METHOD_GALLERY_STREAM = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.stream" + API_FILE_EXTENSION;
    public static final String METHOD_GALLERY_FEED = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.feed" + API_FILE_EXTENSION;
    public static final String METHOD_GALLERY_FAVORITES = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.favorites" + API_FILE_EXTENSION;
    public static final String METHOD_GALLERY_GET = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.get" + API_FILE_EXTENSION;
    public static final String METHOD_GALLERY_GET_ITEM = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.getItem" + API_FILE_EXTENSION;
    public static final String METHOD_GALLERY_REMOVE_ITEM = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.removeItem" + API_FILE_EXTENSION;
    public static final String METHOD_GALLERY_NEARBY = API_DOMAIN + "api/" + API_VERSION + "/method/gallery.nearby" + API_FILE_EXTENSION;

    public static final String METHOD_IMAGE_UPLOAD = API_DOMAIN + "api/" + API_VERSION + "/method/upload.image" + API_FILE_EXTENSION;
    public static final String METHOD_VIDEO_UPLOAD = API_DOMAIN + "api/" + API_VERSION + "/method/upload.video" + API_FILE_EXTENSION;

    public static final String METHOD_FRIENDS_GET = API_DOMAIN + "api/" + API_VERSION + "/method/friends.get" + API_FILE_EXTENSION;
    public static final String METHOD_FRIENDS_REJECT = API_DOMAIN + "api/" + API_VERSION + "/method/friends.reject" + API_FILE_EXTENSION;
    public static final String METHOD_FRIENDS_ACCEPT = API_DOMAIN + "api/" + API_VERSION + "/method/friends.accept" + API_FILE_EXTENSION;
    public static final String METHOD_FRIENDS_REQUEST = API_DOMAIN + "api/" + API_VERSION + "/method/friends.request" + API_FILE_EXTENSION;
    public static final String METHOD_FRIENDS_CANCEL_REQUEST = API_DOMAIN + "api/" + API_VERSION + "/method/friends.cancel" + API_FILE_EXTENSION;
    public static final String METHOD_FRIENDS_REMOVE = API_DOMAIN + "api/" + API_VERSION + "/method/friends.remove" + API_FILE_EXTENSION;

    public static final String METHOD_LIKE_ADD = API_DOMAIN + "api/" + API_VERSION + "/method/like.add" + API_FILE_EXTENSION;
    public static final String METHOD_LIKE_GET_LIKERS = API_DOMAIN + "api/" + API_VERSION + "/method/like.getLikers" + API_FILE_EXTENSION;

    public static final String METHOD_COMMENT_ADD = API_DOMAIN + "api/" + API_VERSION + "/method/comment.add" + API_FILE_EXTENSION;
    public static final String METHOD_COMMENT_REMOVE = API_DOMAIN + "api/" + API_VERSION + "/method/comment.remove" + API_FILE_EXTENSION;
    public static final String METHOD_COMMENT_UPLOAD_IMAGE = API_DOMAIN + "api/" + API_VERSION + "/method/comment.uploadImage" + API_FILE_EXTENSION;

    public static final String METHOD_GIFTS_ITEMS_GET = API_DOMAIN + "api/" + API_VERSION + "/method/gifts.getItems" + API_FILE_EXTENSION;
    public static final String METHOD_GIFTS_ADD = API_DOMAIN + "api/" + API_VERSION + "/method/gifts.add" + API_FILE_EXTENSION;
    public static final String METHOD_GIFTS_GET = API_DOMAIN + "api/" + API_VERSION + "/method/gifts.get" + API_FILE_EXTENSION;
    public static final String METHOD_GIFTS_REMOVE = API_DOMAIN + "api/" + API_VERSION + "/method/gifts.remove" + API_FILE_EXTENSION;

    public static final String METHOD_GUESTS_GET = API_DOMAIN + "api/" + API_VERSION + "/method/guests.get" + API_FILE_EXTENSION;
    public static final String METHOD_GUESTS_CLEAR = API_DOMAIN + "api/" + API_VERSION + "/method/guests.clear" + API_FILE_EXTENSION;

    public static final String METHOD_UPGRADES_GHOST = API_DOMAIN + "api/" + API_VERSION + "/method/upgrades.ghostUpgrade" + API_FILE_EXTENSION;
    public static final String METHOD_UPGRADES_ADMOB = API_DOMAIN + "api/" + API_VERSION + "/method/upgrades.admobUpgrade" + API_FILE_EXTENSION;
    public static final String METHOD_UPGRADES_VERIFIED_BADGE = API_DOMAIN + "api/" + API_VERSION + "/method/upgrades.verifiedBadgeUpgrade" + API_FILE_EXTENSION;
    public static final String METHOD_UPGRADES_GUESTS = API_DOMAIN + "api/" + API_VERSION + "/method/upgrades.guestsUpgrade" + API_FILE_EXTENSION;

    public static final String METHOD_SPOTLIGHT_GET = API_DOMAIN + "api/" + API_VERSION + "/method/spotlight.get" + API_FILE_EXTENSION;
    public static final String METHOD_SPOTLIGHT_ADD = API_DOMAIN + "api/" + API_VERSION + "/method/spotlight.add" + API_FILE_EXTENSION;

    public static final String APP_TEMP_FOLDER = "networkpro"; //directory for temporary storage for images from the camera

    public static final int LIST_ITEMS = 20;

    // Costs for Upgrades Section

    public static final int VERIFIED_BADGE_COST = 100;
    public static final int DISABLE_ADS_COST = 250;
    public static final int ENABLE_GUESTS_COST = 350;
    public static final int GHOST_MODE_COST = 450;
    public static final int ADD_TO_SPOTLIGHT_COST = 50;

    // Nearby dialog items. Value in miles

    public static final int NEARBY_DIALOG_OPTION_1 = 50;
    public static final int NEARBY_DIALOG_OPTION_2 = 100;
    public static final int NEARBY_DIALOG_OPTION_3 = 250;
    public static final int NEARBY_DIALOG_OPTION_4 = 500;
    public static final int NEARBY_DIALOG_OPTION_5 = 1000;

    public static final int ENABLED = 1;
    public static final int DISABLED = 0;

    public static final int FCM_ENABLED = 1;
    public static final int FCM_DISABLED = 0;

    public static final int COMMENTS_ENABLED = 1;
    public static final int COMMENTS_DISABLED = 0;

    public static final int MESSAGES_ENABLED = 1;
    public static final int MESSAGES_DISABLED = 0;

    public static final int ERROR_SUCCESS = 0;

    public static final int SEX_UNKNOWN = 0;
    public static final int SEX_MALE = 1;
    public static final int SEX_FEMALE = 2;

    public static final int NOTIFY_TYPE_LIKE = 0;
    public static final int NOTIFY_TYPE_FOLLOWER = 1;
    public static final int NOTIFY_TYPE_MESSAGE = 2;
    public static final int NOTIFY_TYPE_COMMENT = 3;
    public static final int NOTIFY_TYPE_COMMENT_REPLY = 4;
    public static final int NOTIFY_TYPE_FRIEND_REQUEST_ACCEPTED = 5;
    public static final int NOTIFY_TYPE_GIFT = 6;

    public static final int NOTIFY_TYPE_REVIEW = 14;
    public static final int NOTIFY_TYPE_FRIEND_REQUEST = 15;
    public static final int NOTIFY_TYPE_REPOST = 16;
    public static final int NOTIFY_TYPE_POST = 17;
    public static final int NOTIFY_TYPE_MENTION = 18;

    public static final int GCM_MESSAGE_FOR_ALL_USERS = 0;
    public static final int GCM_MESSAGE_ONLY_FOR_AUTHORIZED_USERS = 1;
    public static final int GCM_MESSAGE_ONLY_FOR_PERSONAL_USER = 2;

    public static final int GCM_NOTIFY_CONFIG = 0;
    public static final int GCM_NOTIFY_SYSTEM = 1;
    public static final int GCM_NOTIFY_CUSTOM = 2;
    public static final int GCM_NOTIFY_LIKE = 3;
    public static final int GCM_NOTIFY_ANSWER = 4;
    public static final int GCM_NOTIFY_QUESTION = 5;
    public static final int GCM_NOTIFY_COMMENT = 6;
    public static final int GCM_NOTIFY_FOLLOWER = 7;
    public static final int GCM_NOTIFY_PERSONAL = 8;
    public static final int GCM_NOTIFY_MESSAGE = 9;
    public static final int GCM_NOTIFY_COMMENT_REPLY = 10;
    public static final int GCM_FRIEND_REQUEST_INBOX = 11;
    public static final int GCM_FRIEND_REQUEST_ACCEPTED = 12;
    public static final int GCM_NOTIFY_GIFT = 14;
    public static final int GCM_NOTIFY_SEEN = 15;
    public static final int GCM_NOTIFY_TYPING = 16;
    public static final int GCM_NOTIFY_URL = 17;

    public static final int GCM_NOTIFY_REVIEW = 24;
    public static final int GCM_NOTIFY_EMAIL_VERIFIED = 25;
    public static final int GCM_NOTIFY_PHONE_VERIFIED = 26;
    public static final int GCM_NOTIFY_TYPING_START = 27;
    public static final int GCM_NOTIFY_TYPING_END = 28;
    public static final int GCM_NOTIFY_REPOST = 29;
    public static final int GCM_NOTIFY_POST = 30;
    public static final int GCM_NOTIFY_MENTION = 31;
    public static final int GCM_NOTIFY_UPDATE_PROFILE = 32;


    public static final int ERROR_LOGIN_TAKEN = 300;
    public static final int ERROR_EMAIL_TAKEN = 301;
    public static final int ERROR_FACEBOOK_ID_TAKEN = 302;

    public static final int ACCOUNT_STATE_ENABLED = 0;
    public static final int ACCOUNT_STATE_DISABLED = 1;
    public static final int ACCOUNT_STATE_BLOCKED = 2;
    public static final int ACCOUNT_STATE_DEACTIVATED = 3;

    public static final int ACCOUNT_TYPE_PROFILE = 0;
    public static final int ACCOUNT_TYPE_COMMUNITY = 1;
    public static final int ACCOUNT_TYPE_PAGE = 1;

    public static final int ERROR_UNKNOWN = 100;
    public static final int ERROR_ACCESS_TOKEN = 101;

    public static final int ERROR_ACCOUNT_ID = 400;

    public static final int UPLOAD_TYPE_PHOTO = 0;
    public static final int UPLOAD_TYPE_COVER = 1;

    public static final int ACTION_NEW = 1;
    public static final int ACTION_EDIT = 2;
    public static final int SELECT_POST_IMG = 3;
    public static final int VIEW_CHAT = 4;
    public static final int CREATE_POST_IMG = 5;
    public static final int SELECT_CHAT_IMG = 6;
    public static final int CREATE_CHAT_IMG = 7;
    public static final int FEED_NEW_POST = 8;
    public static final int FRIENDS_SEARCH = 9;
    public static final int ITEM_EDIT = 10;
    public static final int STREAM_NEW_POST = 11;
    public static final int ACTION_LOGIN = 100;
    public static final int ACTION_SIGNUP = 101;
    public static final int ACTION_SPOTLIGHT = 102;

    public static final int ACCOUNT_ACCESS_LEVEL_AVAILABLE_TO_ALL = 0;
    public static final int ACCOUNT_ACCESS_LEVEL_AVAILABLE_TO_FRIENDS = 1;

    public static final int GALLERY_ITEM_TYPE_IMAGE = 0;
    public static final int GALLERY_ITEM_TYPE_VIDEO = 1;

    public static final int WALL_ITEM_TYPE_PROFILE_POST = 0;
    public static final int WALL_ITEM_TYPE_COMMUNITY_POST = 1;


    public static final int FRIEND_TYPE_FRIEND = 0;
    public static final int FRIEND_TYPE_FOLLOWER = 1;

    public static final int ITEM_TYPE_IMAGE = 0;
    public static final int ITEM_TYPE_VIDEO = 1;
    public static final int ITEM_TYPE_POST = 2;
    public static final int ITEM_TYPE_COMMENT = 3;
    public static final int ITEM_TYPE_PROFILE = 4;
    public static final int ITEM_TYPE_GALLERY_ITEM = 5;

    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_PHOTO = 1;                  //WRITE_EXTERNAL_STORAGE
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_COVER = 2;                  //WRITE_EXTERNAL_STORAGE
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 3;                               //ACCESS_COARSE_LOCATION
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 4;                        //WRITE_EXTERNAL_STORAGE

    public static final int PAGE_UNKNOWN = -1;
    public static final int PAGE_STREAM = 0;
    public static final int PAGE_FEED = 1;
    public static final int PAGE_PROFILE = 2;
    public static final int PAGE_SEARCH = 3;
    public static final int PAGE_NEARBY = 4;
    public static final int PAGE_FRIENDS = 5;
    public static final int PAGE_FAVORITES = 6;
    public static final int PAGE_NOTIFICATIONS = 7;
    public static final int PAGE_MESSAGES = 8;
    public static final int PAGE_NEWS = 9;
    public static final int PAGE_GUESTS = 10;
    public static final int PAGE_UPGRADES = 11;

    public static final String TAG = "TAG";
}