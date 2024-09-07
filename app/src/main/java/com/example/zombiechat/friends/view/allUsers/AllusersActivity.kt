package com.example.zombiechat.friends.view.allUsers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zombiechat.R
import com.example.zombiechat.friends.data.repo.AllUsersRepo
import com.example.zombiechat.friends.viewModels.AllUsersViewModel
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

        val repo = AllUsersRepo()
        allUsersViewModel = AllUsersViewModel(repo)

        allUsersViewModel!!.getLiveUsers().observe(this) {
            madapter =
                AllUsersRecyclerAdapter(
                    it
                )
            mrecyclerview!!.setAdapter(madapter)
        }

        runBlocking { allUsersViewModel!!.getAllUsers() }


    }


    companion object {
        private const val TAG = "AllusersActivity"
    }
}
