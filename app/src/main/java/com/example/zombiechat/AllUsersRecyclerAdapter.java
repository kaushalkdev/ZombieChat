package com.example.zombiechat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {


    private static final String TAG = "AllUsersRecyclerAdapter";
    List<SingleUserModel> userModels;

    public AllUsersRecyclerAdapter(List<SingleUserModel> userModels, String uid) {
        this.userModels = userModels;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_all_user_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.username.setText(userModels.get(i).getName());
        viewHolder.userstatus.setText(userModels.get(i).getStatus());
        viewHolder.setImage(userModels.get(i).getImage());
        viewHolder.setOnclick(userModels.get(i).getUserid(), userModels.get(i).getName());


    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + userModels.size());
        return userModels.size();
    }
}


class ViewHolder extends RecyclerView.ViewHolder {


    TextView username, userstatus;
    CircleImageView userimage;

    public ViewHolder(@NonNull View itemView) {


        super(itemView);

        username = itemView.findViewById(R.id.user_name);
        userstatus = itemView.findViewById(R.id.user_status);
        userimage = itemView.findViewById(R.id.user_image);


    }

    public void setImage(String image) {

        Picasso.with(itemView.getContext()).load(image).error(R.drawable.default_user).placeholder(R.drawable.default_user).into(userimage);
    }

    public void setOnclick(final String userid, final String name) {

        itemView.setOnClickListener(c -> {
            Intent userProfileIntent = new Intent(itemView.getContext(), UserProfile.class);
            userProfileIntent.putExtra("uid", userid);
            userProfileIntent.putExtra("name", name);
            itemView.getContext().startActivity(userProfileIntent);
        });

    }
}
