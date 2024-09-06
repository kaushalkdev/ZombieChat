package com.example.zombiechat.friends.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zombiechat.R
import com.example.zombiechat.account.data.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot


class AllusersActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var mToolbar: Toolbar? = null
    private var mrecyclerview: RecyclerView? = null
    private val db = FirebaseFirestore.getInstance()
    private var madapter: AllUsersRecyclerAdapter? = null
    var registration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allusers)

        //toolbar
        mToolbar = findViewById(R.id.all_users_aap_bar)
        setSupportActionBar(mToolbar)
        supportActionBar!!.title = "All Users"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //recyclerview
        mrecyclerview = findViewById(R.id.all_users_recyclerview)
        mrecyclerview!!.setHasFixedSize(true)
        mrecyclerview!!.setLayoutManager(LinearLayoutManager(this))


        //firebase Auth
        mAuth = FirebaseAuth.getInstance()


        val userModels: MutableList<UserModel> = ArrayList()


        registration = db.collection("users")
            .addSnapshotListener(EventListener<QuerySnapshot?> { queryDocumentSnapshots, e ->
                if (e != null) {
                    Toast.makeText(this@AllusersActivity, "Error: " + e.message, Toast.LENGTH_SHORT)
                        .show()
                    return@EventListener
                }
                checkNotNull(queryDocumentSnapshots)
                for (documentSnapshot in queryDocumentSnapshots) {
                    val userModel = documentSnapshot.toObject(UserModel::class.java)
                    if (userModel.userid != mAuth!!.currentUser!!.uid) {
                        userModels.add(userModel)
                    }
                }
                madapter = AllUsersRecyclerAdapter(userModels, mAuth!!.currentUser!!.uid)
                mrecyclerview!!.setAdapter(madapter)
            })
    }

    override fun onStop() {
        super.onStop()
        registration!!.remove()
    }

    companion object {
        private const val TAG = "AllusersActivity"
    }
}
