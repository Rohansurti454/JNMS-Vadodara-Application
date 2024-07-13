package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SetPasswordFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    EditText pswd, cnfpswd;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_password, container, false);

        pswd = view.findViewById(R.id.psdwET);
        cnfpswd = view.findViewById(R.id.cnfPsdwET);

        Button replaceNext , replacePrev;

        replaceNext = view.findViewById(R.id.pswdNext);
        replacePrev = view.findViewById(R.id.pswdPrev);


        replaceNext.setOnClickListener(view12 -> {

            validatePasswords();
        });

        replacePrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replacePrevMethod(new OTPFragment());

            }
        });

        return view;
    }

    private void replacePrevMethod(Fragment fragment)
    {
        if(getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0){
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            getActivity().finish();
        }
    }

    private void replaceNextMethod(){
        Toast.makeText(getContext(), "Successfull Registration", Toast.LENGTH_SHORT).show();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
    
    private void validatePasswords() {
        String password = pswd.getText().toString().trim();
        String confirmPassword = cnfpswd.getText().toString().trim();

        if (password.isEmpty() || confirmPassword.isEmpty()){
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.32);

            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.custom_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(width, height);
            dialog.show();

            TextView messageTextView = dialog.findViewById(R.id.tv_msg);
            messageTextView.setText("Password can not be blank");

            Button okBtn = dialog.findViewById(R.id.btnOkay);
            okBtn.setOnClickListener(view1 -> dialog.dismiss());

            return;
        }

        if(password.length()!=4 || confirmPassword.length()!=4){
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.32);

            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.custom_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(width, height);
            dialog.show();

            TextView messageTextView = dialog.findViewById(R.id.tv_msg);
            messageTextView.setText("Password should be 4 digit");

            Button okBtn = dialog.findViewById(R.id.btnOkay);
            okBtn.setOnClickListener(view1 -> dialog.dismiss());
            return;
        }

        if (!password.equals(confirmPassword)) {
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.32);

            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.custom_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(width, height);
            dialog.show();

            TextView messageTextView = dialog.findViewById(R.id.tv_msg);
            messageTextView.setText("Password and confirm password do not match");

            Button okBtn = dialog.findViewById(R.id.btnOkay);
            okBtn.setOnClickListener(view1 -> dialog.dismiss());

            return;
        }

        replaceNextMethod();
    }
    
}