package com.example.zombiechat;

import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class UsersChatAdapter extends FirestoreRecyclerAdapter<SingleChatModel, UsersChatAdapter.UsersChatViewHolder> {


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private RecyclerView mrecyclerview;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UsersChatAdapter(@NonNull FirestoreRecyclerOptions<SingleChatModel> options) {
        super(options);
    }

    @Override
    public int getItemViewType(int position) {

        SingleChatModel model = getItem(position);

        if (model.getSendBy().equals(mAuth.getCurrentUser().getUid())) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull UsersChatViewHolder holder, int position, @NonNull SingleChatModel model) {


        switch (holder.getItemViewType()) {

            case 1: {
                final UsersChatViewHolder viewHolder = holder;
                viewHolder.setIsRecyclable(false);
                Log.d("adapter", "other: " + model.getMessage());
                viewHolder.setCurrentMessage(model.getMessage());


            }

            break;

            case 0: {
                final UsersChatViewHolder viewHolder = holder;
                viewHolder.setIsRecyclable(false);
                Log.d("adapter", "current: " + model.getMessage());
                viewHolder.setOtherMessage(model.getMessage());

            }
            break;


            default:
                break;
        }


    }

    @Override
    public void onDataChanged() {

        super.onDataChanged();
    }

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

    class UsersChatViewHolder extends RecyclerView.ViewHolder {

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
