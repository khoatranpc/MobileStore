package com.thanhtrung.mobilestore.ui.profile;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firestore.v1.TransactionOptions;
import com.thanhtrung.mobilestore.HomeActivity;
import com.thanhtrung.mobilestore.R;
import com.thanhtrung.mobilestore.models.UserModel;

import java.util.HashMap;
import java.util.Objects;

import javax.xml.transform.Result;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    Uri imageUri;
    CircleImageView profileImg;
    EditText editname, editemail, editnumber,editaddress;
    Button update;
    FirebaseStorage storage;
    FirebaseAuth auth;
    String userID;
    DatabaseReference roof;
    FirebaseDatabase database;
    FirebaseUser firebaseUser;
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result != null){
                        // this result is the result of uri
                        profileImg.setImageURI(result);
                        //result will be set in imageUri
                        imageUri = result;
                    }
                    if(imageUri != null){
                        StorageReference reference = storage.getReference().child("profile_picture").child(FirebaseAuth.getInstance().getUid());
                        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getContext(), "Uploaded...", Toast.LENGTH_SHORT).show();

                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        database.getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                                                .child("profileImg").setValue(uri.toString());

                                        Toast.makeText(getContext(), "Profile Picture Uploaded", Toast.LENGTH_SHORT).show();

                                    }

                                });
                            }

                        });
                    }
                }
            });


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile,container,false);


        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userID = firebaseUser.getUid();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        roof = FirebaseDatabase.getInstance().getReference().child("User");
        roof.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                UserModel usersnapshot=snapshot.getValue(UserModel.class);
                if(usersnapshot!=null){
                    String username=usersnapshot.getName();
                    String Email=usersnapshot.getEmail();
                    String phonenumber= usersnapshot.getPhone();
                    String address = usersnapshot.getAddress();
                    //String password=usersnapshot.getPassword();
                    editname.setText(username);
                    editemail.setText(Email);
                    editnumber.setText(phonenumber);
                    editaddress.setText(address);
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
        profileImg = root.findViewById(R.id.profile_img);
        editname = root.findViewById(R.id.profile_name);
        editemail = root.findViewById(R.id.profile_email);
        editnumber = root.findViewById(R.id.profile_number);
        editaddress = root.findViewById(R.id.profile_address);
        update = root.findViewById(R.id.update);

        database.getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);

                        Glide.with(getContext()).load(userModel.getProfileImg()).into(profileImg);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserProfile();
            }
        });

        return root;
    }




    private void updateUserProfile() {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("name", editname.getText().toString());
        userMap. put("email", editemail.getText().toString());
        userMap. put("phone", editnumber.getText().toString());
        userMap. put("address", editaddress.getText().toString());
        roof.child(userID).updateChildren(userMap);
        Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
    }
}