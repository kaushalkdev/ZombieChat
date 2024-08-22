package com.example.zombiechat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.zombiechat.chat.ChatFragment;
import com.example.zombiechat.friends.FriendsFragment;
import com.example.zombiechat.friends.RequestFragment;

public class SectionPageradapter extends FragmentPagerAdapter {
    public SectionPageradapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ChatFragment ChatTab = new ChatFragment();
                return ChatTab;


            case 1:
                FriendsFragment FriendsTab = new FriendsFragment();
                return FriendsTab;
            case 2:

                RequestFragment RequestTab = new RequestFragment();
                return RequestTab;


            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }


    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "CHATS";
            case 1:
                return "FRIENDS";
            case 2:
                return "REQUESTS";

            default:
                return null;
        }

    }
}
