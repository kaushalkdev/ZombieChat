package com.example.zombiechat.friends.view;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zombiechat.R;
import com.example.zombiechat.account.data.models.UserModel;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestRecyclerAdaper extends FirestoreRecyclerAdapter<RequestModel, RequestRecyclerAdaper.mViewHolder> {

    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RequestRecyclerAdaper(@NonNull FirestoreRecyclerOptions<RequestModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final mViewHolder holder, int position, @NonNull RequestModel model) {

        mAuth = FirebaseAuth.getInstance();
        db.collection("users")
                .document(model.getSendBy())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        UserModel userModel = documentSnapshot.toObject(UserModel.class);

                        final Map<String, String> friends = new HashMap<>(), others = new HashMap<>();
                        friends.put("userid", mAuth.getCurrentUser().getUid());
                        friends.put("friendId", userModel.getUserid());

                        others.put("friendId", mAuth.getCurrentUser().getUid());
                        others.put("userid", userModel.getUserid());


                        holder.username.setText(userModel.getName());
                        holder.userstatus.setText(userModel.getStatus());
                        holder.setImage(userModel.getImage());
                        holder.setOnClick(userModel.getUserid(), mAuth.getCurrentUser().getUid(), others, friends);

                    }
                });


    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_request_layout, viewGroup, false);

        return new mViewHolder(view);
    }

    class mViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "mViewHolder";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        TextView username, userstatus;
        CircleImageView userimage;
        Button acceptBtn, rejectBtn;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        public mViewHolder(@NonNull View itemView) {
            super(itemView);


            username = itemView.findViewById(R.id.user_name);
            userstatus = itemView.findViewById(R.id.user_status);
            userimage = itemView.findViewById(R.id.user_image);

            acceptBtn = itemView.findViewById(R.id.accept_btn);
            rejectBtn = itemView.findViewById(R.id.reject_btn);

        }

        public void setImage(String image) {

            Picasso.with(itemView.getContext())
                    .load(image)
                    .error(R.drawable.default_user)
                    .placeholder(R.drawable.default_user)
                    .into(userimage);

        }


        public void setOnClick(final String otheruid, final String uid, final Map<String, String> others, final Map<String, String> friends) {


            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("friends")
                            .add(friends)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(final DocumentReference documentReference) {

                                    db.collection("friends")
                                            .add(others);

                                    db.collection("requests")
                                            .whereEqualTo("sentTo", uid)
                                            .whereEqualTo("sendBy", otheruid)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                        String docId = documentSnapshot.getId();
                                                        Log.d(TAG, "onSuccess: " + docId);

                                                        db.collection("requests")
                                                                .document(docId)
                                                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                        final String uuid = UUID.randomUUID().toString().replace("-", "");

                                                                        final HashMap<String, String> chatidmap = new HashMap<>();
                                                                        chatidmap.put("chatid", uuid);
                                                                        chatidmap.put(uid, uid);
                                                                        chatidmap.put(otheruid, otheruid);


                                                                        db.collection("chatids")
                                                                                .add(chatidmap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                    @Override
                                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                                        Toast.makeText(itemView.getContext(), "You are Friends", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });

                                                                        final HashMap<String, String> chatmap = new HashMap<>();
                                                                        chatmap.put("message", "hi");
                                                                        chatmap.put("sentTO", otheruid);
                                                                        chatmap.put("sendBy", mAuth.getCurrentUser().getUid());
                                                                        Date currentTime = Calendar.getInstance().getTime();
                                                                        chatmap.put("time", currentTime.toString());

                                                                        db.collection("chatbox")
                                                                                .document(uuid)
                                                                                .collection("chats")
                                                                                .add(chatmap);
                                                                    }
                                                                });


                                                    }
                                                }
                                            });

                                }
                            });


                }


            });
        }
    }


}
