package com.example.zombiechat.chat.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zombiechat.R;
import com.example.zombiechat.account.data.models.UserModel;
import com.example.zombiechat.chat.data.models.LastChatModel;
import com.example.zombiechat.chat.views.screens.UsersChatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

public class LastChatsAdapter extends RecyclerView.Adapter<LastChatsAdapter.LastChatHolder> {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Activity activity;
    List<LastChatModel> chatBoxes;
    ListenerRegistration registration;

    public LastChatsAdapter(Context activity, List<LastChatModel> chatBoxes) {
        this.activity = (Activity) activity;
        this.chatBoxes = chatBoxes;

    }

    @NonNull
    @Override
    public LastChatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_single_layout, viewGroup, false);
        return new LastChatHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final LastChatHolder holder, int i) {

        LastChatModel chatBox = chatBoxes.get(i);
        holder.text.setText(chatBox.getMsg());
        holder.username.setText(chatBox.getUserName());
        holder.setImgae(chatBox.getUserImage());


//        db.collection("chatbox").document(chatid.get(i)).collection("chats").orderBy("time", Query.Direction.DESCENDING).limit(1).addSnapshotListener(activity, new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                if (queryDocumentSnapshots != null) {
//                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                        if (Objects.equals(documentSnapshot.get("sendBy"), Objects.requireNonNull(mAuth.getCurrentUser()).getUid())) {
//                            holder.text.setText(Objects.requireNonNull(documentSnapshot.get("message")).toString());
//                            db.collection("users").document(Objects.requireNonNull(documentSnapshot.get("sentTO")).toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                    holder.username.setText(documentSnapshot.get("name").toString());
//                                    holder.setImgae(documentSnapshot.get("image").toString());
//
//
//                                    //for item click to open chat
//
////                                    userModel.setImage(documentSnapshot.get("image").toString());
////                                    userModel.setName(documentSnapshot.get("name").toString());
////                                    userModel.setUserid(documentSnapshot.get("userid").toString());
////                                    userModel.setGender(documentSnapshot.get("sex").toString());
//
////                                    holder.setOnclick(userModel);
//                                }
//                            });
//                        } else {
//                            holder.text.setText(documentSnapshot.get("message").toString());
//                            db.collection("users").document(documentSnapshot.get("sendBy").toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                    holder.username.setText(documentSnapshot.get("name").toString());
//                                    holder.setImgae(documentSnapshot.get("image").toString());
//
//
////                                    //for item click to open chat
////                                    UserModel userModel = UserModel.INSTANCE;
////                                    userModel.setImage(documentSnapshot.get("image").toString());
////                                    userModel.setName(documentSnapshot.get("name").toString());
////                                    userModel.setUserid(documentSnapshot.get("userid").toString());
////                                    userModel.setGender(documentSnapshot.get("sex").toString());
////
////                                    holder.setOnclick(userModel);
//                                }
//                            });
//
//                        }
//
//                    }
//                }
//
//            }
//        });


    }

    @Override
    public int getItemCount() {


        return chatBoxes.size();
    }


    static class LastChatHolder extends RecyclerView.ViewHolder {
        CircleImageView userimage;
        TextView username;
        TextView text;


        public LastChatHolder(View itemView) {
            super(itemView);

            userimage = itemView.findViewById(R.id.user_image);
            username = itemView.findViewById(R.id.user_name);
            text = itemView.findViewById(R.id.user_message);

        }

        public void setImgae(String image) {

            Picasso.with(itemView.getContext()).load(image).error(R.drawable.default_user).placeholder(R.drawable.default_user).into(userimage);
        }

        public void setOnclick(final UserModel userModel) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    db.collection("chatids").whereEqualTo(userModel.getUserid(), userModel.getUserid()).whereEqualTo(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//
//                                Intent chatIntent = new Intent(itemView.getContext(), UsersChatActivity.class);
//                                chatIntent.putExtra("uid", userModel.getUserid());
//                                chatIntent.putExtra("image", userModel.getImage());
//                                chatIntent.putExtra("name", userModel.getName());
//                                chatIntent.putExtra("sex", userModel.getGender());
//                                chatIntent.putExtra("chatid", documentSnapshot.get("chatid").toString());
//                                itemView.getContext().startActivity(chatIntent);
//                            }
//                        }
//                    });

                }
            });

        }
    }


}
