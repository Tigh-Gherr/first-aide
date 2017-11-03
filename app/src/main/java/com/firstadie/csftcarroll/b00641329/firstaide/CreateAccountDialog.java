package com.firstadie.csftcarroll.b00641329.firstaide;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.firstadie.csftcarroll.b00641329.firstaide.utils.EncryptionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tigh on 03/11/17.
 */

public class CreateAccountDialog extends AppCompatDialogFragment {

    private View mDialogContentView;

    private AppCompatEditText mFirstNameEditText;
    private AppCompatEditText mSurnameEditText;
    private AppCompatEditText mEmailEditText;
    private AppCompatEditText mPasswordEditText;
    private AppCompatEditText mConfirmPasswordEditText;

    private OnUserConfirmedListener mListener;

    public static CreateAccountDialog newInstance() {
        return new CreateAccountDialog();
    }

    public void setUserConfirmedListener(OnUserConfirmedListener listener) {
        mListener = listener;
    }

    public static final Pattern EMAIL_ADDRESS_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        mDialogContentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_account, null);

        mFirstNameEditText = mDialogContentView.findViewById(R.id.edittext_firstName);
        mSurnameEditText = mDialogContentView.findViewById(R.id.edittext_surname);
        mEmailEditText = mDialogContentView.findViewById(R.id.edittext_email);
        mPasswordEditText = mDialogContentView.findViewById(R.id.edittext_password);
        mConfirmPasswordEditText = mDialogContentView.findViewById(R.id.edittext_confirmPassword);

        builder.setView(mDialogContentView).setTitle("Create Account")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // See `void onStart` for this buttons functionality.
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

    private void createUser() {
        if(verifyFields()) {
            final String firstName = mFirstNameEditText.getText().toString().trim();
            final String surname = mSurnameEditText.getText().toString().trim();
            final String email = mEmailEditText.getText().toString().trim();
            final String password = mPasswordEditText.getText().toString().trim();

            String hashedAndSaltedPassword = EncryptionUtils.hashAndSalt(password, email);

            final JSONObject json = new JSONObject();

            try {
                json.put("first_name", firstName);
                json.put("surname", surname);
                json.put("email", email);
                json.put("password", hashedAndSaltedPassword);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            PostAsyncTask task = new PostAsyncTask();
            task.setOnPostCompleteListener(new OnPostCompleteListener() {
                @Override
                public void onPostComplete(String result) throws JSONException {
                    Log.d(CreateAccountDialog.class.getSimpleName(), result);
                    JSONObject jsonResult = new JSONObject(result);

                    if(jsonResult.getString("status").equals("SUCCESS")) {
                        mListener.onConfirmed(jsonResult.getString("message"), email);
                        getDialog().dismiss();
                    } else {
                        Toast.makeText(getActivity(), jsonResult.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            task.execute("https://uniprojects.000webhostapp.com/create_account.php", json.toString());
        }
    }

    private boolean verifyFields() {
        String emptyField = "Field cannot be left empty";
        boolean isValid = true;
        View errorView = null;

        AppCompatEditText[] fields = new AppCompatEditText[]{
            mFirstNameEditText,
            mSurnameEditText,
            mEmailEditText,
            mPasswordEditText,
            mConfirmPasswordEditText
        };

        for(AppCompatEditText field : fields) {
            field.setError(null);
        }

        for(AppCompatEditText field : fields) {
            if(isEmpty(field)) {
                isValid = false;
                field.setError(emptyField);

                if(errorView == null) {
                    errorView = field;
                }
            }
        }

        // Preliminary check for empty fields
        if(errorView != null) {
            errorView.requestFocus();
            return isValid;
        }

        if(!isValidEmail(mEmailEditText)) {
            mEmailEditText.setError("Invalid Email");
            errorView = mEmailEditText;
            isValid = false;
        }

        String password = mPasswordEditText.getText().toString();
        String confirmPassword = mConfirmPasswordEditText.getText().toString();

        if(!password.equals(confirmPassword)) {
            mPasswordEditText.setError("Passwords do not match.");
            mConfirmPasswordEditText.setError("Passwords do not match.");

            if(errorView == null) {
                errorView = mPasswordEditText;
            }

            isValid = false;
        }

        if(errorView != null) {
            errorView.requestFocus();
        }

        return isValid;
    }

    private boolean isValidEmail(AppCompatEditText emailEditText) {
        Matcher matcher = EMAIL_ADDRESS_PATTERN.matcher(emailEditText.getText().toString().trim());
        return matcher.find();
    }

    private boolean isEmpty(AppCompatEditText et) {
        return et.getText().length() == 0;
    }

    @Override
    public void onStart() {
        super.onStart();

        final AlertDialog dialog = (AlertDialog) getDialog();

        if (dialog != null) {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createUser();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mFirstNameEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 50);
    }
}
