package com.example.myapplication;

import static com.example.myapplication.R.*;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    EditText jngnNo, pswdNo;
    Button btnLogin;
    TextView newRegister;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        jngnNo = findViewById(R.id.userNameET);
        pswdNo = findViewById(id.passwaordET);

        btnLogin = findViewById(id.btn_login);

        newRegister = findViewById(R.id.newRegistrationTV);

        newRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateLogin();

            }
        });

    }

    private void validateLogin(){

        String jngn = jngnNo.getText().toString().trim();
        String pswd = pswdNo.getText().toString().trim();

        if (jngn.isEmpty())
        {
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.32);

            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(layout.custom_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(width, height);
            dialog.show();

            TextView messageTextView = dialog.findViewById(R.id.tv_msg);
            messageTextView.setText("Janganana can not be blank");

            Button okBtn = dialog.findViewById(R.id.btnOkay);
            okBtn.setOnClickListener(view1 -> dialog.dismiss());

            return;
        }
        else if(pswd.isEmpty())
        {
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.32);

            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(layout.custom_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(width, height);
            dialog.show();

            TextView messageTextView = dialog.findViewById(R.id.tv_msg);
            messageTextView.setText("Password can not be blank");

            Button okBtn = dialog.findViewById(R.id.btnOkay);
            okBtn.setOnClickListener(view1 -> dialog.dismiss());

            return;
        } loginAction();

    }

    private void loginAction(){

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }


}