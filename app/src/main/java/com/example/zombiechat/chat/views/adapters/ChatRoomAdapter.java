package com.example.zombiechat.chat.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zombiechat.R;
import com.example.zombiechat.chat.data.models.SingleChatModel;

import java.util.List;


public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.UsersChatViewHolder> {

    final List<SingleChatModel> chatList;

    public ChatRoomAdapter(List<SingleChatModel> chatList) {
        this.chatList = chatList;

    }


    @Override
    public int getItemViewType(int position) {

        SingleChatModel model = chatList.get(position);

//        if (model.getSendBy().equals(mAuth.getCurrentUser().getUid())) {
//            return 1;
//        } else {
//            return 0;
//        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

//    @Override
//    protected void onBindViewHolder(@NonNull UsersChatViewHolder holder, int position, @NonNull SingleChatModel model) {
//
//
//        switch (holder.getItemViewType()) {
//
//            case 1: {
//                final ChatRoomActivity.UsersChatViewHolder viewHolder = holder;
//                viewHolder.setIsRecyclable(false);
//                Log.d("adapter", "other: " + model.getMessage());
//                viewHolder.setCurrentMessage(model.getMessage());
//
//
//            }
//
//            break;
//
//            case 0: {
//                final ChatRoomActivity.UsersChatViewHolder viewHolder = holder;
//                viewHolder.setIsRecyclable(false);
//                Log.d("adapter", "current: " + model.getMessage());
//                viewHolder.setOtherMessage(model.getMessage());
//
//            }
//            break;
//
//
//            default:
//                break;
//        }
//
//
//    }


    @NonNull
    @Override
    public UsersChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 1) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.current_user_chat_layout, viewGroup, false);
            return new UsersChatViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.other_user_chat_layout, viewGroup, false);
            return new UsersChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull UsersChatViewHolder holder, int position) {

        SingleChatModel model = chatList.get(position);
        // TODO set the messages for the current user and the other user
        holder.setOtherMessage(model.getMessage());

    }

    static class UsersChatViewHolder extends RecyclerView.ViewHolder {

        TextView currentUserText;
        TextView otherUserText;

        public UsersChatViewHolder(@NonNull View itemView) {
            super(itemView);


        }

        public void setCurrentMessage(String message) {

            currentUserText = itemView.findViewById(R.id.current_userchat);
            currentUserText.setText(message);
        }

        public void setOtherMessage(String message) {
            otherUserText = itemView.findViewById(R.id.other_userchat);
            otherUserText.setText(message);

        }
    }

}


