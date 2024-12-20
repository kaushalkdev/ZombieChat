package com.example.zombiechat.src.friends.view.friends;

import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zombiechat.R;
import com.example.zombiechat.src.chat.views.screens.activities.ChatRoomScreen;
import com.example.zombiechat.util.consts.Fields;
import com.example.zombiechat.friends.data.models.NewFriendsModel;
import com.example.zombiechat.friends.data.repo.FriendsRepo;
import com.example.zombiechat.src.friends.viewModels.FriendsVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsRecyclerAdapter extends RecyclerView.Adapter<friendsViewHolder> {

    public static final String TAG = "FriendsRecyclerAdapter";

    private List<NewFriendsModel> userModels;
    private FriendsVM friendsVM = new FriendsVM(new FriendsRepo());

    public FriendsRecyclerAdapter(List<NewFriendsModel> friends) {
        this.userModels = friends;

    }

    @NonNull
    @Override
    public friendsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friends_single_layout, viewGroup, false);
        return new friendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final friendsViewHolder mViewHolder, int i) {
        int position = mViewHolder.getAdapterPosition();
        mViewHolder.username.setText(userModels.get(position).getUserName());
        mViewHolder.userstatus.setText(userModels.get(position).getUserStatus());
        mViewHolder.setImage(userModels.get(position).getUserImage());
        mViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chatRoomIntent = new Intent(v.getContext(), ChatRoomScreen.class);
                NewFriendsModel userModel = userModels.get(i);
                chatRoomIntent.putExtra(Fields.otherUserId, userModel.getUserid());
                chatRoomIntent.putExtra(Fields.otherUserImage, userModel.getUserImage());
                chatRoomIntent.putExtra(Fields.otherUserName, userModel.getUserName());
                chatRoomIntent.putExtra(Fields.chatRoomId, userModel.getChatRoomId());

                v.getContext().startActivity(chatRoomIntent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }
}

class friendsViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "mViewHolder";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextView username, userstatus;
    CircleImageView userimage;
    Button acceptBtn, rejectBtn;


    public friendsViewHolder(@NonNull View itemView) {
        super(itemView);


        username = itemView.findViewById(R.id.user_name);
        userstatus = itemView.findViewById(R.id.user_status);
        userimage = itemView.findViewById(R.id.user_image);

        acceptBtn = itemView.findViewById(R.id.accept_btn);
        rejectBtn = itemView.findViewById(R.id.reject_btn);


    }

    public void setImage(String image) {

        Picasso.with(itemView.getContext()).load(image).error(R.drawable.default_user).placeholder(R.drawable.default_user).into(userimage);

    }


}