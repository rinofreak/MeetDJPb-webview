package com.djpb.sdm.meetdjpbbeta;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class WebInterface {

    String value;
    private Context context;

    public WebInterface(String value, Context context) {
        this.value = value;
        this.context = context;
    }

    @JavascriptInterface
    public String getValue() {
        return value;
    }
}
