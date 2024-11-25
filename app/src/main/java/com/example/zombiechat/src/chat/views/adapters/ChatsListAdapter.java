package com.example.zombiechat.src.chat.views.adapters;

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
import com.example.zombiechat.src.chat.data.models.LastChatModel;
import com.example.zombiechat.src.chat.views.screens.activities.ChatRoomScreen;
import com.example.zombiechat.util.consts.Fields;
import com.squareup.picasso.Picasso;

import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.LastChatHolder> {
    Activity activity;
    List<LastChatModel> chatBoxes;


    public ChatsListAdapter(Context activity, List<LastChatModel> chatBoxes) {
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
        holder.userLastMsg.setText(chatBox.msg);
        holder.userName.setText(chatBox.userName);
        holder.setUserImage(chatBox.userImage);
        holder.msgDateTime.setText(chatBox.msgTime.toDate().toString());
        holder.itemView.setOnClickListener(v -> {
            Intent chatRoomIntent = new Intent(activity, ChatRoomScreen.class);
            chatRoomIntent.putExtra(Fields.otherUserName, chatBox.userName);
            chatRoomIntent.putExtra(Fields.otherUserImage, chatBox.userImage);
            chatRoomIntent.putExtra(Fields.chatRoomId, chatBox.chatId);
            activity.startActivity(chatRoomIntent);
        });


    }

    @Override
    public int getItemCount() {
        return chatBoxes.size();
    }


    static class LastChatHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName;
        TextView userLastMsg;
        TextView msgDateTime;


        public LastChatHolder(View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
            userLastMsg = itemView.findViewById(R.id.user_message);
            msgDateTime = itemView.findViewById(R.id.last_msg_date);


        }

        public void setUserImage(String image) {

            Picasso.with(itemView.getContext()).load(image).error(R.drawable.default_user).placeholder(R.drawable.default_user).into(userImage);
        }

    }


}
