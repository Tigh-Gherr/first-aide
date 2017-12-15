package com.firstadie.csftcarroll.b00641329.firstaide.ui.LoginActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firstadie.csftcarroll.b00641329.firstaide.OnEndpointQueryCompleteListener;
import com.firstadie.csftcarroll.b00641329.firstaide.OnUserConfirmedListener;
import com.firstadie.csftcarroll.b00641329.firstaide.PostAsyncTask;
import com.firstadie.csftcarroll.b00641329.firstaide.R;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.EncryptionUtils;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.ValidationUtils;
import com.firstadie.csftcarroll.b00641329.firstaide.utils.TextFormatUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tigh on 03/11/17.
 */

public class CreateAccountDialog extends AppCompatDialogFragment {

    public static final String KEY_FRAGMENT = CreateAccountDialog.class.getName();

    public static final String KEY_EMAIL = KEY_FRAGMENT + ".EMAIL";

    public static final String KEY_PASSWORD = KEY_FRAGMENT + ".PASSWORD";

    private View mDialogContentView;

    private LinearLayout mCreateAccountFormLinearLayout;
    private AppCompatEditText mFirstNameEditText;
    private AppCompatEditText mSurnameEditText;
    private AppCompatEditText mEmailEditText;
    private AppCompatEditText mPasswordEditText;
    private AppCompatEditText mConfirmPasswordEditText;

    private LinearLayout mCreateAccountProgressLinearLayout;
    private ProgressBar mCreateAccountProgressBar;
    private AppCompatTextView mCreateAccountStatusTextView;

    private OnUserConfirmedListener mListener;

    public static CreateAccountDialog newInstance(String email, String password) {
        Bundle args = new Bundle();
        args.putString(KEY_EMAIL, email);
        args.putString(KEY_PASSWORD, password);

        CreateAccountDialog fragment = new CreateAccountDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setUserConfirmedListener(OnUserConfirmedListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        mDialogContentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_account, null);

        mCreateAccountFormLinearLayout = mDialogContentView.findViewById(R.id.linearlayout_createAccountForm);
        mFirstNameEditText = mDialogContentView.findViewById(R.id.edittext_firstName);
        mSurnameEditText = mDialogContentView.findViewById(R.id.edittext_surname);
        mEmailEditText = mDialogContentView.findViewById(R.id.edittext_email);
        mPasswordEditText = mDialogContentView.findViewById(R.id.edittext_password);
        mConfirmPasswordEditText = mDialogContentView.findViewById(R.id.edittext_confirmPassword);

        mEmailEditText.setText(getArguments().getString(KEY_EMAIL));
        mPasswordEditText.setText(getArguments().getString(KEY_PASSWORD));

        mConfirmPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    AlertDialog dialog = (AlertDialog) getDialog();
                    if (dialog != null) {
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).callOnClick();
                    }
                    return true;
                }
                return false;
            }
        });

        mCreateAccountProgressLinearLayout = mDialogContentView.findViewById(R.id.linearlayout_createAccountProgress);
        mCreateAccountProgressBar = mDialogContentView.findViewById(R.id.progressbar_createAccount);
        mCreateAccountStatusTextView = mDialogContentView.findViewById(R.id.textview_createAccountStatus);

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
        if (verifyFields()) {
            displayView(true);
            final String firstName = mFirstNameEditText.getText().toString().trim();
            final String surname = mSurnameEditText.getText().toString().trim();
            final String email = mEmailEditText.getText().toString().trim().toLowerCase();
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
            task.setEndpointQueryCompleteListener(new OnEndpointQueryCompleteListener() {
                @Override
                public void onQueryComplete(String result) throws JSONException {
                    Log.d(CreateAccountDialog.class.getSimpleName(), result);
                    JSONObject jsonResult = new JSONObject(result);

                    if (jsonResult.getString("status").equals("SUCCESS")) {
                        mListener.onConfirmed(jsonResult.getString("message"), email);
                        getDialog().dismiss();
                    } else {
                        mCreateAccountProgressBar.animate().setDuration(500).alpha(0f);
                        mCreateAccountStatusTextView.setText(jsonResult.getString("message"));

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                displayView(false);
                            }
                        }, 2500);
                    }
                }
            });
            task.execute(TextFormatUtils.databaseUrlFor("create_account.php"), json.toString());
        }
    }

    private void displayView(boolean isAccountBeingCreated) {
        if(isAccountBeingCreated) {
            mCreateAccountProgressBar.setAlpha(1f);
            mCreateAccountFormLinearLayout.animate().alpha(0f).setDuration(200)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            mCreateAccountFormLinearLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });


            mCreateAccountProgressLinearLayout.animate().alpha(1f).setDuration(200)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            AlertDialog dialog = (AlertDialog) getDialog();
                            if(dialog != null) {
                                Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                                positiveButton.setEnabled(false);
                            }
                            mCreateAccountProgressLinearLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override public void onAnimationRepeat(Animator animator) { }
                    });

            mCreateAccountStatusTextView.setText("Creating Account...");
        } else {
            mCreateAccountFormLinearLayout.animate().alpha(1f).setDuration(200)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            mCreateAccountFormLinearLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });


            mCreateAccountProgressLinearLayout.animate().alpha(0f).setDuration(200)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            AlertDialog dialog = (AlertDialog) getDialog();
                            if(dialog != null) {
                                Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                                positiveButton.setEnabled(true);
                            }
                            mCreateAccountProgressLinearLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
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

        for (AppCompatEditText field : fields) {
            field.setError(null);

            if (isEmpty(field)) {
                isValid = false;
                field.setError(emptyField);

                if (errorView == null) {
                    errorView = field;
                }
            }
        }

        // Preliminary check for empty fields
        if (errorView != null) {
            errorView.requestFocus();
            return isValid;
        }

        if (!ValidationUtils.isValidEmail(mEmailEditText.getText().toString().trim())) {
            mEmailEditText.setError("Invalid Email");
            errorView = mEmailEditText;
            isValid = false;
        }

        String password = mPasswordEditText.getText().toString();
        String confirmPassword = mConfirmPasswordEditText.getText().toString();

        if (!password.equals(confirmPassword)) {
            mPasswordEditText.setError("Passwords do not match.");
            mConfirmPasswordEditText.setError("Passwords do not match.");

            if (errorView == null) {
                errorView = mPasswordEditText;
            }

            isValid = false;
        }

        if (errorView != null) {
            errorView.requestFocus();
        }

        return isValid;
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
