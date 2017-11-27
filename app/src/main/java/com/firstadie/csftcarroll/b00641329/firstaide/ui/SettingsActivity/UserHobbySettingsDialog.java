package com.firstadie.csftcarroll.b00641329.firstaide.ui.SettingsActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;

import com.firstadie.csftcarroll.b00641329.firstaide.R;
import com.firstadie.csftcarroll.b00641329.firstaide.events.UserHobby;

import java.io.Serializable;

/**
 * Created by tigh on 26/11/17.
 */

public class UserHobbySettingsDialog extends AppCompatDialogFragment {

    public final static String KEY_FRAGMENT = UserHobbySettingsDialog.class.getName();

    public final static String KEY_USER_HOBBY = KEY_FRAGMENT + ".USERHOBBY";

    private View mDialogContentView;

    private AppCompatEditText mTitleEditText;
    private AppCompatEditText mDurationEditText;
    private AppCompatEditText mPriorityEditText;

    private UserHobby mUserHobby;

    private SettingsActivityFragment.OnSettingConfirmedListener mOnSettingConfirmedListener;

    public static UserHobbySettingsDialog newInstance(Serializable userHobby) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_USER_HOBBY, userHobby);

        UserHobbySettingsDialog fragment = new UserHobbySettingsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnSettingConfirmedListener(SettingsActivityFragment.OnSettingConfirmedListener onSettingConfirmedListener) {
        mOnSettingConfirmedListener = onSettingConfirmedListener;
    }

    private void saveSetting() {
        View errorView = null;

        AppCompatEditText[] fields = {
                mTitleEditText,
                mDurationEditText,
                mPriorityEditText
        };

        for(AppCompatEditText field : fields) {
            field.setError(null);

            if(field.getText().toString().trim().isEmpty()) {
                field.setError("Field cannot be left blank.");

                if(errorView == null) {
                    errorView = field;
                }
            }
        }

        if(errorView != null) {
            errorView.requestFocus();
            return;
        }

        mUserHobby.setTitle(mTitleEditText.getText().toString().trim());
        mUserHobby.setDuration(Integer.parseInt(mDurationEditText.getText().toString()));
        mUserHobby.setPriority(Integer.parseInt(mPriorityEditText.getText().toString()));

        if(mOnSettingConfirmedListener != null) {
            mOnSettingConfirmedListener.onSettingConfirmed(mUserHobby);
        }

        dismiss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mUserHobby = (UserHobby) getArguments().getSerializable(KEY_USER_HOBBY);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        mDialogContentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_userhobby, null);

        mTitleEditText = mDialogContentView.findViewById(R.id.edittext_userHobbyTitle);
        mDurationEditText = mDialogContentView.findViewById(R.id.edittext_userHobbyDuration);
        mPriorityEditText = mDialogContentView.findViewById(R.id.edittext_userHobbyPriority);

        if (mUserHobby != null) {
            mTitleEditText.setText(mUserHobby.getTitle());
            mDurationEditText.setText(Integer.toString(mUserHobby.getDuration()));
            mPriorityEditText.setText(Integer.toString(mUserHobby.getPriority()));
        }

        builder.setView(mDialogContentView).setTitle("User Hobby")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Blah
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        AlertDialog dialog = (AlertDialog) getDialog();

        if (dialog != null) {
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveSetting();
                }
            });
        }
    }
}
