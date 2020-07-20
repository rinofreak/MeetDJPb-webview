package com.djpb.sdm.meetdjpbbeta.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.djpb.sdm.meetdjpbbeta.MyWebViewClient;
import com.djpb.sdm.meetdjpbbeta.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WebViewFragment extends Fragment implements ConfirmDialogFragment.Listener, MessageDialogFragment.Listener {

    private static final String TAG = WebViewFragment.class.getSimpleName();
    private static final String FRAGMENT_DIALOG = "dialog";
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private PermissionRequest mPermissionRequest;

    @BindView(R.id.wv_room)
    WebView wvRoom;
    Unbinder unbinder;

    ProgressDialog loading;
    String namaRoom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        loading = new ProgressDialog(getActivity());
        loading.setMessage("Tunggu ya...");

        namaRoom = getArguments().getString("namaRoom");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settings();

        wvRoom.setWebChromeClient(mWebChromeClient);
        wvRoom.setWebViewClient(new MyWebViewClient(loading, getActivity()));

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        } else {
            wvRoom.loadUrl("https://meet.setditjen-djpb.net/" + namaRoom);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // This is for runtime permission on Marshmallow and above; It is not directly related to
        // PermissionRequest API.
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (permissions.length != 1 || grantResults.length != 1 ||
                    grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Camera permission not granted.");
            } else if (wvRoom != null) {
                wvRoom.loadUrl("https://meet.setditjen-djpb.net/" + namaRoom);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            MessageDialogFragment.newInstance(R.string.permission_message)
                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    private void settings() {
        String desktop_mode = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";

        WebSettings webSettings = wvRoom.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUserAgentString(desktop_mode);
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        // This method is called when the web content is requesting permission to access some
        // resources.
        @Override
        public void onPermissionRequest(PermissionRequest request) {
            Log.i(TAG, "onPermissionRequest");
            mPermissionRequest = request;
            final String[] requestedResources = request.getResources();
            for (String r : requestedResources) {
                if (r.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                    // In this sample, we only accept video capture request.
                    ConfirmDialogFragment
                            .newInstance(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE})
                            .show(getChildFragmentManager(), FRAGMENT_DIALOG);
                    break;
                }
            }
        }

        // This method is called when the permission request is canceled by the web content.
        @Override
        public void onPermissionRequestCanceled(PermissionRequest request) {
            Log.i(TAG, "onPermissionRequestCanceled");
            // We dismiss the prompt UI here as the request is no longer valid.
            mPermissionRequest = null;
            DialogFragment fragment = (DialogFragment) getChildFragmentManager()
                    .findFragmentByTag(FRAGMENT_DIALOG);
            if (null != fragment) {
                fragment.dismiss();
            }
        }
    };

    @Override
    public void onConfirmation(boolean allowed, String[] resources) {
        if (allowed) {
            mPermissionRequest.grant(resources);
            Log.d(TAG, "Permission granted.");
        } else {
            mPermissionRequest.deny();
            Log.d(TAG, "Permission request denied.");
        }
        mPermissionRequest = null;
    }

    @Override
    public void onOkClicked() {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
