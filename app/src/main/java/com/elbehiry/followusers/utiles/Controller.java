package com.elbehiry.followusers.utiles;

import com.elbehiry.followusers.model.User;
import com.elbehiry.followusers.view.fragment.UserShowFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.ReplaySubject;

/**
 * Created by elbehiry on 15/10/16.
 */

//observable
public class Controller implements Action1<User>{
    List<User> users;
    ReplaySubject<Controller> notifier = ReplaySubject.create();

    public Controller(){


        users = new ArrayList<User>();

    }

    public void Add_Users(User user){
        users.add(user);
        //notify for this update
        notifier.onNext(this);

    }
    public void Delete_User(User user){
        users.remove(user);
        notifier.onNext(this);
    }

    public List<User> getAllUsers (){

        return users;
    }
    public List<User> getFollowers(){
        ArrayList<User> followers = new ArrayList<User>();
        for(User u: users){
            if(u.getIsfollow()){
                followers.add(u);
            }
        }
        return followers;
    }
    public List<User> getUnFollowers(){
        ArrayList<User> unfollowers = new ArrayList<User>();
        for(User u: users){
            if(!u.getIsfollow()){
                unfollowers.add(u);
            }
        }
        return unfollowers;
    }
    @Override
    public void call(User user) {

        //update when click follow user
        User  userdata = users.get(users.indexOf(user));
        userdata.setIsfollow(!userdata.getIsfollow());
        notifier.onNext(this);

    }

    public Observable<Controller> getObservable(){
        return notifier;
    }
}
