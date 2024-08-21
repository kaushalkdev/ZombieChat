package com.example.zombiechat.friends;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zombiechat.R;
import com.example.zombiechat.account.SingleUserModel;
import com.example.zombiechat.chat.UsersChatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsRecyclerAdapter extends RecyclerView.Adapter<friendsViewHolder> {

    public static final String TAG = "FriendsRecyclerAdapter";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private List<SingleUserModel> userModels;
    List<String> userId;

    public FriendsRecyclerAdapter(List<SingleUserModel> userModels, List<String> userId) {
        this.userModels = userModels;
        this.userId = userId;
    }

    public FriendsRecyclerAdapter(List<String> friendid) {
        this.userId = friendid;
    }

    @NonNull
    @Override
    public friendsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friends_single_layout, viewGroup, false);
        return new friendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final friendsViewHolder mViewHolder, int i) {


        db.collection("users")
                .whereEqualTo("userid", userId.get(i))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }


                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(queryDocumentSnapshots)) {
                            SingleUserModel singleUserModel = documentSnapshot.toObject(SingleUserModel.class);
                            Log.d(TAG, "single user: " + singleUserModel.getName());

                            mViewHolder.username.setText(singleUserModel.getName());
                            mViewHolder.userstatus.setText(singleUserModel.getStatus());
                            mViewHolder.setImage(singleUserModel.getImage());
                            mViewHolder.setOnclick(singleUserModel);

                        }

                    }
                });

    }

    @Override
    public int getItemCount() {
        return userId.size();
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