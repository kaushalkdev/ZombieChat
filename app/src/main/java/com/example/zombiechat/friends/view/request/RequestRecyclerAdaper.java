package com.example.zombiechat.friends.view.request;


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
import com.example.zombiechat.account.data.models.RequestModel;
import com.example.zombiechat.account.data.models.UserModel;

import com.example.zombiechat.friends.data.repo.FriendsRepo;
import com.example.zombiechat.friends.viewModels.FriendsVM;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.security.auth.callback.Callback;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestRecyclerAdaper extends RecyclerView.Adapter<RequestRecyclerAdaper.RequestViewHolder> {
    final List<UserModel> requestModels;
    final FriendsVM friendsVM = new FriendsVM(new FriendsRepo());

    RequestRecyclerAdaper(List<UserModel> requestModels) {
        this.requestModels = requestModels;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RequestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_request_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        UserModel userModel = requestModels.get(position);
        holder.username.setText(userModel.getName());
        holder.userstatus.setText(userModel.getStatus());
        holder.setImage(userModel.getImage());
        holder.acceptBtn.setOnClickListener(v -> {
            friendsVM.acceptRequest(Objects.requireNonNull(userModel.getUserid()));
            holder.acceptBtn.setVisibility(View.GONE);
            holder.rejectBtn.setVisibility(View.GONE);
        });

        holder.rejectBtn.setOnClickListener(v -> {
            friendsVM.rejectRequest(Objects.requireNonNull(userModel.getUserid()));
            holder.acceptBtn.setVisibility(View.GONE);
            holder.rejectBtn.setVisibility(View.GONE);
        });

    }

    @Override
    public int getItemCount() {
        return requestModels.size();
    }


    static class RequestViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "mViewHolder";
        TextView username, userstatus;
        CircleImageView userimage;
        Button acceptBtn, rejectBtn;


        public RequestViewHolder(@NonNull View itemView) {
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

}









