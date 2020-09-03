package com.chhd.cniaoshops.global;

import com.chhd.cniaoshops.R;

/**
 * Created by CWQ on 2016/11/18.
 */

public interface Constant {

    String APP_KEY = "1838010015";
    String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    String TAG = "debug_Logger";
    String TAG_NOHTTP = "debug_NoHttp";
    String TAG_OKGO = "debug_OkGo";
    String TAG_OKHTTP_UTILS = "debug_Okhttp-Utils";

    String SERVER_URL = "http://112.124.22.238:8081/course_api/";

    String ERROR = "error";

    String KEY_DES = "CNiaoShops";

    int REQUEST_FROM_ALBUM = 999;

    int DIMEN_NORMAL = 10;
    int DIMEN_SMALL = 5;

    int REQUEST_CODE_READ_SMS = 500;

    int REQUEST_ADDRESS_LIST_ACTIVITY = 500;

    int[] MT_PULL_ANIM_SRCS = new int[]{R.drawable.mt_pull, R.drawable.mt_pull01, R.drawable.mt_pull02, R.drawable.mt_pull03, R.drawable.mt_pull04, R.drawable.mt_pull05};
    int[] MT_REFRESH_ANIM_SRCS = new int[]{R.drawable.mt_refreshing01, R.drawable.mt_refreshing02, R.drawable.mt_refreshing03, R.drawable.mt_refreshing04, R.drawable.mt_refreshing05, R.drawable.mt_refreshing06};
    int[] MT_LOADING_ANIM_SRCS = new int[]{R.drawable.mt_loading01, R.drawable.mt_loading02};

    int TIME_OUT = 15 * 1000;

    int DELAYMILLIS_FOR_RQUEST_FINISH = 650;
    int DELAYMILLIS_FOR_SHOW_EMPTY = 100;

    int BANNER_DESCRIPTION_LAYOUT_HEIGHT = 30;

    int placeholderResId = R.drawable.empty_photo;

    int[] SWIPE_REFRESH_LAYOUT_COLORS = new int[]{
            R.color.colorAccent
    };
}
