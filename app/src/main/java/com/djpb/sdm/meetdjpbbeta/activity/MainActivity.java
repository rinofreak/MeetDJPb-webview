package com.djpb.sdm.meetdjpbbeta.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.djpb.sdm.meetdjpbbeta.R;
import com.djpb.sdm.meetdjpbbeta.utils.SharedPrefManager;
import com.djpb.sdm.meetdjpbbeta.utils.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_create_room)
    AppCompatButton btnCreateRoom;
    @BindView(R.id.btn_join_room)
    AppCompatButton btnJoinRoom;
    @BindView(R.id.rl_button)
    RelativeLayout rlButton;
    @BindView(R.id.tv_no_room)
    TextView tvNoRoom;
    @BindView(R.id.tv_last_room)
    TextView tvLastRoom;
    @BindView(R.id.cv_room)
    CardView cvRoom;

    AlertDialog.Builder dialog;
    LayoutInflater layoutInflater;
    View dialogView;
    EditText etRoom, etName;
    TextView tvDialog;
    String namaRoom, displayName;

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogCustom);
        layoutInflater = getLayoutInflater();
        dialogView = layoutInflater.inflate(R.layout.room_dialog, null);
        etRoom = dialogView.findViewById(R.id.et_room);
        tvDialog = dialogView.findViewById(R.id.tv_dialog_room);

        btnJoinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogView.getParent() != null) {
                    ViewGroup parent = (ViewGroup) dialogView.getParent();
                    parent.removeAllViews();
                }
                tvDialog.setText("Masukkan nama room");
                dialogForm();
            }
        });

        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogView.getParent() != null) {
                    ViewGroup parent = (ViewGroup) dialogView.getParent();
                    parent.removeView(dialogView);
                }
                tvDialog.setText("Buat Room baru");
                etRoom.setText(null);
                dialogForm();
            }
        });

        initLastRoom();

        Tools.setSystemBarColorActivity(this, R.color.dark_blue);
    }

    private void initLastRoom() {
        String roomName = SharedPrefManager.getRoomName(getBaseContext());

        if (!TextUtils.isEmpty(roomName)) {
            cvRoom.setVisibility(View.VISIBLE);
            tvNoRoom.setVisibility(View.GONE);
            tvLastRoom.setText(roomName);

            cvRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    cvRoom.setEnabled(false);

                    String userName = SharedPrefManager.getUserName(getBaseContext());

                    Bundle bundle = new Bundle();
                    bundle.putString("namaRoom", roomName);
                    bundle.putString("displayName", userName);
                    startActivityBundle(WebViewActivity.class, bundle);
                }
            });
        } else if(TextUtils.isEmpty(roomName)) {
            cvRoom.setVisibility(View.GONE);
            tvNoRoom.setVisibility(View.VISIBLE);
        }
    }

    private void dialogForm() {
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        etName = dialogView.findViewById(R.id.et_name);
        etRoom.setText(null);

        String userName = SharedPrefManager.getUserName(getBaseContext());

        if (userName != null) {
            etName.setText(userName);
        } else {
            etName.setText(null);
        }

        dialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                namaRoom = etRoom.getText().toString().trim();
                displayName = etName.getText().toString().trim();
                SharedPrefManager.setUserName(getBaseContext(), displayName);
                SharedPrefManager.setRoomName(getBaseContext(), namaRoom);
                Bundle bundle = new Bundle();
                bundle.putString("namaRoom", namaRoom);
                bundle.putString("displayName", displayName);
                startActivityBundle(WebViewActivity.class, bundle);
            }
        });

        dialog.setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private void startActivityBundle(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}
