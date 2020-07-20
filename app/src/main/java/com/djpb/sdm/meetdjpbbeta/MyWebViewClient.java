package com.djpb.sdm.meetdjpbbeta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

    ProgressDialog progressDialog;
    Activity activity;

    public MyWebViewClient(ProgressDialog progressDialog, Activity activity) {
        this.activity = activity;
        this.progressDialog = progressDialog;
        progressDialog.show();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if ("meet.setditjen-djpb.net".equals(Uri.parse(url).getHost())) {
            // This is my website, so do not override; let my WebView load the page
            return false;
        }
        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if(progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        super.onPageFinished(view, url);
    }
}
