package com.example.myapplication;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

public class SetPasswordFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_password, container, false);
        EditText pswd, cnfpswd;
        pswd = view.findViewById(R.id.psdwET);
        cnfpswd = view.findViewById(R.id.cnfPsdwET);
        Button replaceNext , replacePrev;
        replaceNext = view.findViewById(R.id.pswdNext);
        replacePrev = view.findViewById(R.id.pswdPrev);

        replaceNext.setOnClickListener(view12 -> {
            isEmpty(pswd, cnfpswd);
        });

        replacePrev.setOnClickListener(view1 -> replacePrev(new OTPFragment()));

        return view;
    }

    private void replacePrev(Fragment fragment)
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

    private void replaceNext(){

    }

    private boolean isEmpty(EditText pswdET, EditText cnfpswdET) {
        String etpswd = pswdET.getText().toString();
        String etcnfpswd = cnfpswdET.getText().toString();

        if(etpswd.trim().length()>0 && etcnfpswd.trim().length()>0){
            Toast.makeText(getContext(), "Successfull Registration", Toast.LENGTH_SHORT).show();

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