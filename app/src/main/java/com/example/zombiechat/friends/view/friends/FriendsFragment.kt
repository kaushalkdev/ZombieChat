package com.example.zombiechat.friends.view.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zombiechat.R
import com.example.zombiechat.account.data.models.UserModel
import com.example.zombiechat.friends.data.repo.FriendsRepo
import com.example.zombiechat.friends.viewModels.FriendsVM
import kotlinx.coroutines.runBlocking

class FriendsFragment : Fragment() {
    private var mrecyclerview: RecyclerView? = null
    private var madapter: FriendsRecyclerAdapter? = null
    private var friendsVM: FriendsVM? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friends, container, false)


        //recyclerview
        mrecyclerview = view.findViewById(R.id.friends_recycler_view)
        mrecyclerview!!.setHasFixedSize(true)
        mrecyclerview!!.setLayoutManager(LinearLayoutManager(view.context))


        return view
    }


    override fun onStart() {
        super.onStart()

        friendsVM = FriendsVM(FriendsRepo())
        runBlocking {
            friendsVM!!.fetchAllFriends()
        }

        friendsVM!!.getAllFriends().observe(this) { userModels: List<UserModel?>? ->
            madapter = FriendsRecyclerAdapter(userModels)
            mrecyclerview!!.adapter = madapter
        }
    }


    companion object {
        const val TAG: String = "FriendsFragment"
    }
}

