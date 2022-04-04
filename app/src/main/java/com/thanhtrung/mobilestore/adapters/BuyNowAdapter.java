package com.thanhtrung.mobilestore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thanhtrung.mobilestore.R;
import com.thanhtrung.mobilestore.models.BuyNowModel;


import java.util.List;

public class BuyNowAdapter extends RecyclerView.Adapter<BuyNowAdapter.ViewHolder>{


    Context context;
    List<BuyNowModel> buyNowModelList;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public BuyNowAdapter(Context context, List<BuyNowModel> buyNowModelList) {
        this.context = context;
        this.buyNowModelList = buyNowModelList;
        firestore =  FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public BuyNowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BuyNowAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BuyNowAdapter.ViewHolder holder, int position) {

        holder.name.setText(buyNowModelList.get(position).getName());
        holder.phoneNumber.setText(buyNowModelList.get(position).getPhoneNumber());
        holder.address.setText(buyNowModelList.get(position).getAddress());
        holder.productName.setText(buyNowModelList.get(position).getProductName());
        holder.productPrice.setText(buyNowModelList.get(position).getProductPrice());
        holder.totalPrice.setText(String.valueOf(buyNowModelList.get(position).getTotalPrice()));
        holder.totalQuantity.setText(buyNowModelList.get(position).getTotalQuantity());
        //-------//

    }

    @Override
    public int getItemCount() {
        return buyNowModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,phoneNumber,address,productName,productPrice,totalPrice,totalQuantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.my_order_name);

            phoneNumber = itemView.findViewById(R.id.my_order_number_phone);

            address = itemView.findViewById(R.id.my_order_address);

            productName = itemView.findViewById(R.id.my_order_product_name);

            productPrice = itemView.findViewById(R.id.my_order_product_price);

            totalPrice = itemView.findViewById(R.id.my_order_product_total_price);

            totalQuantity = itemView.findViewById(R.id.my_order_product_total_quantity);

        }
    }
}
