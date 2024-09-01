package com.example.zombiechat.friends;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Objects;


public class FriendsFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecyclerView mrecyclerview;
    private FriendsRecyclerAdapter madapter;
    public static final String TAG = "FriendsFragment";
    final List<String> friendid = new ArrayList<>();
    ListenerRegistration registration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_friends, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //recyclerview
        mrecyclerview = view.findViewById(R.id.friends_recycler_view);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(view.getContext()));


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();


        registration = db.collection("friends")
                .whereEqualTo("userid", Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .addSnapshotListener(requireActivity(), new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            friendid.clear();
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                friendid.add(Objects.requireNonNull(documentSnapshot.get("friendId")).toString());


                                final List<UserModel> userModels = new ArrayList<>();


                                db.collection("users")
                                        .whereEqualTo("userid", documentSnapshot.get("friendId"))
                                        .addSnapshotListener(requireActivity(), new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                                if (e != null) {
                                                    return;
                                                }


                                                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(queryDocumentSnapshots)) {
                                                    UserModel userModel = documentSnapshot.toObject(UserModel.class);
                                                    Log.d(TAG, "single user: " + userModel.getName());

                                                    userModels.add(userModel);

                                                }
                                                madapter = new FriendsRecyclerAdapter(friendid);
                                                mrecyclerview.setAdapter(madapter);


                                            }
                                        });


                            }
                        }

                    }
                })
        ;

    }


    @Override
    public void onStop() {
        super.onStop();
//        userId.clear();
        registration.remove();
    }


}

