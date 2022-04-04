package com.thanhtrung.mobilestore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.thanhtrung.mobilestore.adapters.BuyNowAdapter;
import com.thanhtrung.mobilestore.adapters.MyCartAdapter;
import com.thanhtrung.mobilestore.models.BuyNowModel;
import com.thanhtrung.mobilestore.models.MyCartModel;

import java.util.ArrayList;
import java.util.List;


public class MyOrdersFragment extends Fragment {
    FirebaseFirestore db;
    FirebaseAuth auth;

    RecyclerView recyclerView;
    BuyNowAdapter buyNowAdapter;
    List<BuyNowModel> buyNowModelList;



    public MyOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_my_orders2, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = root.findViewById(R.id.recyclerView_my_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        buyNowModelList =  new ArrayList<>();
        buyNowAdapter = new BuyNowAdapter(getActivity(),buyNowModelList);
        recyclerView.setAdapter(buyNowAdapter);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("BuyNow").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot :task.getResult().getDocuments() ){

                        String documentId = documentSnapshot.getId();

                        BuyNowModel buyNowModel;
                        buyNowModel = documentSnapshot.toObject(BuyNowModel.class);

                        buyNowModelList.add(buyNowModel);
                        buyNowAdapter.notifyDataSetChanged();

                    }
                }
            }
        });


        return root;
    }
}