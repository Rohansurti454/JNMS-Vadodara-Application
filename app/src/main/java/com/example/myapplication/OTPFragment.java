package com.example.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class OTPFragment extends Fragment {

    private EditText otpEditText;
    private static final String KEY_OTP = "key_otp";
    private TextView plsWaitTV, timerTV, phoneNumberTV;
    private ProgressDialog progressDialog;
    private Button OtpNext, OtpPrev, otpBtn;
    private String phoneNumber;
    private String verificationId;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_o_t_p, container, false);


        OtpPrev = view.findViewById(R.id.otpPrev);
        OtpNext = view.findViewById(R.id.otpNext);
        otpBtn = view.findViewById(R.id.otpBtn);
        otpEditText = view.findViewById(R.id.otpET);
        phoneNumberTV = view.findViewById(R.id.phoneNumberTV);
        plsWaitTV = view.findViewById(R.id.plsWaitTV);

        timerTV = new TextView(getContext());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.plsWaitTV);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        timerTV.setLayoutParams(params);
        ((RelativeLayout) view.findViewById(R.id.rlCrd)).addView(timerTV);


        if (getArguments() != null) {
            phoneNumber = getArguments().getString("phone_number");
            if (!TextUtils.isEmpty(phoneNumber)) {
                phoneNumberTV.setText("OTP sent to " + phoneNumber);
            } else {
                Toast.makeText(getContext(), "Phone number is empty", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        } else {
            Toast.makeText(getContext(), "Phone number is null", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }


        otpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(phoneNumber)) {
                    showProgressDialog();
                    sendVerificationCode(phoneNumber);
                } else {
                    Toast.makeText(getContext(), "Phone number is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


        OtpNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = otpEditText.getText().toString().trim();
                if (TextUtils.isEmpty(code) || code.length() < 6) {
                    otpEditText.setError("Enter valid code");
                    otpEditText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });


        OtpPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    getActivity().finish();
                }
            }
        });

        if (savedInstanceState != null) {
            String savedOtp = savedInstanceState.getString(KEY_OTP);
            otpEditText.setText(savedOtp);
        }

        return view;
    }

    private void replaceNext(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = fm.popBackStackImmediate(backStackName, 0);

        if (!fragmentPopped) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContainerView, SetPasswordFragment.class, null);
            ft.addToBackStack(backStackName)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_OTP, otpEditText.getText().toString());
    }


    private boolean isEmpty(EditText OTPet) {
        String etOTP = OTPet.getText().toString();

        if (etOTP.trim().length() > 0) {
            replaceNext(new OTPFragment());

            return true;
        } else {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.32);

            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.custom_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(width, height);
            dialog.show();

            TextView messageTextView = dialog.findViewById(R.id.tv_msg);
            messageTextView.setText("OTP can not be blank");

            Button okBtn = dialog.findViewById(R.id.btnOkay);
            okBtn.setOnClickListener(view1 -> dialog.dismiss());

            return false;
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void startTimer() {
        new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                timerTV.setText(String.format("Time remaining: %02d:%02d", secondsRemaining / 60, secondsRemaining % 60));
            }

            public void onFinish() {
                timerTV.setText("Time's up!");
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }.start();
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(getActivity())
                        .setCallbacks(mCallBack)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "OTP sent successfully", Toast.LENGTH_SHORT).show();
            startTimer();
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                otpEditText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Verification successful", Toast.LENGTH_SHORT).show();
                        // Save OTP and other information to Firebase Realtime Database
                        saveDataToFirebase();
                        // Navigate to next fragment
                        replaceNext(new SetPasswordFragment());
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Verification failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveDataToFirebase() {
        // Example of saving data to Firebase Realtime Database
        String otp = otpEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(phoneNumber)) {
            databaseReference.child(phoneNumber).child("OTP").setValue(otp);
        }
    }

}