package com.thanhtrung.mobilestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.thanhtrung.mobilestore.models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FormResetPasswordActivity extends AppCompatActivity {
    String email = "";
    String idUser = "";

    String password = "";
    String repassword = "";

    TextView tv_emailer;

    EditText input_pass_formResetPass,input_Repass_formResetPass;

    Button btn_update_pass;
    String tempPass = "";
    String userID;

    DatabaseReference dbUser;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_reset_password);
        Intent intent = getIntent();

        email = intent.getStringExtra("email");

        idUser= intent.getStringExtra("id");

        tv_emailer = findViewById(R.id.tv_emailer);
        tv_emailer.setText(email);

        mAuth = FirebaseAuth.getInstance();


        //password
        input_pass_formResetPass = findViewById(R.id.input_pass_formResetPass);
        input_pass_formResetPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    password = editable.toString();
            }
        });

        //repassword
        input_Repass_formResetPass = findViewById(R.id.input_Repass_formResetPass);
        input_pass_formResetPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                repassword = editable.toString();
            }
        });



        //btn update
        btn_update_pass = findViewById(R.id.btn_update_pass);
        btn_update_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(FormResetPasswordActivity.this, "Password is Empty!", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(repassword)){
                    Toast.makeText(FormResetPasswordActivity.this, "Re-type Password is Empty!", Toast.LENGTH_SHORT).show();

                }
                else if(password.length() < 6 || repassword.length()<6){
                    Toast.makeText(FormResetPasswordActivity.this, "Password and Re-Password length must be greater then 6 letter ", Toast.LENGTH_SHORT).show();

                }
                else if(!password.equals(repassword) && !password.equals("") && !repassword.equals("")){
                    Toast.makeText(FormResetPasswordActivity.this, "Password and Re-Password not matched! Try again!", Toast.LENGTH_SHORT).show();
                }else{
                    try {

                        dbUser = FirebaseDatabase.getInstance().getReference();
                            dbUser.child("User").child(idUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.d("firebase error", String.valueOf(task.getResult().getValue()));
                                    }
                                    else {
                                        try {
                                            UserModel userModel = task.getResult().getValue(UserModel.class);
                                            Log.d("firebase succ", userModel.getPassword());
                                            tempPass = userModel.getPassword();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            });
//
                            mAuth.signInWithEmailAndPassword(email,tempPass)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
//                                            Toast.makeText(FormResetPasswordActivity.this, "Login Successful" + tempPass, Toast.LENGTH_SHORT).show();
                                            }
                                            else{
//                                            Toast.makeText(FormResetPasswordActivity.this, "Error!"+ task.getException(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            currentUser.updatePassword(password)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                dbUser.child("User").child(idUser).child("password").setValue(password);
                                                Toast.makeText(FormResetPasswordActivity.this, "Password updated! Auto switch form login now!", Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                                startActivity(new Intent(FormResetPasswordActivity.this,LoginActivity.class));
                                            }
                                        }
                                    });
//                        Toast.makeText(FormResetPasswordActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                    }catch(Exception e){
                    }
                }

            }
        });
    }
}