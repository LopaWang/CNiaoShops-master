# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\CWQ\AppData\Local\Android\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn com.squareup.okhttp.**

#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}

#okgo
-dontwarn com.lzy.okgo.**
-keep class com.lzy.okgo.**{*;}

#okrx
-dontwarn com.lzy.okrx.**
-keep class com.lzy.okrx.**{*;}

#okserver
-dontwarn com.lzy.okserver.**
-keep class com.lzy.okserver.**{*;}

#BaseRecyclerViewAdapterHelper
-keep class com.chad.library.adapter.** {
   *;
}

#NoHttp
-dontwarn com.yanzhenjie.nohttp.**
-keep class com.yanzhenjie.nohttp.**{*;}

#okhttputils
-dontwarn com.zhy.http.**
-keep class com.zhy.http.**{*;}

#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}

#album
-dontwarn com.yanzhenjie.album.**
-keep class com.yanzhenjie.album.**{*;}

#ucrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

# Ping++ 混淆过滤
-dontwarn com.pingplusplus.**
-keep class com.pingplusplus.** {*;}

# 支付宝混淆过滤
-dontwarn com.alipay.**
-keep class com.alipay.** {*;}

# 微信或QQ钱包混淆过滤
-dontwarn  com.tencent.**
-keep class com.tencent.** {*;}

# 银联支付混淆过滤
-dontwarn  com.unionpay.**
-keep class com.unionpay.** {*;}

# 招行一网通混淆过滤
-keepclasseswithmembers class cmb.pb.util.CMBKeyboardFunc {
    public <init>(android.app.Activity);
    public boolean HandleUrlCall(android.webkit.WebView,java.lang.String);
    public void callKeyBoardActivity();
}

# 百付宝混淆过滤
-dontwarn com.baidu.**
-keep class com.baidu.** {*;}

# 内部WebView混淆过滤
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# ShareSDK
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R$*

# fastjson
-dontwarn  com.alibaba.**
-keep class com.alibaba.** {*;}

# bmob
-dontwarn  cn.bmob.**
-keep class cn.bmob.** {*;}

# amap
-dontwarn  cn.amap.**
-keep class cn.amap.** {*;}

-dontwarn  com.autonavi.**
-keep class com.autonavi.** {*;}

-dontwarn  io.reactivex.**
-keep class io.reactivex.** {*;}

-dontwarn  com.loc.**
-keep class com.loc.** {*;}

-dontwarn  rx.internal.**
-keep class rx.internal.** {*;}

-dontwarn  com.chhd.cniaoshops.bean.**
-keep class com.chhd.cniaoshops.bean.** {*;}

