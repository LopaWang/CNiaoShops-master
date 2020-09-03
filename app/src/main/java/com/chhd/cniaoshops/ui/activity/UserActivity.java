package com.chhd.cniaoshops.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.User;
import com.chhd.cniaoshops.biz.UserLocalData;
import com.chhd.cniaoshops.global.App;
import com.chhd.cniaoshops.http.bmob.SimpleUpdateListener;
import com.chhd.cniaoshops.http.bmob.SimpleUploadListener;
import com.chhd.cniaoshops.ui.base.activity.BaseActivity;
import com.chhd.cniaoshops.util.DialogUtils;
import com.chhd.per_library.util.UiUtils;
import com.chhd.per_library.util.UriUtils;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yanzhenjie.album.Album;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class UserActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.scroll)
    ObservableScrollView scrollView;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();

        scrollView.setScrollViewCallbacks(this);

    }

    @Override
    public void onResume() {
        super.onResume();

        initUserData();
    }

    private void initUserData() {
        if (App.user.getAvatar() != null) {
            Picasso
                    .with(this)
                    .load(App.user.getAvatar().getUrl())
                    .centerCrop()
                    .resize(UiUtils.dp2px(45), UiUtils.dp2px(45))
                    .into(ivAvatar);
        }
        tvNickname.setText(App.user.getNickname());
    }

    private void initActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(scrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_user;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int parallaxHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height) - toolbar.getHeight();
        int baseColor = getResources().getColor(R.color.colorPrimary);
        float alpha = Math.min(1, (float) scrollY / parallaxHeight);
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(imageView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @OnClick({R.id.avatar, R.id.nickname, R.id.btn_logout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar:
                Album.album(this)
                        .requestCode(REQUEST_FROM_ALBUM) // 请求码，返回时onActivityResult()的第一个参数。
                        .title(getString(R.string.picture)) // 配置title。
                        .toolBarColor(UiUtils.getColor(R.color.colorAccent)) // Toolbar 颜色，默认蓝色。
                        .statusBarColor(UiUtils.getColor(R.color.colorAccent)) // StatusBar 颜色，默认蓝色。
                        .selectCount(1) // 最多选择几张图片。
                        .columnCount(3) // 相册展示列数，默认是2列。
                        .camera(true) // 是否有拍照功能。
                        .start();
                break;
            case R.id.nickname:
                showInputDialog();
                break;
            case R.id.btn_logout:
                BmobUser.logOut();
                App.user = null;
                UserLocalData.clearUser();
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_FROM_ALBUM:
                if (resultCode == RESULT_OK) {
                    String path = Album.parseResult(data).get(0);
                    String destinationFileName = "avater.png";
                    crop(UriUtils.getImageContentUri(new File(path)), Uri.fromFile(new File(getCacheDir(), destinationFileName)));
                }
                break;
            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    final Uri uri = UCrop.getOutput(data);
                    final BmobFile file = new BmobFile(UriUtils.getFileFromContentUri(uri));
                    file.upload(new SimpleUploadListener(context) {
                        @Override
                        public void success() {
                            User newUser = new User();
                            newUser.setAvatar(file);
                            newUser.update(BmobUser.getCurrentUser().getObjectId(), new SimpleUpdateListener(context) {
                                @Override
                                public void success() {
                                    App.user.setAvatar(file);
                                    UserLocalData.putUser(App.user);
                                    Picasso
                                            .with(context)
                                            .load(file.getUrl())
                                            .into(ivAvatar);
                                }
                            });
                        }
                    });
                }
                break;
        }
    }

    private void crop(Uri source, Uri destination) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setStatusBarColor(UiUtils.getColor(R.color.colorAccent));
        options.setToolbarColor(UiUtils.getColor(R.color.colorAccent));
        options.setHideBottomControls(true);
        UCrop
                .of(source, destination)
                .withAspectRatio(1, 1)
                .withMaxResultSize(512, 512)
                .withOptions(options)
                .start(this);
    }

    private void showInputDialog() {
        DialogUtils
                .newBuilder(this)
                .title(R.string.nickname)
                .input("", App.user.getNickname(), false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, final CharSequence input) {
                        User newUser = new User();
                        newUser.setNickname(input.toString());
                        newUser.update(BmobUser.getCurrentUser().getObjectId(), new SimpleUpdateListener(context) {
                            @Override
                            public void success() {
                                App.user.setNickname(input.toString());
                                UserLocalData.putUser(App.user);
                                initUserData();
                            }
                        });
                    }
                })
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .show();
    }

}
