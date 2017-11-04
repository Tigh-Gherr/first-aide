package com.firstadie.csftcarroll.b00641329.firstaide.ui.TimelineActivity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firstadie.csftcarroll.b00641329.firstaide.R;
import com.firstadie.csftcarroll.b00641329.firstaide.User;
import com.firstadie.csftcarroll.b00641329.firstaide.UserSingleton;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimelineActivityFragment extends Fragment {

    private User mUser;
    private AppCompatTextView mUserInfoTextView;

    public TimelineActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mUser = UserSingleton.get(getActivity()).getUser();

        mUserInfoTextView = view.findViewById(R.id.textview_userInfo);
        mUserInfoTextView.setText(mUser.toJSONString());
    }
}
