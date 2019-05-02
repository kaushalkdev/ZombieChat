package com.example.zombiechat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Chat> chatsList;

    public ChatAdapter(List<Chat> chatsList) {
        this.chatsList = chatsList;
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

        if (chatsList.get(i).getSendBy().equals(mAuth.getCurrentUser().getUid())) {
            holder.text.setText(chatsList.get(i).getMessage());
            db.collection("users")
                    .document(chatsList.get(i).getSentTO())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            holder.username.setText(documentSnapshot.get("name").toString());
                            holder.setImgae(documentSnapshot.get("image").toString());


                            //for item click to open chat
                            SingleUserModel singleUserModel = new SingleUserModel();
                            singleUserModel.setImage(documentSnapshot.get("image").toString());
                            singleUserModel.setName(documentSnapshot.get("name").toString());
                            singleUserModel.setUserid(documentSnapshot.get("userid").toString());
                            singleUserModel.setSex(documentSnapshot.get("sex").toString());

                            holder.setOnclick(singleUserModel);
                        }
                    });

        }else{
            holder.text.setText(chatsList.get(i).getMessage());
            db.collection("users")
                    .document(chatsList.get(i).getSendBy())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            holder.username.setText(documentSnapshot.get("name").toString());
                            holder.setImgae(documentSnapshot.get("image").toString());


                            //for item click to open chat
                            SingleUserModel singleUserModel = new SingleUserModel();
                            singleUserModel.setImage(documentSnapshot.get("image").toString());
                            singleUserModel.setName(documentSnapshot.get("name").toString());
                            singleUserModel.setUserid(documentSnapshot.get("userid").toString());
                            singleUserModel.setSex(documentSnapshot.get("sex").toString());

                            holder.setOnclick(singleUserModel);
                        }
                    });
        }


    }

    @Override
    public int getItemCount() {
        return chatsList.size();
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

        public void setOnclick(final SingleUserModel singleUserModel) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("chatids")
                            .whereEqualTo(singleUserModel.getUserid(), singleUserModel.getUserid())
                            .whereEqualTo(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                        Intent chatIntent = new Intent(itemView.getContext(), UsersChatActivity.class);
                                        chatIntent.putExtra("uid", singleUserModel.getUserid());
                                        chatIntent.putExtra("image", singleUserModel.getImage());
                                        chatIntent.putExtra("name", singleUserModel.getName());
                                        chatIntent.putExtra("sex", singleUserModel.getSex());
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
