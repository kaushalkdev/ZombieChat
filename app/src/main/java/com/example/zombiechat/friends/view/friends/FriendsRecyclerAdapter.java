package com.example.zombiechat.friends.view.friends;

import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zombiechat.R;
import com.example.zombiechat.account.data.models.UserModel;
import com.example.zombiechat.chat.views.screens.UsersChatActivity;
import com.example.zombiechat.friends.data.repo.FriendsRepo;
import com.example.zombiechat.friends.viewModels.FriendsVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsRecyclerAdapter extends RecyclerView.Adapter<friendsViewHolder> {

    public static final String TAG = "FriendsRecyclerAdapter";

    private List<UserModel> userModels;
    private FriendsVM friendsVM = new FriendsVM(new FriendsRepo());

    public FriendsRecyclerAdapter(List<UserModel> friends) {
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
        mViewHolder.username.setText(userModels.get(i).getName());
        mViewHolder.userstatus.setText(userModels.get(i).getStatus());
        mViewHolder.setImage(userModels.get(i).getImage());
        mViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chatIntent = new Intent(v.getContext(), UsersChatActivity.class);
                UserModel userModel = userModels.get(i);
                chatIntent.putExtra("uid", userModel.getUserid());
                chatIntent.putExtra("image", userModel.getImage());
                chatIntent.putExtra("name", userModel.getName());
                chatIntent.putExtra("sex", userModel.getGender());
                chatIntent.putExtra("chatid", "someRandomeChantId");
                v.getContext().startActivity(chatIntent);
//                friendsVM.
//                db.collection("chatids").whereEqualTo(userModel.getUserid(), userModel.getUserid()).whereEqualTo(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//
//
//                        }
//                    }
//                });
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