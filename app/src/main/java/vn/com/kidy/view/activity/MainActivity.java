package vn.com.kidy.view.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.com.kidy.R;
import vn.com.kidy.data.Constants;
import vn.com.kidy.data.model.home.Note;
import vn.com.kidy.data.model.note.Message;
import vn.com.kidy.data.model.note.Notes;
import vn.com.kidy.data.model.login.ClassInfo;
import vn.com.kidy.data.model.login.Kid;
import vn.com.kidy.data.model.login.User;
import vn.com.kidy.data.model.notification.Notification;
import vn.com.kidy.data.model.notification.Notifications;
import vn.com.kidy.interactor.MainInteractor;
import vn.com.kidy.network.client.Client;
import vn.com.kidy.notification.Config;
import vn.com.kidy.notification.utils.NotificationUtils;
import vn.com.kidy.presenter.MainPresenter;
import vn.com.kidy.utils.Tools;
import vn.com.kidy.view.adapter.ViewPagerAdapter;
import vn.com.kidy.view.fragment.AddNoteFragment;
import vn.com.kidy.view.fragment.AlbumFragment;
import vn.com.kidy.view.fragment.ArticleFragment;
import vn.com.kidy.view.fragment.CalendarFragment;
import vn.com.kidy.view.fragment.CommentDetailFragment;
import vn.com.kidy.view.fragment.CommentFragment;
import vn.com.kidy.view.fragment.FullCalendarFragment;
import vn.com.kidy.view.fragment.HomeFragment;
import vn.com.kidy.view.fragment.LoginFragment;
import vn.com.kidy.view.fragment.MediaFragment;
import vn.com.kidy.view.fragment.NewsFragment;
import vn.com.kidy.view.fragment.NotificationFragment;
import vn.com.kidy.view.fragment.SettingFragment;
import vn.com.kidy.view.fragment.ViewImageFragment;
import vn.com.kidy.view.widget.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, MainPresenter.View {

    @BindView(R.id.fr_login)
    FrameLayout fr_login;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.btn_menu)
    ImageView btn_menu;
    @BindView(R.id.rl_toolbar)
    RelativeLayout rl_toolbar;
    @BindView(R.id.txt_schoolName)
    TextView txt_schoolName;
    @BindView(R.id.txt_className)
    TextView txt_className;
    @BindView(R.id.kid_avatar)
    SimpleDraweeView kid_avatar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    //Navigation Menu
    @BindView(R.id.txt_acc_name)
    TextView txt_acc_name;
    @BindView(R.id.acc_avatar)
    SimpleDraweeView acc_avatar;
    @BindView(R.id.ln_kid_acc)
    LinearLayout ln_kid_acc;
    @BindView(R.id.ln_setting)
    LinearLayout ln_setting;
    @BindView(R.id.ln_intro)
    LinearLayout ln_intro;
    @BindView(R.id.ln_logout)
    LinearLayout ln_logout;
    @BindView(R.id.txt_notification_size)
    TextView txt_notification_size;

    private LoginFragment loginFragment;
    //    private Parent parent;
    private User user;
    private ArrayList<vn.com.kidy.data.model.login.Kid> kids;

    private int kidPos;
    private Kid kid;
    private Menu bottom_menu;

    private int isLogin = 0;
    private Notes notes = new Notes();
    private Notifications notifications;

    private HomeFragment homeFragment;
    private CalendarFragment calendarFragment;
    private CalendarDay selectedDate;
    private MainPresenter mainPresenter;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initView();
        mainPresenter = new MainPresenter(new MainInteractor(new Client(Constants.API_SLL_URL)));
        mainPresenter.setView(this);

        selectedDate = CalendarDay.today();

        initFCM();
    }

    private void initFCM() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    Log.e("a", message);
                }
            }
        };

        displayFirebaseRegId();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("a", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            Log.e("a", "Firebase Reg Id: " + regId);
    }

    private void initView() {
        btn_menu.setOnClickListener(this);
        kid_avatar.setOnClickListener(this);
        addLoginFragment();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            rl_toolbar.setPadding(0, Tools.getStatusBarHeight(this), 0, 0);
        }
        bottom_menu = bottomNavigationView.getMenu();
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            for (int i = 0; i < bottom_menu.size(); i++) {
                if (item.getItemId() == bottom_menu.getItem(i).getItemId()) {
                    viewPager.setCurrentItem(i);
                    item.setChecked(true);
                    break;
                }
            }
            return false;
        });
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        ln_setting.setOnClickListener(this);
    }

    private void initViewPager() {
        bottomNavigationView.setSelectedItemId(bottom_menu.getItem(0).getItemId());

        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance(kidPos);
        }
        if (calendarFragment == null) {
            calendarFragment = CalendarFragment.newInstance(kidPos);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(homeFragment, Constants.FragmentName.LOGIN_FRAGMENT);
        adapter.addFragment(calendarFragment, Constants.FragmentName.CALENDAR_FRAGMENT);
        adapter.addFragment(NewsFragment.newInstance(kidPos), Constants.FragmentName.NEWS_FRAGMENT);
        adapter.addFragment(CommentFragment.newInstance(kidPos), Constants.FragmentName.COMMENT_FRAGMENT);
        adapter.addFragment(MediaFragment.newInstance(kidPos), Constants.FragmentName.MEDIA_FRAGMENT);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.setSelectedItemId(bottom_menu.getItem(position).getItemId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

//    public Parent getMyParent() {
//        return parent;
//    }

//    public void setMyParent(Parent parent) {
//        this.parent = parent;
//        isLogin = 1;
//    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        isLogin = 1;
    }

    public ArrayList<vn.com.kidy.data.model.login.Kid> getKids() {
        return this.kids;
    }

    public void setKids(ArrayList<vn.com.kidy.data.model.login.Kid> kids) {
        this.kids = kids;
    }

    public int getKidPos() {
        return this.kidPos;
    }

    public vn.com.kidy.data.model.login.Kid getKid() {
        return getKids().get(getKidPos());
    }

    private void setCurKidData(int position) {
        this.kidPos = position;
        this.kid = getKids().get(kidPos);
        txt_schoolName.setText(kid.getSchoolInfo().getName());
        txt_className.setText(kid.getClassName());

        if (kid.getAvatar() != null) {
            kid_avatar.setImageURI(Uri.parse(kid.getAvatar()));
        }
        if (user.getContact().getAvatar() != null) {
            acc_avatar.setImageURI(Uri.parse(user.getContact().getAvatar()));
        }

        initViewPager();
        mainPresenter.onGetNotifications(kid.getBabyId());
        mainPresenter.onGetClassInfo(kid.getClassId());
        txt_notification_size.setVisibility(View.INVISIBLE);
    }

    public void setCurKid(int position) {
        txt_acc_name.setText(user.getContact().getFullName());
        setCurKidData(position);
        ln_kid_acc.removeAllViews();
        View[] ln_kids = new View[getKids().size()];
        SimpleDraweeView[] img_kids = new SimpleDraweeView[getKids().size()];
        TextView[] txt_kid_name_menus = new TextView[getKids().size()], txt_kid_birthday_menus = new TextView[getKids().size()], txt_isActives = new TextView[getKids().size()];
        View[] view_strokes = new View[getKids().size()];

        for (int i = 0; i < getKids().size(); i++) {
            ln_kids[i] = LayoutInflater.from(this).inflate(R.layout.item_menu_kid,
                    null, false);
            img_kids[i] = ln_kids[i].findViewById(R.id.kid_avatar_menu);
            txt_kid_name_menus[i] = ln_kids[i].findViewById(R.id.txt_kid_name_menu);
            txt_kid_birthday_menus[i] = ln_kids[i].findViewById(R.id.txt_kid_birthday_menu);
            txt_isActives[i] = ln_kids[i].findViewById(R.id.txt_isActive);
            view_strokes[i] = ln_kids[i].findViewById(R.id.view_stroke);

            img_kids[i].setImageURI(Uri.parse(getKids().get(i).getAvatar()));
            txt_kid_name_menus[i].setText(getKids().get(i).getFullName());
            txt_kid_birthday_menus[i].setText(getKids().get(i).getBirthday());
            if (kidPos == i) {
                txt_isActives[i].setVisibility(View.VISIBLE);
                view_strokes[i].setVisibility(View.VISIBLE);
            } else {
                txt_isActives[i].setVisibility(View.INVISIBLE);
                view_strokes[i].setVisibility(View.INVISIBLE);
            }
            ln_kids[i].setClickable(true);
            final int clickPos = i;
            ln_kids[i].setOnClickListener(view -> {
                txt_isActives[kidPos].setVisibility(View.INVISIBLE);
                view_strokes[kidPos].setVisibility(View.INVISIBLE);
                txt_isActives[clickPos].setVisibility(View.VISIBLE);
                view_strokes[clickPos].setVisibility(View.VISIBLE);
                kidPos = clickPos;
                setCurKidData(kidPos);
                drawerLayout.closeDrawer(Gravity.START);
            });
            ln_kid_acc.addView(ln_kids[i]);
        }
    }

    public void setNotes(Notes notes) {
        this.notes = notes;
    }

    public Notes getNotes() {
        return this.notes;
    }

    public void setSelectedDate(CalendarDay date) {
        if (this.selectedDate != date) {
            this.selectedDate = date;
            if (calendarFragment != null) {
                calendarFragment.refreshDateData(date);
            }
        }
    }

    public CalendarDay getSelectedDate() {
        return this.selectedDate;
    }

    public Notifications getNotifications() {
        return notifications;
    }

    public void setNotification(Notifications notifications) {
        this.notifications = notifications;
    }

    public void addNote(Message note) {
//        Log.e("a", this.notes.getData().size() + " Notes size 1");
        if (notes.getMessages() == null) {
            notes.setMessages(new ArrayList<>());
        }
        this.notes.getMessages().add(0, note);
        homeFragment.addNote(note, true);
        calendarFragment.addNote(note, true);
        Log.e("a", this.notes.getMessages().size() + " Notes size");
    }

    private void addLoginFragment() {
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
        }
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fr_login, loginFragment, Constants.FragmentName.LOGIN_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addNoteFragment() {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, AddNoteFragment.newInstance(kidPos, Constants.ADD_NOTE_NAME), Constants.FragmentName.ADDNOTE_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addRequestFragment() {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, AddNoteFragment.newInstance(kidPos, Constants.ADD_REQUEST_NAME), Constants.FragmentName.ADDNOTE_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addFullCalendarFragment(int kidPos, long curDate) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, FullCalendarFragment.newInstance(kidPos, curDate), Constants.FragmentName.ADDNOTE_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addCommentDetailFragment(int week, int year, String content, String fromDate, String toDate) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, CommentDetailFragment.newInstance(week, year, content, fromDate, toDate), Constants.FragmentName.CONTENTDETAIL_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addArticleFragment(String title, String content, String date, String image) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, ArticleFragment.newInstance(title, content, date, image), Constants.FragmentName.ARTICLE_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addAlbumFragment(String albumId, String title) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, AlbumFragment.newInstance(kidPos, albumId, title), Constants.FragmentName.ALBUM_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addViewImageFragment(String[] images, int position) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, ViewImageFragment.newInstance(images, position), Constants.FragmentName.VIEW_IMAGE_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addSettingFragment() {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fr_login, SettingFragment.newInstance(), Constants.FragmentName.VIEW_IMAGE_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void closeLoginFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }
        isLogin = 2;
    }

    public void showNotificationDialog() {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom);
        ft.replace(R.id.fr_login, NotificationFragment.newInstance(kidPos), Constants.FragmentName.NOTIFICATION_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void onNotificationClick(Notification item) {
        onBackPressed();
        switch (item.getType()) {
            case Constants.NOTIFICATION_TYPE.TYPE_NEW:
                addArticleFragment(item.getTitle(), item.getNotifyId(), item.getDate() + "", "");
                break;
            case Constants.NOTIFICATION_TYPE.TYPE_COMMENT:
//                addCommentDetailFragment(item.getTitle(), item.getNotifyId(), item.getDate());
                break;
            case Constants.NOTIFICATION_TYPE.TYPE_NOTE:
                addNoteFragment();
                break;
            case Constants.NOTIFICATION_TYPE.TYPE_ALBUM:
                addAlbumFragment(item.getNotifyId(), item.getTitle());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            if (isLogin == 2) {
                fm.popBackStack();
                return;
            } else if (isLogin == 1) {
                System.exit(0);
            }
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a myParent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (item.isCheckable()) {
            item.setChecked(!item.isChecked());
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_menu:
                onMenuClick();
                break;
            case R.id.kid_avatar:
                showNotificationDialog();
                break;
            case R.id.ln_setting:
                drawerLayout.closeDrawer(Gravity.START);
                addSettingFragment();
                break;
        }
    }

    private void onMenuClick() {
        drawerLayout.openDrawer(Gravity.START);
    }

    @Override
    public void getDataSuccess(Notifications notifications) {
        this.notifications = notifications;
        if (notifications.getNotifications().size() > 0) {
            txt_notification_size.setVisibility(View.VISIBLE);
            txt_notification_size.setText("" + notifications.getNotifications().size());
        } else {
            txt_notification_size.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void getDataError(int statusCode) {

    }

    @Override
    public void getClassInfoSuccess(ClassInfo classInfo) {
        this.kids.get(kidPos).setClassId(classInfo.getId());
        txt_className.setText(classInfo.getClassName());
    }

    @Override
    public Context context() {
        return null;
    }

    public boolean hasPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    public void requestPermissions() {
        Log.e("a", "requestPermissions");
        if (!hasPermissions()) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
//                Snackbar.make(rl_contentMain, R.string.write_permission_denied,
//                        Snackbar.LENGTH_SHORT)
//                        .setAction("OK", view -> requestPermissions())
//                        .show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.

            } else {
                // Permission request was denied.
//                Snackbar.make(rl_contentMain, R.string.write_permission_denied,
//                        Snackbar.LENGTH_SHORT)
//                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

}
