package com.chhd.cniaoshops.biz;

import com.chhd.cniaoshops.bean.User;
import com.chhd.cniaoshops.util.JsonUtils;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.chhd.per_library.util.SpUtils;

/**
 * Created by CWQ on 2017/4/16.
 */

public class UserLocalData {

    private UserLocalData() {
    }

    public static void putUser(User user) {
        LoggerUtils.d("user: " + JsonUtils.toJSON(user));
        SpUtils.putString("user", JsonUtils.toJSON(user));
    }

    public static User getUser() {
        return JsonUtils.fromJson(SpUtils.getString("user", ""), User.class);
    }

    public static void clearUser() {
        SpUtils.putString("user", "");
    }
}
