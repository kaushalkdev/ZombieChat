package com.example.zombiechat.friends.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zombiechat.R
import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.friends.data.repo.FriendsRepo
import com.example.zombiechat.friends.viewmodels.AllUsersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.runBlocking


class AllusersActivity : AppCompatActivity() {
    private var mToolbar: Toolbar? = null
    private var mrecyclerview: RecyclerView? = null
    private var madapter: AllUsersRecyclerAdapter? = null
    private var allUsersViewModel: AllUsersViewModel? = null

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

        val repo = FriendsRepo()
        allUsersViewModel = AllUsersViewModel(repo)

        allUsersViewModel!!.getLiveUsers().observe(this) {
            madapter = AllUsersRecyclerAdapter(it)
            mrecyclerview!!.setAdapter(madapter)
        }

        runBlocking { allUsersViewModel!!.getAllUsers() }


    }


    companion object {
        private const val TAG = "AllusersActivity"
    }
}
