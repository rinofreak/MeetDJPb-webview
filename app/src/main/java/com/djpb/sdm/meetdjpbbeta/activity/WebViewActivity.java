package com.djpb.sdm.meetdjpbbeta.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.djpb.sdm.meetdjpbbeta.MyWebViewClient;
import com.djpb.sdm.meetdjpbbeta.R;
import com.djpb.sdm.meetdjpbbeta.utils.ViewAnimation;
import com.djpb.sdm.meetdjpbbeta.utils.Tools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST_CAMERA = 1;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 2;
    private static final int MY_PERMISSIONS_REQUEST_MODIFY_AUDIO = 3;

    @BindView(R.id.wv_room)
    WebView wvRoom;

    ProgressDialog loading;
    String namaRoom, displayName, hangup;
    @BindView(R.id.fab_hangup)
    FloatingActionButton fabHangup;
    @BindView(R.id.btn_hangup)
    AppCompatButton btnHangup;

    AlertDialog.Builder dialog;
    View dialogView;
    LayoutInflater layoutInflater;

    private String pathFile = "file:///android_asset/meetdjpb.html";
    private boolean rotate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        loading = new ProgressDialog(this);
        loading.setMessage("Tunggu ya...");

        initFab();
        initData();

        settings();
        wvRoom.setWebViewClient(new MyWebViewClient(loading, this));
        wvRoom.setWebChromeClient(myWebChromeClient);
        if (savedInstanceState == null) {
            wvRoom.loadUrl(pathFile);
        }

        wvRoom.addJavascriptInterface(new WebAppInterface(this), "AndroidFunction");

        Tools.setSystemBarColorActivity(this, R.color.dark_blue);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        wvRoom.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        wvRoom.restoreState(savedInstanceState);
    }

    private void initFab() {
        ViewAnimation.initShowOut(btnHangup);

        fabHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotate = ViewAnimation.rotateFab(v, !rotate);
                if(rotate) {
                    ViewAnimation.fadeIn(btnHangup);
                } else {
                    ViewAnimation.fadeOut(btnHangup);
                }
            }
        });

        btnHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        Uri uri = getIntent().getData();
        if(uri != null) {
            List<String> params = uri.getPathSegments();
            namaRoom = params.get(params.size()-1);
        } else {
            Intent i = getIntent();
            Bundle b = i.getExtras();
            namaRoom = b.getString("namaRoom");
            displayName = b.getString("displayName");
        }
    }

    private void settings() {
        String desktop_mode = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.96 Safari/537.36";

        WebSettings webSettings = wvRoom.getSettings();
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUserAgentString(desktop_mode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSION_REQUEST_CAMERA || requestCode == MY_PERMISSIONS_REQUEST_RECORD_AUDIO || requestCode == MY_PERMISSIONS_REQUEST_MODIFY_AUDIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(WebViewActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(WebViewActivity.this, "permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public String getRoomName() {
            return namaRoom;
        }

        @JavascriptInterface
        public String getDisplayName() {
            return displayName;
        }

        @JavascriptInterface
        public String getHangup() {
            return hangup;
        }
    }

    private WebChromeClient myWebChromeClient = new WebChromeClient() {
        @Override
        public void onPermissionRequest(PermissionRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                request.grant(request.getResources());
            }
        }

        @Override
        public void onPermissionRequestCanceled(PermissionRequest request) {
            super.onPermissionRequestCanceled(request);
            Toast.makeText(WebViewActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onBackPressed() {
        if (wvRoom.canGoBack()) {
            wvRoom.goBack();
        } else {
            dialogForm();
        }
    }

    private void dialogForm() {
        dialog = new AlertDialog.Builder(WebViewActivity.this, R.style.AlertDialogCustom);
        layoutInflater = getLayoutInflater();
        dialogView = layoutInflater.inflate(R.layout.hangup_dialog, null);

        dialog.setView(dialogView);
        dialog.setCancelable(true);

        dialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(WebViewActivity.this, MainActivity.class);
                startActivity(i);
                wvRoom.loadUrl("javascript: hangupMeeting()");
                Toast.makeText(WebViewActivity.this, "Terima kasih telah menggunakan meetDJPb", Toast.LENGTH_SHORT).show();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });

        dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
