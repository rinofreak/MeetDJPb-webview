package com.djpb.sdm.meetdjpbbeta.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.djpb.sdm.meetdjpbbeta.R;
import com.djpb.sdm.meetdjpbbeta.utils.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.btnBackSetting)
    ImageView btnBackSetting;
    @BindView(R.id.et_nama_user)
    EditText etNamaUser;
    @BindView(R.id.ll_setting_camera)
    LinearLayout llSettingCamera;
    @BindView(R.id.ll_setting_microphone)
    LinearLayout llSettingMicrophone;
    @BindView(R.id.sc_hd_mode)
    SwitchCompat scHdMode;
    @BindView(R.id.sc_room_baru)
    SwitchCompat scRoomBaru;
    @BindView(R.id.ll_setting_more)
    LinearLayout llSettingMore;
    @BindView(R.id.ll_faq)
    LinearLayout llFaq;
    @BindView(R.id.ll_privacy_policy)
    LinearLayout llPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        btnBackSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Tools.setSystemBarColorActivity(this, R.color.white);
        Tools.setSystemBarLightActivity(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
