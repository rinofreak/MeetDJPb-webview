package com.djpb.sdm.meetdjpbbeta.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.djpb.sdm.meetdjpbbeta.R;
import com.djpb.sdm.meetdjpbbeta.fragment.CreateRoomFragment;
import com.djpb.sdm.meetdjpbbeta.fragment.JoinRoomFragment;
import com.djpb.sdm.meetdjpbbeta.utils.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContainerFragment extends AppCompatActivity {

    @BindView(R.id.btnBackRoom)
    ImageView btnBackRoom;
    @BindView(R.id.tv_menu_name)
    TextView tvMenuName;
    @BindView(R.id.showRoom)
    FrameLayout showRoom;
    MainActivity mainActivity;

    private String fragmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_fragment);
        ButterKnife.bind(this);
        
        initData();
        initFragment();

        btnBackRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Tools.setSystemBarColorActivity(this, R.color.white);
        Tools.setSystemBarLightActivity(this);
    }

    private void initData() {
        Bundle b = getIntent().getExtras();
        fragmentName = b.getString("fragmentName");
    }

    private void initFragment() {

        Fragment fragment = null;

        if(fragmentName.equals("joinroom")) {
            tvMenuName.setText("Gabung ke Room");
            fragment = new JoinRoomFragment();
        } else if(fragmentName.equals("createroom")) {
            tvMenuName.setText("Buat Room");
            fragment = new CreateRoomFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.showRoom, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
