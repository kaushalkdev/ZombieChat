package com.example.zombiechat.friends;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.zombiechat.R;
import com.example.zombiechat.account.data.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class AllusersActivity extends AppCompatActivity {

    private static final String TAG = "AllusersActivity";
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private RecyclerView mrecyclerview;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserModel singleuser;
    private AllUsersRecyclerAdapter madapter;
    ListenerRegistration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allusers);

        //toolbar
        mToolbar = findViewById(R.id.all_users_aap_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recyclerview
        mrecyclerview = findViewById(R.id.all_users_recyclerview);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));

        //firebase Auth
        mAuth = FirebaseAuth.getInstance();


        final List<UserModel> userModels = new ArrayList<>();


        registration = db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(AllusersActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }


                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    UserModel userModel = documentSnapshot.toObject(UserModel.class);
                    if (!userModel.getUserid().equals(mAuth.getCurrentUser().getUid())) {
                        userModels.add(userModel);
                    }
                }
                madapter = new AllUsersRecyclerAdapter(userModels, mAuth.getCurrentUser().getUid());
                mrecyclerview.setAdapter(madapter);


            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        registration.remove();
    }
}
