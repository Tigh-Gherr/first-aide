package com.firstadie.csftcarroll.b00641329.firstaide.ui.LoginActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firstadie.csftcarroll.b00641329.firstaide.OnEndpointQueryCompleteListener;
import com.firstadie.csftcarroll.b00641329.firstaide.OnUserConfirmedListener;
import com.firstadie.csftcarroll.b00641329.firstaide.PostAsyncTask;
import com.firstadie.csftcarroll.b00641329.firstaide.R;
import com.firstadie.csftcarroll.b00641329.firstaide.location.LocationHelper;
import com.firstadie.csftcarroll.b00641329.firstaide.location.LocationSingleton;
import com.firstadie.csftcarroll.b00641329.firstaide.ui.TimelineActivity.TimelineActivity;
import com.firstadie.csftcarroll.b00641329.firstaide.User;
import com.firstadie.csftcarroll.b00641329.firstaide.UserSingleton;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.EncryptionUtils;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.ValidationUtils;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.NetworkUtils;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.TextFormatUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {

    public static final int PERMISSION_REQUEST_CODE = 0;

    private AppCompatEditText mEmailEditText;
    private AppCompatEditText mPasswordEditText;
    private AppCompatButton mSignInButton;
    private AppCompatButton mCreateAccountButton;

    private ProgressBar mSignInProgressBar;

    public LoginActivityFragment() {
    }

    private void attemptLogin() {
        String email = mEmailEditText.getText().toString().trim().toLowerCase();
        String password = mPasswordEditText.getText().toString();

        if (!ValidationUtils.isValidEmail(email)) {
            mEmailEditText.setError("Email address is not valid.");
        }

        if (!ValidationUtils.isValidPassword(password)) {
            mPasswordEditText.setError("Password does not meet criteria.");
        }

        if (mEmailEditText.getError() != null) {
            mEmailEditText.requestFocus();
            return;
        }

        if (mPasswordEditText.getError() != null) {
            mPasswordEditText.requestFocus();
            return;
        }

        String hashedAndSaltedPassword = EncryptionUtils.hashAndSalt(password, email);

        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", hashedAndSaltedPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSignInProgressBar.setVisibility(View.VISIBLE);
        mSignInProgressBar.animate().setDuration(50).alpha(1);


        PostAsyncTask task = new PostAsyncTask();
        task.setEndpointQueryCompleteListener(new OnEndpointQueryCompleteListener() {
            @Override
            public void onQueryComplete(String result) throws JSONException {
                mSignInProgressBar.animate().setDuration(50).alpha(0);
                mSignInProgressBar.setVisibility(View.INVISIBLE);

                if(result == null) {
                    Snackbar.make(
                            getView(),
                            "An error occured. Login returned no result",
                            Snackbar.LENGTH_SHORT).show();

                    return;
                }

                Log.i(getClass().getSimpleName(), "Result is null");

                JSONObject jsonResult = new JSONObject(result);
                Snackbar.make(getView(), jsonResult.getString("message"), Snackbar.LENGTH_SHORT).show();

                if (jsonResult.getString("status").equals("SUCCESS")) {
                    UserSingleton.get().setUser(User.buildFromJSON(jsonResult.getJSONObject("user")));
                    startActivity(new Intent(getActivity(), TimelineActivity.class));
                }
            }
        });
        task.execute(TextFormatUtils.databaseUrlFor("login.php"), json.toString());
    }

    private void showAccountCreationDialog(String email, String password) {
        CreateAccountDialog dialog = CreateAccountDialog.newInstance(email, password);
        dialog.setUserConfirmedListener(new OnUserConfirmedListener() {
            @Override
            public void onConfirmed(String message, String email) {
                Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
                mPasswordEditText.setText("");
                mEmailEditText.setText(email);
            }
        });

        dialog.show(getActivity().getSupportFragmentManager(), CreateAccountDialog.KEY_FRAGMENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mEmailEditText = view.findViewById(R.id.edittext_email);
        mPasswordEditText = view.findViewById(R.id.edittext_password);
        mPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    mSignInButton.callOnClick();
                    return true;
                }
                return false;
            }
        });

        mCreateAccountButton = view.findViewById(R.id.button_createAccount);
        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    String email = mEmailEditText.getText().toString().trim();
                    String password = mPasswordEditText.getText().toString();
                    showAccountCreationDialog(email, password);
                } else {
                    Snackbar.make(getView(), "Internet connection required.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        mSignInButton = view.findViewById(R.id.button_signIn);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    attemptLogin();
                } else {
                    Snackbar.make(getView(), "Internet connection required.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        mSignInProgressBar = view.findViewById(R.id.progressbar_signIn);

        permissionCheck();

    }

    private void permissionCheck() {
        String[] requiredPermissions = new String[]{
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.VIBRATE
        };

        ArrayList<String> forbiddenPermissions = new ArrayList<>();
        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                forbiddenPermissions.add(permission);
            }
        }

        if (forbiddenPermissions.size() > 0) {
            requestPermissions(
                    forbiddenPermissions.toArray(new String[forbiddenPermissions.size()]),
                    PERMISSION_REQUEST_CODE
            );
        } else {
            LocationSingleton.get().setLocationHelper(new LocationHelper(getActivity()));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean allGranted = true;
                    for (int result : grantResults) {
                        allGranted &= result == PackageManager.PERMISSION_GRANTED;
                    }

                    if(!allGranted) {
                        Snackbar.make(getView(), "All permissions are needed.", Snackbar.LENGTH_SHORT).show();
                        mSignInButton.setEnabled(false);
                        mCreateAccountButton.setEnabled(false);
                    } else {
                        LocationSingleton.get().setLocationHelper(new LocationHelper(getActivity()));
                    }
                } else {
                    // Something something
                    mSignInButton.setEnabled(false);
                    mCreateAccountButton.setEnabled(false);
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPasswordEditText.setText("");
        mEmailEditText.requestFocus();
    }
}
