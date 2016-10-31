package com.elbehiry.followusers.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.elbehiry.followusers.R;
import com.elbehiry.followusers.model.User;
import com.elbehiry.followusers.utiles.Controller;
import com.elbehiry.followusers.utiles.CustPagerTransformer;
import com.elbehiry.followusers.view.adapter.UsersAdapter;
import com.elbehiry.followusers.view.fragment.UserShowFragment;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity{
    private static final int ALL = 0;
    private static final int Followings = 1;
    private static final int unFollowing = 2;


    private ViewPager viewPager;
   // private List<UserShowFragment> fragments = new ArrayList<>();
    private final String[] imageArray = {"assets://image1.jpg"};
    Spinner spinner;
    EditText uname_edit;
    Controller DataList;
    CompositeSubscription cSubscription;
   // UsersAdapter adapter;
    FloatingActionButton fab;
    Handler handler = new Handler();
    List<User> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // adapter = new UsersAdapter(getSupportFragmentManager(),users);
        cSubscription = new CompositeSubscription();

        DataList = new Controller();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       fab = (FloatingActionButton) findViewById(R.id.fab);

        spinner = (Spinner) findViewById(R.id.spin);
        uname_edit = (EditText) findViewById(R.id.name);


        initImageLoader();
        fillViewPager();

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


       Subscription usersSubscription = Observable.combineLatest(RxAdapterView.itemSelections(spinner).skip(1), DataList.getObservable(),
                new Func2<Integer, Controller, List<User>>() {
                    @Override
                    public List<User> call(Integer integer, Controller controller) {
                        List<User> result = null;
                        switch (integer) {
                            case unFollowing:
                                result= controller.getUnFollowers();
                                break;
                            case Followings:
                                result= controller.getFollowers();
                                break;
                            default:
                                result= controller.getAllUsers();
                        }
                        return result;
                    }
                }).subscribe(new Observer<List<User>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("dd", e.getMessage(), e);
                    }

                    @Override
                    public void onNext(final List<User> tempUsers) {
                       handler.post(new Runnable() {
                           @Override
                           public void run() {
                               users.clear();

                              // viewPager.getAdapter().notifyDataSetChanged();
                               users.addAll(tempUsers);

                               viewPager.getAdapter().notifyDataSetChanged();
                               if(tempUsers.size()>0){
                                   viewPager.setCurrentItem(0, true);
                               }


                           }
                       });
                    }
                });
        
        cSubscription.add(
                usersSubscription
        );

        cSubscription.add(
                //fabbutton
                RxView.clicks(fab).map(new Func1<Void, String>() {
                    @Override
                    public String call(Void aVoid) {
                        return uname_edit.getText().toString();
                    }
                }).filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        if (s.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Please type any names :D", Toast.LENGTH_SHORT).show();
                            return false;

                        } else {
                            return true;
                        }
                    }
                }).subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("dd", e.getMessage(), e);
                    }

                    @Override
                    public void onNext(String s) {
                        User d = new User();
                        d.setIsfollow(false);
                        d.setName(s);
                        d.setImage_link(imageArray[0]);
                        //d.setFragment(new UserShowFragment());
                        uname_edit.setText("");
                        DataList.Add_Users(d);
                    }
                })


        );



        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"All", "Following", "unFollowing"}));
        spinner.setSelection(ALL);




    }
    private void fillViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
viewPager.setOffscreenPageLimit(0);
        viewPager.setPageTransformer(false, new CustPagerTransformer(this));


//        for (int i = 0; i < list.size(); i++) {
//            fragments.add(new UserShowFragment());
//        }
//        adapter.AddData(list);
        viewPager.setAdapter(new UsersAdapter(getFragmentManager(),users,DataList));





        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
//                updateIndicatorTv();
           //     viewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




//        updateIndicatorTv();
    }




    @SuppressWarnings("deprecation")
    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .memoryCacheExtraOptions(480, 800)
                // default = device screen dimensions
                .threadPoolSize(3)
                // default
                .threadPriority(Thread.NORM_PRIORITY - 1)
                // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13) // default
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileCount(100)
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs().build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }


}
