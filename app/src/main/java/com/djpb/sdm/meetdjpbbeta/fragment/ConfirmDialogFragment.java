package com.djpb.sdm.meetdjpbbeta.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.text.TextUtils;

import com.djpb.sdm.meetdjpbbeta.R;

public class ConfirmDialogFragment extends DialogFragment {

    private static final String ARG_RESOURCES = "resources";

    public static ConfirmDialogFragment newInstance(String[] resources) {
        ConfirmDialogFragment fragment = new ConfirmDialogFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_RESOURCES, resources);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] resources = getArguments().getStringArray(ARG_RESOURCES);
        return new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.confirmation, TextUtils.join("\n", resources)))
                .setNegativeButton(R.string.deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Listener) getParentFragment()).onConfirmation(false, resources);
                    }
                })
                .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Listener) getParentFragment()).onConfirmation(true, resources);
                    }
                })
                .create();
    }


    interface Listener {
        void onConfirmation(boolean allowed, String[] resources);
    }
}
