package com.example.zombiechat;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.zombiechat.chat.views.screens.fragments.ChatListScreen;
import com.example.zombiechat.friends.view.friends.FriendsFragment;
import com.example.zombiechat.friends.view.request.RequestFragment;

public class SectionPageradapter extends FragmentPagerAdapter {
    public SectionPageradapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return switch (position) {

            case 1 -> new FriendsFragment();
            case 2 -> new RequestFragment();
            default -> new ChatListScreen();
        };
    }

    @Override
    public int getCount() {
        return 3;
    }


    public CharSequence getPageTitle(int position) {

        return switch (position) {
            case 0 -> "CHATS";
            case 1 -> "FRIENDS";
            case 2 -> "REQUESTS";
            default -> null;
        };

    }
}
