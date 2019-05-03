package com.example.zombiechat;

import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.circularreveal.cardview.CircularRevealCardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;


public class UsersChatActivity extends AppCompatActivity {

    public static final String TAG = "SmoothScroll";
    private TextToSpeech mTTS;
    private Toolbar mToolbar;
    private CircleImageView muserimage;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private EditText userinput;
    private Button sendmessageBtn;
    private RecyclerView mrecyclerview;
    private UsersChatAdapter adapter;
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_chat);


        mToolbar = findViewById(R.id.user_chat_app_bar);
        setSupportActionBar(mToolbar);

        //tts
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });


        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        String sex = getIntent().getStringExtra("sex");
        final String uid = getIntent().getStringExtra("uid");
        final String chatid = getIntent().getStringExtra("chatid");
        Log.d(TAG, "chat activity: chatid: " + chatid);

        //setting tool bar
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recyclerview
        mrecyclerview = findViewById(R.id.user_chat_recycler_view);
        mrecyclerview.setHasFixedSize(true);
        // Now set the properties of the LinearLayoutManager
        mLayoutManager = new LinearLayoutManager(UsersChatActivity.this);

        mLayoutManager.setStackFromEnd(true);

// And now set it to the RecyclerView
        mrecyclerview.setLayoutManager(mLayoutManager);


        //firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        muserimage = findViewById(R.id.user_image);
        //setting image
        Picasso.with(getApplicationContext())
                .load(image)
                .error(R.drawable.default_user)
                .placeholder(R.drawable.default_user)
                .into(muserimage);


        //button and edit text
        sendmessageBtn = findViewById(R.id.chat_box_send_btn);
        userinput = findViewById(R.id.chat_box_inout_text);


        sendmessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(userinput.getText().toString())) {
                    Toast.makeText(UsersChatActivity.this, "empty text", Toast.LENGTH_SHORT).show();
                } else {

                    final String message = userinput.getText().toString();
                    userinput.setText("");


                    HashMap<String, String> chatmap = new HashMap<>();

                    chatmap.put("message", message);
                    chatmap.put("sentTO", uid);
                    chatmap.put("sendBy", mAuth.getCurrentUser().getUid());
                    Date currentTime = Calendar.getInstance().getTime();
                    chatmap.put("time", currentTime.toString());

                    db.collection("chatbox")
                            .document(chatid)
                            .collection("chats")
                            .add(chatmap);




                }


            }
        });


    }



    @Override
    protected void onStart() {
        super.onStart();

        final String chatid = getIntent().getStringExtra("chatid");
        final String uid = getIntent().getStringExtra("uid");



        Query query = db.collection("chatbox")
                .document(chatid)
                .collection("chats")
                .orderBy("time", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<SingleChatModel> options = new FirestoreRecyclerOptions.Builder<SingleChatModel>()
                .setQuery(query, SingleChatModel.class)
                .build();

        adapter = new UsersChatAdapter(options);
        adapter.onDataChanged();
        mrecyclerview.setAdapter(adapter);
        adapter.startListening();



        //for speaking message only female voice currently

        db.collection("chatbox")
                .document(chatid)
                .collection("chats")
                .orderBy("time", Query.Direction.DESCENDING)
                .whereEqualTo("sendBy",uid)
                .limit(1)
                .addSnapshotListener(this,new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){

                            speak(documentSnapshot.get("message").toString());
                        }
                    }
                });






    }

    private void speak(String message) {

        float pitch = 1.1f;
        if (pitch < 0.1) pitch = 0.1f;
        float speed = 1.1f;
        if (speed < 0.1) speed = 0.1f;

        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);

        mTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
adapter.stopListening();
        super.onDestroy();
    }




    class UsersChatAdapter extends FirestoreRecyclerAdapter<SingleChatModel, UsersChatActivity.UsersChatViewHolder> {


        private FirebaseAuth mAuth = FirebaseAuth.getInstance();



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
        protected void onBindViewHolder(@NonNull UsersChatActivity.UsersChatViewHolder holder, int position, @NonNull SingleChatModel model) {


            switch (holder.getItemViewType()) {

                case 1: {
                    final UsersChatActivity.UsersChatViewHolder viewHolder = holder;
                    viewHolder.setIsRecyclable(false);
                    Log.d("adapter", "other: " + model.getMessage());
                    viewHolder.setCurrentMessage(model.getMessage());


                }

                break;

                case 0: {
                    final UsersChatActivity.UsersChatViewHolder viewHolder = holder;
                    viewHolder.setIsRecyclable(false);
                    Log.d("adapter", "current: " + model.getMessage());
                    viewHolder.setOtherMessage(model.getMessage());

                }
                break;


                default:
                    break;
            }


        }


        //added for updating the chat ui when new message received or sent

        @Override
        public void onDataChanged() {


            if(getItemCount()>0){
                mrecyclerview.smoothScrollToPosition(getItemCount());
            }
        }

        @NonNull
        @Override
        public UsersChatActivity.UsersChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            if (i == 1) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.current_user_chat_layout, viewGroup, false);
                return new UsersChatViewHolder(view);
            } else {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.other_user_chat_layout, viewGroup, false);
                return new UsersChatViewHolder(view);
            }
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
