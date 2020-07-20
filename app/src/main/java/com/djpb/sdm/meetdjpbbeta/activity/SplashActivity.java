package com.djpb.sdm.meetdjpbbeta.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import com.djpb.sdm.meetdjpbbeta.R;
import com.djpb.sdm.meetdjpbbeta.utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private int waktu_loading = 3000;

    List<String> listPermission = new ArrayList<>();
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermissions()) {
                newHandleStart();
            }
        } else {
            newHandleStart();
        }

        Tools.setSystemBarColorActivity(this, R.color.grey_5);
        Tools.setSystemBarLightActivity(this);
    }

    private void newHandleStart() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah ke login activity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();

            }
        }, waktu_loading);
    }

    private boolean checkAndRequestPermissions() {
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS
        };

        for ( String permission: permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                listPermission.add(permission);
            }
        }

        if (!listPermission.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermission.toArray
                    (new String[listPermission.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                Boolean allPermissionsGranted  = true;
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            allPermissionsGranted  = false;
                            break;
                        }
                    }
                    if (!allPermissionsGranted) {
                        boolean somePermissionsForeverDenied = false;
                        boolean checkAllPermissonAllAlowed= true;
                        for(String permission: permissions){
                            if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                                //denied
                                checkAndRequestPermissions();
                                checkAllPermissonAllAlowed = false;
                                Log.e("denied", permission);
                            }else{
                                if(ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED){
                                    //allowed
                                    Log.e("allowed", permission);
                                } else{
                                    //set to never ask again
                                    Log.e("set to never ask again", permission);
                                    somePermissionsForeverDenied = true;
                                    checkAllPermissonAllAlowed = false;
                                    break;
                                }
                            }
                        }

                        if(somePermissionsForeverDenied){
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                            alertDialogBuilder.setTitle("Permissions Required")
                                    .setMessage("You have forcefully denied some of the required permissions " +
                                            "for this action. Please open settings, go to permissions and allow them.")
                                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                    Uri.fromParts("package", getPackageName(), null));
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            System.exit(1);
                                        }
                                    })
                                    .setCancelable(false)
                                    .create()
                                    .show();
                        }
                        if (checkAllPermissonAllAlowed){
                            newHandleStart();
                        }
                    } else {
                        newHandleStart();
                    }
                }
        }
    }
}
