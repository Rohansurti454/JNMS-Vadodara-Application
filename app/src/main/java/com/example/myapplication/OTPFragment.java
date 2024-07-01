package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class OTPFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_o_t_p, container, false);

        Button OtpNext , OtpPrev;

        OtpPrev = view.findViewById(R.id.otpPrev);
        OtpNext = view.findViewById(R.id.otpNext);

        EditText otp = view.findViewById(R.id.otpET);

        OtpNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEmpty(otp);
            }
        });


        OtpPrev.setOnClickListener(view12 -> replacePrev(new RegisterFragment()));

        return view;
    }

    private void replaceNext(Fragment fragment)
    {
        FragmentManager fm = getParentFragmentManager();
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = fm.popBackStackImmediate(backStackName, 0);

        if(!fragmentPopped){
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContainerView, SetPasswordFragment.class, null);
            ft.addToBackStack(backStackName)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }

    private void replacePrev(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = fm.popBackStackImmediate(backStackName, 0);

        if (!fragmentPopped) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContainerView, RegisterFragment.class, null);
            ft.addToBackStack(backStackName)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }

    private boolean isEmpty(EditText OTPet) {
        String etOTP = OTPet.getText().toString();

        if(etOTP.trim().length()>0){
            replaceNext(new OTPFragment());

            return true;
        } else {
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.32);

            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.custom_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(width, height);
            dialog.show();

            Button okBtn = dialog.findViewById(R.id.btnOkay);
            okBtn.setOnClickListener(view1 -> dialog.dismiss());

            return false;
        }
    }
}