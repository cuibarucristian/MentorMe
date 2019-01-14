package com.example.cristian.mentorme.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter
{
    public PagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case 1:
                OffersFragment offersFragment = new OffersFragment();
                return  offersFragment;
            case 2:
                UsersFragment usersFragment = new UsersFragment();
                return usersFragment;

            default: return null;
        }
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0: return "Chats";
            case 1: return "Offers";
            case 2: return "Users";
            default:return null;
        }
    }

}

