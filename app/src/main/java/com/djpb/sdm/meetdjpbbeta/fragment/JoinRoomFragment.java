package com.djpb.sdm.meetdjpbbeta.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.djpb.sdm.meetdjpbbeta.R;
import com.djpb.sdm.meetdjpbbeta.activity.WebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class JoinRoomFragment extends Fragment {

    @BindView(R.id.et_id_join_room)
    EditText etIdJoinRoom;
    @BindView(R.id.et_name_join_room)
    EditText etNameJoinRoom;
    @BindView(R.id.btn_gabung_room)
    Button btnGabungRoom;
    @BindView(R.id.sc_camera_join_room)
    SwitchCompat scCameraJoinRoom;
    @BindView(R.id.sc_mic_join_room)
    SwitchCompat scMicJoinRoom;
    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_join_room, container, false);
        unbinder = ButterKnife.bind(this, view);

        btnGabungRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
