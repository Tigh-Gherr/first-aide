package com.firstadie.csftcarroll.b00641329.firstaide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firstadie.csftcarroll.b00641329.firstaide.utils.EncryptionUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {

    private AppCompatEditText mEmailEditText;
    private AppCompatEditText mPasswordEditText;
    private AppCompatButton mSignInButton;
    private AppCompatButton mCreateAccountButton;

    private ProgressBar mSignInProgressBar;

    public LoginActivityFragment() {
    }

    private void attemptLogin() {
        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString();

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
        task.setOnPostCompleteListener(new OnPostCompleteListener() {
            @Override
            public void onPostComplete(String result) throws JSONException {
                Log.i(getClass().getSimpleName(), result);

                mSignInProgressBar.animate().setDuration(50).alpha(0);
                mSignInProgressBar.setVisibility(View.INVISIBLE);

                JSONObject jsonResult = new JSONObject(result);
                Snackbar.make(getView(), jsonResult.getString("message"), Snackbar.LENGTH_SHORT).show();

                if(jsonResult.getString("status").equals("SUCCESS")) {
                    UserSingleton.get(getActivity()).setUser(User.buildFromJSON(jsonResult.getJSONObject("user")));
                    startActivity(new Intent(getActivity(), TimelineActivity.class));
                }
            }
        });
        task.execute("https://uniprojects.000webhostapp.com/login.php", json.toString());
    }

    private void showAccountCreationDialog() {
        CreateAccountDialog dialog = CreateAccountDialog.newInstance();
        dialog.setUserConfirmedListener(new OnUserConfirmedListener() {
            @Override
            public void onConfirmed(String message, String email) {
                Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
                mPasswordEditText.setText("");
                mEmailEditText.setText(email);
            }
        });

        dialog.show(getActivity().getSupportFragmentManager(), "NEWUSER");
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
        mPasswordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
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
                showAccountCreationDialog();
            }
        });

        mSignInButton = view.findViewById(R.id.button_signIn);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mSignInProgressBar = view.findViewById(R.id.progressbar_signIn);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPasswordEditText.setText("");
        mEmailEditText.requestFocus();
    }
}
