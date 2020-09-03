package com.chhd.cniaoshops.biz;

import android.app.Activity;

import com.chhd.cniaoshops.bean.User;
import com.chhd.cniaoshops.global.App;
import com.chhd.cniaoshops.global.Constant;
import com.chhd.cniaoshops.http.SimpleFileCallback;
import com.chhd.cniaoshops.http.bmob.SimpleSaveListener;
import com.chhd.cniaoshops.http.bmob.SimpleUploadListener;
import com.chhd.cniaoshops.util.DESUtils;
import com.chhd.per_library.util.Md5Utils;
import com.chhd.sharesdk.login.ThirdPartyUser;
import com.lzy.okgo.OkGo;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by CWQ on 2017/4/16.
 */

public class BmobApi implements Constant {

    private Activity context;

    public BmobApi(Activity context) {
        this.context = context;
    }

    public void requestLogin(String username, String pwd) {
        BmobUser user = new BmobUser();
        user.setUsername(username);
        user.setPassword(DESUtils.encode(KEY_DES, pwd));
        user.login(new SimpleSaveListener<User>(context) {
            @Override
            public void success(User user) {
                App.user = user;
                UserLocalData.putUser(user);
                context.setResult(Activity.RESULT_OK);
                if (App.intent == null) {
                    context.finish();
                } else {
                    App.jumpToTargetActivity(context);
                    context.finish();
                }
            }
        });
    }

    public void requestLogin(final ThirdPartyUser thirdPartyUser) {
        BmobUser user = new BmobUser();
        user.setUsername(thirdPartyUser.getUserID());
        user.setPassword(DESUtils.encode(KEY_DES, thirdPartyUser.getUserID()));
        user.login(new SimpleSaveListener<User>(context) {
            @Override
            public void success(User user) {
                App.user = user;
                UserLocalData.putUser(user);
                context.setResult(Activity.RESULT_OK);
                if (App.intent == null) {
                    context.finish();
                } else {
                    App.jumpToTargetActivity(context);
                    context.finish();
                }
            }

            @Override
            protected void error(BmobException e) {
                switch (e.getErrorCode()) {
                    case 101:
                        OkGo
                                .get(thirdPartyUser.getIcon())
                                .execute(new SimpleFileCallback("avater.png", context) {
                                    @Override
                                    public void success(File file, Call call, Response response) {
                                        final BmobFile bmobFile = new BmobFile(file);
                                        bmobFile.upload(new SimpleUploadListener(context) {
                                            @Override
                                            public void success() {
                                                requestRegisterByThirdParty(thirdPartyUser, bmobFile);
                                            }
                                        });
                                    }
                                });
                        break;
                }
            }
        });
    }

    public void requestRegisterByName(String username, String pwd) {
        User user = new User();
        user.setUsername(username);
        user.setNickname(username);
        user.setPassword(DESUtils.encode(KEY_DES, pwd));
        user.signUp(new SimpleSaveListener<User>(context) {
            @Override
            public void success(User user) {
                App.user = user;
                UserLocalData.putUser(user);
                context.setResult(Activity.RESULT_OK);
                if (App.intent == null) {
                    context.finish();
                } else {
                    App.jumpToTargetActivity(context);
                    context.finish();
                }
            }
        });
    }

    public void requestRegisterByThirdParty(ThirdPartyUser thirdPartyUser, BmobFile avater) {
        User user = new User();
        user.setUsername(thirdPartyUser.getUserID());
        user.setNickname(thirdPartyUser.getNickname());
        user.setPassword(DESUtils.encode(KEY_DES, thirdPartyUser.getUserID()));
        user.setAvatar(avater);
        user.signUp(new SimpleSaveListener<User>(context) {
            @Override
            public void success(User user) {
                App.user = user;
                UserLocalData.putUser(user);
                context.setResult(Activity.RESULT_OK);
                if (App.intent == null) {
                    context.finish();
                } else {
                    App.jumpToTargetActivity(context);
                    context.finish();
                }
            }
        });
    }

    public void requestRegisterByNumber(final String phone, String code, final String pwd) {
        String encoder = Md5Utils.encoder(phone);
        encoder = encoder.substring(encoder.length() - 10, encoder.length());
        User user = new User();
        user.setMobilePhoneNumber(phone);
        user.setUsername(encoder);
        user.setNickname(phone);
        user.setPassword(DESUtils.encode(KEY_DES, pwd));
        user.signOrLogin(code, new SimpleSaveListener<User>(context) {
            @Override
            public void success(User user) {
                App.user = user;
                UserLocalData.putUser(user);
                context.setResult(Activity.RESULT_OK);
                if (App.intent == null) {
                    context.finish();
                } else {
                    App.jumpToTargetActivity(context);
                    context.finish();
                }
            }
        });
    }
}
