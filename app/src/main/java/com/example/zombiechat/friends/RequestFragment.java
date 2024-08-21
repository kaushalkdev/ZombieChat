package com.example.zombiechat.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zombiechat.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class RequestFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecyclerView mrecyclerview;
    private RequestRecyclerAdaper madapter;
    public static final String TAG = "RequestFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_request, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //recyclerview
        mrecyclerview = view.findViewById(R.id.request_recycler_view);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(view.getContext()));




//        final List<String> userId = new ArrayList<>();
//        db.collection("requests")
//                .whereEqualTo("sentTo", mAuth.getCurrentUser().getUid())
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//
//                            userId.add(documentSnapshot.get("sendBy").toString());
//
//                            final List<SingleUserModel> userModels =  new ArrayList<>();
//
//
//
//
//
//                            db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                                    if(e != null){
//                                         return;
//                                    }
//
//
//                                    for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
//                                        SingleUserModel singleUserModel = documentSnapshot.toObject(SingleUserModel.class);
//                                        if( ! singleUserModel.getUserid().equals(mAuth.getCurrentUser().getUid())) {
//                                            userModels.add(singleUserModel);
//                                        }
//                                    }
////                                    madapter = new RequestRecyclerAdapter(userModels,userId);
////                                    mrecyclerview.setAdapter(madapter);
//
//
//
//                                }
//                            });
//
//
//
//
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(view.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();

        Query query = db.collection("requests")
                 .whereEqualTo("sentTo",mAuth.getCurrentUser().getUid());

        FirestoreRecyclerOptions<RequestModel> options = new FirestoreRecyclerOptions.Builder<RequestModel>()
                .setQuery(query, RequestModel.class)
                .build();
        madapter = new RequestRecyclerAdaper(options);
        mrecyclerview.setAdapter(madapter);
        madapter.startListening();

    }
}
