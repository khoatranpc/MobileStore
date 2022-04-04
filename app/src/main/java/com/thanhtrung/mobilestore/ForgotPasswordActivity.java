package com.thanhtrung.mobilestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.thanhtrung.mobilestore.models.MailerModel;
import com.thanhtrung.mobilestore.models.ViewAllModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextView tv_loginNow;
    EditText input_email_forgotPass, OTP;
    Button btn_sendOTP;
    String email = "";
    String OTPParse = "";
    FirebaseFirestore db;
    String emailSend="";
    String OTPSend = "";
    String doc = "";
    String idDoc = "";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        db =  FirebaseFirestore.getInstance();
        //edittext Email
        input_email_forgotPass = findViewById(R.id.input_email_forgotPass);
        input_email_forgotPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                email = editable.toString();
            }
        });



        //edit OTP
        OTP = findViewById(R.id.OTP);
        OTP.setEnabled(false);

        OTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                OTPParse = editable.toString();
            }
        });
        //btn SendOTP
        btn_sendOTP = findViewById(R.id.btn_sendOTP);
        btn_sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(btn_sendOTP.getText().toString().equals("Send OTP")){
                        if(email.equals("")) {
                            Toast.makeText(ForgotPasswordActivity.this, "Email is not allowed to be empty", Toast.LENGTH_SHORT).show();
                        }else {
                            OTP.setEnabled(true);
                            OTP.setHint("Type your OTP in Email address");
                            btn_sendOTP.setText("Verify OTP");
                                db.collection("VerifyOTP").whereEqualTo("email",email).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful() && task.getResult() != null){
                                                    try {
                                                        for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                                                            MailerModel mailerModel = documentSnapshot.toObject(MailerModel.class);
                                                            emailSend = mailerModel.getEmail();
                                                            OTPSend = String.valueOf(mailerModel.getOTP());
                                                            Toast.makeText(ForgotPasswordActivity.this, "We sent otp to " + mailerModel.getEmail(), Toast.LENGTH_SHORT).show();
                                                        }
                                                        //call function sending email
                                                        postDataUsingVolley(emailSend,OTPSend);
                                                    }
                                                    catch (Exception e) {
                                                        Toast.makeText(ForgotPasswordActivity.this, "Something Error", Toast.LENGTH_SHORT).show();
                                                    }
                                                }else {
                                                    doc= "Error";
                                                    Toast.makeText(ForgotPasswordActivity.this, doc, Toast.LENGTH_SHORT).show();
                                                }
                                            }});
                        }
                    }else{
                        if(OTPParse.equals("")) {
                            Toast.makeText(ForgotPasswordActivity.this, "OTP is not allowed to be empty", Toast.LENGTH_SHORT).show();
                        }else {
                            if(isNumeric(OTPParse)){
                                //get OTP
                                db.collection("VerifyOTP").whereEqualTo("email",email).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful() && task.getResult() != null){
                                                    try {
                                                        for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                                                            MailerModel mailerModel = documentSnapshot.toObject(MailerModel.class);
                                                            idDoc = mailerModel.getId();
                                                            if( String.valueOf(mailerModel.getOTP()).equals(OTPParse)){
                                                                Toast.makeText(ForgotPasswordActivity.this, "Verify Successful!", Toast.LENGTH_SHORT).show();
                                                                //Update OTP after verify
                                                                db.collection("VerifyOTP").document(idDoc).update("OTP", (int)(Math.random() * (999999 - 111111 + 1) + 111111))
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
//                                                                                Log.d(TAG, "DocumentSnapshot successfully updated!");
//                                                                                Toast.makeText(ForgotPasswordActivity.this, "Updated OTP", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
//                                                                                Log.w(TAG, "Error updating document", e);
                                                                                Toast.makeText(ForgotPasswordActivity.this, "Updated error OTP " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                                                            }
                                                                        });;
                                                                Intent intent = new Intent(ForgotPasswordActivity.this,FormResetPasswordActivity.class);
                                                                intent.putExtra("email", mailerModel.getEmail());
                                                                intent.putExtra("id", mailerModel.getId());
                                                                startActivity(intent);
                                                            }else{
                                                                Toast.makeText(ForgotPasswordActivity.this, "Verify Unsuccessful!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }
                                                    catch (Exception e) {
                                                        Toast.makeText(ForgotPasswordActivity.this, "Something Error", Toast.LENGTH_SHORT).show();
                                                    }
                                                }else {
                                                    doc= "Error";
                                                    Toast.makeText(ForgotPasswordActivity.this, doc, Toast.LENGTH_SHORT).show();
                                                }
                                            }});
                            }else{
                                Toast.makeText(ForgotPasswordActivity.this, "Type OTP is ERROR", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            }
        });



        //tv Switch form login
        tv_loginNow = findViewById(R.id.tv_loginNow);
        tv_loginNow.setText(Html.fromHtml("<u>Switch to Login!</u>"));
        tv_loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
            }
        });
    }public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    //http request net working
    private void postDataUsingVolley(String mail, String otp) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL =  "https://mailer-server.herokuapp.com/api/verify/verify-otp";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        // request body goes here
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("otp", otp);
                        jsonBody.put("mail", mail);
                        String requestBody = jsonBody.toString();
                        return requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException | JSONException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("id", "oneapp.app.com");
                    params.put("key", "fgs7902nskagdjs");

                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            Log.d("string", stringRequest.toString());
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
