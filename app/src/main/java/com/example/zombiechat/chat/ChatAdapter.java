package com.example.zombiechat.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zombiechat.R;
import com.example.zombiechat.account.data.models.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;


import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Activity activity;
    List<String> chatid;
    ListenerRegistration registration;

    public ChatAdapter(FragmentActivity activity, List<String> chatid) {

        this.activity = activity;
        this.chatid = chatid;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_single_layout,
                viewGroup, false);
        return new ChatHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatHolder holder, int i) {


          db.collection("chatbox")
                .document(chatid.get(i))
                .collection("chats")
                .orderBy("time", Query.Direction.DESCENDING)
                .limit(1)
                .addSnapshotListener(activity,new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (Objects.equals(documentSnapshot.get("sendBy"), Objects.requireNonNull(mAuth.getCurrentUser()).getUid())) {
                                    holder.text.setText(Objects.requireNonNull(documentSnapshot.get("message")).toString());
                                    db.collection("users")
                                            .document(Objects.requireNonNull(documentSnapshot.get("sentTO")).toString())
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    holder.username.setText(documentSnapshot.get("name").toString());
                                                    holder.setImgae(documentSnapshot.get("image").toString());


                                                    //for item click to open chat
                                                    UserModel userModel = new UserModel();
                                                    userModel.setImage(documentSnapshot.get("image").toString());
                                                    userModel.setName(documentSnapshot.get("name").toString());
                                                    userModel.setUserid(documentSnapshot.get("userid").toString());
                                                    userModel.setGender(documentSnapshot.get("sex").toString());

                                                    holder.setOnclick(userModel);
                                                }
                                            });
                                } else {
                                    holder.text.setText(documentSnapshot.get("message").toString());
                                    db.collection("users")
                                            .document(documentSnapshot.get("sendBy").toString())
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    holder.username.setText(documentSnapshot.get("name").toString());
                                                    holder.setImgae(documentSnapshot.get("image").toString());


                                                    //for item click to open chat
                                                    UserModel userModel = new UserModel();
                                                    userModel.setImage(documentSnapshot.get("image").toString());
                                                    userModel.setName(documentSnapshot.get("name").toString());
                                                    userModel.setUserid(documentSnapshot.get("userid").toString());
                                                    userModel.setGender(documentSnapshot.get("sex").toString());

                                                    holder.setOnclick(userModel);
                                                }
                                            });

                                }

                            }
                        }

                    }
                });


    }

    @Override
    public int getItemCount() {


        return chatid.size();
    }


    class ChatHolder extends RecyclerView.ViewHolder {
        CircleImageView userimage;
        TextView username;
        TextView text;


        public ChatHolder(View itemView) {
            super(itemView);

            userimage = itemView.findViewById(R.id.user_image);
            username = itemView.findViewById(R.id.user_name);
            text = itemView.findViewById(R.id.user_message);

        }

        public void setImgae(String image) {

            Picasso.with(itemView.getContext())
                    .load(image)
                    .error(R.drawable.default_user)
                    .placeholder(R.drawable.default_user)
                    .into(userimage);
        }

        public void setOnclick(final UserModel userModel) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("chatids")
                            .whereEqualTo(userModel.getUserid(), userModel.getUserid())
                            .whereEqualTo(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                        Intent chatIntent = new Intent(itemView.getContext(), UsersChatActivity.class);
                                        chatIntent.putExtra("uid", userModel.getUserid());
                                        chatIntent.putExtra("image", userModel.getImage());
                                        chatIntent.putExtra("name", userModel.getName());
                                        chatIntent.putExtra("sex", userModel.getGender());
                                        chatIntent.putExtra("chatid", documentSnapshot.get("chatid").toString());
                                        itemView.getContext().startActivity(chatIntent);
                                    }
                                }
                            });

                }
            });

        }
    }



}
