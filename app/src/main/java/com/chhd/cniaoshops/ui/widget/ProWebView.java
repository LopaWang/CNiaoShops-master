package com.chhd.cniaoshops.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

/**
 * Created by CWQ on 2017/4/11.
 */

public class ProWebView extends RelativeLayout {

    private WebView webView;
    private ProgressView progressView;

    private WebSettings settings;

    public ProWebView(Context context) {
        this(context, null);
    }

    public ProWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        webView = new WebView(getContext());
        addView(webView, params);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, dp2px(1.5f));
        progressView = new ProgressView(getContext());
        addView(progressView, params);

        settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(webChromeClient);

    }

    private int dp2px(float dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    private WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressView.setProgress(newProgress);
        }
    };

    public WebView getWebView() {
        return webView;
    }

    public ProgressView getProgressView() {
        return progressView;
    }

}
