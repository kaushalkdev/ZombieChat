package com.example.zombiechat.account.view;


import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.zombiechat.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {


    private CircleImageView muserimage;
    private TextView musername, muserstatus, musersex;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private Button msendRequestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //got Intent values.
        final String uid = getIntent().getStringExtra("uid");
        String name = getIntent().getStringExtra("name");

        //Toolbar
        mToolbar = findViewById(R.id.user_profile_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //text, image, button reference
        muserimage = findViewById(R.id.user_image);
        musername = findViewById(R.id.user_name);
        muserstatus = findViewById(R.id.user_status);
        musersex = findViewById(R.id.user_sex);
        msendRequestBtn = findViewById(R.id.send_request_btn);


        //cloud firestore
        db = FirebaseFirestore.getInstance();

        //firebase auth
        mAuth = FirebaseAuth.getInstance();


        // check if the user is already friend or request is already sent
        db.collection("requests")
                .whereEqualTo("sendBy", mAuth.getCurrentUser().getUid())
                .whereEqualTo("sentTo", uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            msendRequestBtn.setText("Request Sent");
                            msendRequestBtn.setClickable(false);
                        }
                    }
                });

        final Map<String, String> userMap = new HashMap<>();

        // get user details from cloud firestore
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        musername.setText(documentSnapshot.get("name").toString());
                        muserstatus.setText(documentSnapshot.get("status").toString());
                        musersex.setText(documentSnapshot.get("sex").toString());
                        Picasso.with(UserProfile.this)
                                .load(documentSnapshot.get("image").toString())
                                .error(R.drawable.default_user)
                                .placeholder(R.drawable.default_user)
                                .into(muserimage);


                    }
                });

        // find all the friends of the user
        db.collection("friends")
                .whereEqualTo("userid", mAuth.getCurrentUser().getUid())
                .whereEqualTo("friendId", uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                msendRequestBtn.setText("Friend");
                                msendRequestBtn.setClickable(false);
                            }
                        }

                    }
                });

        msendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                userMap.put("sendBy", mAuth.getCurrentUser().getUid());
                userMap.put("sentTo", uid);
                userMap.put("requeststatus", "sendRequest");

                // send friend request
                db.collection("requests")
                        .add(userMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                msendRequestBtn.setText("Request Sent");
                                msendRequestBtn.setClickable(false);
                            }
                        });

            }
        });

    }
}
