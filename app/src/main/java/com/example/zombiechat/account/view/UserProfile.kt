package com.example.zombiechat.account.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.zombiechat.R
import com.example.zombiechat.account.data.repo.ProfileRepoImpl
import com.example.zombiechat.account.viewModel.ProfileVM
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.runBlocking


class UserProfile : AppCompatActivity() {
    private var muserimage: CircleImageView? = null
    private var musername: TextView? = null
    private var muserstatus: TextView? = null
    private var musersex: TextView? = null
    private var mToolbar: Toolbar? = null
    private var msendRequestBtn: Button? = null
    private var profileVM: ProfileVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        //got Intent values.
        val uid = intent.getStringExtra("uid")
        val name = intent.getStringExtra("name")

        //Toolbar
        mToolbar = findViewById(R.id.user_profile_appbar)
        setSupportActionBar(mToolbar)
        supportActionBar!!.setTitle(name)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //text, image, button reference
        muserimage = findViewById(R.id.user_image)
        musername = findViewById(R.id.user_name)
        muserstatus = findViewById(R.id.user_status)
        musersex = findViewById(R.id.user_sex)
        msendRequestBtn = findViewById(R.id.send_request_btn)
        profileVM = ProfileVM(ProfileRepoImpl())
        profileVM!!.getUserModel().observe(this) {
            musername?.text = it.name
            muserstatus?.text = it.status
            musersex?.text = it.gender
            Picasso.with(this).load(it.image).error(R.drawable.default_user)
                .placeholder(R.drawable.default_user).into(muserimage)
        }

        profileVM!!.friendStatus.observe(this) {


            if (it == null) {
                msendRequestBtn?.text = "Send Request"
                msendRequestBtn?.isClickable = true
            } else if (it.status == "requestSent") {
                msendRequestBtn?.text = "Pennding"
                msendRequestBtn?.isClickable = true
            } else {
                msendRequestBtn?.text = "Friends"
                msendRequestBtn?.isClickable = false

            }
        }

        profileVM!!.isRequestSent.observe(this) {
            if (it) {
                msendRequestBtn?.text = "Pennding"
                msendRequestBtn?.isClickable = false
            }
        }


        if (uid != null) {
            runBlocking {
                profileVM!!.getUser(uid)
                profileVM!!.checkIfFriend(uid)
            }
        }


        msendRequestBtn!!.setOnClickListener {
            runBlocking {
                profileVM!!.sendFriendRequest(uid!!)
            }
        }


//        profileVM = ProfileVM()


//        //cloud firestore
//        db = FirebaseFirestore.getInstance();
//
//        //firebase auth
//        mAuth = FirebaseAuth.getInstance();
        val userMap: MutableMap<String, String?> = HashMap()

        // get user details from cloud firestore
//        db.collection("users").document(uid).get()
//            .addOnSuccessListener(OnSuccessListener<DocumentSnapshot> { documentSnapshot ->
//                val model = checkNotNull(
//                    documentSnapshot.toObject(
//                        UserModel::class.java
//                    )
//                )
//                musername.setText(model.name)
//                muserstatus.setText(model.status)
//                musersex.setText(model.gender)
//                Picasso.with(this@UserProfile).load(model.image).error(R.drawable.default_user)
//                    .placeholder(R.drawable.default_user).into(muserimage)
//            })

        // find all the friends of the user
//        db.collection("friends").whereEqualTo("userid", mAuth.getCurrentUser().getUid())
//            .whereEqualTo("friendId", uid).get().addOnSuccessListener(
//            OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
//                for (documentSnapshot in queryDocumentSnapshots) {
//                    if (documentSnapshot.exists()) {
//                        msendRequestBtn.setText("Friend")
//                        msendRequestBtn.setClickable(false)
//                    }
//                }
//            })

//        msendRequestBtn.setOnClickListener(View.OnClickListener {
//            userMap["sendBy"] = mAuth.getCurrentUser().getUid()
//            userMap["sentTo"] = uid
//            userMap["requeststatus"] = "sendRequest"
//
//            // send friend request
//            db.collection("requests").add(userMap)
//                .addOnSuccessListener(OnSuccessListener<DocumentReference?> {
//                    msendRequestBtn.setText("Request Sent")
//                    msendRequestBtn.setClickable(false)
//                })
//        })

    }
}