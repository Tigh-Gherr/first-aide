package com.firstadie.csftcarroll.b00641329.firstaide.ui.SettingsActivity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firstadie.csftcarroll.b00641329.firstaide.OnEndpointQueryCompleteListener;
import com.firstadie.csftcarroll.b00641329.firstaide.PostAsyncTask;
import com.firstadie.csftcarroll.b00641329.firstaide.R;
import com.firstadie.csftcarroll.b00641329.firstaide.UserSingleton;
import com.firstadie.csftcarroll.b00641329.firstaide.events.UserHobby;
import com.firstadie.csftcarroll.b00641329.firstaide.ui.TimelineActivity.AccessibleFragment;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.NetworkUtils;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.TextFormatUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsActivityFragment extends Fragment implements AccessibleFragment<UserHobby> {

    private List<UserHobby> mUserHobbies;

    @Override
    public void passData(UserHobby data) {
        if(NetworkUtils.isNetworkAvailable(getActivity())) {
            displaySettingsDialog(data);
        } else {
            Snackbar.make(
                    getView(),
                    "No internet connection. Cannot create new setting.",
                    Snackbar.LENGTH_LONG
            ).show();
        }
    }

    public interface OnSettingConfirmedListener {
        void onSettingConfirmed(Object setting);
    }

    private RecyclerView mSettingsRecyclerView;
    private SettingsAdapter mSettingsAdapter;

    private ItemTouchHelper.SimpleCallback mSettingsRecyclerViewCallBack = new ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
    ) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if(!(direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT)) {
                return;
            }

            int position = viewHolder.getAdapterPosition();
            if(NetworkUtils.isNetworkAvailable(getActivity())) {
                UserHobby toBeDeleted = mUserHobbies.get(position);

                mUserHobbies.remove(position);
                mSettingsAdapter.notifyItemRemoved(position);

                PostAsyncTask task = new PostAsyncTask();
                task.execute(TextFormatUtils.databaseUrlFor("delete_hobby.php"), toBeDeleted.toJSONString());
            } else {
                Snackbar.make(
                        getView(),
                        "No internet connection. Cannot deleted hobby.",
                        Snackbar.LENGTH_LONG
                ).show();

                mSettingsAdapter.notifyItemChanged(position);
            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                float width = viewHolder.itemView.getWidth();
                float fadeBy = (Math.abs(dX) / width) * 2f;
                float alpha = fadeBy <= 1f ? 1f - fadeBy : 0f;
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    };

    public SettingsActivityFragment() {
    }

    private void displaySettingsDialog(UserHobby userHobby) {
        UserHobbySettingsDialog dialog = UserHobbySettingsDialog.newInstance(userHobby);

        dialog.setOnSettingConfirmedListener(new OnSettingConfirmedListener() {
            @Override
            public void onSettingConfirmed(Object setting) {
                if(setting instanceof UserHobby) {
                    if(NetworkUtils.isNetworkAvailable(getActivity())) {
                        postUserHobby((UserHobby) setting);
                    } else {
                        Snackbar.make(
                                getView(),
                                "No internet connection. Cannot post user hobby.",
                                Snackbar.LENGTH_LONG
                        ).show();
                    }
                }
            }
        });

        dialog.show(getActivity().getSupportFragmentManager(), UserHobbySettingsDialog.KEY_FRAGMENT);
    }

    private void postUserHobby(final UserHobby userHobby) {
        Log.d(getClass().getSimpleName(), userHobby.toJSONString());
        PostAsyncTask task = new PostAsyncTask();

        task.setEndpointQueryCompleteListener(new OnEndpointQueryCompleteListener() {
            @Override
            public void onQueryComplete(String result) throws JSONException {
                Log.d(SettingsActivityFragment.class.getSimpleName(), result);

                if(result == null) {
                    Snackbar.make(
                            getView(),
                            "An error occurred.",
                            Snackbar.LENGTH_LONG
                    ).show();

                    return;
                }

                JSONObject jsonResult = new JSONObject(result);

                String hobbyType = jsonResult.getString("hobby_type");
                if(hobbyType.equals("UPDATE")) {
                    mSettingsAdapter.notifyItemChanged(userHobby);
                } else if(hobbyType.equals("NEW")) {
                    int id = jsonResult.getJSONObject("hobby").getInt("id");
                    userHobby.setId(id);
                    mSettingsAdapter.appendHobby(userHobby);
                }
            }
        });

        task.execute(TextFormatUtils.databaseUrlFor("upsert_hobby.php"), userHobby.toJSONString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        mUserHobbies = UserSingleton.get().getUser().getUserHobbies();

        mSettingsRecyclerView = view.findViewById(R.id.recyclerview_settings);
        mSettingsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSettingsAdapter = new SettingsAdapter(mUserHobbies);
        mSettingsAdapter.setOnHobbySelectedListener(new SettingsAdapter.OnHobbySelectedListener() {
            @Override
            public void onHobbySelected(UserHobby userHobby) {
                if(NetworkUtils.isNetworkAvailable(getActivity())) {
                    displaySettingsDialog(userHobby);
                } else {
                    Snackbar.make(
                            getView(),
                            "No internet connection. Cannot edit setting.",
                            Snackbar.LENGTH_LONG
                    ).show();
                }
            }
        });
        mSettingsRecyclerView.setAdapter(mSettingsAdapter);

        ItemTouchHelper touchHelper = new ItemTouchHelper(mSettingsRecyclerViewCallBack);
        touchHelper.attachToRecyclerView(mSettingsRecyclerView);
    }
}
