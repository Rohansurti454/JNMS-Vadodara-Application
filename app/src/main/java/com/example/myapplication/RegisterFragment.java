package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFragment extends Fragment {

    String st1, st2, st3;
    EditText et1jgn, et2name, et3phno;
    Button next;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    OTPFragment otpFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        next = view.findViewById(R.id.regNext);

        et1jgn = view.findViewById(R.id.jangananaET);
        et2name = view.findViewById(R.id.nameET);
        et3phno = view.findViewById(R.id.phnoET);



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                st1 = et1jgn.getText().toString().trim();
                st2 = et2name.getText().toString().trim();
                st3 = et3phno.getText().toString().trim();//phone number edittext

                if(validateRegister(st1, st2, st3)){
                    showConfirmationDialog(st1, st2, st3);
                }
            }
        });

        return view;
    }

    private void showConfirmationDialog(String st1, String st2, String st3) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Details");
        builder.setMessage("Are the details correct?\n\nJanganana: " + st1 + "\nName: " + st2 + "\nPhone Number: " + st3);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            showProgress();
            addValuesToRDB(st1, st2, st3);
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private boolean validateRegister(String st1, String st2, String st3) {
        if (st1.isEmpty() || st2.isEmpty() || st3.isEmpty()) {
            showEmptyCredentialsDialog();
            return false;
        }

        if (!validatePhoneNumber(st3)) {
            showInvalidPhoneNumberDialog();
            return false;
        }

        return true;
    }

    private boolean validatePhoneNumber(String phoneNumber) {
//        return Patterns.PHONE.matcher(phoneNumber).matches();
        return phoneNumber.matches("^\\+91[6-9]\\d{9}$");
    }


    private void showEmptyCredentialsDialog() {

            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.32);

            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.custom_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(width, height);
            dialog.show();

            Button okBtn = dialog.findViewById(R.id.btnOkay);
            okBtn.setOnClickListener(view1 -> dialog.dismiss());

            return;
    }

    private void showInvalidPhoneNumberDialog() {
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.32);

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        // Assuming there is a TextView in custom_dialog layout to show the error message
        TextView messageTextView = dialog.findViewById(R.id.tv_msg);
        messageTextView.setText("Enter a valid phone number");

        Button okBtn = dialog.findViewById(R.id.btnOkay);
        okBtn.setOnClickListener(view -> dialog.dismiss());
    }

    private void showProgress(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void addValuesToRDB(String st1, String st2, String st3){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Janganana");

        Users users = new Users(st1,st2,st3);
        databaseReference.child(st1).setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("phone_number", st3);

                OTPFragment otpFragment = new OTPFragment();
                otpFragment.setArguments(bundle);

                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, otpFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }


    private void replace(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = fm.popBackStackImmediate(backStackName, 0);

        if (!fragmentPopped) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContainerView, OTPFragment.class, null);
            ft.addToBackStack(backStackName)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }
}