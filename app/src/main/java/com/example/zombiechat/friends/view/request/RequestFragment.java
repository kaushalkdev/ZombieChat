package com.example.zombiechat.friends.view.request;

import android.os.Bundle;


import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zombiechat.R;
import com.example.zombiechat.friends.data.repo.FriendsRepo;
import com.example.zombiechat.friends.viewModels.FriendsVM;


public class RequestFragment extends Fragment {


    private RecyclerView mrecyclerview;
    private RequestRecyclerAdaper madapter;
    public static final String TAG = "RequestFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_request, container, false);


        //recyclerview
        mrecyclerview = view.findViewById(R.id.request_recycler_view);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(view.getContext()));
        FriendsVM friendsVM = new FriendsVM(new FriendsRepo());
        friendsVM.getAllRequests().observe(getViewLifecycleOwner(), requestModels -> {
            madapter = new RequestRecyclerAdaper(requestModels);
            mrecyclerview.setAdapter(madapter);

        });
        friendsVM.fetchAllRequests();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
    }
}





