package com.elbehiry.followusers.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.elbehiry.followusers.R;
import com.elbehiry.followusers.model.User;
import com.elbehiry.followusers.utiles.Controller;
import com.elbehiry.followusers.utiles.DragLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by elbehiry on 15/10/16.
 */

public class UserShowFragment extends Fragment implements DragLayout.UserActionListener {
    private ImageView imageView;
//    private View address1, address2, address3, address4, address5;
//    private RatingBar ratingBar;
//    private View head1, head2, head3, head4;
//    private String imageUrl;
    private AppCompatCheckBox follow;
    Action1<User> subscribe;
    public Subscription subscription;
    User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common, null);


//        subscribe = new Controller();
        DragLayout dragLayout = (DragLayout) rootView.findViewById(R.id.drag_layout);
        imageView = (ImageView) dragLayout.findViewById(R.id.image);

        follow = (AppCompatCheckBox) dragLayout.findViewById(R.id.follow);




        String imglink = user.getImage_link();
        if(!imglink.equals("")) {
            ImageLoader.getInstance().displayImage(imglink, imageView);
        }
//        address1 = dragLayout.findViewById(R.id.address1);
//        address2 = dragLayout.findViewById(R.id.address2);
//        address3 = dragLayout.findViewById(R.id.address3);
//        address4 = dragLayout.findViewById(R.id.address4);
//        address5 = dragLayout.findViewById(R.id.address5);
//        ratingBar = (RatingBar) dragLayout.findViewById(R.id.rating);
//
//        head1 = dragLayout.findViewById(R.id.head1);
//        head2 = dragLayout.findViewById(R.id.head2);
//        head3 = dragLayout.findViewById(R.id.head3);
//        head4 = dragLayout.findViewById(R.id.head4);
        dragLayout.setActionListener(this);
        subscription = RxCompoundButton.checkedChanges(follow)
                .skip(1)
                .map(new Func1<Boolean, User>() {
                    @Override
                    public User call(Boolean aBoolean) {
                        return user;
                    }
                }).subscribe(subscribe);
        return rootView;
    }


    public void bindData(User user,Action1<User> l) {
        subscribe = l;
        this.user = user;

    }

    @Override
    public void onTop(View v) {
        Toast.makeText(getActivity(), "ontop", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBottom(View v) {
        Toast.makeText(getActivity(), "onbuttom", Toast.LENGTH_SHORT).show();

    }

//    @Override
//    public void call(List<User> list) {
//
//        Toast.makeText(getActivity(), "notify", Toast.LENGTH_SHORT).show();
//
//    }
}
