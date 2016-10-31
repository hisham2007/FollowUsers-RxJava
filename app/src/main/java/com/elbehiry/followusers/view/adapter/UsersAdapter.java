package com.elbehiry.followusers.view.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.elbehiry.followusers.model.User;
import com.elbehiry.followusers.utiles.Controller;
import com.elbehiry.followusers.view.fragment.UserShowFragment;

import java.util.List;

/**
 * Created by hishambakr on 10/31/16.
 */

public class UsersAdapter extends StateRemovableFragmentStatePageAdapter {
    private long baseId = 0;
    private List<User> users;
    private Controller DataList;


    public UsersAdapter(FragmentManager fm, List<User> users, Controller DataList) {
        super(fm);
        this.users = users;
        this.DataList = DataList;
    }

    @Override
    public Fragment getItem(int position) {
        UserShowFragment fragment = new UserShowFragment();

        //fragment.setUser(users.get(position));
        fragment.bindData(users.get(position), DataList);
        return fragment;

    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    /* @Override
        public void call(List<User> l) {
            Toast.makeText(MainActivity.this, "call", Toast.LENGTH_SHORT).show();
            list = l;
            notifyDataSetChanged();

        }*/
    @Override
    public int getItemPosition(Object object) {
        /// return PagerAdapter.POSITION_NONE;
        int index = users.indexOf(object);

        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }
}


