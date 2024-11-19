package com.example.zombiechat.src.chat.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zombiechat.R;
import com.example.zombiechat.src.chat.data.models.SingleChatModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;


public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.UsersChatViewHolder> {

    final List<SingleChatModel> chatList;

    public ChatRoomAdapter(List<SingleChatModel> chatList) {
        this.chatList = chatList;

    }


    @Override
    public int getItemViewType(int position) {

        SingleChatModel model = chatList.get(position);
        String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        // Adding this check as we want to differentiate messages sent by the current user and the other user
        return model.getSendBy().equals(currentUserId) ? UserTypes.currentUser : UserTypes.otherUser;

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


    @NonNull
    @Override
    public UsersChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int userType) {


        View view;
        // Inflating correct layout based on the user type
        if (userType == UserTypes.otherUser) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.other_user_chat_layout, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.current_user_chat_layout, viewGroup, false);
        }
        return new UsersChatViewHolder(view);


    }


    @Override
    public void onBindViewHolder(@NonNull UsersChatViewHolder holder, int position) {

        SingleChatModel model = chatList.get(position);

        // set the messages for the current user and the other user by checking the item view type
        int userType = holder.getItemViewType();
        if (userType == UserTypes.currentUser) {
            holder.setCurrentMessage(model.getMessage(), model.getTime());
        } else {
            holder.setOtherMessage(model.getMessage(), model.getTime());
        }


    }

    static class UsersChatViewHolder extends RecyclerView.ViewHolder {

        TextView currentUserText;
        TextView otherUserText;
        TextView dateTime;

        public UsersChatViewHolder(@NonNull View itemView) {
            super(itemView);


        }

        public void setCurrentMessage(String message, Timestamp msgDateTime) {

            currentUserText = itemView.findViewById(R.id.current_userchat);
            dateTime = itemView.findViewById(R.id.current_user_message_date);
            currentUserText.setText(message);
            dateTime.setText(msgDateTime.toDate().toString());
        }

        public void setOtherMessage(String message, Timestamp msgDateTime) {
            otherUserText = itemView.findViewById(R.id.other_userchat);
            dateTime = itemView.findViewById(R.id.other_user_message_date);
            otherUserText.setText(message);
            dateTime.setText(msgDateTime.toDate().toString());

        }
    }

    static class UserTypes {
        static final int currentUser = 1;
        static final int otherUser = 2;

    }
}


