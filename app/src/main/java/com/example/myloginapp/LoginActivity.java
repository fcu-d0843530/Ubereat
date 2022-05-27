package com.example.myloginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
        implements OnCompleteListener<AuthResult> {

    private EditText etEmail;//abc@gmail.com
    private EditText etPassword;//123456
    FirebaseAuth firebaseAuth;
    Intent intent;
    String role;
    TextView tv_role;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getPreferences (MODE_PRIVATE);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        firebaseAuth = FirebaseAuth.getInstance();
        intent = getIntent();
        role = intent.getStringExtra("Role");
        tv_role = findViewById(R.id.tv_role);
        tv_role.setText(role);
    }

    public void onLogin(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, this);
    }

    public void onRegister(View view) {
        Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            Toast.makeText(this, "登入成功", Toast.LENGTH_LONG).show();
            MainActivity.VALID_USER = true;
            Intent intent = new Intent();
            if(role.equals("customer")){
                intent.setClass(this, MainActivity.class);
            }
            else if (role.equals("store")){ //!未改
                intent.setClass(this, StoreProductActivity.class);
            }
            else{ //deliver !未改
                intent.setClass(this, MainActivity.class);
            }
            //intent.setClass(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "登入失敗", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("EMAIL", email);
        editor.putString("PASSWORD", password);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String email = preferences.getString("EMAIL", "");
        String password = preferences.getString("PASSWORD", "");
        etEmail.setText (email);
        etPassword.setText(password);

    }
}