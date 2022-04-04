package com.thanhtrung.mobilestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thanhtrung.mobilestore.models.BuyNowModel;
import com.thanhtrung.mobilestore.models.MyCartModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kotlin.contracts.ExperimentalContracts;

public class ConfirmOrderActivity extends AppCompatActivity {
    EditText confirm_name,confirm_number,confirm_address;
    String tempName, tempPhone,tempAddress = "";
    TextView tv_priceTotal;
    Button btn_confirm;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    int tempSum = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        confirm_name = findViewById(R.id.confirm_name);

        confirm_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tempName = confirm_name.getText().toString();
            }
        });

        confirm_number = findViewById(R.id.confirm_number);
        confirm_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tempPhone = confirm_number.getText().toString();
            }
        });


        confirm_address = findViewById(R.id.confirm_address);

        confirm_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tempAddress = confirm_address.getText().toString();
            }
        });


        //get Total price
        tv_priceTotal = findViewById(R.id.tv_priceTotal);

        Intent intent = getIntent();




        //btn confirm
        btn_confirm = findViewById(R.id.btn_confirm);

        List<MyCartModel> tempList = (ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList");
        for(MyCartModel model : tempList ){
            tempSum += model.getTotalPrice();
        }

        tv_priceTotal.setText("Total price: $" + tempSum);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(TextUtils.isEmpty(tempName)){
                        Toast.makeText(ConfirmOrderActivity.this, "Name is Empty!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(tempPhone)){
                        Toast.makeText(ConfirmOrderActivity.this, "Number phone is Empty!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(tempAddress)){
                        Toast.makeText(ConfirmOrderActivity.this, "Address is Empty!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                List<MyCartModel> list = (ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList");

                if(list != null && list.size() > 0){
                    for(MyCartModel model : list ){
                        final HashMap<String, Object> cartMap = new HashMap<>();
                        cartMap.put("productName",model.getProductName());
                        cartMap.put("productPrice",model.getProductPrice());
                        cartMap.put("currentDate",model.getCurrentDate());
                        cartMap.put("currentTime",model.getCurrentTime());
                        cartMap.put("totalQuantity",model.getTotalQuantity());
                        cartMap.put("totalPrice",model.getTotalPrice());


                        cartMap.put("name",tempName);
                        cartMap.put("phoneNumber",tempPhone);
                        cartMap.put("address",tempAddress);
                        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                                .collection("BuyNow").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(ConfirmOrderActivity.this, "Your order has been placed!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }

                }

            }
        });
    }
}