package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Button next = view.findViewById(R.id.regNext);

        EditText jangananaET, nameET, phonenoET;
        jangananaET = view.findViewById(R.id.jangananaET);
        nameET = view.findViewById(R.id.nameET);
        phonenoET = view.findViewById(R.id.phnoET);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            isEmpty(jangananaET, nameET, phonenoET);

            }
        });

        return view;
    }

    private void replace(Fragment fragment)
    {
        FragmentManager fm = getParentFragmentManager();
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = fm.popBackStackImmediate(backStackName, 0);

        if(!fragmentPopped){
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContainerView, OTPFragment.class, null);
            ft.addToBackStack(backStackName)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }
    private boolean isEmpty(EditText etText1, EditText etText2, EditText etText3){

        String ETXT1, ETXT2, ETXT3;
        ETXT1 = etText1.getText().toString();
        ETXT2 = etText2.getText().toString();
        ETXT3 = etText3.getText().toString();

        if(ETXT1.trim().length()>0 && ETXT2.trim().length()>0 && ETXT3.trim().length()>0){

            replace(new OTPFragment());

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