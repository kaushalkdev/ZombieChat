package com.example.zombiechat.chat.views.screens.activities;

import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zombiechat.R;
import com.example.zombiechat.chat.data.models.SingleChatModel;
import com.example.zombiechat.chat.data.repo.FirebaseChatRepo;
import com.example.zombiechat.chat.viewModels.ChatRoomViewModel;
import com.example.zombiechat.chat.views.adapters.ChatRoomAdapter;
import com.example.zombiechat.constants.fields.Fields;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Locale;


import de.hdodenhof.circleimageview.CircleImageView;


public class ChatRoomScreen extends AppCompatActivity {

    public static final String TAG = "SmoothScroll";
    private TextToSpeech mTTS;
    private Toolbar mToolbar;
    private CircleImageView muserimage;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private EditText userinput;
    private Button sendmessageBtn;
    private RecyclerView mrecyclerview;
    private ChatRoomAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private ChatRoomViewModel chatRoomViewModel;
    private String otherUserId;
    private String chatRoomId;
    private String otherUserImage;
    private String otherUserName;
//    ListenerRegistration registration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_chat);
        chatRoomViewModel = new ChatRoomViewModel(new FirebaseChatRepo());


        mToolbar = findViewById(R.id.user_chat_app_bar);
        setSupportActionBar(mToolbar);

        //tts
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        // values fetched from intent to get the chat up and running.
        otherUserName = getIntent().getStringExtra(Fields.otherUserName);
        otherUserImage = getIntent().getStringExtra(Fields.otherUserImage);
        otherUserId = getIntent().getStringExtra(Fields.otherUserId);
        chatRoomId = getIntent().getStringExtra(Fields.chatRoomId);


        //setting tool bar
        getSupportActionBar().setTitle(otherUserName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recyclerview
        mrecyclerview = findViewById(R.id.user_chat_recycler_view);
        mrecyclerview.setHasFixedSize(true);
        // Now set the properties of the LinearLayoutManager
        mLayoutManager = new LinearLayoutManager(ChatRoomScreen.this);

        mLayoutManager.setStackFromEnd(true);

        // And now set it to the RecyclerView
        mrecyclerview.setLayoutManager(mLayoutManager);


        muserimage = findViewById(R.id.user_image);
        //setting image
        Picasso.with(getApplicationContext()).load(otherUserImage).error(R.drawable.default_user).placeholder(R.drawable.default_user).into(muserimage);


        //button and edit text
        sendmessageBtn = findViewById(R.id.chat_box_send_btn);
        userinput = findViewById(R.id.chat_box_inout_text);


        sendmessageBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(userinput.getText().toString())) {
                Toast.makeText(ChatRoomScreen.this, "empty text", Toast.LENGTH_SHORT).show();
            } else {

                final String message = userinput.getText().toString();
                userinput.setText("");

                // sending chat to server
                chatRoomViewModel.sendMessage(message, otherUserId, chatRoomId);


            }


        });


    }


    @Override
    protected void onStart() {
        super.onStart();


//        Query query = db.collection("chatbox").document(chatid).collection("chats").orderBy("time", Query.Direction.ASCENDING);

        // TODO fetch chat list from firebase and set it to adapter
//
//        adapter = new ChatRoomAdapter();
//        mrecyclerview.setAdapter(adapter);


        //for speaking message only female voice currently

//        registration = db.collection("chatbox").document(chatid).collection("chats").orderBy("time", Query.Direction.DESCENDING).whereEqualTo("sendBy", uid).limit(1).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//
//                    speak(documentSnapshot.get("message").toString());
//                }
//            }
//        });


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

    }

    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }


//        registration.remove();
        super.onDestroy();
    }


}
