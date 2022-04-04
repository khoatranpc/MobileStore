package com.thanhtrung.mobilestore.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thanhtrung.mobilestore.DetailedActivity;
import com.thanhtrung.mobilestore.HomeActivity;
import com.thanhtrung.mobilestore.LoginActivity;
import com.thanhtrung.mobilestore.MainActivity;
import com.thanhtrung.mobilestore.NavCategoryActivity;
import com.thanhtrung.mobilestore.OffersFragment;
import com.thanhtrung.mobilestore.R;
import com.thanhtrung.mobilestore.models.NavCategoryDetailedModel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class NavCategoryDetailedAdapter extends RecyclerView.Adapter<NavCategoryDetailedAdapter.ViewHolder> {
    Context context;
    List<NavCategoryDetailedModel> list;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public NavCategoryDetailedAdapter(Context context, List<NavCategoryDetailedModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_category_detailed,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(list.get(position).getName());
        holder.price.setText("Price: $" + list.get(position).getPrice());
        //btn buynow



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,add_item,remove_item;
        TextView name, price,quantity;
        int tempCurrentQuantity = 1;
        AppCompatButton btnBuy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.cat_detailed_nav_img);
            name = itemView.findViewById(R.id.cat_detailed_name);
            price = itemView.findViewById(R.id.cat_detailed_price);
            quantity = itemView.findViewById(R.id.quantity);

            btnBuy = itemView.findViewById(R.id.buy_now);

            //increment quantity
            add_item = itemView.findViewById(R.id.add_item);
            add_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tempCurrentQuantity = tempCurrentQuantity+1;
                    quantity.setText(String.valueOf(tempCurrentQuantity));
                }
            });

            //decrement quantity
            remove_item = itemView.findViewById(R.id.remove_item);
            remove_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tempCurrentQuantity>1){
                        tempCurrentQuantity = tempCurrentQuantity-1;
                        quantity.setText(String.valueOf(tempCurrentQuantity));
                    }

                }
            });
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Successful Purchase", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, HomeActivity.class);
                    context.startActivity(intent);

                }
            });


        }
    }
}
