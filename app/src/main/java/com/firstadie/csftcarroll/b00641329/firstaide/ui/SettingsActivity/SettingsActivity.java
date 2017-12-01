package com.firstadie.csftcarroll.b00641329.firstaide.ui.SettingsActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firstadie.csftcarroll.b00641329.firstaide.R;
import com.firstadie.csftcarroll.b00641329.firstaide.events.UserHobby;
import com.firstadie.csftcarroll.b00641329.firstaide.ui.TimelineActivity.AccessibleFragment;

public class SettingsActivity extends AppCompatActivity {

    private AccessibleFragment<UserHobby> mAccessibleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if(fragment instanceof SettingsActivityFragment) {
            mAccessibleFragment = (AccessibleFragment<UserHobby>) fragment;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_hobby:
                UserHobby newHobby = new UserHobby();
                mAccessibleFragment.passData(newHobby);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
