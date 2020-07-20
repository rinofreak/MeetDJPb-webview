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
import com.djpb.sdm.meetdjpbbeta.activity.RoomActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CreateRoomFragment extends Fragment {

    @BindView(R.id.et_id_new_room)
    EditText etIdNewRoom;
    @BindView(R.id.et_name_new_room)
    EditText etNameNewRoom;
    @BindView(R.id.sc_camera_new_room)
    SwitchCompat scCameraNewRoom;
    @BindView(R.id.sc_mic_new_room)
    SwitchCompat scMicNewRoom;
    @BindView(R.id.btn_buat_room)
    Button btnBuatRoom;
    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_room, container, false);
        unbinder = ButterKnife.bind(this, view);

        btnBuatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RoomActivity.class);
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
