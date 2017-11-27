package com.firstadie.csftcarroll.b00641329.firstaide.ui.SettingsActivity;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.firstadie.csftcarroll.b00641329.firstaide.R;
import com.firstadie.csftcarroll.b00641329.firstaide.UserSingleton;
import com.firstadie.csftcarroll.b00641329.firstaide.events.UserHobby;

import java.util.List;

/**
 * Created by tigh on 26/11/17.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {

    public interface OnHobbySelectedListener {
        void onHobbySelected(UserHobby userHobby);
    }

    private List<UserHobby> mUserHobbies;
    private OnHobbySelectedListener mOnHobbySelectedListener;

    public SettingsAdapter(List<UserHobby> userHobbies) {
        mUserHobbies = userHobbies;
    }

    public void setOnHobbySelectedListener(OnHobbySelectedListener onHobbySelectedListener) {
        mOnHobbySelectedListener = onHobbySelectedListener;
    }

    public void appendHobby(UserHobby userHobby) {
        mUserHobbies.add(userHobby);
        super.notifyItemInserted(mUserHobbies.size());
    }


    public void notifyItemChanged(UserHobby userHobby) {
        super.notifyItemChanged(mUserHobbies.indexOf(userHobby));
    }

    @Override
    public SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_user_hobby_setting, parent, false);

        return new SettingsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SettingsViewHolder holder, int position) {
        final UserHobby hobby = mUserHobbies.get(position);

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnHobbySelectedListener.onHobbySelected(hobby);
            }
        });
        holder.mTitleTextView.setText(hobby.getTitle());
        holder.mDurationTextView.setText(hobby.getDuration() + " minutes");
        holder.mPriorityTextView.setText("Priority: " + hobby.getPriority());
    }

    @Override
    public int getItemCount() {
        return mUserHobbies != null ? mUserHobbies.size() : 0;
    }

    public class SettingsViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mRelativeLayout;
        AppCompatTextView mTitleTextView;
        AppCompatTextView mDurationTextView;
        AppCompatTextView mPriorityTextView;

        public SettingsViewHolder(View itemView) {
            super(itemView);

            mRelativeLayout = itemView.findViewById(R.id.recyclerview_userHobby);
            mTitleTextView = itemView.findViewById(R.id.textview_userHobbyTitle);
            mDurationTextView = itemView.findViewById(R.id.textview_userHobbyDuration);
            mPriorityTextView = itemView.findViewById(R.id.textview_userHobbyPriority);
        }
    }
}
